/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sudoku.daos;

import com.mycompany.sudoku.SudokuBoard;
import com.mycompany.sudoku.exceptions.ApplicationException;
import java.util.ResourceBundle;

public abstract class AbstractDao implements Dao<SudokuBoard>, AutoCloseable {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("exceptions.Dao");

    public AbstractDao() throws ApplicationException {
        if (bundle == null) {
            throw new ApplicationException(ApplicationException.RESOURCE_BUNDLE_IS_NULL);
            
        }
    }

    public String getDaoMessage(final String key) {
        return bundle.getString(key);
    }
}
