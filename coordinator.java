import java.io.*;
import javax.swing.table.DefaultTableModel;

public class coordinator {
    private String name;
    private String id;
    private static final String SESSION_FILE = "session.txt";
    private static final String AWARD_FILE = "award.txt";



    public coordinator() {}
    public coordinator(String name, String id) {
        this.name = name;
        this.id = id;
    }
    
    public DefaultTableModel getSessionsModel() {
        String[] columns = {"Date", "Venue", "Type", "Status", "Presenter" , "Evaluators"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        File file = new File(SESSION_FILE);
        if (!file.exists()) return model;
        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length >= 6) { String statusDisplay = row[3].equals("1") ? "Taken" : "Available";
                    model.addRow(new Object[]{row[0], row[1], row[2], statusDisplay, row[4],row[5]});}
                else if(row.length >= 3 ){model.addRow(new Object[]{row[0], row[1], row[2], "Available", "TBD", "TBD"});}}
        } catch (IOException e) {}return model;}

    public void addSession(String date, String venue, String type,String Presentor , String Evaluator) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(SESSION_FILE, true)))) {
            out.println(date + "," + venue + "," + type + ",0," + Presentor +","+ Evaluator);
        } catch (IOException e) {}
    }
public void selectSchedule(int targetRow) {
    StringBuilder allData = new StringBuilder();
    int i = 0;
    
    try (BufferedReader read = new BufferedReader(new FileReader(SESSION_FILE))) {
        String line;
        while ((line = read.readLine()) != null) {
            String[] p = line.split(",");
            if (i == targetRow) {
                // If status is "0" (Available)
                if (p[3].trim().equals("0")) {
                    // SAFETY: If current object name is null, use the name already in file (p[4])
                    String activeName = (this.name != null && !this.name.equals("null")) ? this.name : p[4];
                    
                    // Reconstruct: Date, Venue, Type, Status(1), Presenter, Evaluator
                    line = p[0] + "," + p[1] + "," + p[2] + ",1," + activeName + "," + (p.length > 5 ? p[5] : "TBD");
                }
            }
            allData.append(line).append("\n");
            i++; 
        }
    } catch (IOException e) { e.printStackTrace(); }

    // Save the updated data back to the file
    try (PrintWriter write = new PrintWriter(new FileWriter(SESSION_FILE))) {
        write.print(allData.toString());
    } catch (Exception e) { e.printStackTrace(); }
}
    public String[] getStudentDetails(String id) {
    if (id == null || id.trim().isEmpty()) {return null; }
    try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");
            if (p.length > 0 && p[0].trim().equals(id.trim())) {
                return p;}}
    } catch (IOException e) { e.printStackTrace(); }
    return null;}

   

    public void saveWinner(String name, String award) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(AWARD_FILE, true))) {
        writer.write(name + "," + award);
        writer.newLine();
    } catch (IOException e) { e.printStackTrace(); }
}



public String getWinnersList() {
    StringBuilder sb = new StringBuilder("--- Current Award Winners ---\n");
    try (BufferedReader br = new BufferedReader(new FileReader(AWARD_FILE))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                sb.append("Student: ").append(parts[0]).append(" | Award: ").append(parts[1]).append("\n");
            }
        }
    } catch (IOException e) { return "No awards assigned yet."; }
    return sb.toString();
}

}