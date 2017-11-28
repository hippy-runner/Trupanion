
package data;

import java.util.Date;

/**
 * This class stores the raw claim-level data provided in the origianl CSV file.
 * @author Nathan Minor
 */
public class ClaimLevel implements Comparable<ClaimLevel> {
    
    // internal class variables for storing data parsed out of CSV file
    private long _policyId;          // stores the policyId associated with a claim
    private Date _claimDate;         // stores the date a claim was made
    private double _claimedAmount;   // stores the amount claimed on said date
    private double _paidAmount;      // stores the amount paid out to customer
    
    /**
     * Default constructor to populate with blank data upon instantiation. 
     **/
    public ClaimLevel() {
        this._policyId = 0;
        this._claimDate = null;
        this._claimedAmount = 0.0;
        this._paidAmount = 0.0;
    }
    
    /**
     * Constuctor to populate with data upon instantiation.
     * @param policyIdNo policy number
     * @param claimedDate date of claim filing
     * @param claimed amount claimed
     * @param paid amount paid out on claim
     **/
    public ClaimLevel(long policyIdNo, Date claimedDate, double claimed, double paid) {
        this._policyId = policyIdNo;
        this._claimDate = claimedDate;
        this._claimedAmount = claimed;
        this._paidAmount = paid;
    }
    
    /**
     * "Get" method for retrieving the policyId for this row-vector object.
     * @return Policy Id associated with this claim.
     **/
    public long getPolicyId() {
        return this._policyId;
    }
    
    /**
     * "Get" method for retrieving the date the claim was filed for this row-vector object.
     * @return Date claim was filed.
     **/
    public Date getClaimDate() {
        return this._claimDate;
    }
    
    /**
     * "Get" method for retrieving the amount claimed for this row-vector object.
     * @return The dollar amount claimed.
     **/
    public double getClaimedAmount() {
        return this._claimedAmount;
    }
    
    /**
     * "Get" method for retrieving the amount paid for this row-vector object.
     * @return The amount paid out against this claim.
     **/
    public double getPaidAmount() {
        return this._paidAmount;
    }
    
    /**
     * Method used for comparing ClaimLevel row-vector objects for the 
     * purpose of sorting a list of ClaimLevel objects
     * @param d ClaimLevel object to compare this to.
     * @return 0 = equal, -1 = less than, 1 = greater than
     */
    @Override
    public int compareTo(ClaimLevel d) {
        int lastCompare = 0;
        
        if (this.getPolicyId() > d.getPolicyId()) {
            lastCompare = 1;
        } else if (this.getPolicyId() < d.getPolicyId()) {
            lastCompare = -1;
        } else {
            lastCompare = 0;
        }
        
        return (lastCompare != 0 ? lastCompare : this.getClaimDate().compareTo(d.getClaimDate()));
    }
}
