package com.babel88.paycal.logic.base;

import com.babel88.paycal.api.controllers.DefaultControllers;
import com.babel88.paycal.controllers.base.TTController;
import com.babel88.paycal.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * ExclusiveImportedService test
 * Created by edwin.njeru on 01/09/2017.
 */
public class ExclusiveImportedServiceImplTest extends TestUtils<ExclusiveImportedServiceImpl> {

    private ExclusiveImportedServiceImpl importedService;
    @Mock
    private TTController ttController;

    @Before
    public void setUp() throws Exception {
        //super();
        MockitoAnnotations.initMocks(ExclusiveImportedServiceImplTest.class);
        importedService = new ExclusiveImportedServiceImpl(ttController);
    }

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public ExclusiveImportedServiceImpl getBeanInstance() {

        return importedService;
    }

    @Test
    public void calculateTotalExpenses() throws Exception {
    }

    @Test
    public void calculateToPayee() throws Exception {
    }

    @Test
    public void calculateWithholdingTax() throws Exception {
    }

    @Test
    public void calculateWithholdingVat() throws Exception {
    }

}