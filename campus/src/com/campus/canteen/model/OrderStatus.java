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

    // return next status
    public static String next(String status){
        if(status.equals("PLACED")){
            return PREPARING;
        }
        else if( status.equals("PREPARING")){
            return READY;
        }
        else if(status.equals("READY")){
            return COLLECTED;
        }
        return null;
    }
}
