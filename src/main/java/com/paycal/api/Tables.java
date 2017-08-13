package com.paycal.api;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public interface Tables {

    /**
     * This method creates a row with every call
     * @param row index for the row
     * @param colum index for the column
     * @param content string item to dosplay
     */
    void addString(int row, int colum, String content);

    /**
     * This method takes all fields in the table printing them in table format
     *
     * @return table String
     */
    String toString();
}
