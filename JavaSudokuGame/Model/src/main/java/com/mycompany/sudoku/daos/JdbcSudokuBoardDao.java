/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sudoku.daos;

import com.mycompany.sudoku.SudokuBoard;
import com.mycompany.sudoku.exceptions.ApplicationException;
import com.mycompany.sudoku.exceptions.DaoException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JdbcSudokuBoardDao extends AbstractDao {

    static Logger log = Logger.getLogger(JdbcSudokuBoardDao.class.getName());
    static final String WRITE_OBJECT_SQL = "INSERT INTO sudoku_objects(name, sudoku_board) VALUES (?, ?)";
    static final String READ_OBJECT_SQL = "SELECT sudoku_board FROM sudoku_objects WHERE id = ?";
    static final String GET_OBJECT_SQL = "SELECT sudoku_board FROM sudoku_objects WHERE name = ?";

    private final String fileName;
    private int id;
    private Connection conn;
    private FileHandler fh;

    public JdbcSudokuBoardDao(final String fileName, final int id) throws ApplicationException {
        try {
            initializeToFileLogger();
        } catch (DaoException de) {
            log.log(Level.SEVERE, de.getLocalizedMessage(), de);
        }
        this.fileName = fileName;
        this.id = id;
    }

    public JdbcSudokuBoardDao(final String fileName) throws ApplicationException {
        try {
            initializeToFileLogger();
        } catch (DaoException de) {
            log.log(Level.SEVERE, de.getLocalizedMessage(), de);
        }
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void initializeToFileLogger() throws DaoException {
        try {
            File path = new File("logs/MyLog.log");
            fh = new FileHandler(path.getPath());
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new DaoException(DaoException.RESOURCE_BUNDLE_IS_NULL, ex);
        } catch (SecurityException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new DaoException(DaoException.RESOURCE_BUNDLE_IS_NULL, ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        conn.close();
    }

    @Override
    public void write(final SudokuBoard obj) throws DaoException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);) {
            conn = getConnection();
            String className = obj.getClass().getName();
            PreparedStatement pstmt = conn.prepareStatement(WRITE_OBJECT_SQL, Statement.RETURN_GENERATED_KEYS);

            oos.writeObject(obj);

            byte[] data = bos.toByteArray();

            // set input parameters
            pstmt.setString(1, fileName);
            pstmt.setObject(2, data);
            pstmt.executeUpdate();

            // get the generated key for the id
            ResultSet rs = pstmt.getGeneratedKeys();
//            int id = -1;
            if (rs.next()) {
                this.id = rs.getInt(1);
            }

            rs.close();
            pstmt.close();
            log.info("write: done serializing: " + className + " id: " + id);

        } catch (SQLException | IOException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new DaoException(DaoException.WRITE_ERROR, ex);
        }

    }

    public SudokuBoard readWithId(final int id) throws DaoException {
        try {
            conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(READ_OBJECT_SQL);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
//            SudokuBoard object = (SudokuBoard) rs.getObject(1);

            ByteArrayInputStream bais;
            ObjectInputStream ins;
            bais = new ByteArrayInputStream(rs.getBytes("sudoku_board"));
            ins = new ObjectInputStream(bais);

            SudokuBoard object = (SudokuBoard) ins.readObject();
            String className = object.getClass().getName();
            ins.close();

            rs.close();
            pstmt.close();
            log.info("read: done de-serializing: " + className);
            return object;
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new DaoException(DaoException.READ_ERROR, ex);
        }
//        return null;
    }

//    public void getObjectId() {
//        Connection conn;
//        try {
//            conn = getConnection();
//            PreparedStatement pstmt = conn.prepareStatement(GET_OBJECT_SQL, Statement.RETURN_GENERATED_KEYS);
//
//            pstmt.setString(1, fileName);
//
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                //id = rs.getInt("id");
//                System.out.println("ESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSA");
//                System.out.println(id);
//            }
//        } catch (ApplicationException | SQLException ex) {
//            Logger.getLogger(JdbcSudokuBoardDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public Connection getConnection() throws DaoException {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/sudokuschema?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String username = "lukasz";
            String password = "lukasz";
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            return conn;
        } catch (ClassNotFoundException | SQLException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new DaoException(DaoException.WRITE_ERROR, ex);
        }
    }

    @Override
    public SudokuBoard read() throws DaoException {
        try {
            conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(READ_OBJECT_SQL);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            SudokuBoard object = (SudokuBoard) rs.getBlob("sudoku_board");
            String className = object.getClass().getName();

            rs.close();
            pstmt.close();
            log.info("read: done de-serializing: " + className);
            return object;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new DaoException(DaoException.READ_ERROR, ex);
        }
    }

    @Override
    public void close() throws DaoException {
        log.info("Closing!");
    }
}
