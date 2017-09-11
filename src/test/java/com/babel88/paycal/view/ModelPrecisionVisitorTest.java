package com.babel88.paycal.view;

import com.babel88.paycal.api.DefaultPaymentModel;
import com.babel88.paycal.models.PaymentModel;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static java.math.RoundingMode.HALF_EVEN;
import static java.math.RoundingMode.UP;
import static org.junit.Assert.assertEquals;

public class ModelPrecisionVisitorTest {

    private ModelPrecisionVisitor modelPrecisionVisitor;
    private DefaultPaymentModel paymentModel;

    @Before
    public void setUp() throws Exception {

        paymentModel = new PaymentModel()
                .setTotalExpense(bd("22433.52").setScale(2,HALF_EVEN))
                .setToPayee(bd("41063.66").setScale(2,HALF_EVEN))
                .setToPrepay(bd("22932.04").setScale(2,HALF_EVEN))
                .setWithHoldingVat(bd("2346.49").setScale(2,HALF_EVEN))
                .setWithholdingTax(bd("1955.41").setScale(2,HALF_EVEN))
        ;

        modelPrecisionVisitor = new ModelPrecisionVisitor()
                .setPaymentModel(paymentModel);

    }

    @Test
    public void PrecisionOfTheVendorRoundingMode() throws Exception {

        assertEquals(bd("1.05"),
                Arrays.asList(modelPrecisionVisitor.round(bd("1.03"), bd("0.05"), HALF_EVEN))
                .get(0)
        );
        assertEquals(bd("1.05"),
                Arrays.asList(modelPrecisionVisitor.round(bd("1.06"), bd("0.05"), HALF_EVEN))
                .get(0)
        );
        assertEquals(bd("1.65"),
                Arrays.asList(modelPrecisionVisitor.round(bd("1.66"), bd("0.05"), HALF_EVEN))
                .get(0)
        );
        assertEquals(bd("1.65"),
                Arrays.asList(modelPrecisionVisitor.round(bd("1.65"), bd("0.05"), HALF_EVEN))
                .get(0)
        );
        assertEquals(bd("1.90"),
                Arrays.asList(modelPrecisionVisitor.round(bd("1.90"), bd("0.05"), HALF_EVEN))
                .get(0)
        );
        assertEquals(bd("1.90"),
                Arrays.asList(modelPrecisionVisitor.round(bd("1.900001"), bd("0.05"), HALF_EVEN))
                .get(0)
        );
    }

    @Test
    public void precisionOfWithholdingTaxes() throws Exception {

        assertEquals(bd("2.00"),
                Arrays.asList(modelPrecisionVisitor.round(bd("1.05"), bd("1.00"), UP))
                        .get(0));
        assertEquals(bd("4.00"),
                Arrays.asList(modelPrecisionVisitor.round(bd("3.05"), bd("1.00"), UP))
                        .get(0));
        assertEquals(bd("8.00"),
                Arrays.asList(modelPrecisionVisitor.round(bd("7.65"), bd("1.00"), UP))
                        .get(0));
        assertEquals(bd("4.00"),
                Arrays.asList(modelPrecisionVisitor.round(bd("4.00"), bd("1.00"), UP))
                        .get(0));
        assertEquals(bd("4.00"),
                Arrays.asList(modelPrecisionVisitor.round(bd("3.00001"), bd("1.00"), UP))
                        .get(0));
    }

    @Test
    public void stateBeforePrecisionReview() throws Exception {

        assertEquals(
                paymentModel.getTotalExpense().add(paymentModel.getToPrepay()),
                paymentModel.getWithholdingVat()
                        .add(paymentModel.getWithholdingTax())
                        .add(paymentModel.getToPayee())
        );
    }

    @Test
    public void stateAfterPrecisionReview() throws Exception {

        modelPrecisionVisitor.reviewModelPrecision(paymentModel);

        assertEquals(
                paymentModel.getTotalExpense().add(paymentModel.getToPrepay()),
                paymentModel.getWithholdingVat()
                        .add(paymentModel.getWithholdingTax())
                        .add(paymentModel.getToPayee())
        );
    }

    @Test
    public void roundinAmountToVendor() throws Exception {

        modelPrecisionVisitor.reviewModelPrecision(paymentModel);

        assertEquals(bd("41062.55"),paymentModel.getToPayee());
    }

    @Test
    public void roundingAmountToWithhold() throws Exception {

        modelPrecisionVisitor.reviewModelPrecision(paymentModel);

        assertEquals(bd("1956.00"),paymentModel.getWithholdingTax());
    }

    @Test
    public void prepaymentDoesNotChange() throws Exception {

        modelPrecisionVisitor.reviewModelPrecision(paymentModel);

        assertEquals(bd("22932.04"),paymentModel.getToPrepay());
    }

    @Test
    public void roundingVatToWithhold() throws Exception {

        modelPrecisionVisitor.reviewModelPrecision(paymentModel);

        assertEquals(bd("2347.00"),paymentModel.getWithholdingVat());
    }

    @NotNull
    private BigDecimal bd(String value){

        return new BigDecimal(value);
    }
}