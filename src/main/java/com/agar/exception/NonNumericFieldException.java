package com.agar.exception;

/**
 * Created by SDEV2 on 04/07/2016.
 */
public class NonNumericFieldException extends Exception {
    public NonNumericFieldException(){
        super();
    }

    public NonNumericFieldException(String message){
        super(message);
    }
}
