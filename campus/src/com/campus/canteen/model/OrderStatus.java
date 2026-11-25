package com.campus.canteen.model;
// this file will contain order
// placed  , preparing , ready , collected , cancelled
public class OrderStatus {

//    order status
    public static final String PLACED="PLACED";
    public static final String PREPARING="PREPARING";
    public static final String READY="READY";
    public static final String COLLECTED="COLLECTED";
    public static final String CANCELLED="CANCELLED";

    // Check whether a status string is a valid order status
    public static boolean isValid(String status) {
        return PLACED.equals(status)
                || PREPARING.equals(status)
                || READY.equals(status)
                || COLLECTED.equals(status)
                || CANCELLED.equals(status);
    }

    // return next status (null-safe)
    public static String next(String status) {
        if (PLACED.equals(status)) {
            return PREPARING;
        } else if (PREPARING.equals(status)) {
            return READY;
        } else if (READY.equals(status)) {
            return COLLECTED;
        }
        return null;
    }
}
