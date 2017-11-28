/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms;

import data.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Encapsulates the main algorithm functionality for calculating payout predictions.
 * @author Nathan Minor
 */
public class ForecastEngine {
    
    // private local variables
    private List<ClaimLevel> _rawData;
    private List<MonthlySummary> _monthlySummaries;
    private List<PayoutSummary> _payoutSummaries;
    private List<PayoutPrediction> _predictions;
    
    /**
     * Constucts a new instance of the main algorithm class.
     * @param csvData raw data from CSV file stored in an array list of row-vector objects
     */
    public ForecastEngine(List<ClaimLevel> csvData) {
        this._rawData = csvData;
        this._monthlySummaries = new ArrayList<>();
        this._payoutSummaries = new ArrayList<>();
        this._predictions = new ArrayList<>();
    }
    
    /**
     * "Get" method used for retrieving the list of predictions.
     * @return list of payout predictions as row-vector objects
     */
    public List<PayoutPrediction> getPredictions(){
        return this._predictions;
    }
    
    /**
     * "Get" method used for retrieving the list of predictions.
     * @return list of payout predictions as row-vector objects
     */
    public List<PayoutSummary> getPayoutSummaries(){
        return this._payoutSummaries;
    }
    
    /**
     * Makes the calculations to populate aggregate summary data for use in
     * forecasting predictions.
     * @return success flag used for error trapping
     */
    public boolean CalculatePolicySummaries(){
        
        // local variable definitions
        boolean success = true;
        int cMonth = 0;
        int monthlyCount = 0;
        long cPolicyId = 0;
        double monthlySum = 0.0;
        double overallSum = 0.0;
        Calendar cal = Calendar.getInstance();
        PayoutSummary tmp = null;
        List<Double> tmpData = new ArrayList<>();
        List<Integer> tmpTimeline = new ArrayList<>();
        
        // use try-catch block for error trapping purposes
        try {
            
            // first, sort the CSV data by policyId, then by date
            Collections.sort(this._rawData);

            // walk thru csv data and populate summaries
            for (ClaimLevel tmpRow : this._rawData) {

                // setup a calendar to parse the month number out of the claim dates
                cal.setTime(tmpRow.getClaimDate());

                // check to see if a new month has been encountered
                if (cMonth < cal.get(Calendar.MONTH)) { 

                    // add info to monthly summaries list before resetting
                    this._monthlySummaries.add(
                            new MonthlySummary(
                                    cPolicyId, 
                                    cMonth, 
                                    monthlySum, 
                                    monthlyCount));

                    // reset monthly counters and sum when a new month passes
                    cMonth = (int) cal.get(Calendar.MONTH);
                    monthlyCount = 0;
                    monthlySum = 0;
                }

                // check to see if a new policyId has been encountered
                if (cPolicyId < tmpRow.getPolicyId()) {

                    // dont' forget to add info to monthly summaries list before resetting
                    // but only do so if this isn't the first iteration
                    if (cPolicyId > 0) {
                        
                        this._monthlySummaries.add(
                                new MonthlySummary(
                                        cPolicyId, 
                                        cMonth, 
                                        monthlySum, 
                                        monthlyCount));
                    }
                    
                    // only reset the current policyId when a new policy Id is encountered
                    cPolicyId = tmpRow.getPolicyId();

                    // but also reset monthly counters and sum when a new policyId is encountered
                    cMonth = (int) cal.get(Calendar.MONTH);
                    monthlyCount = 0;
                    monthlySum = 0;

                }

                // check to see if this claim was ever paid out
                if (tmpRow.getPaidAmount() > 0) {
                    
                    // increase counter and add to sum if so
                    monthlyCount++;
                    monthlySum += tmpRow.getPaidAmount();
                }
            }
            
            // also reset policyId, and all the other temp counters/sums/etc.
            cPolicyId = 0;
            cMonth = 0;
            monthlyCount = 0;
            monthlySum = 0;

            // iterate over monthly payout summaries per policyId, populating policy payout summaries
            for (MonthlySummary tmpSummary : this._monthlySummaries) {
                
                // reset when a new policyId is encountered
                if (cPolicyId < tmpSummary.getPolicyId()) {
                                        
                    // add info to payout summaries list before resetting
                    // but not on the first iteration...
                    if (cPolicyId > 0) {
                        // create new payout summary
                        tmp = new PayoutSummary(cPolicyId, overallSum, tmpData, tmpTimeline);
                        
                        // add the newly created and initialized payout summary to the list
                        this._payoutSummaries.add(tmp);                        
                    }                    
                    
                    // only reset the current policyId when a new policy Id is encountered
                    cPolicyId = tmpSummary.getPolicyId();
                    
                    // also reset data sum and tmp data list when a new policyId is encountered
                    overallSum = 0;
                    tmpData = new ArrayList<>();
                    tmpTimeline = new ArrayList<>();
                }
                
                // increment overall counters/sums appropriately
                overallSum += tmpSummary.getSum();
                
                // add payout data to tmp list
                tmpData.add(tmpSummary.getSum());
                tmpTimeline.add(tmpSummary.getMonth());
            }
        // catch ANY exceptions here:  
        } catch (Exception ex) {
            // set the success flag to false to indicate failure :(
            success = false;
            System.err.println(ex.getMessage());
        }
        
        // return success flag
        return success;
    }
    
