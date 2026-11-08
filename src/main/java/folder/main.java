/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package folder;
import java.io.IOException;
import java.util.*;
import java.nio.file.Path;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Shayne
 */
public class main {
    
    private final String inputFolder = "src/main/java/folder/";
    
    private int[] parametors = new int[10];
    /*
    0) day
    1) claimGateWay_N;
    2) claimGateWay_batch;
    3) paymentGateWay_N;
    4) paymentGateWay_token;
    5) brokerThread_N;
    6) brokerThread_min;
    7) brokerThread_max;
    8) assessorThread_N;
    9) assessorThread_max;
    */
    
    private final ArrayList<ClaimGateway> claimGateways = new ArrayList<>();
    private final ArrayList<PaymentGateway> paymentGateways = new ArrayList<>();
    private final ArrayList<BrokerThread> brokerThreads = new ArrayList<>();
    private final ArrayList<assessorThread> assessorThreads = new ArrayList<>();
    
    private CyclicBarrier barrier;

    public void main(String[] args) {
        
        Scanner fileReader = createScanner("config_1.txt");
        
        handleInputInt(skipCommentedLine(fileReader),new int[]{0});
        handleInputInt(skipCommentedLine(fileReader),new int[]{1,2});
        handleInputInt(skipCommentedLine(fileReader),new int[]{3,4});
        handleInputInt(skipCommentedLine(fileReader),new int[]{5,6,7});
        handleInputInt(skipCommentedLine(fileReader),new int[]{8,9});
        
        overridePrintFormat();
        
        System.out.printf("=================================== Parameters ====================================\n");
        System.out.printf("Days of simulation : %d\n", parametors[0]);
        
        
        for (int i = 0 ; i < parametors[1] ; i ++) {
            claimGateways.add(new ClaimGateway(i));
        }
        System.out.printf("ClaimGateways      : %s\n", claimGateways.toString());
        System.out.printf("  Batch size       : %d\n", parametors[2]);
       
        
        for (int i = 0 ; i < parametors[3] ; i ++) {
            paymentGateways.add(new PaymentGateway(parametors[4]));
        }
        System.out.printf("PaymentGateways    : %s\n", paymentGateways.toString());
        System.out.printf("  Daily tokens     : %d\n", parametors[4]);
        
        
        
        barrier = new CyclicBarrier(parametors[5] + parametors[8] + 1);
        
        for (int i = 0 ; i < parametors[5] ; i ++) {
            brokerThreads.add(new BrokerThread(claimGateways, parametors[0], parametors[2], parametors[6], parametors[7],
                                               "BrokerThread_"+i, barrier, barrier, barrier));
            brokerThreads.get(i).start();
        }
        System.out.printf("BrokerThreads      : %s\n", brokerThreads.toString());
        System.out.printf("  Daily claims     : min = %d, max = %d\n", parametors[6], parametors[7]);
        
        
        for (int i = 0 ; i < parametors[8] ; i ++) {
            assessorThreads.add(new assessorThread("AssessorThread_"+i,parametors[9], claimGateways, paymentGateways, barrier));
            assessorThreads.get(i).start();
        }
        System.out.printf("AssessotThreads    : %s\n", assessorThreads.toString());
        System.out.printf("  Daily capacity   : %d\n", parametors[9]);
             
        
        for (int day = 1 ; day <= parametors[0] ; day ++) {
            System.out.printf("\n");
            System.out.printf("=====================================================================================\n");
            System.out.printf("Day %d\n", day);
            
            reportClaimGatewayDaily();
            resetPaymentGatewayDaily();
            
            System.out.printf("\n");
            
            for (int i = 0 ; i < 6 ; i ++){
                purse();
            }
            
        }
        
    }
    
    private Scanner createScanner (String name) {
        Scanner scanObject ;
        Scanner userInput = new Scanner(System.in);
        while (true) {
            try {
                scanObject = new Scanner(Path.of(inputFolder+name));
                break;
            } catch (IOException e) {
                System.out.printf("%s (The system cannot find the file specified)\n", e);
                System.out.printf("New file name =   ");
                name = userInput.nextLine();
            }
        }
        return scanObject;
    }
    
    private String skipCommentedLine (Scanner s) {
        String data;
        while (true) {
            data = s.nextLine();
            if (!data.startsWith("#")){
                return data;
            }
        }
    }
    
    private void handleInputInt (String line, int[] expectedInt) {
        String[] data = line.split(",");
        
        for (int i = 0 ; i < expectedInt.length ; i++) {
            parametors[expectedInt[i]] = Integer.parseInt(data[i+1].strip());
        }
    }
    
    private void reportClaimGatewayDaily () {
        for (int i = 0 ; i < parametors[1] ; i ++) {
            ClaimGateway claimGateway = claimGateways.get(i);
            System.out.printf("%s pending claims =%6d\n", claimGateway.toString(), claimGateway.getPendingClaims());
        }
    }
    
    private void resetPaymentGatewayDaily () {
        for (int i = 0 ; i < parametors[3] ; i++) {
            System.out.printf("reset PaymentGateway_%d tokens =%6d\n", i, parametors[4]);
            paymentGateways.get(i).resetTokens();
        }
    }
    
    private void purse () {
        try {
            barrier.await();
        }catch(InterruptedException | BrokenBarrierException e) {
            System.out.printf("Thrown from barrier : %s\n", e);
        }
            
    }
    
    private void overridePrintFormat () {
        System.setOut(new java.io.PrintStream(System.out) {
            @Override
            public java.io.PrintStream printf(String format, Object... args) {
                return super.printf("%16s".formatted(Thread.currentThread().getName()) + "  >>  " + format, args);
            }
        });
    }
}