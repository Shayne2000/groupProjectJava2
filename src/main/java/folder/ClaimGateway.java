package project2;

/*  6813256 sornchai soontranon
    6813259 hassadin kheawkhum
    6813255 Warisa Inthararak
    6813104 Shayne Chawengchot
 */

public class ClaimGateway {
    private int pendingClaims;
    
    public ClaimGateway() { 
        pendingClaims = 0;
    }
    
    public synchronized void submitClaims(int claims) {
        if (claims <= 0) {
            System.out.println("Invalid claims: " + claims);
            return ;
        }
        pendingClaims += claims;
    }
    
    public synchronized int approveClaims (int claims) {
        if (claims <= 0) {
            return 0;
        }
        int approved = Math.min(claims, pendingClaims);
                
        pendingClaims -= approved;

        return approved;
    }
    
    public synchronized int getPendingClaims() { return pendingClaims; } 
}
