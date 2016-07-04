package com.agar.exception;

/**
 * Created by SDEV2 on 04/07/2016.
 */
public class EmptyFieldException extends Exception {
    public EmptyFieldException(){
            super();
    }

    public EmptyFieldException(String message){
            super(message);
    }
}
