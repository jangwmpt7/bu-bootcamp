import java.io.*; 
import java.util.ArrayList;
 
public class GradeAnalyzer {
    public static int totalProcessed = 0;
    public static int invalidLines = 0;

    public static void main(String[] args) {
        // Step 1: read scores from file
        ArrayList<Integer> scores = readScores("scores.txt");
        //      System.out.println("Scores read from file: " + scores);
        //      System.out.println("Total scores processed: " + totalProcessed + ", Invalid lines skipped: " + invalidLines);
        if (scores.isEmpty()) {
            System.out.println("The file, scores.txt, is empty or lacks valid data.");
            return; // Exit the program gracefully after printing a message if the file is empty or lacking valid data.
        }
        
        // Step 1.5: Find the highest and lowest scores, not by using Collections.max(scores) or Collections.min(scores), but by loop iteration.
        int high = Integer.MIN_VALUE;
        int low = Integer.MAX_VALUE;
        for (int score : scores) {
            if (score > high) { high = score; }
            if (score < low) { low = score; }
        }
        //      System.out.println("Max: " + high + ", Min: " + low);

        // Step 2: calculate statistics
        double avg = calculateAverage(scores);
        //      System.out.println("Average score: " + avg);
        
        // Step 3: write and print report
        writeReport(scores, avg, high, low, "report.txt");
    } 
 
    // Returns a list of valid scores read from the file
    public static ArrayList<Integer> readScores(String filename) {
        ArrayList<Integer> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){ // Use BufferedReader to read the file...
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {  // ...line by line.
                lineNumber++;
                if (!line.trim().isEmpty()) { // Skip blank lines.
                    try { // Parses each line.
                        int score = Integer.parseInt(line.trim());
                        if (score >= 0 && score <= 100) { scores.add(score); } // A valid score is added to an array list of scores.
                        else { // If the score is not valid, print out a warning and skip the line.
                            System.out.println("Invalid score found on line #" + lineNumber + ": " + score);
                            invalidLines++;
                        }
                        totalProcessed++;
                    } catch (NumberFormatException e) { // Prints out a warning and skips the line if parseInt throws NumberFormatException.
                        System.out.println("Invalid data found on line #" + lineNumber + ": '" + line + "'");
                        invalidLines++;
                    }
                }
                else { 
                    System.out.println("Line #" + lineNumber + " is blank and has been skipped.");
                    invalidLines++;
                }
            }
        } catch (IOException e) { System.out.println("Could not read file: " + e.getMessage()); }
        return scores; // Returns the list of scores, be it empty or not.
    }
 
    // Returns the average of a list of scores, or 0.0 if the list is empty
    public static double calculateAverage(ArrayList<Integer> scores) {
        // your code here
        if (scores.isEmpty()) { // If the scores list is empty, return 0.0 immediately.  
            return 0.0;
        } else {
            int sum = 0;
            for (int score : scores) { sum += score; } //Loop through all scores and accumulate the total in a double variable.
            return (double) sum / scores.size(); // Return the total divided by scores.size().
        }
    } 
 
    // Writes and prints the report
    public static void writeReport(ArrayList<Integer> scores,
                                   double avg, int high, int low,
                                   String outputFile) {
        int countA = 0; //Count the grade bands, starting with creating 5 int counters: countA, countB, countC, countD, and countF, all starting at 0.
        int countB = 0;
        int countC = 0;
        int countD = 0;
        int countF = 0;

        for (int score : scores) { // Loop through the scores list, using if, else if, and else to assign each score to the correct band and increment the counter.
            if (score >= 90) { countA++; } // A = 90+
            else if (score >= 80) { countB++; } // B = 80-89
            else if (score >= 70) { countC++; } // C = 70-79
            else if (score >= 60) { countD++; } // D = 60-69
            else { countF++; } // F = below 60
        }

        //Set up format rules.
        String linesFormatRule = "%-23s %3d%n";
        String doubleScoreFormatRule = "%-16s %3.2f%n";
        String intScoreFormatRule = "%-16s %3d%n";
        String gradeDistributionFormatRule = "%-16s %d%n";


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) { // Use BufferedWriter and FileWriter to write to report.txt.
            writer.write("=== Grade Analysis Report ===\n");
            writer.write(String.format(linesFormatRule, "Total scores processed:", totalProcessed));
            writer.write(String.format(linesFormatRule, "Invalid lines skipped:", invalidLines));
            writer.newLine(); // Use String.format for aligned columns to format the report neatly.
            writer.write(String.format(doubleScoreFormatRule, "Average score:", avg));
            writer.write(String.format(intScoreFormatRule, "Highest score:", high));
            writer.write(String.format(intScoreFormatRule, "Lowest score:", low));
            writer.newLine();
            writer.write("Grade Distribution:\n");
            writer.write(String.format(gradeDistributionFormatRule, "  A (90-100):", countA));
            writer.write(String.format(gradeDistributionFormatRule, "  B (80-89):", countB));
            writer.write(String.format(gradeDistributionFormatRule, "  C (70-79):", countC));
            writer.write(String.format(gradeDistributionFormatRule, "  D (60-69):", countD));
            writer.write(String.format(gradeDistributionFormatRule, "  F (below 60):", countF));
        } catch (IOException e) {
            System.out.println("Could not write file: " + e.getMessage());
        }
            
    }
} 