package com.mycompany.sudoku;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SudokuBox implements Serializable, Cloneable {

    private OurHashMap<Integer, Integer, SudokuField> boxes = new OurHashMap<>();

    public SudokuBox() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boxes.put(j, i, new SudokuField());
            }
        }
    }

    public boolean verify(int x, int y, int v) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == x && i == y) {
                    continue;
                }
                if (v == boxes.get(j, i).getFieldValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void set(int j, int i, final SudokuField field) {
        boxes.put(j, i, field);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Box:\n");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                str.append(boxes.get(j, i).getFieldValue());
            }
            str.append("\n");
        }
        str.append("\n");

        return str.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SudokuBox) {
            final SudokuBox other = (SudokuBox) obj;
            return new EqualsBuilder()
                    .append(boxes, other.boxes)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(boxes).
                toHashCode();
    }

    @Override
    public SudokuBox clone() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            return (SudokuBox) new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException error) {
            error.getMessage();
        }
        return null;
    }

}
