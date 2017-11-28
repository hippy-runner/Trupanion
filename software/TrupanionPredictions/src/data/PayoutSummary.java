
package data;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the summary data of the payouts for each policy across 2016.
 * @author Nathan Minor
 */
public final class PayoutSummary implements Comparable<PayoutSummary> {
    
    // internal class variables for storing data parsed out of CSV file, aggregated by policyId    
    private List<Integer> _timeline; // stores a list of payout months for use with calculating timeline stats
    private List<Double> _payoutData; // stores a (sorted) list of payouts for use with calculating descriptive stats below
    private Centroid _p;        // the centroid assigned to this policyId
    private long _policyId;      // stores the policyId
    private double _min;        // stores the minimum monthly payout for this policyId during the 2016
    private double _max;        // stores the maximum monthly payout for this policyId during 2016
    private double _sum;        // stores the sum of all payouts for this policyId in 2016
    
    
    /**
     * Default constructor to populate with blank data upon instantiation. 
     **/
    public PayoutSummary () {
        
        this._payoutData = new ArrayList<>();
        this._timeline = new ArrayList<>();
        this._p = new Centroid();
        
        this._policyId = 0;
        this._min = 0.0;
        this._max = 0.0;
        this._sum = 0.0;
    }
    
    
    /**
     * Constuctor to populate with data upon instantiation.
     * @param policyId policy number
     * @param data a list of payout values aggregated by month per policyId across 2016
     * @param sum the sum of all payouts for this policyId in 2016
     * @param timeline
     **/
    public PayoutSummary (long policyId, double sum, List<Double> data, List<Integer> timeline) {
               
        // assign inputs approiately
        this._policyId = policyId;
        this._sum = sum;
        this._payoutData = data;
        this._timeline = timeline;
        this._max = 0.0;
        this._min = sum;
        
        
        // find min and max values
        for (double d : data) {
            if (d > this._max)
                this._max = d;
            
            if (d < this._min)
                this._min = d;
        }
        
        
        // setup the initial centroid
        this._p = new Centroid(
                this.getAvgBetweenTime(), 
                this.getMean());
        
    }
    
    /**
     * "Get" method for retrieving the policyId for this row-vector object.
     * @return The policyId.
     **/
    public long getPolicyId() {
        return this._policyId;
    }
    
    /**
     * "Get" method for retrieving the min payout for this policyId during 2016.
     * @return The minimum monthly payout for this policyId across 2016
     **/
    public double getMin() {        
        return this._min;
    }
    
    /**
     * "Get" method for retrieving the max monthly payout for this policyId during 2016.
     * @return The maximum monthly payout for this policyId across 2016
     **/
    public double getMax() {
        return this._max;
    }
    
    /**
     * "Get" method for retrieving the sum of all payouts for this policyId during 2016.
     * @return The sum of all payouts for this policyId across 2016
     **/
    public double getSum() {
        return this._sum;
    }
    
    /**
     * "Get" method for retrieving the number of payouts for this policyId during 2016.
     * @return The number of payouts for this policyId across 2016
     **/
    public final int getCount() {
        return this._payoutData.size();
    }
    
    
    /**
     * "Get" method for retrieving the range on monthly payouts for this policyId during 2016.
     * @return The range on monthly payouts for this policyId across 2016
     **/
    public double getRange() {
        return (this._max - this._min);
    }
    
    /**
     * "Get" method for retrieving the mean on monthly payouts for this policyId during 2016.
     * @return The mean on monthly payouts for this policyId across 2016
     **/
    public double getMean() {
        return (this._sum / this.getCount());
    }
    
    /**
     * "Get" method for retrieving the variance on monthly payouts for this policyId during 2016.
     * @return The variance on monthly payouts for this policyId across 2016
     **/
    public double getVariance() {
        double var = 0.0;
        
        // loop over the payout data to calculate variance as the sum of the squares of differences from the mean
        for (int i = 0; i < this.getCount(); i++) {
            var += Math.pow((this._payoutData.get(i) - this.getMean()), 2);
        }
        
        return var;
    }
    
    /**
     * "Get" method for retrieving the standard deviation on monthly payouts for this policyId during 2016.
     * @return The number of payouts for this policyId across 2016
     **/
    public double getStandardDev() {        
        return Math.sqrt(this.getVariance());
    }
    
