/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sudoku.exceptions;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DaoException extends ApplicationException {

    private static final ResourceBundle messages;
    
    //Message keys
    public static final String NULL_NAME = "null.name";
    public static final String OPEN_ERROR = "open.error";
    public static final String WRITE_ERROR = "write.error";
    public static final String READ_ERROR = "read.error";
    public static final String CONN_ERROR = "conn.error";
    public static final String LOAD_ERROR = "load.error";

    static {
        Locale locale = Locale.getDefault(Locale.Category.DISPLAY);
//        locale = new Locale("pl", "PL");
        messages = ResourceBundle.getBundle("exceptions.Dao", locale);
    }

    public DaoException(final String message) {
        super(message);
    }

    public DaoException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getLocalizedMessage() {
        String message;
        try {
            //Exception message is a key
            message = messages.getString(getMessage());
        } catch (MissingResourceException mre) {
            message = "No resource for " + getMessage() + "key";
        }
        return message;
    }

}
