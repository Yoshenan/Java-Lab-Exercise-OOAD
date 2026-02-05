import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FileManager {
    private static final String FILE_PATH = "seminars.txt";

    // 1. SAVE: Records new seminar data
    public static void saveSeminar(String id, String Name, Student s, double marks, String eval) {
        String path = (s.getMaterialPath() == null || s.getMaterialPath().isEmpty()) ? "None" : s.getMaterialPath();
        String data = id + "," + Name + "," + s.researchTitle + "," + s.supervisorName + "," + 
                     s.Abstract + "," + s.presentationType + "," + path + "," + marks + "," + eval;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 2. VIEW: Prepares the table model for the Evaluator
    public static DefaultTableModel getEvaluatorView() {
        String[] columns = {"ID", "Student Name", "Research Title", "Type", "Abstract", "Marks", "Evaluator"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 9) {
                    model.addRow(new Object[]{ d[0], d[1], d[2], d[5], d[4], d[7], d[8]});
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return model;
    }

    // 3. UPDATE: Updates marks and evaluator name for a specific student ID
    public static void updateMarks(String studentId, double marks, String EvalName) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length > 0 && p[0].trim().equals(studentId.trim())) {
                    // Re-indexes: 0:ID, 1:Name, 2:Title, 3:Super, 4:Abs, 5:Type, 6:Path, 7:Marks, 8:Eval
                    line = p[0] + "," + p[1] + "," + p[2] + "," + p[3] + "," + p[4] + "," + 
                           p[5] + "," + p[6] + "," + marks + "," + EvalName;
                }
                data.append(line).append("\n");
            }
        } catch (IOException e) { e.printStackTrace(); }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))){
            writer.write(data.toString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    // 4. SEARCH: Finds full student details by Name (Required by Coordinator)
    public static String[] getStudentDetailsByName(String name) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] s = line.split(",");
                if (s.length > 1 && s[1].trim().equalsIgnoreCase(name.trim())) {
                    return s;
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    // 5. FILE PATH: Gets the material path by student ID
    public static String getFilePathForStudent(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(","); 
                if (p.length >= 7 && p[0].trim().equals(id.trim())) {
                    return p[6].trim();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 6. EXPORT: Saves a JTable's data into a CSV file
    public static void exportCsv(JTable table){
        JFileChooser choose = new JFileChooser();
        choose.setDialogTitle("Save as CSV");

        if(choose.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ){
            File file = choose.getSelectedFile();
            if(!file.getName().toLowerCase().endsWith(".csv")){
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(file))){
                for (int i = 0; i < table.getColumnCount(); i++) {
                    writer.print(table.getColumnName(i) + (i == table.getColumnCount() - 1 ? "" : ","));
                }
                writer.println();
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Object val = table.getValueAt(i, j);
                        String cleanData = (val != null) ? val.toString().replace(",", ";") : "";
                        writer.print(cleanData + (j == table.getColumnCount() - 1 ? "" : ","));
                    }
                    writer.println();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}