
import java.io.File;

public class Student {
    public  String researchTitle;
    public  String Abstract;
    public  String supervisorName;
    public  String presentationType;
    public   String materialPath;

    public String getMaterialPath(){
        return this.materialPath;
    }
    public Student(String researchTitle ,String Abstract , String supervisorName ){
       this.researchTitle = researchTitle;
       this.Abstract = Abstract;
       this.supervisorName = supervisorName;
    }

    public boolean uploadMaterials(String path) {
        if (path == null || path.isEmpty()) return false;
        
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            this.materialPath = path; 
            return true;
        }
        return false;
    }

}
