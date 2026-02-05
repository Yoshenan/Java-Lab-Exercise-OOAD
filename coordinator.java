import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
public void selectSchedule(int rowIndex) {
    List<String> lines = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) { 
                lines.add(line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        return;
    }

    if (rowIndex >= 0 && rowIndex < lines.size()) {
        String[] data = lines.get(rowIndex).split(",", -1);
        if (data.length >= 3) {
            String date = data[0];
            String venue = data[1];
            String type = data[2];
            String presenter = (data.length > 4) ? data[4] : "TBD";
            String evaluators = (data.length > 5) ? data[5] : "TBD";
            
            String updatedLine = date + "," + venue + "," + type + ",1," + presenter + "," + evaluators;
            lines.set(rowIndex, updatedLine);
        }
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(SESSION_FILE))) {
        for (String s : lines) {
            bw.write(s);
            bw.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
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


public boolean isDateTaken(String date) {
        File file = new File(SESSION_FILE);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length > 0 && row[0].trim().equalsIgnoreCase(date.trim())) {
                    return true; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; 
    }

}