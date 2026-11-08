/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package folder;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Shayne
 */
public class assessorThread extends Thread {
    
    private ArrayList<ClaimGateway> claimGateways;
    private ArrayList<PaymentGateway> paymentGateways;
    private final int maxToken;
    private CyclicBarrier barrier;
    private int pendingPayment;
    
    private final Random random = new Random();
    
    public assessorThread(String name, int max, ArrayList<ClaimGateway> c, ArrayList<PaymentGateway> p, CyclicBarrier b) {
        maxToken = max;
        claimGateways = c;
        paymentGateways = p;
        barrier = b;
        pendingPayment = 0;
        super(name);
    }
    
    @Override
    public void run () {
        while (true) {
            for (int step = 0 ; step < 3 ; step ++) {
                purse();
            }
            pendingPayment += approvingClaims();
            purse();
            
            System.out.printf("pending payments =%4d\n", pendingPayment);
            purse();
            
            issuePayments();
        }
    }
    
    private int approvingClaims () {
        int claimGatewayIndex = random.nextInt(0,claimGateways.size()+1);
        ClaimGateway claimGateway = claimGateways.get(claimGatewayIndex);
        int approvedClaim = claimGateway.approveClaims(random.nextInt(0,maxToken+1));
        
        System.out.printf("approve%4d claims from ClaimGateway_%d        ClaimGateway_%d pending claims =%6d\n", approvedClaim, claimGatewayIndex, claimGatewayIndex, claimGateway.getPendingClaims());
        return approvedClaim;
    }
    
    private void issuePayments () {
        int paymentGatewayIndex = random.nextInt(0,paymentGateways.size()+1);
        PaymentGateway paymentGateway = paymentGateways.get(paymentGatewayIndex);
    }
    
    private void purse () {
        try {
            barrier.await();
        }catch(InterruptedException | BrokenBarrierException e) {
            System.out.printf("Thrown from barrier : %s\n", e);
        }
            
    }
}