    /**
     * "Get" method for retrieving the most recent month that a payout was made for this policyId during 2016.
     * @param t
     * @return The number of payouts for this policyId across 2016
     **/    
    public int getMostRecentPayoutMonth(int t) {
        int month = 0;
        int i = 0;
        
        try {
            // check to make sure time is valid input value
            if  (t > 0) {

                // to avoid an out of bounds exception,
                // keep t below the max value possible on the timeline
                if (t > 11) {
                    t = 11;
                }

                // handle case where there is only one month in the timelime...
                if (this._timeline.size() == 1) {                    
                    month = this._timeline.get(0);
                    
                } else if (this._timeline.get(0) < t) {
                    
                    // loop thru an determine which index on the timeline corresponds to
                    // the previous month to the most recent month a payout was made                    
                    for (int T : this._timeline) {
                        if (T < t)
                            i++;
                    }

                    // validate index to stop an out of bound exception
                    if (!(this._timeline.size() > i)) {
                        i = this._timeline.size() - 1;
                    }
                    
                    // sets month accordingly
                    month = (this._timeline.get(i) > t) ? this._timeline.get(i - 1) : this._timeline.get(i);
                }
            }
        } catch (Exception ex) {            
            // print exception message
            System.err.println(ex.getMessage());
        }
        
        return month;
    }
    
    /**
     * "Get" method for retrieving the previous most recent month that a payout was made for this policyId during 2016.
     * @param t
     * @return The number of payouts for this policyId across 2016
     **/    
    public int getPreviousPayoutMonth(int t) {
        int month = 0;
        int i = 0;
        
        try {            
            // check to make sure time is valid input value
            if  (t > 0) {

                // to avoid an out of bounds exception,
                // keep t below the max value possible on the timeline
                if (t > 11) {
                    t = 11;
                }

                // loop thru an determine which index on the timeline corresponds to
                // the previous month to the most recent month a payout was made
                for (int T : this._timeline) {
                    if (T < t)
                        i++;
                }

                // validate index to stop an out of bound exception
                if (i >= this.getCount()) {
                    i = this.getCount() - 1;
                }

                // validate index before risking an out of bound exception
                if (i > 1) {
                    // assign month value appropriately based on when the index i determined the comparison month
                    month = (this._timeline.get(i) > t) ? this._timeline.get(i - 2) : this._timeline.get(i - 1);
                } else {
                    month = this._timeline.get(i);
                }
            }
        } catch (Exception ex) {            
            // print exception message
            System.err.println(ex.getMessage());
        }
        
        return month;
    }
    
    /**
     * "Get" method for retrieving the most recent amount paid on monthly payouts for this policyId during 2016.
     * @param t
     * @return The most recent amount paid out for this policyId 
     **/
    public double getMostRecentPaidAmount(int t) { 
        double amount = 0;
        int i = -1;
                
        try {
            // check to make sure time is valid input value
            if  (t > 0) {

                // to avoid an out of bounds exception,
                // keep t below the max value possible on the timeline
                if (t > 11) {
                    t = 11;
                }

                // loop thru an determine which index on the timeline corresponds to
                // the previous month to the most recent month a payout was made
                for (int T : this._timeline) {
                    if (T < t) {
                        i++;
                    }
                }
                
                // make sure there were any months prior to the prediction month, t
                if (i > 0) {
                    // validate index to stop an out of bound exception
                    if (i >= this.getCount()) {
                        i = this.getCount() - 1;
                    }

                    // set month accordingly
                    amount = (this._timeline.get(i) > t) ? this._payoutData.get(i - 1) : this._payoutData.get(i);
                }
            }
        } catch (Exception ex) {            
            // print exception message
            System.err.println(ex.getMessage());
        }
        
        return amount;
    }
    
    /**
     * "Get" method for retrieving the most recent amount paid on monthly payouts for this policyId during 2016.
     * @param t
     * @return The most recent amount paid out for this policyId 
     **/
    public double getPreviousPaidAmount(int t) { 
        double amount = 0;
        int i = 0;
                
        try {
            // check to make sure time is valid input value
            if  (t > 0) {

                // to avoid an out of bounds exception,
                // keep t below the max value possible on the timeline
                if (t > 11) {
                    t = 11;
                }

                // loop thru an determine which index on the timeline corresponds to
                // the previous month to the most recent month a payout was made
                
                for (int T : this._timeline) {
                    if (T < t) {
                        i++;
                    }
                }

                // validate index to stop an out of bound exception
                if (i >= this.getCount()) {
                    i = this.getCount() - 1;
                }
                
                // validate index to stop an out of bound exception                
                if (i > 1) {
                    // assign payout value appropriately based on when the index i determined the comparison month
                    amount = (this._timeline.get(i) > t) ? this._payoutData.get(i - 2) : this._payoutData.get(i - 1);
                } else {
                    amount = this._payoutData.get(i);
                }
            }
        } catch (Exception ex) {            
            // print exception message
            System.err.println(ex.getMessage());
        }
        
        return amount;
    }
    
