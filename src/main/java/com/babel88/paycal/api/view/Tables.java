package com.babel88.paycal.api.view;

import com.babel88.paycal.view.DisplayImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public interface Tables {

    /**
     * This method creates a row with every call
     *
     * @param row     index for the row
     * @param colum   index for the column
     * @param content string item to display
     */
    void addString(int row, int colum, String content);

    /**
     * This method takes all fields in the table printing them in table format
     *
     * @return table String
     */
    String toString();

    /**
     * create string table
     *
     * @param vatWithhold Amount of withholding VAT
     * @param withHold Amount of withholding tax
     * @param prepayment Amount to be prepaid
     * @param expensed Amount to be expensed
     * @param paid Amount to be given to the payee
     * @param display Implementation fo the Display to be used
     * @return table string
     */
    @NotNull
    default Tables createTable(String vatWithhold, String withHold, String prepayment, String expensed, String paid, DisplayImpl display) {
        // We are now going to create an object in table maker, which we'll user
        // to paymentModelView data on the console


        addString(0, 0, "");
        addString(0, 1, "RESULTS!!!");
        addString(0, 2, "");
        // This is the title row

        addString(1, 0, "");
        addString(1, 1, "");
        addString(1, 2, "");
        // That should create an empty ROW

        addString(2, 0, "Particulars");
        addString(2, 1, "Debit");
        addString(2, 2, "Credit");
        // That should label the columns

        addString(3, 0, "a) Expense");
        addString(4, 0, "b) Prepayment");
        addString(5, 0, "c) Withhold VAT");
        addString(6, 0, "d) Withhold TAX");
        addString(7, 0, "e) To Payee");
        // That should label the rows

        addString(3, 1, expensed);
        addString(3, 2, "-");
        // That's your expense

        addString(4, 1, prepayment);
        addString(4, 2, "-");
        // That your prepayment

        addString(5, 1, "-");
        addString(5, 2, vatWithhold);
        // That's the withholding VAT

        addString(6, 1, "-");
        addString(6, 2, withHold);
        // That's the withholding tax chargeable

        addString(7, 1, "-");
        addString(7, 2, paid);
        // That's the amount payable to payee

        return this;
    }
}
