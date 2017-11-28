
package data;

import java.text.DateFormatSymbols;

/**
 * This class stores the results of the payout predictions for each month, not just January, of 2017.
 * @author Nathan Minor
 */
public class PayoutPrediction {
    
    // internal class variables for storing calculated predictions, aggregated by policyId by month
    private long _policyId;
    private int _year;
    private int _month;
    private int _prevT;
    private double _avgT;
    private double _timingP;
    private double _efm;
    private double _efmI;
    private double _efmD;
    private double _kMean;
    private double _estPayout;
    private double _actPayout;
    
    /**
     * Default constructor to populate with blank data upon instantiation. 
     **/
    public PayoutPrediction () {
        this._policyId = 0;
        this._year = 2017;
        this._month = 1;
        this._prevT = 0;
        this._avgT = 1.0;
        this._timingP = 0.0;
        this._efm = 0.0;
        this._efmI = 0.0;
        this._efmD = 0.0;
        this._kMean = 0.0;
        this._estPayout = 0.0;
        this._actPayout = 0.0;
    }
    
    /**
     * Constructor to populate with data upon instantiation.
     * @param policyId the policyId associated with a by-month estimated payout
     * @param year the year associated with this estimated payout
     * @param month the month associated with this estimated payout
     * @param prevT the previous month an amount was paid against this policy Id
     * @param avgT the mean time between payouts on this policy Id
     * @param timingP the timing probability approximation
     * @param kMean the target mean
     * @param efm the error from the target mean
     * @param efmI the balance (integral) around the target mean
     * @param efmD the slope of the error from the target mean
     * @param estPayout the estimated payout for a given month and policyId
     * @param actPayout the actual payout for a given month and policyId
     */
    public PayoutPrediction(long policyId, int year, int month, int prevT, double avgT, 
            double timingP, double kMean, double efm, double efmI, double efmD,
            double estPayout, double actPayout){
        this._policyId = policyId;
        this._year = year;
        this._month = month;
        this._prevT = prevT;
        this._avgT = avgT;
        this._timingP = timingP;
        this._kMean = kMean;
        this._efm = efm;
        this._estPayout = estPayout;
        this._actPayout = actPayout;
    }
    
    /**
     * "Get" method for retrieving the policyId associated with this payout prediciton.
     * @return The policyId associated with this payout prediciton.
     **/
    public long getPolicyId() {
        return this._policyId;
    }
    
    /**
     * "Get" method for retrieving the year this predication was made for on this policyId.
     * @return The year this prediction was made on this policyId
     **/
    public int getYear() {
        return this._year;
    }
    
    /**
     * "Get" method for retrieving the month for which this predication was made on this policyId.
     * @return The month for which this prediction wad made on this policyId.
     **/
    public String getMonth() {
        return new DateFormatSymbols().getMonths()[(this._month % 12)];
    }
    
    /**
     * "Get" method for retrieving the previous month an amount was paid out on this policyId.
     * @return The previous month an amount was paid out on this policyId.
     */
    public String getPrevMonth() {
        return new DateFormatSymbols().getMonths()[this._prevT];
    }
    
    /**
     * "Get" method for retrieving the mean time between payouts on this policyId.
     * @return  The mean time between payouts on this policyId.
     */
    public double getMeanTimeBetween() {
        return this._avgT;
    }
    
    /**
     * "Get" method for retrieving the approximate probability that the month under prediction will have a payout on this policyId.
     * @return A probablity (0-1) that the month being predicted will have a monthly amount paid out on this policyId.
     */
    public double getTimingProbability() {
        return this._timingP;
    }
    
    /**
     * "Get" method for retrieving the target mean monthly payout used to predict the next monthly payout on this policyId.
     * @return The target mean monthly amount paid out in this policyId's cluster.
     */
    public double getTargetMean() {
        return this._kMean;
    }
    
    /**
     * "Get" method for retrieving the error from the target mean on monthly payouts for this policyId.
     * @return Error from target mean, normalized to target mean.
     */
    public double getEfm() {
        return this._efm;
    } 
    
    /**
     * "Get" method for retrieving the balance (integral) around the target mean on monthly payouts for this policyId.
     * @return Error from target mean, normalized to target mean.
     */
    public double getEfmI() {
        return this._efmI;
    }
    
    /**
     * "Get" method for retrieving the slope of the error from the target mean on monthly payouts for this policyId.
     * @return Error from target mean, normalized to target mean.
     */
    public double getEfmD() {
        return this._efmD;
    }   
    
    /**
     * "Get" method for retrieving the estimated payout for this policyId during the month under representation.
     * @return 
     **/
    public double getEstimatedPayout() {
        return this._estPayout;
    }    
    
    /**
     * "Get" method for retrieving the actual payout for this policyId during the month under representation.
     * @return 
     **/
    public double getActualPayout() {
        return this._actPayout;
    }    
    
    /**
     * "Get" method for retrieving the percent error on the prediction for this policyId during the month under representation.
     * @return 
     **/
    public double getPercentError() {
        // local variables
        double pError = 0.0;
        
        try {
            // make sure to not divide by zero
            if (this._actPayout > 0) {
                pError =  100*((this._estPayout - this._actPayout) / this._actPayout);
            } else {
                if (this._estPayout > 0) {
                    pError =  100*((this._estPayout - this._actPayout) / this._estPayout);
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        
        return pError;
    }    
}
