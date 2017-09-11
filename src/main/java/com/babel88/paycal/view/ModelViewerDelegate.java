package com.babel88.paycal.view;

import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.grid.Border;
import com.inamik.text.tables.grid.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.inamik.text.tables.Cell.Functions.LEFT_ALIGN;
import static com.inamik.text.tables.Cell.Functions.RIGHT_ALIGN;

public class ModelViewerDelegate {

    private final Logger log = LoggerFactory.getLogger(ModelViewerDelegate.class);

    /* private pointer to the delegator*/
    private ModelViewerVisitor modelViewerVisitor;


    public ModelViewerDelegate(ModelViewerVisitor modelViewerVisitor) {

        log.debug("Creating a model viewer delegate {} with {} as argument",
                this,modelViewerVisitor);
        this.modelViewerVisitor = modelViewerVisitor;
    }

    public String renderPaymentModel() {

        String tableString = "";
        try {
            if (modelViewerVisitor != null) {
                log.debug("Rendering the payment model in table format with payment model \n" +
                        ": {}", modelViewerVisitor);

                tableString = tableRendering();
            } else {

                log.error("The payment Model is null");
            }
        }catch (Exception e){

            e.printStackTrace();
        }

        log.debug("Returning a rendered table : {}",tableString);

        return tableString;
    }

    private String tableRendering(){

        // calculate total expenses
        BigDecimal totalExpenses = modelViewerVisitor.getPaymentModel().getTotalExpense()
                .add(modelViewerVisitor.getPaymentModel().getToPrepay())
                .setScale(2, RoundingMode.HALF_EVEN);

        GridTable gridTable = GridTable.of(9,4)
                .put(0,0, Cell.of("No."))
                .put(0,1, Cell.of("LEDGER"))
                .put(0,2, Cell.of("DEBITS"))
                .put(0,3, Cell.of("CREDITS"))

                .put(1,0, Cell.of("==="))
                .put(1,1, Cell.of("========"))
                .put(1,2, Cell.of("========"))
                .put(1,3, Cell.of("========"))

                .put(2,0,Cell.of("1."))
                .put(2,1, Cell.of("Total Expenses"))
                .put(2,2, Cell.of(modelViewerVisitor.getPaymentModel().getTotalExpense().toString()))
                .put(2,3,Cell.of(" - "))

                .put(3,0,Cell.of("2."))
                .put(3,1, Cell.of("Prepayment"))
                .put(3,2, Cell.of(modelViewerVisitor.getPaymentModel().getToPrepay().toString()))
                .put(3,3,Cell.of(" - "))

                .put(4,0,Cell.of("3."))
                .put(4,1, Cell.of("Withholding Vat"))
                .put(4,2, Cell.of(" - "))
                .put(4,3,Cell.of(modelViewerVisitor.getPaymentModel().getWithholdingVat().toString()))

                .put(5,0,Cell.of("4."))
                .put(5,1, Cell.of("Withholding Tax"))
                .put(5,2, Cell.of(" - "))
                .put(5,3,Cell.of(modelViewerVisitor.getPaymentModel().getWithholdingTax().toString()))

                .put(6,0,Cell.of("5."))
                .put(6,1, Cell.of("To Pay Vendor"))
                .put(6,2, Cell.of(" - "))
                .put(6,3,Cell.of(modelViewerVisitor.getPaymentModel().getToPayee().toString()))

                .put(7,0,Cell.of(" "))
                .put(7,1, Cell.of(" "))
                .put(7,2, Cell.of("---------"))
                .put(7,3, Cell.of("---------"))


                .put(8,0,Cell.of(" "))
                .put(8,1, Cell.of("TOTALS"))
                .put(8,2, Cell.of(totalExpenses.toString()))
                .put(8,3,Cell.of(totalExpenses.toString()))
                ;

        configureTable(gridTable);

        return Util.asString(gridTable);
    }

    @NotNull
    @Contract(pure = true)
    private GridTable configureTable(GridTable gridTable) {

       gridTable
                .applyToCol(0,LEFT_ALIGN.withWidth(4).withChar(' '))
                .applyToCol(1,LEFT_ALIGN.withWidth(16).withChar(' '))
                .applyToCol(2,RIGHT_ALIGN.withWidth(13).withChar(' '))
                .applyToCol(3,RIGHT_ALIGN.withWidth(13).withChar(' '))

                /*.applyToRow(0,VERTICAL_CENTER.withHeight(2).withChar(' '))
                .applyToRow(1,VERTICAL_CENTER.withHeight(0).withChar(' '))
                .applyToRow(2,VERTICAL_CENTER.withHeight(2).withChar(' '))
                .applyToRow(3,VERTICAL_CENTER.withHeight(2).withChar(' '))
                .applyToRow(4,VERTICAL_CENTER.withHeight(2).withChar(' '))
                .applyToRow(5,VERTICAL_CENTER.withHeight(2).withChar(' '))
                .applyToRow(6,VERTICAL_CENTER.withHeight(2).withChar(' '))
                .applyToRow(7,VERTICAL_CENTER.withHeight(2).withChar(' '))*/;

       //gridTable = Border.SINGLE_LINE.apply(gridTable);

       return Border.SINGLE_LINE.apply(gridTable);
    }
}
