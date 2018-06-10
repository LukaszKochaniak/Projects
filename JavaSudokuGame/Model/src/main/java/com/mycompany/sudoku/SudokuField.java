package com.mycompany.sudoku;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SudokuField implements Serializable, Cloneable, Comparable {

    private int value;
    private boolean initialized = false;

    public SudokuField() {
        this.value = 0;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean i) {
        initialized = i;
    }

    public SudokuField(int value) {
        this.value = value;
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Value:\n");
        str.append(value).append("\n\n");
        return str.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SudokuField) {
            final SudokuField other = (SudokuField) obj;
            return new EqualsBuilder()
                    .append(value, other.value)
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(value).
                toHashCode();
    }

    @Override
    public SudokuField clone() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            return (SudokuField) new ObjectInputStream(bais).readObject();
        } catch (IOException | ClassNotFoundException error) {
            error.getMessage();
        }
        return null;
    }

    @Override
    public int compareTo(final Object o) {
        return Integer.compare(this.getFieldValue(), ((SudokuField) o).getFieldValue());
    }
}
