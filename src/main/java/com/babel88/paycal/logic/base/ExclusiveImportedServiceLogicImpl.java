package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.ExclusiveImportedServiceLogic;
import com.babel88.paycal.models.TTArguments;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class ExclusiveImportedServiceLogicImpl implements ExclusiveImportedServiceLogic,Serializable {

    private static final Logger log = LoggerFactory.getLogger(ExclusiveImportedServiceLogicImpl.class);
    private static final ExclusiveImportedServiceLogic instance = new ExclusiveImportedServiceLogicImpl();

    public ExclusiveImportedServiceLogicImpl() {

        log.debug("Creating the ExclusiveImportedServiceLogicImpl : {}",this);
    }

    /**
     * Total expenses when the vendor's settlement is immune to withholding tax
     *
     * @param ttArguments contains payment description
     * @return total expenses
     */
    @Override
    public BigDecimal calculateTotalExpenses(TTArguments ttArguments) {

        log.debug("CalculateTotalExpenses called with {} as argument",ttArguments);
        BigDecimal totalExpenses;
        BigDecimal amountBeforeTax = null;
        
        if(ttArguments != null) {
            amountBeforeTax = helperCalculateAmountBeforeTax(ttArguments);

            log.debug("Amount before tax calculated as : {}",amountBeforeTax);
        }
        else {
            log.debug("ttArguments object {} is null", ttArguments);
        }
        totalExpenses = helperCalculateTotalExpenses(ttArguments, amountBeforeTax);

        log.debug("Total expenses calculated as : {}",totalExpenses);

        return totalExpenses.setScale(2,HALF_EVEN);
    }

    @Override
    public BigDecimal helperCalculateTotalExpenses(TTArguments ttArguments, BigDecimal amountBeforeTax) {

        log.debug("helperCalculateTotalExpenses helper method called with {} and {} as arguments",
                ttArguments, amountBeforeTax);
        return amountBeforeTax.multiply(
                (ONE.add(ttArguments.getReverseVatRate()))
        );
    }

    @Override
    public BigDecimal helperCalculateAmountBeforeTax(TTArguments ttArguments) {

        log.debug("helperCalculateAmountBeforeTax called with {} as argument",ttArguments);
        BigDecimal amountBeforeTax = null;

        if(ttArguments != null) {
            amountBeforeTax = ttArguments.getInvoiceAmount()
                    .divide(
                            (ONE.subtract(ttArguments.getWithholdingTaxRate())),
                            ROUND_HALF_EVEN
                    );
        } else{

            log.debug("ttArguments passed is null");
        }

        log.debug("Amount before tax calculated as {}",amountBeforeTax);
        return amountBeforeTax;
    }

    /**
     * Payable to vendor when the vendor's settlement is immune to withholding tax
     *
     * @param ttArguments contains payment description
     * @return amount payable to vendor
     */
    @Override
    public BigDecimal calculateToPayee(TTArguments ttArguments) {

        log.debug("calculateToPayee called with {} as argument",ttArguments);
        BigDecimal toPayee = null;
        if(ttArguments!=null){

            toPayee = ttArguments.getInvoiceAmount();
        }

        log.debug("Amount payable to vendor calculate as : {}",toPayee);
        return toPayee.setScale(2,HALF_EVEN);
    }

    /**
     * Withholding tax when the vendor's settlement is immune to withholding tax
     *
     * @param ttArguments contains payment description
     * @return withholding tax
     */
    @Override
    public BigDecimal calculateWithholdingTax(TTArguments ttArguments) {

        log.debug("CalculateWithholdingTax called with {} as argument",ttArguments);
        BigDecimal wth = null;

        if(ttArguments!=null){
            log.debug("Using helper method to get amount before tax...");
            wth = helperCalculateAmountBeforeTax(ttArguments)
                    .multiply(ttArguments.getWithholdingTaxRate());
        } else {

            log.debug("The ttArguments is null");
        }
        log.debug("WithholdingTax calculated as : {}",wth);
        return wth.setScale(2,HALF_EVEN);
    }

    /**
     * Withholding vat when the vendor's settlement is immune to withholding tax
     *
     * @param ttArguments contains payment description
     * @return withholding vat
     */
    @Override
    public BigDecimal calculateWithholdingVat(TTArguments ttArguments) {

        BigDecimal wth = null;

        if(ttArguments!=null){
            log.debug("Using helper method to get amount before tax...");
            wth = helperCalculateAmountBeforeTax(ttArguments)
                    .multiply(ttArguments.getReverseVatRate());
        } else {

            log.debug("The ttArguments is null");
        }
        log.debug("WithholdingTax calculated as : {}",wth);
        return wth.setScale(2,HALF_EVEN);
    }

    @Contract(pure = true)
    public static ExclusiveImportedServiceLogic getInstance() {
        return instance;
    }
}
