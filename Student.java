
import java.io.File;
import java.util.Scanner;

public class Student {
    

    public  String researchTitle;
    public  String Abstract;
    public  String supervisorName;
    public  String presentationType;
    public   String materialPath;
    public  Scanner sc = new Scanner(System.in);
    

    public String getPresentationType(){
        return presentationType();
    }

    public String getMaterialPath(){
        return this.materialPath;
    }

   

    public Student(String researchTitle ,String Abstract , String supervisorName ){
       this.researchTitle = researchTitle;
       this.Abstract = Abstract;
       this.supervisorName = supervisorName;
       


    }

    public String presentationType(){
        System.out.println("Enter type of presentation(oral/poster) ; ");
        String choice = sc.nextLine();

       if (choice.equals("oral") || choice.equals("poster")){
          uploadMaterials("");
       } else{
        System.out.println("Invalid try again");
       }
        return choice;

    }

    public boolean uploadMaterials(String path){
            
            File file = new File(path);
             
            if(file.exists() && file.isFile()){
                this.materialPath = path;
                return true;
            } else{
                  return false;
                  
            }
         }  
}
