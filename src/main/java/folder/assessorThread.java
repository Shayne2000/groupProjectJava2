/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package folder;
import java.util.*;

/**
 *
 * @author Shayne
 */
public class assessorThread extends Thread {
    
    private ArrayList<ClaimGateway> claimGateways;
    private ArrayList<PaymentGateway> paymentGateways;
    private final int maxToken;
    
    public assessorThread(String name, int max) {
        maxToken = max;
        super(name);
    }
}
