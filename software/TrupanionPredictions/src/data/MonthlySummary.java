
package data;

/**
 * This class stores the summary data (sum and count) of the payouts for each month by policy.
 * @author Nathan Minor
 */
public class MonthlySummary {
    
    // internal class variables for storing data parsed out of CSV file, aggregated by policyId
    private long _policyId;      // stores the policyId
    private int _month;        // stores the month under consideration
    private double _sum;        // stores the sum of all payouts for this policyId during this month
    private int _count;         // stores number of claims paid for this policyId during this month
    
    /**
     * Default constructor to populate with blank data upon instantiation. 
     **/
    public MonthlySummary () {
        this._policyId = 0;
        this._month = 0;
        this._sum = 0.0;
        this._count = 0;
    }
    
    
    /**
     * Constuctor to populate with data upon instantiation.
     * @param policyId policy number
     * @param month the month under consideration
     * @param sum the sum of all payouts for this policyId during this month
     * @param count the count of all claims paid for this policyId during this month
     **/
    public MonthlySummary (
            long policyId,  
            int month,
            double sum,
            int count) {
        this._policyId = policyId;
        this._month = month;
        this._sum = sum;
        this._count = count;
    }
    
    /**
     * "Get" method for retrieving the policyId for this row-vector object.
     * @return The policyId.
     **/
    public long getPolicyId() {
        return this._policyId;
    }
    
    /**
     * "Get" method for retrieving the month under consideration.
     * @return The minimum monthly payout for this policyId across 2016
     **/
    public int getMonth() {
        return this._month;
    }
    
    /**
     * "Get" method for retrieving the sum of all payouts for this policyId during this month.
     * @return The sum of all payouts for this policyId during this month
     **/
    public double getSum() {
        return this._sum;
    }
    
    /**
     * "Get" method for retrieving the number of payouts for this policyId during this month.
     * @return The number of payouts for this policyId during this month
     **/
    public int getCount() {
        return this._count;
    }
}
