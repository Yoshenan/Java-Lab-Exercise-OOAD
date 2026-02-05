import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {
    public Student currentStudent;
    final private Evaluator eval = new Evaluator();
    private JTabbedPane evaluatorTabs;
    final private CardLayout cardLayout = new CardLayout();
    final  private JPanel mainPanel = new JPanel(cardLayout);
    final private coordinator coord = new coordinator();

    
    private JTextField titleIn;
    private JTextField abstractIn;
    private JTextField supervisorIn;
    private JTextField presentationIn;
    private JTextField userID = new JTextField();
    private JTextField nameField = new JTextField();
    double finalMarks;

    public GUI() {
        setTitle("Seminar Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.add(createLoginPage(), "LOGIN");
        mainPanel.add(createStudentDashboard(), "STUDENT");
        mainPanel.add(createEvaluatorDashboard(), "EVALUATOR");
        mainPanel.add(createCoorDashboard(), "COORDINATOR");
        add(mainPanel);setVisible(true);}
    public void showPage(String pageName) {cardLayout.show(mainPanel, pageName);}

    private JPanel createLoginPage() {
    JPanel screen = new JPanel(new GridBagLayout());
    JPanel panel = new JPanel(new GridLayout(0, 1,5,5)); 
    panel.setPreferredSize(new Dimension(300, 250)); 
    panel.setBorder(BorderFactory.createTitledBorder("Sign In"));
    userID = new JTextField();
    nameField = new JTextField();
    String[] roles = {"Student", "Evaluator", "Coordinator"};
    JComboBox<String> roleSelector = new JComboBox<>(roles);
    JButton loginBtn = new JButton("Login");

    panel.add(new JLabel("User ID :"));
    panel.add(userID);
    panel.add(new JLabel("Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Role:"));
    panel.add(roleSelector);
    panel.add(loginBtn);

    loginBtn.addActionListener(e -> {
        try{
        int userid = Integer.parseInt(userID.getText().trim());
        String selectedRole = (String) roleSelector.getSelectedItem();
        if(userid == 243 && selectedRole.equals("Student") ){showPage("STUDENT");}
        else if(userid == 343 && selectedRole.equals("Evaluator")){showPage("EVALUATOR");}
        else if(userid == 443 && selectedRole.equals("Coordinator")){showPage("COORDINATOR");}
        else{JOptionPane.showMessageDialog(this, "Enter Correct ID");}}
        catch(NumberFormatException ex){JOptionPane.showMessageDialog(this, "Error: ID must be a number!");}
        });
        screen.add(panel);
        return screen;
}

    private JPanel createCoorDashboard() {
    JPanel panel = new JPanel(new BorderLayout());
    JTabbedPane mainTabs = new JTabbedPane();

    mainTabs.addTab("Scheduling", createSchedulingPanel());
    mainTabs.addTab("Award", createAwardPanel());

    panel.add(mainTabs, BorderLayout.CENTER);
    return panel;
}

    private JPanel createSchedulingPanel() {
    final JTable table = new JTable();
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    JButton back = new JButton("back");
    JButton csvSessionBtn = new JButton("Export Sessions CSV");
    JButton csvSeminarBtn = new JButton("Export Seminars CSV");
    back.addActionListener(e -> showPage("LOGIN"));

    JPanel inputs = new JPanel(new GridLayout(8, 2, 10, 10));
    inputs.setBorder(BorderFactory.createTitledBorder("Add / Manage Seminar Session"));

    JTextField dateIn = new JTextField(), venueIn = new JTextField(),
               typeIn = new JTextField(), presenterIn = new JTextField(),
               evaluatorsIn = new JTextField();

    JButton addBtn = new JButton("Add Session"),
            viewBtn = new JButton("Check Seminar Schedule"),
            lookBtn = new JButton("Check Details");

    inputs.add(new JLabel("Date:")); inputs.add(dateIn);
    inputs.add(new JLabel("Venue:")); inputs.add(venueIn);
    inputs.add(new JLabel("Type:")); inputs.add(typeIn);
    inputs.add(new JLabel("Presenter:")); inputs.add(presenterIn);
    inputs.add(new JLabel("Evaluators:")); inputs.add(evaluatorsIn);
    inputs.add(viewBtn); inputs.add(addBtn);inputs.add(back);
    inputs.add(lookBtn);inputs.add(csvSessionBtn);inputs.add(csvSeminarBtn);

    csvSessionBtn.addActionListener(e -> FileManager.exportCsv(table));

      csvSeminarBtn.addActionListener(e -> {
    JTable tempTable = new JTable(FileManager.getEvaluatorView());
    FileManager.exportCsv(tempTable);
});

    JScrollPane scroll = new JScrollPane(table);
    scroll.setBorder(BorderFactory.createTitledBorder("Seminar Sessions"));

    viewBtn.addActionListener(e -> table.setModel(coord.getSessionsModel()));
    lookBtn.addActionListener(e -> table.setModel(FileManager.getEvaluatorView()));

    addBtn.addActionListener(e -> {
        if (dateIn.getText().isEmpty() || venueIn.getText().isEmpty() || typeIn.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        coord.addSession(dateIn.getText(), venueIn.getText(), typeIn.getText(),
                         presenterIn.getText(), evaluatorsIn.getText());
        table.setModel(coord.getSessionsModel());
        JOptionPane.showMessageDialog(this, "Session added successfully!");
    });

    panel.add(inputs, BorderLayout.NORTH);
    panel.add(scroll, BorderLayout.CENTER);
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
        JButton back = new JButton("Back");
        back.addActionListener(e -> showPage("LOGIN"));
        panel.add(evaluatorTabs, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }
    private JPanel createRegistrationPanel() {
        titleIn = new JTextField();
        abstractIn = new JTextField();
        supervisorIn = new JTextField();
        presentationIn = new JTextField();

        JButton uploadBtn = new JButton("Select Seminar File");
        JButton saveBtn = new JButton("Register / Save");
        JButton viewBtn = new JButton("Check Seminar Schedule");
       
        JButton ResBtn = new JButton("Check Result data");
        JLabel statusLabel = new JLabel("No file selected");
        JButton viewAwardsBtn = new JButton("View Award Winners");

        viewAwardsBtn.addActionListener(e -> {
         String winners = coord.getWinnersList();
         JOptionPane.showMessageDialog(this, winners, "Award Hall of Fame", JOptionPane.INFORMATION_MESSAGE);
        });

        JTable table = new JTable();
        JScrollPane scroll = new JScrollPane(table);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.add(new JLabel("Research Title:")); formPanel.add(titleIn);
        formPanel.add(new JLabel("Abstract:")); formPanel.add(abstractIn);
        formPanel.add(new JLabel("Supervisor Name:")); formPanel.add(supervisorIn);
        formPanel.add(new JLabel("Presentation Type:")); formPanel.add(presentationIn);
        formPanel.add(saveBtn); formPanel.add(uploadBtn);formPanel.add(ResBtn);
        formPanel.add(viewAwardsBtn);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(new JLabel("Status:"), BorderLayout.WEST);
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        statusPanel.add(viewBtn, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        mainPanel.add(scroll, BorderLayout.CENTER);

        viewBtn.addActionListener(e -> table.setModel(coord.getSessionsModel()));
         table.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int row = table.getSelectedRow();
        String status = table.getValueAt(row, 3).toString();
        
        if (status.equals("Available")) {
            coord.selectSchedule(row);
            table.setModel(coord.getSessionsModel()); }}});
   uploadBtn.addActionListener(e -> {
    JFileChooser chooser = new JFileChooser();
    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        String path = chooser.getSelectedFile().getAbsolutePath();
        if (currentStudent == null) {
            currentStudent = new Student(titleIn.getText(), abstractIn.getText(), supervisorIn.getText());
        }
        currentStudent.uploadMaterials(path); 
        statusLabel.setText("File linked: " + chooser.getSelectedFile().getName()); 
    }
});
    saveBtn.addActionListener(e -> {
    if (titleIn.getText().isBlank() || abstractIn.getText().isBlank() || supervisorIn.getText().isBlank()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields!");
        return;}
    if (currentStudent == null) {
        currentStudent = new Student(titleIn.getText(), abstractIn.getText(), supervisorIn.getText());
    } else {currentStudent.researchTitle = titleIn.getText();currentStudent.Abstract = abstractIn.getText();currentStudent.supervisorName = supervisorIn.getText();}
    currentStudent.presentationType = presentationIn.getText();
    FileManager.saveSeminar(userID.getText(), nameField.getText(), currentStudent, finalMarks, nameField.getText());
    JOptionPane.showMessageDialog(this, "All Data Saved!");
});
return mainPanel;}
    private  JPanel createEvaluationPanel() {
        JPanel p = new JPanel(new GridLayout(6, 2, 10, 10));
        JButton calc = new JButton("Calculate Marks");
        JButton scheduleBtn =  new JButton("View schedule");
        JButton viewBtn = new JButton("view student details ");
        JComboBox<String> cBox = new JComboBox<>(eval.clarityOpts);
        JComboBox<String> mBox = new JComboBox<>(eval.methOpts);
        JComboBox<String> pBox = new JComboBox<>(eval.PresOpts);
        JComboBox<String> rBox = new JComboBox<>(eval.resultsOpts);
        p.add(new JLabel("Problem Clarity:")); p.add(cBox);
        p.add(new JLabel("Methodology:")); p.add(mBox);
        p.add(new JLabel("Presentation:")); p.add(pBox);
        p.add(new JLabel("Results:")); p.add(rBox);
        p.add(viewBtn);
        p.add(calc);
        p.add(scheduleBtn);
       
       viewBtn.addActionListener(e -> {
    DefaultTableModel model = FileManager.getEvaluatorView();
    JTable table = new JTable(model);

    int choice = JOptionPane.showConfirmDialog(
        null,
        new JScrollPane(table),
        "Select Student & Click OK",
        JOptionPane.OK_CANCEL_OPTION
    );

    if (choice == JOptionPane.OK_OPTION && table.getSelectedRow() != -1) {
        int row = table.getSelectedRow();
        String id = table.getValueAt(row, 0).toString();
       try {
    String filePath = FileManager.getFilePathForStudent(id);

    if (filePath == null || filePath.equals("None") || filePath.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No material uploaded for this student.");
    } else {
        String cleanPath = filePath
                .replace("\r", "")
                .replace("\n", "")
                .replace("\"", "")
                .trim();
        File f = new File(cleanPath);
        if (f.exists()) {Desktop.getDesktop().open(f);} else {JOptionPane.showMessageDialog(null,"Material NOT found:\n" + cleanPath);
        }}} catch (Exception ex) {JOptionPane.showMessageDialog(null, "Unable to open material.");}
        String input = JOptionPane.showInputDialog("Enter marks for " + table.getValueAt(row, 1));
        if (input != null && !input.isEmpty()) {
            try {
                double marks = Double.parseDouble(input);
                String loggedInEvaluator = nameField.getText();
                FileManager.updateMarks(id, marks, loggedInEvaluator);
                JOptionPane.showMessageDialog(null, "Done! Marks saved to seminars.txt");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid marks! Please enter a number.");
            }}}});
        calc.addActionListener(e -> {
            String cVal = (String) cBox.getSelectedItem();
            String mVal = (String) mBox.getSelectedItem();
            String pVal = (String) pBox.getSelectedItem();
            String rVal = (String) rBox.getSelectedItem();
            eval.problemClarity = eval.new ProblemClarity(cVal);
            eval.methodology = eval.new Methodology(mVal);
            eval.presentation = eval.new Presentation(pVal);
            eval.results = eval.new Results(rVal);
            double finalMarks = eval.getEvaluationMarks();
            JOptionPane.showMessageDialog(this, "Final Marks: " + finalMarks, "Evaluation Result", JOptionPane.INFORMATION_MESSAGE);});
        scheduleBtn.addActionListener(e ->{JTable scheduleTable = new JTable(coord.getSessionsModel());
            JOptionPane.showMessageDialog(this, new JScrollPane(scheduleTable), "Session Schedule", JOptionPane.INFORMATION_MESSAGE);
        });return p;}

       private JPanel createAwardPanel() {
    JPanel p = new JPanel(new BorderLayout(10, 10));
    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    JTable awardTable = new JTable(FileManager.getEvaluatorView());
    JScrollPane scrollPane = new JScrollPane(awardTable);
    p.add(scrollPane, BorderLayout.CENTER);
    JButton btnViewWinners = new JButton("View Winners List");
      btnViewWinners.addActionListener(e -> {
      JOptionPane.showMessageDialog(this, coord.getWinnersList(), "Hall of Fame", JOptionPane.INFORMATION_MESSAGE);
     });
    JButton csvAwardBtn = new JButton("Export Awards CSV");
    csvAwardBtn.addActionListener(e -> FileManager.exportCsv(awardTable));
    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnAssign = new JButton("Assign Award");
    JButton btnRefresh = new JButton("Refresh Nominees");
    controlPanel.add(btnRefresh);
    controlPanel.add(btnAssign);
    controlPanel.add(btnViewWinners);
    controlPanel.add(csvAwardBtn);
    p.add(controlPanel, BorderLayout.SOUTH);
    btnRefresh.addActionListener(e -> {
        awardTable.setModel(FileManager.getEvaluatorView()); });
    btnAssign.addActionListener(e -> {
        int selectedRow = awardTable.getSelectedRow();
        if (selectedRow != -1) {
            String studentId = awardTable.getValueAt(selectedRow, 0).toString();
            String studentName = awardTable.getValueAt(selectedRow, 1).toString();
            String[] options = {"Best Oral", "Best Poster", "People's Choice"};
            String awardChoice = (String) JOptionPane.showInputDialog(null, 
                    "Assign Award to: " + studentName, "Determine Winner", 
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (awardChoice != null) {
                coord.saveWinner(studentName, awardChoice);
                double currentMarks = Double.parseDouble(awardTable.getValueAt(selectedRow, 4).toString());
                FileManager.updateMarks(studentId, currentMarks, "Awarded: " + awardChoice);
                JOptionPane.showMessageDialog(null, "Award granted successfully!");
                awardTable.setModel(FileManager.getEvaluatorView());
            }} else {JOptionPane.showMessageDialog(null, "Please select a student from the table.");}});
            return p;}
    public static void main(String[] args) { new GUI(); }
}
