
/**
 * Console application for calculating payout predictions
 * per policyId for January 2017.
 * 
 * @author Nathan Minor
 */

// import libraries for use here:

import algorithms.*;
import data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    /**
     * Executable entry point.
     * @param args 
     */
    public static void main(String[] args) {
        
        // local variables
        boolean success = true; // success flag for error trapping
        long start = 0; // use to run a stopwatch over different segments of the process
        Scanner keyboard = new Scanner(System.in);  // Scanner class instance used for getting user input
        String inFilePath = ""; // used for storing the file path to the original data file
        String outFilePath = ""; // used for storing the file path to the original data file
        DataHandling dh = null; // used to handle that data
        List<ClaimLevel> csvData = null; // stores raw parsed data from csv
        ForecastEngine fEngine = null; // custom class object that does all the heavy lifting
        List<String> monthYears = new ArrayList<>();
        
        monthYears.add("Jan16");
        monthYears.add("Feb16");
        monthYears.add("Mar16");
        monthYears.add("Apr16");
        monthYears.add("May16");
        monthYears.add("Jun16");
        monthYears.add("Jul16");
        monthYears.add("Aug16");
        monthYears.add("Sep16");
        monthYears.add("Oct16");
        monthYears.add("Nov16");
        monthYears.add("Dec16");
        monthYears.add("Jan17");
        
        try {
            // get the input file path
            System.out.println("Enter absolute file path for input CSV data file:");
            inFilePath = keyboard.next();

//            // get the number of clusters to compute
//            System.out.println("Enter the month to forecast (0 = Jan 2016,..., 12 = Jan 2017):");
//            month = Integer.parseInt(keyboard.next());
            
            for (int month = 0; month < monthYears.size(); month++) {
                
                System.out.println();
                System.out.println("Predictions for " + monthYears.get(month));
            
                // setup output file path
                outFilePath = 
                        inFilePath.substring(0, (inFilePath.lastIndexOf("/") + 1)) 
                        + "Predictions" + monthYears.get(month) + ".csv";

                // start the clock
                start = System.currentTimeMillis();

                // update console with progress
                System.out.print("\t->retrieving data...");

                // creat data handler
                dh = new DataHandling(inFilePath, outFilePath);

                // retrieve data from csv file

                csvData = dh.RetrieveData();

                // only continue if there was data retieved
                if (!csvData.isEmpty()) {

                    // update console with progress
                    System.out.print("data retrieved (time:" + (System.currentTimeMillis() - start) + " ms)\n");

                    // reset the clock
                    start = System.currentTimeMillis();

                    // create a new forecasting object
                    fEngine = new ForecastEngine(csvData);

                    // update console with progress
                    System.out.print("\t->calculating summaries...");

                    // calculate monthly summaries for each policy
                    success = fEngine.CalculatePolicySummaries();

                    // only continue if successful
                    if (success) {

                        // update console with progress
                        System.out.print("summaries calculated \t(time:" + (System.currentTimeMillis() - start) + " ms)\n");

                        // reset the clock
    //                    start = System.currentTimeMillis();
    //
    //                     update console with progress
    //                    System.out.print("\t->computing clusters...");
    //
    //                     compute clusters
    //                    success = fEngine.ComputeClusters(45, 0.1);
    //
    //                     only continue if successful
                        if (success) {
    //
    //                         update console with progress
    //                        System.out.print("clusters computed \t(time:" + (System.currentTimeMillis() - start) + " ms)\n");

                            // reset the clock
                            start = System.currentTimeMillis();

                            // update console with progress
                            System.out.print("\t->calculating predictions...");

                            // calculate monthly predictions for each policy
                            success = fEngine.CalculatePredictions(month);

                            // only continue if successful
                            if (success) {

                                // update console with progress
                                System.out.print("predictions calculated \t(time:" + (System.currentTimeMillis() - start) + " ms)\n");

                                // reset the clock
                                start = System.currentTimeMillis();

                                // update console with progress
                                System.out.print("\t->saving predictions...");

                                // save predictions (and calculations)
                                success = dh.SavePredictions(fEngine);

                                // only continue if successful
                                if (success) {

                                    // update console with progress
                                    System.out.print("output data saved \t(time:" + (System.currentTimeMillis() - start) + " ms)\n");
                                }
                            }
                        }
                    }           
                }
            }
        } catch (Exception ex) {
            System.out.println();
            System.out.println(ex.getMessage());
        }
        
    }
}
