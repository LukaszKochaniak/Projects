package com.mycompany.sudoku.daos;

import com.mycompany.sudoku.exceptions.ApplicationException;

public interface Dao<T> {

    T read() throws ApplicationException;

    void write(T obj) throws ApplicationException;

}
