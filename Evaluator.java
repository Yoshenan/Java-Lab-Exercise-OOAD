public class Evaluator {

    public String[] clarityOpts = {
        "clear identifications of gap",
        "Problem is clear but weak justification",
        "broad problem statement",
        "Poor"
    };

    public String[] methOpts = {
        "unbiased",
        "minimal bias",
        "noticable bias",
        "biased",
        "Poor"
    };

    public String[] resultsOpts = {
        "Professional Visuals  & Excellent Interpretation",
        "Accurate but some Visual flaw & Good Interpretation",
        "Unprofessional Visuals & Needs to polish the interpretation",
        "bad visuals & bad interpretation"
    };

    public String[] PresOpts = {
        "Confident & Professional",
        "Clear & Clean",
        "Not Confident & Crowded",
        "Unclear"
    };

    ProblemClarity problemClarity;
    Methodology methodology;
    Results results;
    Presentation presentation;

 

    public abstract class Evaluation {
        protected String Rating;

        public double marks() {
            return switch (Rating) {
                case "Excellent" -> 4.0;
                case "Good" -> 3.0;
                case "Fair" -> 2.0;
                case "Poor" -> 1.0;
                default -> 0.0;
            };
        }
    }



    public class ProblemClarity extends Evaluation {

        public ProblemClarity(String input) {
            Rating = switch (input) {
                case "clear identifications of gap" -> "Excellent";
                case "Problem is clear but weak justification" -> "Good";
                case "broad problem statement" -> "Fair";
                default -> "Poor";
            };
        }
    }

  

    public class Methodology extends Evaluation {

        public Methodology(String input) {
            Rating = switch (input) {
                case "unbiased" -> "Excellent";
                case "minimal bias" -> "Good";
                case "noticable bias" -> "Fair";
                default -> "Poor";
            };
        }
    }


    public class Results extends Evaluation {

        public Results(String input) {
            Rating = switch (input) {
                case "Professional Visuals  & Excellent Interpretation" -> "Excellent";
                case "Accurate but some Visual flaw & Good Interpretation" -> "Good";
                case "Unprofessional Visuals & Needs to polish the interpretation" -> "Fair";
                default -> "Poor";
            };
        }
    }

  

    public class Presentation extends Evaluation {

        public Presentation(String input) {
            input = input.toLowerCase();

            if (input.contains("confident")) {
                Rating = "Excellent";
            } else if (input.contains("clear")) {
                Rating = "Good";
            } else if (input.contains("not confident")) {
                Rating = "Fair";
            } else {
                Rating = "Poor";
            }
        }
    }

    public double getEvaluationMarks() {

        if (problemClarity == null || methodology == null ||
            results == null || presentation == null) {
            return 0.0;
        }

        return (problemClarity.marks() +
                methodology.marks() +
                results.marks() +
                presentation.marks()) / 4.0;
    }
}
