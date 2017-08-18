package com.babel88.paycal.logic;

import com.babel88.paycal.api.logic.Contractors;
import com.babel88.paycal.config.PaymentParameters;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static java.math.RoundingMode.HALF_EVEN;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
public class ContractorPayments implements Contractors {

    private final AtomicReference<ThreadLocal<BigDecimal>> vatRate;
    private final AtomicReference<ThreadLocal<BigDecimal>> withholdingTaxRate;
    private final AtomicReference<ThreadLocal<BigDecimal>> withholdingVatRate;

    private PaymentParameters parameters;

    public ContractorPayments(PaymentParameters parameters) {

        this.parameters = parameters;

        vatRate =
                new AtomicReference<>(
                        ThreadLocal.withInitial(
                            () -> this.parameters.getVatRate().divide(BigDecimal.valueOf(100)
                            )
                ));

        withholdingTaxRate =
                new AtomicReference<>(
                        ThreadLocal.withInitial(
                                () -> this.parameters.getWithholdingTaxContractor().divide(BigDecimal.valueOf(100))
                        )
                );
        withholdingVatRate =
                new AtomicReference<>(
                        ThreadLocal.withInitial(
                            () -> this.parameters.getWithholdingVatRate().divide(BigDecimal.valueOf(100)
                        )
                ));

    }

    /**
     * Calculates the amount to go the contractor's account or cheque
     *
     * @param total this is the total amount of the Invoice
     * @return the calculated amount payable to contractor
     */
    @Override
    public BigDecimal calculatePayableToContractor(BigDecimal total) {


        BigDecimal invoiceTotal = total;

        BigDecimal b4Tax = total.divide(vatRate.get().get().add(BigDecimal.ONE), HALF_EVEN);

        BigDecimal withholdingTax = b4Tax.multiply(withholdingTaxRate.get().get());

        BigDecimal vatWithholding = b4Tax.multiply(withholdingVatRate.get().get());

        return invoiceTotal.subtract(withholdingTax).subtract(vatWithholding).setScale(2, HALF_EVEN);
    }

    /**
     * calculate the amount to withholding on the contractor being 3% of the amount before taxes
     *
     * @param total this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    @Override
    public BigDecimal calculateContractorWithholdingTax(BigDecimal total) {


        BigDecimal invoiceTotal = total;

        BigDecimal b4Tax = total.divide(vatRate.get().get().add(BigDecimal.ONE),HALF_EVEN);

        BigDecimal withholdingTax = b4Tax.multiply(withholdingTaxRate.get().get());

        return withholdingTax.setScale(2,HALF_EVEN);
    }

    /**
     * calculates the amount to withhold being 6% of Invoice amount before tax
     *
     * @param total this is the total amount of the Invoice
     * @return 3% of withholding tax to withhold
     */
    @Override
    public BigDecimal calculateContractorWithholdingVat(BigDecimal total) {


        BigDecimal invoiceTotal = total;

        BigDecimal amountBeforeTax = total.divide(vatRate.get().get().add(BigDecimal.ONE),HALF_EVEN);

        BigDecimal vatWithholding = amountBeforeTax.multiply(withholdingVatRate.get().get());

        return vatWithholding.setScale(2,HALF_EVEN);
    }

    /**
     * calculate the total expense for a contractor - involving engagement
     *
     * @param invoiceAmount the amount requested in the payment requisition
     * @return return amount expendable to ledger in BigDecimal
     */
    @Override
    public BigDecimal calculateTotalExpense(BigDecimal invoiceAmount) {

        return invoiceAmount;
    }
}
