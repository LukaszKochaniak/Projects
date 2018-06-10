package com.mycompany.sudoku.daos;

import com.mycompany.sudoku.SudokuBoard;
import com.mycompany.sudoku.exceptions.ApplicationException;
import com.mycompany.sudoku.exceptions.DaoException;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileSudokuBoardDao extends AbstractDao {

    static Logger log = Logger.getLogger(FileSudokuBoardDao.class.getName());

    private final String fileName;

    public FileSudokuBoardDao(final String fileName) throws ApplicationException {
        this.fileName = fileName;
    }

    @Override
    public SudokuBoard read() throws DaoException {
        try (FileInputStream fis = new FileInputStream(fileName);
                ObjectInputStream out = new ObjectInputStream(fis);) {
            SudokuBoard clone = (SudokuBoard) out.readObject();
            return clone;
        } catch (IOException | ClassNotFoundException ex) {
            log.log(Level.SEVERE, getDaoMessage("exception.message"), ex);
            throw new DaoException(DaoException.READ_ERROR, ex);
        }
    }

//    public SudokuBoard readTXT() throws DaoException {
//        OurHashMap<Integer, Integer, SudokuField> board = new OurHashMap<>();
//        try (BufferedReader dirFile = new BufferedReader(new FileReader(fileName))) {
//            String input;
//            int j = 0;
//            while ((input = dirFile.readLine()) != null) {
//                String[] data = input.split(",");
//                for (int i = 0; i < 9; i++) {
//                    SudokuField field = new SudokuField(Integer.parseInt(data[i]));
//                    board.put(i, j, field);
//                }
//                j++;
//            }
//            return new SudokuBoard(board);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public void write(final SudokuBoard obj) throws DaoException {
        try (FileOutputStream fos = new FileOutputStream(fileName);
                ObjectOutputStream ous = new ObjectOutputStream(fos);) {
            ous.writeObject(obj);
        } catch (IOException ex) {
            log.log(Level.SEVERE, getDaoMessage("exception.message"), ex);
            throw new DaoException(DaoException.WRITE_ERROR, ex);
        }
    }

//    public void writeTXT(final SudokuBoard obj) throws DaoException {
//        try (BufferedWriter locFile = new BufferedWriter(new FileWriter(fileName))) {
//            Iterator iter = obj.iterator();
//            int i = 1;
//            while (iter.hasNext()) {
//                SudokuField field = (SudokuField) iter.next();
//                locFile.write(String.valueOf(field.getFieldValue()));
//                if (i % 9 == 0) {
//                    locFile.newLine();
//                } else {
//                    locFile.write(",");
//                }
//                i++;
//            }
//
//        } catch (IOException ex) {
//            log.log(Level.SEVERE, getDaoMessage("exception.message"), ex);
//            throw new DaoException(DaoException.WRITE_ERROR, ex);
//        }
//    }

    @Override
    public void close() throws DaoException {
        log.info("Closing!");
    }

}
