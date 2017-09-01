package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.ExclusiveImportedService;
import com.babel88.paycal.controllers.base.TTController;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.*;
import static java.math.RoundingMode.*;

/**
 * To this class is delegated computations on payments for imported services where
 * the vendor's settlement is immune from withholding taxes
 * Upon creation the amount before tax is calculated and stored and reused in other
 * calculations
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public class ExclusiveImportedServiceImpl implements ExclusiveImportedService,Serializable {

    private static final Logger log = LoggerFactory.getLogger(ExclusiveImportedServiceImpl.class);
    private TTController ttController;
    private BigDecimal invoiceAmount;
    private BigDecimal amountBeforeTax;
    private BigDecimal reverseVatRate;
    private BigDecimal withholdingTaxRate;
    private BigDecimal totalExpenses;
    private BigDecimal withholdingTaxAmount;
    private BigDecimal withholdingVatAmount;
    private BigDecimal withholdingVatRate;
    private BigDecimal toPayee;

    public ExclusiveImportedServiceImpl(TTController ttController) {

        log.debug("Creating delegate for eclusive imported services, with \n" +
                "ttController : {} as argument",ttController);
        this.ttController = ttController;
        init();
    }

    private void init(){

        log.debug("Composing internal variables for {}",this);
        invoiceAmount = ttController.getTTArgument().getInvoiceAmount();
        amountBeforeTax = calculateAmountBeforeTax();
        reverseVatRate = ttController.getTTArgument().getReverseVatRate();
        withholdingTaxRate = ttController.getTTArgument().getWithholdingTaxRate();
        withholdingVatRate = ttController.getTTArgument().getReverseVatRate();
    }

    /**
     * This method's only role is to calculate amount before tax fetching the same from the
     * delegator which is the ttController
     *
     * @return amount before tax
     */
    private BigDecimal calculateAmountBeforeTax(){

        BigDecimal amountBeforeTax;
        if(invoiceAmount != null){
            log.debug("Using invoiceAmount : {}, to calculate amount before tax",invoiceAmount);
            amountBeforeTax = invoiceAmount.multiply(preTaxCoefficient());
            log.debug("Returning amount before tax as : {}",amountBeforeTax);
        } else {
            log.debug("Invoice amount is null attempting to use to redundant approach to get the \n" +
                    "invoice amount from ttController : {}",ttController);
            invoiceAmount= ttController.getTTArgument().getInvoiceAmount();
            amountBeforeTax = invoiceAmount.multiply(preTaxCoefficient());
            log.debug("Returning amount before tax as : {}",amountBeforeTax);
        }
        return amountBeforeTax;
    }

    /**
     * This methods only role is to return the coefficient which when multiplied with exclusive
     * invoice amount yields amount before tax
     *
     * @return coeffiecient for amount before tax
     */
    private BigDecimal preTaxCoefficient(){

        return ONE.divide(
                ONE.subtract(ttController.getTTArgument().getWithholdingTaxRate()).setScale(10,HALF_EVEN),HALF_EVEN
        );
    }


    /**
     * Method implements the ExclusiveImportedService contract by using ttController-derived
     * varibles to calculate total expenses
     * @return total expenses
     */
    @Override
    public BigDecimal calculateTotalExpenses() {

        if(amountBeforeTax != null && reverseVatRate != null){
            log.debug("Total expenses calculated using amountBeforeTax : {}",amountBeforeTax);
            totalExpenses = amountBeforeTax.multiply(ONE.add(reverseVatRate));
            log.debug("Total expenses calculated : {}",totalExpenses);
        } else if(reverseVatRate != null){

            log.debug("The amount before tax is null, redundant approach to get the amount from\n" +
                    "the ttController");
            amountBeforeTax = calculateAmountBeforeTax();
            totalExpenses = amountBeforeTax.multiply(ONE.add(reverseVatRate));
            log.debug("Total expenses calculated : {}",totalExpenses);
        } else{

            log.debug("Both the amount before tax and the reverseVatRate are null. Expensively \n" +
                    "fetching variables from the ttController");
            amountBeforeTax = calculateAmountBeforeTax();
            reverseVatRate = ttController.getTTArgument().getReverseVatRate();
            totalExpenses = amountBeforeTax.multiply(ONE.add(reverseVatRate));
            log.debug("Total expenses calculated : {}",totalExpenses);
        }
        return totalExpenses;
    }

    /**
     * @return amount payable to vendor
     */
    @Override
    public BigDecimal calculateToPayee() {

        if(invoiceAmount != null){
            log.debug("Calculating amount payable to payee for : {}",invoiceAmount);
            toPayee = invoiceAmount;
            log.debug("Returning amount payable to payee : {}",toPayee);
            return toPayee;
        } else {

            log.debug("The invoice amount variable is null and must be acquired expensively from \n" +
                    "the delegator {}",ttController);
            invoiceAmount=ttController.getTTArgument().getInvoiceAmount();
            toPayee = invoiceAmount;
            log.debug("Returning amount payable to payee : {}",toPayee);
            return toPayee;
        }
    }

    /**
     * @return withholding tax
     */
    @Override
    public BigDecimal calculateWithholdingTax() {

        if(amountBeforeTax != null && withholdingTaxRate != null){
            withholdingTaxAmount = amountBeforeTax.multiply(withholdingTaxRate);
        } else if(amountBeforeTax != null){
            withholdingTaxRate = ttController.getTTArgument().getWithholdingTaxRate();
            withholdingTaxAmount = amountBeforeTax.multiply(withholdingTaxRate);
        } else if(withholdingTaxRate != null){
            amountBeforeTax= calculateAmountBeforeTax();
            withholdingTaxAmount = amountBeforeTax.multiply(withholdingTaxRate);
        } else {
            withholdingTaxRate = ttController.getTTArgument().getWithholdingTaxRate();
            amountBeforeTax= calculateAmountBeforeTax();
            withholdingTaxAmount = amountBeforeTax.multiply(withholdingTaxRate);
        }

        return withholdingTaxAmount;
    }

    /**
     * @return withholding vat
     */
    @Override
    public BigDecimal calculateWithholdingVat() {

        if(amountBeforeTax != null && withholdingVatRate != null){
            withholdingVatAmount = amountBeforeTax.multiply(withholdingVatRate);
        } else if(amountBeforeTax != null){
            withholdingVatRate=ttController.getTTArgument().getReverseVatRate();
            withholdingVatAmount = amountBeforeTax.multiply(withholdingVatRate);
        } else if(withholdingVatRate != null){
            amountBeforeTax = ttController.getTTArgument().getAmountBeforeTax();
            withholdingVatAmount = amountBeforeTax.multiply(withholdingVatRate);
        } else{
            withholdingVatRate=ttController.getTTArgument().getReverseVatRate();
            amountBeforeTax = ttController.getTTArgument().getAmountBeforeTax();
            withholdingVatAmount = amountBeforeTax.multiply(withholdingVatRate);
        }
        return withholdingVatAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExclusiveImportedServiceImpl that = (ExclusiveImportedServiceImpl) o;
        return Objects.equal(ttController, that.ttController) &&
                Objects.equal(invoiceAmount, that.invoiceAmount) &&
                Objects.equal(amountBeforeTax, that.amountBeforeTax) &&
                Objects.equal(reverseVatRate, that.reverseVatRate) &&
                Objects.equal(withholdingTaxRate, that.withholdingTaxRate) &&
                Objects.equal(totalExpenses, that.totalExpenses) &&
                Objects.equal(withholdingTaxAmount, that.withholdingTaxAmount) &&
                Objects.equal(withholdingVatAmount, that.withholdingVatAmount) &&
                Objects.equal(withholdingVatRate, that.withholdingVatRate) &&
                Objects.equal(toPayee, that.toPayee);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ttController, invoiceAmount, amountBeforeTax, reverseVatRate,
                withholdingTaxRate, totalExpenses, withholdingTaxAmount, withholdingVatAmount,
                withholdingVatRate, toPayee);
    }
}