    /**
     * Method used to calculate the intial values for the centroids on k clusters.
     * Starting with randomly selected indices.
     * @param k Number of clusters to find centroids for.
     * @return 
     */
    public List<Centroid> FindInitialCentroids(int k) {
        
        // local variables
        int random = 0;
        Centroid tmp;
        List<Centroid> centroids;
        Random r;
        
        // initialize objects
        centroids = new ArrayList<>();
        r = new Random();
        
        // use try-catch block for trapping errors
        try {
            
            // loop k times for k clusters
            for (int i = 0; i < k; i++) {
                
                // get the next random index
                random = r.nextInt(this._payoutSummaries.size());
                
                // find a new random centroid at that random index
                tmp = this._payoutSummaries.get(random).getCentroid();
                
                // only add unique centroids
                if (!centroids.contains(tmp)) {
                    centroids.add(tmp);
                }
            }
            
        // catch any exceptions here:
        } catch (Exception ex) {
            
            // print exceptions when caught
            System.err.println(ex.getMessage());
        }
        
        // return list of randomly selected centroids
        return centroids;
    }
    
    /**
     * Method used for cimputing the clustering groups on the payout summaries.
     * @param k Number of clusters to form.
     * @param epsilon (epsilon) Threshold value for determining when to stop computing.
     * @return Success flag for error trapping.
     */
    public boolean ComputeClusters(int k, double epsilon) {
        
        // local variables
        boolean success = true;
        int clusterSize = 0;
        double clusterSumX = 0.0;
        double clusterSumY = 0.0;
        double centroidDiff = epsilon;        
        
        Centroid current;
        Centroid closest;
        List<Centroid> initialCentroids;
        List<Centroid> newCentroids;
        
        // use try-catch block for error trapping purposes
        try {
            
            // (1) get initial centroids
            initialCentroids = this.FindInitialCentroids(k);
            
            /// LOOP START
            while (centroidDiff >= epsilon) {
            
                // reset k to match the size of initial Centroids,
                // handles cases where less clusters are found than k.
                k = initialCentroids.size();

                // set up list objects for storing the new centroid values and differnces
                newCentroids = new ArrayList<>();

                // reset variable for calculating centroid differences
                centroidDiff = 0  ;

                // (2) loop through and assign each policyId to its nearest centroid
                for (int i = 0; i < this._payoutSummaries.size(); i++) {

                    // assign the current policyId's centroid to a temp variable
                    current = this._payoutSummaries.get(i).getCentroid();
                    
                    // snag a starting closest centroid
                    closest = initialCentroids.get(0);

                    // loop through cluster centroids looking for the closest centroid
                    for (Centroid o : initialCentroids) {

                        // compare the distance between the current centroid in the loop, p, and
                        // the closest centroid, with the distance from the closest to the current 
                        // summary's centroid, keeping the lowest value
                        if (o.distanceTo(current) < closest.distanceTo(current)) {
                            closest = o;
                        }
                    }

                    this._payoutSummaries.get(i).setCentroid(closest);
                }

                // sort payout summaries now that cluster assignments have been made
//                Collections.sort(this._payoutSummaries);

                // (3) calculate new centroid values based on clustering assignment from (2)
                for (Centroid c : initialCentroids) {

                    // reset cluster size and sums
                    clusterSize = 0;
                    clusterSumX = 0;
                    clusterSumY = 0;

                    // loop over all policyIds
                    for (PayoutSummary tmp : this._payoutSummaries) {

                        // only focusing on policy Ids in current cluster
                        if (tmp.getCentroid().equals(c)) {

                            // increment cluster size counter
                            clusterSize++;

                            // add to cluster sums, for calc new centroid values
                            clusterSumX += tmp.getAvgBetweenTime();
                            clusterSumY += tmp.getMean();
                        }
                    }

                    // only add new centroid if cluster size > 0
                    if (clusterSize > 0) {
                        // add new centroid to list
                        newCentroids.add(new Centroid(
                                clusterSumX / clusterSize,
                                clusterSumY / clusterSize
                            ));
                    }

                }

                // (4) calculate the differences between new centroids and initial centroids
                for (Centroid o : initialCentroids) {
                    for (Centroid p : newCentroids) {
                        double tmp = o.distanceTo(p);
                        if (!Double.isNaN(tmp)) {
                            centroidDiff += tmp;
                        } else {
                            centroidDiff += 0;
                        }
                    }
                }
                
                // (5) set next initial centroids to the list of new centroids
                initialCentroids = newCentroids;
            }
        } catch (Exception ex) {
            // set the success flag to false to indicate failure :(
            success = false;
            System.err.println(ex.getMessage());
        }
        
        // return the success flag for error trapping
        return success;
    }
    
