package com.campus.canteen.model;
// this has payment info.
public class Payment {
    private long paymentId;
    private long orderId;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;

    public Payment(long paymentId, long orderId, double amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = "CASH";
        this.paymentStatus =  PaymentStatus.PENDING;
    }

    public long getPaymentId() {
        return paymentId;
    }
    public long getOrderId() {
        return orderId;
    }
    public double getAmount() {
        return amount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isPaid() {
        return PaymentStatus.PAID.equals(paymentStatus);
    }
}