    public double getPaidAmount(int month) {
        // local variables
        int indexT = this._timeline.size();
        double paidAmount = 0.0;
        
        // iff timelime contains month, grab its index
        for (int i = 0; i < this._timeline.size(); i++) {
            // notice: months in timeline list are NOT zero based lke month input variable
            if (this._timeline.get(i) == month) {
                indexT = i;
                break;
            }
        }
        
        // iff a proper index was found, then
        if (indexT < this._timeline.size()) {
            
            // snag the approriate paid amount
            paidAmount = this._payoutData.get(indexT);
            
        } // otherwise leave paid amount at 0
        
        // return whatever amount was found
        return paidAmount;
    }
    
    /**
     * "Get" method for retrieving the mean on months between payouts for this policyId during 2016.
     * @return The average (mean) time, in months, between payouts for this policyId across 2016
     **/
    public final double getAvgBetweenTime() {
        double sum = 0.0;
        double avg = 0.0;
        
        for (int i = 0; i < this.getCount() - 1; i++) {
            sum += (this._timeline.get(i + 1) - this._timeline.get(i));
        }
            
        // handles divide by 0 cases
        if (this.getCount() == 1) {
            avg = 0;
        } else {
            
            avg = (sum / (this.getCount() - 1));
        }
        
        return avg;
    }
    
    /**
     * "Get" method for retrieving the variance on months between payouts for this policyId during 2016.
     * @return The number of payouts for this policyId across 2016
     **/
    public double getVarBetweenTime() {
        double var = 0.0;
        
        for (int i = 0; i < this.getCount() - 1; i++) {
            var += Math.pow(((this._timeline.get(i + 1) - this._timeline.get(i)) - this.getAvgBetweenTime()), 2);
        }
        
        return var;
    }
    
    /**
     * "Get" method for retrieving the standard deviation on months between payouts for this policyId during 2016.
     * @return The number of payouts for this policyId across 2016
     **/
    public double getStdDevBetweenTime() {        
        return Math.sqrt(this.getVarBetweenTime());
    }  
    
    /**
     * "Get" method for retrieving the Error (from mean) function for this policyId during 2016.
     * @param t
     * @return Error from the cluster mean as a percent value on a scale from 0 to 1..
     **/
    public double getEfm(int t) {
        
        // local variables
        double efm = 0.0;
        
        // avoid dividing by zero
        if (this.getRange() > 0)
            efm = (this.getMostRecentPaidAmount(t) - this.getMin()) / this.getRange();
        
        return efm;
    }
    
    /**
     * Calculates the balance (integral) of the error from mean function
     * up to month t.
     * @param t The month up to which to calculate the balance (integral) of the error function.
     * @return The balance of the error from mean function up to month t.
     */
    public double getEfmI(int t) {
        // local variables
        double integral = 0.0;
        
        // loop thru timeline of paid months
        for (int T : this._timeline) {
            
            // when the current month in the timeline loop is prior to the input
            // variable t, add to the integral
            if (T < t) 
                integral += this.getEfm(T) * (T - this.getPreviousPayoutMonth(T));
        }
        
        // retrun the balance of the efm
        return integral;
    }
    
    public double getEfmD(int t) {
        // local variables
        double slope = 0.0;
        int prevT = this.getPreviousPayoutMonth(t);
        
        // only calculate a slope when not dividing by zero
        if (t - prevT != 0) {
            slope = (this.getEfm(t) - this.getEfm(prevT)) / (t - prevT);
        }
        
        return slope;
    }
    
    /**
     * "Get" method for retrieveing the centroid value associated with this policyId
     * for the cluster grouping on average time between payouts
     * @return  The centroid assign to this policyId
     */
    public Centroid getCentroid() {
        return this._p;
    }
    
    /**
     * "Set" method for changing/updating the value of the centroid, clustered
     * on average time between payouts, associated with this policyId
     * @param p   The new centroid to assign.
     */
    public void setCentroid(Centroid p) {
        this._p = p;
    }
    
    /**
     * Used to sort a list of PayoutSummary objects
     * @param s
     * @return 
     */
    @Override
    public int compareTo(PayoutSummary s) {
        
        if (this._p.getY() > s.getCentroid().getY()) {
            return 1;
        } else if (this._p.getY() < s.getCentroid().getY()) {
            return -1;
        } else {
            return 0;
        }
    }
}
