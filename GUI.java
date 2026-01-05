import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame {
    public Student currentStudent;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    public GUI() {
        setTitle("Seminar Management System");
        setSize(700, 500);
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

        loginBtn.addActionListener(e -> {
            String selected = (String) roleSelector.getSelectedItem();
            showPage(selected.toUpperCase());
        });
        return panel;
    }

    
    private JPanel createStudentDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        
        tabs.addTab("Registration", createRegistrationPanel());
        
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> showPage("LOGIN"));
        
        panel.add(tabs, BorderLayout.CENTER);
        panel.add(logout, BorderLayout.SOUTH);
        return panel;
    }

    
    private JPanel createEvaluatorDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createEvaluationPanel(), BorderLayout.CENTER);
        
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> showPage("LOGIN"));
        panel.add(logout, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createRegistrationPanel() {
        JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField titleIn = new JTextField();
        JButton saveBtn = new JButton("Register Student");

        p.add(new JLabel("Research Title:"));
        p.add(titleIn);
        p.add(new JLabel("")); // Spacer
        p.add(saveBtn);

        saveBtn.addActionListener(e -> {
            // Logic from Student.java
            currentStudent = new Student(titleIn.getText(), "Abstract Pending", "Staff");
            JOptionPane.showMessageDialog(this, "Student registered: " + currentStudent.researchTitle);
        });
        return p;
    }

    private JPanel createEvaluationPanel() {
        JPanel p = new JPanel(new GridLayout(6, 2, 10, 10));
        String[] clarityOpts = {"clear identifications of gap", "Problem is clear but weak justification", "broad problem statement", "Poor"};
        JComboBox<String> clarityBox = new JComboBox<>(clarityOpts);
        String[] methOpts = {"unbiased  ", "minimal bias , sample is large enough", "noticable bias , sample is smaller", "biased"};
        JComboBox<String> methBox = new JComboBox<>(methOpts);
        String[] resultsOpts = {"Professional Visuals & Excellent Interpretation","Accurate but some Visual flaw & Good Interpretation","Unprofessional Visuals & Needs to polish the interpretation"};
        JComboBox<String> resultsBox = new JComboBox<>(resultsOpts);
        String[] PresentationOpts= {"Confident & professional", "Clear & clean ", "Not Clear Enough & crowded " ,"unclear & too crowded " };
        JComboBox<String> PresentationBox = new JComboBox<>(PresentationOpts);

        JButton calcBtn = new JButton("Calculate & Show Mark");
    
       
        p.add(new JLabel("Problem Clarity:")); p.add(clarityBox);
        p.add(new JLabel("Methodology:")); p.add(methBox);
        p.add(new JLabel("Presentation:")); p.add(PresentationBox);
        p.add(new JLabel("Results:")); p.add(resultsBox);
        p.add(new JLabel("")); p.add(calcBtn);

        calcBtn.addActionListener(e -> {
            if (currentStudent == null) {
                JOptionPane.showMessageDialog(this, "No student data found! Register first.");
                return;
            }
        });
        return p;
    }

    public static void main(String[] args) {
        new GUI();
    }
}