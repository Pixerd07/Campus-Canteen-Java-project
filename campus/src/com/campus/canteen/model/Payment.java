package com.campus.canteen.model;
// this has payment info.
public class Payment {
    private long paymentId;
    private long orderId;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;

    public Payment(long paymentId, long orderId, double amount) {
        if (paymentId <= 0) {
            throw new IllegalArgumentException("Payment ID must be greater than zero.");
        }
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID must be greater than zero.");
        }
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Payment amount cannot be negative.");
        }
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
        if (!PaymentStatus.PENDING.equals(paymentStatus)
                && !PaymentStatus.PAID.equals(paymentStatus)
                && !PaymentStatus.REFUNDED.equals(paymentStatus)) {
            throw new IllegalArgumentException("Invalid payment status: " + paymentStatus);
        }
        this.paymentStatus = paymentStatus;
    }

    public boolean isPaid() {
        return PaymentStatus.PAID.equals(paymentStatus);
    }
}
