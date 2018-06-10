package com.mycompany.sudoku;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SudokuLine implements Serializable, Cloneable {

    private List<SudokuField> line = Arrays.asList(new SudokuField[9]);

    public SudokuLine() {
        for (int i = 0; i < 9; i++) {
            line.set(i, new SudokuField());
        }
    }

    public boolean verify(int j, int v) {
        for (int i = 0; i < 9; i++) {
            if (j == i) {
                continue;
            }
            if (v == line.get(i).getFieldValue()) {
                return false;
            }
        }
        return true;
    }

    public void set(int j, final SudokuField field) {
        line.set(j, field);

    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Line:\n");

        for (int i = 0; i < 9; i++) {
            str.append(line.get(i).getFieldValue());
        }
        str.append("\n\n");

        return str.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SudokuLine) {
            final SudokuLine other = (SudokuLine) obj;
            return new EqualsBuilder()
                    .append(line, other.line)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(line).
                toHashCode();
    }

    @Override
    public SudokuLine clone() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            return (SudokuLine) new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException error) {
            error.getMessage();
        }
        return null;
    }

}
