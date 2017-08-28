package com.babel88.utils.tables.samples;

import com.babel88.paycal.api.controllers.BaseController;
import com.babel88.paycal.controllers.ContractorPaymentsController;

/**
 * Created by edwin.njeru on 28/08/2017.
 */
public class BaseControllerClient {

    private BaseController controller;

    public BaseControllerClient() {

        controller = new ContractorPaymentsController();
    }

    public static void main(String[] args) {

        new BaseControllerClient().RunClient();
    }

    public void RunClient() {

        controller.invoke();
    }
}
