import java.awt.*;
import java.io.*;
import javax.swing.*;

public class GUI extends JFrame {
    public Student currentStudent;
    public Evaluator evaluator;
    private JTabbedPane evaluatorTabs;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    // FIX 1: Move this to class level so 'load' can update it
    private JLabel viewerLabel = new JLabel("No material loaded", SwingConstants.CENTER);

    public GUI() {
        setTitle("Seminar Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.add(createLoginPage(), "LOGIN");
        mainPanel.add(createStudentDashboard(), "STUDENT");
        mainPanel.add(createEvaluatorDashboard(), "EVALUATOR");
        mainPanel.add(new JPanel(), "COORDINATOR"); 

        add(mainPanel);
        setVisible(true);
    }

    public void showPage(String pageName) {
        cardLayout.show(mainPanel, pageName);
    }

    private JPanel createLoginPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        String[] roles = {"Student", "Evaluator", "Coordinator"};
        JComboBox<String> roleSelector = new JComboBox<>(roles);
        JButton loginBtn = new JButton("Login");
        panel.add(new JLabel("Role: "));
        panel.add(roleSelector);
        panel.add(loginBtn);
        loginBtn.addActionListener(e -> showPage(((String) roleSelector.getSelectedItem()).toUpperCase()));
        return panel;
    }

    private JPanel createStudentDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Registration", createRegistrationPanel());
        JButton back = new JButton("Back");
        back.addActionListener(e -> showPage("LOGIN"));
        panel.add(tabs, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createEvaluatorDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        evaluatorTabs = new JTabbedPane();
        evaluatorTabs.addTab("Evaluation Form", createEvaluationPanel());
        evaluatorTabs.addTab("View Materials", viewMaterials("")); 
        JButton back = new JButton("Back");
        back.addActionListener(e -> showPage("LOGIN"));
        panel.add(evaluatorTabs, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createRegistrationPanel() {
        JTextField titleIn = new JTextField();
        JTextField abstractIn = new JTextField();
        JTextField supervisorIn = new JTextField();
        JTextField presentationIn = new JTextField();
        JButton uploadBtn = new JButton("Select Seminar File");
        JLabel statusLabel = new JLabel("No file selected");
        JButton saveBtn = new JButton("Register/Save");
        JPanel p = new JPanel(new GridLayout(6, 2, 10, 10));

        p.add(new JLabel("Research Title :")); p.add(titleIn);
        p.add(new JLabel("Abstract :")); p.add(abstractIn);
        p.add(new JLabel("Supervisor Name :")); p.add(supervisorIn);
        p.add(new JLabel("Presentation Type: ")); p.add(presentationIn);
        p.add(saveBtn); p.add(uploadBtn);
        p.add(new JLabel("Status:")); p.add(statusLabel);

        uploadBtn.addActionListener(e -> {
            if (currentStudent == null) currentStudent = new Student(titleIn.getText(), "", "");
            JFileChooser upload_file = new JFileChooser();
            if (upload_file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = upload_file.getSelectedFile().getAbsolutePath();
                if (currentStudent.uploadMaterials(path)) statusLabel.setText("File Linked");
            }
        });

        // FIX 2: SAVE includes the path at index 4
        saveBtn.addActionListener(e -> {
            String path = (currentStudent != null) ? currentStudent.getMaterialPath() : "None";
            String data = titleIn.getText() + "|" + abstractIn.getText() + "|" + supervisorIn.getText() + "|" + presentationIn.getText() + "|" + path;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seminars.txt", true))) {
                writer.write(data);
                writer.newLine();
                JOptionPane.showMessageDialog(this, "Saved!");
            } catch (IOException ex) { ex.printStackTrace(); }
        });
        return p;
    }

    private JPanel createEvaluationPanel() {
        JButton loadBtn = new JButton("load");

        // FIX 3: LOAD updates the existing viewerLabel
        loadBtn.addActionListener(e -> {
            try (BufferedReader read = new BufferedReader(new FileReader("seminars.txt"))) {
                String line, lastPath = "";
                while ((line = read.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5) lastPath = parts[4];
                }
                updateMedia(lastPath); // Update the visual label
                evaluatorTabs.setSelectedIndex(1); // Auto-switch to the view tab
            } catch (IOException ex) { ex.printStackTrace(); }
        });

        JPanel p = new JPanel(new GridLayout(6, 2, 10, 10));
        String[] clarityOpts = {"clear identifications of gap", "Problem is clear but weak justification", "broad problem statement", "Poor"};
        String[] methOpts = {"unbiased", "minimal bias", "noticable bias", "biased"};
        String[] resultsOpts = {"Professional Visuals", "Accurate but flaws", "Needs polish"};
        String[] PresOpts= {"Confident", "Clear", "Crowded" ,"Unclear"};

        p.add(new JLabel("Problem Clarity:")); p.add(new JComboBox<>(clarityOpts));
        p.add(new JLabel("Methodology:")); p.add(new JComboBox<>(methOpts));
        p.add(new JLabel("Presentation:")); p.add(new JComboBox<>(PresOpts));
        p.add(new JLabel("Results:")); p.add(new JComboBox<>(resultsOpts));
        p.add(new JLabel("Actions:")); p.add(loadBtn);
        p.add(new JLabel("")); p.add(new JButton("Calculate Mark"));
        return p;
    }

    private JPanel viewMaterials(String path) {
        JPanel q = new JPanel(new BorderLayout()); 
        viewerLabel.setBorder(BorderFactory.createTitledBorder("Student Poster / Presentation"));
        q.add(new JScrollPane(viewerLabel), BorderLayout.CENTER);
        if (path != null && !path.isEmpty()) updateMedia(path);
        return q;
    }

    // New helper method to handle the internal change of the label content
    private void updateMedia(String path) {
        if (path == null || path.isEmpty() || path.equals("None")) {
            viewerLabel.setIcon(null);
            viewerLabel.setText("No material found.");
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            viewerLabel.setText("File not found at path: " + path);
        } else if (path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".png") || path.toLowerCase().endsWith(".pdf")) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(1200 , 1800, Image.SCALE_SMOOTH);
            viewerLabel.setIcon(new ImageIcon(img));
            viewerLabel.setText(""); 
        } else {
            viewerLabel.setIcon(null);
            viewerLabel.setText("Video format. Opening player...");
            try { Desktop.getDesktop().open(file); } catch (Exception e) {}
        }
    }

    public static void main(String[] args) { new GUI(); }
}