package project2;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

/*
 *  6813256 sornchai soontranon
    6813259 hassadin kheawkhum
    6813255 Warisa Inthararak
    6813104 Shayne Chawengchot
 *
 */

public class BrokerThread extends Thread {
    private ArrayList<ClaimGateway> gateways;
    
    private int days;
    private int batchSize;
    private int minClaims;
    private int maxClaims;
    private Random random = new Random();
    
    private CyclicBarrier startBarrier;
    private CyclicBarrier endBarrier;
    private CyclicBarrier printBarrier;
    
    public BrokerThread (ArrayList<ClaimGateway> gateways, int days, 
                         int batchSize, int minClaims,
                         int maxClaims, String name,
                         CyclicBarrier startBarrier,
                         CyclicBarrier endBarrier, 
                         CyclicBarrier printBarrier) {
        super(name);
        this.gateways = gateways;
        this.days = days;
        this.batchSize = batchSize;
        this.minClaims = minClaims;
        this.maxClaims = maxClaims;
        
        this.startBarrier = startBarrier;
        this.endBarrier = endBarrier;
        this.printBarrier = printBarrier;
    }
    
    @Override
    public void run() {
        try {
            for (int day = 1; day <= days; day++) {
                startBarrier.await();

                int totalClaims = random.nextInt(maxClaims - minClaims + 1)
                                 + minClaims;

                //split into batch
                ArrayList<Integer> batches = new ArrayList<>();
                int remainingClaims = totalClaims;

                while (remainingClaims > 0) {
                    int currentBatch = Math.min(remainingClaims, batchSize);
                    batches.add(currentBatch);
                    remainingClaims -= currentBatch;
                }
                
                System.out.printf("%18s  >>  new claims = %4d  split to %s\n", getName(), totalClaims, batches);
                
                printBarrier.await();
                
                //submit claims to gateway
                for(int currentBatch : batches) {
                    int num_gateway = random.nextInt(gateways.size());
                    ClaimGateway selectedGateway = gateways.get(num_gateway);

                    selectedGateway.submitClaims(currentBatch);
                    
                    System.out.printf("%18s  >>  submit %2d claims to %-18s  %15s pending claims = %4d\n", 
                            getName(), currentBatch, selectedGateway.toString(), selectedGateway.toString(), selectedGateway.getPendingClaims());
                }
                endBarrier.await();
            }        
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            System.out.println("Barrier was broken: " + e.getMessage());
        }   
    }
}
