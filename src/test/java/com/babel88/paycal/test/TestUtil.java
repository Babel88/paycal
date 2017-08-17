package com.babel88.paycal.test;

import com.babel88.paycal.config.PaymentParameters;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestUtil<T> {

    @Mock
    protected PaymentParameters parameters;

    @InjectMocks
    protected T clazz;

    public void setParametersBehaviour() throws Exception{

        when(parameters.getVatRate()).thenReturn(divPerCent(16).setScale(2));

        when(parameters.getWithholdingVatRate()).thenReturn(divPerCent(6).setScale(2));

        when(parameters.getWithholdingTaxContractor()).thenReturn(divPerCent(3).setScale(2));

        when(parameters.getWithholdingTaxRate()).thenReturn(divPerCent(5).setScale(2));
    }

    private BigDecimal divPerCent(double  denominator){

        return BigDecimal.valueOf(denominator).divide(BigDecimal.valueOf(100).setScale(2));
    }
}