    /**
     * This method does the all-important task of calculating the predictions.
     * @param month the month to make a forecast for, starting the count at 0 for Jan 2016 (e.g., Jan 2017 = 12)
     * @return success flag used for error trapping purposes
     */
    public boolean CalculatePredictions(int month) {
        
        // local variables
        boolean success = true;
        Iterator cursor = null;
        PayoutSummary tmpSummary = null;
                
        // use try-catch block for error trapping purposes
        try {
            
            // setup a new iterator (like an SQL cursor) for walking thru payout
            // summaries and calculating predictions
            cursor = this._payoutSummaries.iterator();
            
            // iterate thru payout summaries
            while (cursor.hasNext()) {
                
                // get the next row-vector object
                tmpSummary = (PayoutSummary) cursor.next();
                
                // get and store prediction
                this._predictions.add(this.Prediction(tmpSummary, month));
            }
            
        // catch any exceptions here:
        } catch (Exception ex) {
            // set the success flag appropriately, and send an error msg
            success = false;
            System.err.print(ex.getMessage());
        }
        
        // return success flag
        return success;
    }
    
    private PayoutPrediction Prediction(PayoutSummary s, int t) {
        
        // local variables
        int prevT = 0;
        double prediction = 0.0;
        double targetMean = 0.0;
        double efm = 0.0;
        double efmI = 0.0;
        double efmD = 0.0;
        double timeProb = 0.0;
        double avgT = 0.0;
        double stdT = 0.0;
        double timeZ = 0.0;
        DecimalFormat df = new DecimalFormat("#.##");
        
        // parse data from object
        avgT = s.getCentroid().getX();
        targetMean = s.getCentroid().getY();        
        prevT = s.getMostRecentPayoutMonth(t);
        efm = s.getEfm(t);
        efmI = s.getEfmI(t);
        efmD = s.getEfmD(t);
        stdT = s.getStdDevBetweenTime();
        
        if (stdT > 0) {
            // calc the z score for the timing of this predicted month
            timeZ = ((t - prevT) - avgT) / stdT;

            // determine timing probability based on 2sigma window
            if (Math.abs(timeZ) > (2 * stdT)) {
                timeProb = 0;
            } else {
                timeProb = Math.abs(timeZ - (2 * stdT)) / (2 * stdT);
            }
        } else {
            timeProb = 0;
        }
        
        // make new prediction
        prediction = targetMean * timeProb * (1 - efm);
        
        // validate prediction
        if (Double.isNaN(prediction)) {
            prediction = 0.0;
        }
        
        // return the results
        return new PayoutPrediction(
                s.getPolicyId(), 
                ((t > 11) ? 2017 : 2016), 
                t, 
                prevT, 
                avgT, 
                timeProb, 
                targetMean, 
                efm,
                efmI,
                efmD,
                Double.parseDouble(df.format(prediction)), 
                ((t > 11) ? Double.NaN : s.getPaidAmount(t)));
    }
}
