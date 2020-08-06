package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.config.PaymentParameters;
import com.babel88.paycal.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test for contractor payments
 *
 * Created by edwin.njeru on 01/09/2017.
 */
public class ContractorPaymentsTest extends TestUtils<DefaultLogic> {

    private DefaultLogic contractorLogic;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        contractorLogic = new ContractorLogic(new PaymentParameters());
    }

    @Test
    public void contractorLogicIsNotNull() throws Exception {

        assertNotNull(contractorLogic);
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public DefaultLogic getBeanInstance() {

        return contractorLogic;
    }

    @Test
    public void calculatePayableToVendor() throws Exception {

        toPayee = contractorLogic.calculateToPayee(invoiceAmount);

        assertEquals(bd(110912.28),toPayee);
    }

    @Test
    public void calculateWithholdingTax() throws Exception {
        wthTax = contractorLogic.calculateWithholdingTax(invoiceAmount);

        assertEquals(bd(3052.63),wthTax);
    }

    @Test
    public void calculateWithholdingVat() throws Exception {

        wthVat = contractorLogic.calculateWithholdingVat(invoiceAmount);

        assertEquals(bd(2035.09),wthVat);
    }

    @Test
    public void calculateTotalExpense() throws Exception {

        totalExpense = contractorLogic.calculateTotalExpense(invoiceAmount);

        assertEquals(bd(116000.00),totalExpense);
    }

    @Override
    public void beanIsSerializable() throws Exception {
        //
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