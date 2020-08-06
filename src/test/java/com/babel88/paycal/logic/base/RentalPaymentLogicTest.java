package com.babel88.paycal.logic.base;

import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public class RentalPaymentLogicTest extends TestUtils<RentalPaymentLogic> {


    private RentalPaymentLogic rentalPaymentLogic;
    private BigDecimal invoiceAmount = BigDecimal.valueOf(116000).setScale(2, RoundingMode.HALF_EVEN);

    @Before
    public void setUp() throws Exception {

        rentalPaymentLogic = new RentalPaymentLogic(new PaymentParameters());
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public RentalPaymentLogic getBeanInstance() {

        return rentalPaymentLogic;
    }

    @Test
    public void calculateTotalExpense() throws Exception {

        BigDecimal totalExpense = rentalPaymentLogic.calculateTotalExpense(invoiceAmount);

        assertEquals(bd(116000.00), totalExpense);
    }

    @Test
    public void calculateToPayee() throws Exception {

        BigDecimal payee = rentalPaymentLogic.calculateToPayee(invoiceAmount);

        assertEquals(bd(103789.47), payee);
    }

    @Test
    public void calculateWithholdingTax() throws Exception {
        BigDecimal wth = rentalPaymentLogic.calculateWithholdingTax(invoiceAmount);

        assertEquals(bd(10175.44), wth);
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        BigDecimal wthVat = rentalPaymentLogic.calculateWithholdingVat(invoiceAmount);

        assertEquals(bd(2035.09), wthVat);
    }

    @Test
    public void calculateTotalExpense1() throws Exception {

        super.totalExpense = rentalPaymentLogic.calculateTotalExpense(super.invoiceAmount);

        assertEquals(bd(116000.00),totalExpense);
    }

    @Test
    public void calculateToPayee1() throws Exception {
        super.toPayee = rentalPaymentLogic.calculateToPayee(super.invoiceAmount);

        assertEquals(bd(103789.47),toPayee);

    }

    @Test
    public void calculateWithholdingTax1() throws Exception {
        super.wthTax = rentalPaymentLogic.calculateWithholdingTax(super.invoiceAmount);

        assertEquals(bd(10175.44),wthTax);
    }

    @Test
    public void calculateWithholdingVat1() throws Exception {

        super.wthVat = rentalPaymentLogic.calculateWithholdingVat(super.invoiceAmount);

        assertEquals(bd(2035.09),wthVat);
    }

    @Override
    public void equalsAndHashcodeContract() throws Exception {
        //
    }

    @Override
    public void getterAndSetterCorrectness() throws Exception {
        //
    }
}