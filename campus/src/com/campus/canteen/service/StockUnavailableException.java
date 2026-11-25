
package com.campus.canteen.service;

public class StockUnavailableException extends RuntimeException {

    public StockUnavailableException(String message) {
        super(message);
    }
}
