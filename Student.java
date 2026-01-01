
import java.io.File;
import java.util.Scanner;

public class Student {
    

    public  String researchTitle;
    public  String Abstract;
    public  String supervisorName;
    public  String materialPath;
    public  String presentationType;
    public  Scanner sc = new Scanner(System.in);
    


    public Student(){
       
       
    }

    public String getPresentationType(){
        return presentationType;
    }

   

    public Student(String researchTitle ,String Abstract , String supervisorName ){
       this.researchTitle = researchTitle;
       this.Abstract = Abstract;
       this.supervisorName = supervisorName;
       


    }

    public void presentationType(){
        System.out.println("Enter type of presentation(oral/poster) ; ");
        String choice = sc.nextLine();

       if (choice.equals("oral") || choice.equals("poster")){
        uploadMaterials(sc);
       } else{
        System.out.println("Invalid try again");
       }
        

    }

    public void uploadMaterials(Scanner sc){
            System.out.println("Enter materials path : ");
            String materialPath = sc.nextLine();
            
            File file = new File(materialPath);
             
            if(file.exists() && file.isFile()){
                System.out.println("Material uploaded successfully");
            } else{
                  System.out.println("File not found. Please try again.");
                  uploadMaterials(sc);
            }
         }  

    
}
