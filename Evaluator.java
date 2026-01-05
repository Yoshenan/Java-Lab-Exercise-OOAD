
public class  Evaluator  {
  Student student;
  Methodology methodology;
  ProblemClarity problemClarity;
  Results results;
  Presentation presentation;

  public Evaluator(Student student, Methodology methodology , ProblemClarity problemClarity , Results results , Presentation presentation){
    this.student = student;
    this.methodology = methodology;
    this.problemClarity = problemClarity;
    this.results = results;
    this.presentation = presentation;
     
  }

  public abstract class Evaluation {
    protected String Rating;
    public double marks(){
            return switch (Rating) {
            case "Excellent" -> 4.0;
            case "Good" -> 3.0;
            case "Fair" -> 2.0;
            case "Poor" -> 1.0;
            default -> 0.0;
        };
      }
  }

  public class ProblemClarity extends  Evaluation{
    protected  String problemStatement;

    
    public ProblemClarity(String problemStatement){
      this.problemStatement = problemStatement;
      Rating = switch(problemStatement) {
        case "clear identifications of gap" -> "Excellent";
        case "Problem is clear but weak justification" -> "Good";
        case "broad problem statement" -> "Fair";
        default -> "Poor";
      };
    }
   }
  public class Methodology extends Evaluation{
    protected String dataSampling;
    protected  String researchDesign;

      public Methodology(String dataSampling , String researchDesign){
         this.dataSampling = dataSampling;
         this.researchDesign = researchDesign;
         if(dataSampling.equals("unbiased  ") && researchDesign.equals("logical and clear")){
            Rating = "Excellent";
         } else if(dataSampling.equals("minimal bias , sample is large enough") && researchDesign.equals("design is appropriate")){
            Rating = "Good";
         }else if(dataSampling.equals("noticable bias , sample is smaller") && researchDesign.equals("some logocal gaps")){
            Rating = "Fair";
         } else{
            Rating = "Poor";
         }
         
      }
      }

      public class Results extends Evaluation{
          protected String resultEval;

          public Results(String resultEval){
            this.resultEval= resultEval;
          
          Rating = switch(resultEval) {
          case "Professional Visuals  & Excellent Interpretation" -> "Excellent";
          case "Accurate but some Visual flaw & Good Interpretation  " -> "Good";
          case "Unprofessional Visuals & Needs to polish the interpretation " -> "Fair";
          default  -> "Poor";
      };
      }
    }

       public class Presentation extends Evaluation{
        protected  String delivery;
        protected  String visualAids;

        public Presentation(String delivery, String visualAids) {
        this.delivery = delivery.toLowerCase();
        this.visualAids = visualAids.toLowerCase();
        if (this.delivery.contains("confident") && this.visualAids.contains("professional")) {
            Rating = "Excellent";
        } 
        else if (this.delivery.contains("clear") || this.visualAids.contains("clean")) {
            Rating = "Good";
        } 
        else if (this.delivery.contains("not clear enough ") || this.visualAids.contains("crowded")) {
            Rating = "Fair";
        } 
        else {
            Rating = "Poor";
        }
    }
  }
  public double EvaluationMarks(){
        return (problemClarity.marks() + methodology.marks()+  results.marks() + presentation.marks()) / 4.0;
  }

  public double getEvaluationMarks(){
    return EvaluationMarks();
  }
}

