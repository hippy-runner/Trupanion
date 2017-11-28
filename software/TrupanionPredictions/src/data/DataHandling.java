package data;

import algorithms.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Encapsulates the functionality for reading/writing data to a CSV file.
 *  
 * @author Nathan Minor
 */

public class DataHandling {
    
    // internal class variables for storing data in/out of a CSV file
    private String _inFile;  // stores the absolute file path for the input CSV file
    private String _outFile; // stores the absolute file path for the output CSV file
    
    /**
     * Default constructor to populate with blank data upon instantiation.
     **/
    public DataHandling() {
        this._inFile = new String();
        this._outFile = new String();
    }
    
    /**
     * Class constructor to populate with file paths upon instantiation.
     * @param inputFile Absolute file path for the input file.
     * @param outFile Absolute file path for the output file.
     **/
    public DataHandling(String inputFile, String outFile) {
        this._inFile = inputFile;
        this._outFile = outFile;
    }
    
    /**
     * "Get" method for retrieving the input file path specified by this instance.
     * @return Absolute file path for input CSV file.
     */
    public String getInputFilePath() {
        return this._inFile;
    }
    
    /**
     * "Get" method for retrieving the output file path for the prediction results specified by this instance. 
     * @return Absolute file path for the predictions CSV file.
     */
    public String getPredOutputFilePath() {
        return this._outFile;
    }
    
    /**
     * "Set" method for retrieving the input file path specified by this instance.
     * @param filePath the new absolute file path for the input CSV file
     */
    public void setInputFilePath(String filePath) {
        this._inFile = filePath;
    }
    
    /**
     * "Set" method for retrieving the output file path specified by this instance.
     * @param filePath the new absolute file path for the output CSV file
     */
    public void setPredOutputFilePath(String filePath) {
        this._outFile = filePath;
    }
    
    /**
     * Method used to retrieve data from input CSV file.
     * @return 
     */
    public List<ClaimLevel> RetrieveData() {
        
        // local variables
        List<ClaimLevel> csvData  = new ArrayList<>(); // list for row-vector data from csv file
        File csvFile = null; // File object for retrieving data from csv file thru a file scanner
        String row = ""; // stores the next row parsed out of the csv file
        String[] cells = null; // stores the row parsed into cells
        Date claimDate = null; // used to temp store the ClaimDate from a row
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // used to parse claim dates
        long policyId = 0;  // used to temp store the PolicyId from a row
        double claimedAmount = 0.0; // used to temp store the ClaimedAmount from a row
        double paidAmount = 0.0; // used to temp store the PaidAmount from a row
        
        try {
            // create the csv File object
            csvFile = new File(this._inFile);
            
            // use scanner object to pull data from csv file one line at a time...
            Scanner inputStream = new Scanner(csvFile);
            
            // iterate through the csv file row by row...
            while (inputStream.hasNext()) {
                
                // grab each row and parse it into an array of cells
                row = inputStream.next();
                cells = row.split(",");

                if (cells[0].matches("\\d*")){
                    
                    // temp store values from each row
                    policyId = Long.parseLong(cells[0]);
                    claimDate = df.parse(cells[1]);
                    claimedAmount = Double.parseDouble(cells[2]);
                    paidAmount = Double.parseDouble(cells[3]);

                    // add the data to the custom object list
                    csvData.add(new ClaimLevel(policyId, claimDate, claimedAmount, paidAmount));
                }
            }
            
        } catch (FileNotFoundException | NumberFormatException | ParseException ex) {
            // catch any exceptions and print them to the err output
            System.err.print(ex.getMessage());
        }
        
        // return retrieved data
        return csvData;
    }
    
    /**
     * Saves summary data for making pretty plots later.
     * @param fEngine ForecastingEngine object that has all the data/calculations
     * @return success flag for error trapping
     */
    public boolean SavePredictions(ForecastEngine fEngine) {
        
        // local variables
        boolean success = true; // success flag used to indicate the success or failure of this method
        File csvFile = null;
        PrintWriter pw = null;
        StringBuilder sb = null;
        Iterator cursor = null;
        PayoutPrediction tmpPred = null;
        
        // conduct all code in a try-catch block for error trapping
        try {
            
            // setup csv file object
            csvFile = new File(this._outFile);
            
            // create file if it does not exist
            csvFile.createNewFile();
            
            // setup print writer and string builder objects
            pw = new PrintWriter(csvFile);
            sb = new StringBuilder();
            
            // write out column headers
            sb.append("PolicyId");
            sb.append(',');
            sb.append("Year");
            sb.append(',');
            sb.append("Month");
            sb.append(',');
            sb.append("PrevMonth");
            sb.append(',');
            sb.append("MeanTimeBetween");
            sb.append(',');
            sb.append("TimingProb");
            sb.append(',');
            sb.append("TargetMean");
            sb.append(',');
            sb.append("Efm");
            sb.append(',');
            sb.append("EfmI");
            sb.append(',');
            sb.append("EfmD");
            sb.append(',');
            sb.append("EstimatedPayout");
            sb.append(',');
            sb.append("ActualPayout");
            sb.append(',');
            sb.append("PercentError");
            sb.append('\n');
            
            // setup cursor (iterator) to walk thru policy predictions
            cursor = fEngine.getPredictions().iterator();
            
            // iterate thru policy predictions
            while (cursor.hasNext()) {
                
                // grab the next prediction
                tmpPred = (PayoutPrediction) cursor.next();

                // write out prediction as a new row in csv file
                sb.append(tmpPred.getPolicyId());
                sb.append(',');
                sb.append(tmpPred.getYear());
                sb.append(',');
                sb.append(tmpPred.getMonth());
                sb.append(',');
                sb.append(tmpPred.getPrevMonth());
                sb.append(',');
                sb.append(tmpPred.getMeanTimeBetween());
                sb.append(',');
                sb.append(tmpPred.getTimingProbability());
                sb.append(',');
                sb.append(tmpPred.getTargetMean());
                sb.append(',');
                sb.append(tmpPred.getEfm());
                sb.append(',');
                sb.append(tmpPred.getEfmI());
                sb.append(',');
                sb.append(tmpPred.getEfmD());
                sb.append(',');
                sb.append(tmpPred.getEstimatedPayout());
                sb.append(',');
                sb.append(tmpPred.getActualPayout());
                sb.append(',');
                sb.append(tmpPred.getPercentError());
                sb.append('\n');                
            }
            
            // publish written-out csv data from string to csv file
            pw.write(sb.toString());
            
            // flush the stream and close the file
            pw.flush();
            pw.close();
            
        // catch any exceptions here:
        } catch (Exception ex) {
            
            // set success to false and print exception
            success = false;
            System.err.println(ex.getMessage());
            
        }
        
        // return success flag
        return success;
    }
}
