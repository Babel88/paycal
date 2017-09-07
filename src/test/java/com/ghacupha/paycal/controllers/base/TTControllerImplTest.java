package com.ghacupha.paycal.controllers.base;

import com.ghacupha.paycal.api.DefaultPaymentModel;
import com.ghacupha.paycal.api.controllers.TTController;
import com.ghacupha.paycal.models.TTArguments;
import com.ghacupha.paycal.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TTControllerImplTest extends TestUtils<TTController> {

    private TTController ttController;
    private TTArguments ttArguments;
    private DefaultPaymentModel paymentModel;


    @Before
    public void setUp() throws Exception {

        ttController = new TTControllerImpl();

        ttArguments = new TTArguments()
                .setInvoiceAmount(setAccuracy(100000.00))
                .setReverseVatRate(setAccuracy(0.16))
                .setWithholdingTaxRate(setAccuracy(0.20))
                .setTaxExclusionPolicy(true);

    }

    @Override
    public TTController getBeanInstance() {

        return ttController;
    }

    @Test
    public void updateTotalExpense() throws Exception {

        paymentModel = ttController.updateTotalExpense(ttArguments);

        assertEquals(paymentModel.getTotalExpense(), setAccuracy(145000.00));
    }

    @Test
    public void updateToPayee() throws Exception {

        paymentModel = ttController.updateToPayee(ttArguments);

        assertEquals(paymentModel.getToPayee(), setAccuracy(100000.00));
    }

    @Test
    public void updateWithholdingTax() throws Exception {

        paymentModel = ttController.updateWithholdingTax(ttArguments);

        assertEquals(paymentModel.getWithholdingTax(), setAccuracy(25000.00));
    }

    @Test
    public void updateWithholdingVat() throws Exception {

        paymentModel = ttController.updateWithholdingVat(ttArguments);

        assertEquals(paymentModel.getWithholdingVat(), setAccuracy(20000.00));
    }

    @Test
    public void updateToPrepay() throws Exception {

//        paymentModel = ttController.updateToPrepay(ttArguments);
//
//        assertEquals(paymentModel.getToPrepay(),setAccuracy(0.00));
    }

    @Test
    public void getPaymentModel() throws Exception {

        TTControllerImpl testController = (TTControllerImpl) TTControllerImpl.getInstance();
        testController.setPaymentModel(paymentModel);

        DefaultPaymentModel testModel = testController.getPaymentModel();

        assertEquals(testModel, paymentModel);
    }

    @Test
    public void getPrepaymentController() throws Exception {

        assertNotNull(TTControllerImpl.getInstance().getPrepaymentController());
    }

    @Test
    public void getTtArguments() throws Exception {

        assertNotNull(TTControllerImpl.getInstance().getTtArguments());
    }

    @Test
    public void setTtArguments() throws Exception {

        TTController testController = TTControllerImpl.getInstance();
        testController.setTtArguments(ttArguments);

        TTArguments testTTArgument = testController.getTtArguments();

        assertEquals(testTTArgument, ttArguments);
    }

}