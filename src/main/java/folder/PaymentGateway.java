package folder;

import java.util.concurrent.atomic.AtomicInteger;


public class PaymentGateway {

    private static final AtomicInteger NEXT_ID = new AtomicInteger();

    private final int gatewayID;
    private int dailyTokenLimit;
    private int remainingTokens;

    
    public PaymentGateway() {
        this(0);
    }

    
    public PaymentGateway(int dailyTokenLimit) {
        this.gatewayID = NEXT_ID.getAndIncrement();
        setDailyTokenLimit(dailyTokenLimit);
        this.remainingTokens = dailyTokenLimit;
    }

    
    public synchronized boolean acquireToken() {
        if (remainingTokens == 0) {
            return false;
        }
        remainingTokens--;
        return true;
    }

    
    public synchronized void resetTokens() {
        remainingTokens = dailyTokenLimit;
    }

    
    public synchronized void resetTokens(int dailyTokenLimit) {
        setDailyTokenLimit(dailyTokenLimit);
        remainingTokens = dailyTokenLimit;
    }

    
    public synchronized int getTokens() {
        return remainingTokens;
    }

    
    public synchronized int getDailyTokenLimit() {
        return dailyTokenLimit;
    }

    private void setDailyTokenLimit(int dailyTokenLimit) {
        if (dailyTokenLimit < 0) {
            throw new IllegalArgumentException("Daily token limit cannot be negative");
        }
        this.dailyTokenLimit = dailyTokenLimit;
    }
    
    public int recivedPaymentTokens (int tokens) {
        if (tokens > remainingTokens) {
            tokens = remainingTokens;
        }
        remainingTokens -= tokens;
        return tokens;
    }

    @Override
    public String toString() {
        return "PaymentGateway_" + gatewayID;
    }
}
