package com.abn.selcucumber.exception;

/**
 * Created by aswathyn on 07/01/17.
 */
public class SelectDropDownNotFoundException extends GenericException {
    public SelectDropDownNotFoundException() {
    }

    public SelectDropDownNotFoundException(String message) {
        super(message);
    }

    public SelectDropDownNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SelectDropDownNotFoundException(Throwable cause) {
        super(cause);
    }
}
