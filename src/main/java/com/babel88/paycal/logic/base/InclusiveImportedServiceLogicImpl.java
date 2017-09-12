package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.InclusiveImportedServiceLogic;
import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.*;

/**
 * Logic for telegraphic transfers when the vendor is subject to withholding tax
 * Created by edwin.njeru on 01/09/2017.
 */
public class InclusiveImportedServiceLogicImpl implements InclusiveImportedServiceLogic {

    private final Logger log = LoggerFactory.getLogger(InclusiveImportedServiceLogicImpl.class);

    public InclusiveImportedServiceLogicImpl() {
        log.debug("InclusiveImportedServiceLogic object has been created : {}",this);
    }

    /**
     * Calculate total expenses
     *
     * @param ttArguments contains payment description
     * @return total expenses when the withholding tax is inclusive
     */
    @Override
    public BigDecimal calculateTotalExpenses(TTArguments ttArguments) {

        log.debug("helperCalculateTotalExpenses has been called with {} as argument",ttArguments);
        BigDecimal totalExpenses = null;

        if(ttArguments != null){

            totalExpenses = helperCalculateTotalExpenses(ttArguments, helperCalculateAmountBeforeTax(ttArguments));

            log.debug("Total expenses calculated : {}",totalExpenses);
        } else {

            log.error("the ttArguments is null");
        }

        return totalExpenses;
    }

    /**
     * Calculate amount to payee
     *
     * @param ttArguments contains payment description
     * @return amount payable to payee when withholding tax is inclusive
     */
    @Override
    public BigDecimal calculateToPayee(TTArguments ttArguments) {

        log.debug("calculateToPayee has been called with {} as argument",ttArguments);
        BigDecimal toPayee = null;

        if(ttArguments != null){

            toPayee = helperCalculateAmountBeforeTax(ttArguments)
                    .multiply(
                            ONE.subtract(ttArguments.getWithholdingTaxRate())
                    );
            log.debug("Amount to payee : {}",toPayee);
        } else {

            log.error("The ttArguments is null");
        }

        return toPayee.setScale(2,HALF_EVEN);
    }

    /**
     * Calculate withholding tax
     *
     * @param ttArguments contains payment description
     * @return withholding tax inclusive in invoice amount
     */
    @Override
    public BigDecimal calculateWithholdingTax(TTArguments ttArguments) {
        log.debug("calculateWithholdingTax has been called with {} as argument",ttArguments);
        BigDecimal withholdingTax = null;

        if(ttArguments != null){

            withholdingTax = helperCalculateAmountBeforeTax(ttArguments)
                    .multiply(ttArguments.getWithholdingTaxRate());

            log.debug("Withholding tax : {}",withholdingTax);
        } else {

            log.error("the ttArguments is null");
        }

        return withholdingTax.setScale(2,HALF_EVEN);
    }

    /**
     * Calculate withholding vat
     *
     * @param ttArguments contains payment description
     * @return withholding vat
     */
    @Override
    public BigDecimal calculateWithholdingVat(TTArguments ttArguments) {

        log.debug("calculateWithholdingVat has been called with {} as argument",ttArguments);
        BigDecimal withholdingVat = null;

        if(ttArguments != null){

            withholdingVat = helperCalculateAmountBeforeTax(ttArguments)
                    .multiply(ttArguments.getReverseVatRate());

            log.debug("Withholding vat : {}",withholdingVat);
        } else {

            log.error("the ttArguments is null");
        }

        return withholdingVat.setScale(2,HALF_EVEN);
    }

    /**
     * Helper method to refactor calculation of total expenses, withholding taxes and withholding vat
     *
     * @param ttArguments contains payment description
     * @return amount before taxes
     */
    @Override
    public BigDecimal helperCalculateAmountBeforeTax(TTArguments ttArguments) {

        BigDecimal amountBeforeTax = null;
        log.debug("helperCalculateAmountBeforeTax helper method has been called with {} as argument",ttArguments);
        if(ttArguments!=null){

            amountBeforeTax = ttArguments.getInvoiceAmount();

            log.debug("AmountBeforeTax : {}",amountBeforeTax);
        } else {

            log.error("The ttArguments is null");
        }
        return amountBeforeTax;
    }

    /**
     * Helper method to refactor calculation of total expenses, using ttArguments,and the calculated amount
     * before taxes
     *
     * @param ttArguments     contains payment description
     * @param amountBeforeTax calculates in the calling method
     * @return total expenses returned to the caller
     */
    @Override
    public BigDecimal helperCalculateTotalExpenses(TTArguments ttArguments, BigDecimal amountBeforeTax) {

        log.debug("helperCalculateTotalExpenses method called with {} and {} as arguments",ttArguments, amountBeforeTax);

        BigDecimal totalExpenses = null;

        if(ttArguments!=null){

            totalExpenses = amountBeforeTax.multiply(ONE.add(ttArguments.getReverseVatRate()));

            log.debug("Total expenses calculated as : {}",totalExpenses);
        } else {

            log.error("ttArguments is null");
        }

        return totalExpenses.setScale(2, HALF_EVEN);
    }
}
