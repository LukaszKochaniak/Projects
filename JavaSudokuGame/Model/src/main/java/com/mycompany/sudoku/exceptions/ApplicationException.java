/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sudoku.exceptions;

public class ApplicationException extends Exception {
    
    public static final String RESOURCE_BUNDLE_IS_NULL = "null.resource";

    public ApplicationException(final String message) {
        super(message);
    }

    public ApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
