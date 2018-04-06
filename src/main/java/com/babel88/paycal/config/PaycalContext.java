/*
 * This file is part of paycal, a commandline business calculations commandline application
 *
 *     Copyright (C) 2018  Edwin Njeru
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.babel88.paycal.config;

import com.babel88.paycal.PaycalApp;
import com.babel88.paycal.PaymentFactory;
import com.babel88.paycal.api.*;
import com.babel88.paycal.api.controllers.*;
import com.babel88.paycal.api.logic.DefaultLogic;
import com.babel88.paycal.api.logic.Prepayable;
import com.babel88.paycal.api.view.FeedBack;
import com.babel88.paycal.api.view.Visitor;
import com.babel88.paycal.controllers.ReportsController;
import com.babel88.paycal.controllers.base.*;
import com.babel88.paycal.controllers.prepayments.PrepaymentControllerImpl;
import com.babel88.paycal.logic.SimplePrepayments;
import com.babel88.paycal.logic.BusinessLogicRouter;
import com.babel88.paycal.logic.base.ContractorLogic;
import com.babel88.paycal.logic.base.DefaultTypicalWithholdingTaxPayment;
import com.babel88.paycal.logic.base.TypicalPaymentsImpl;
import com.babel88.paycal.models.PaymentModel;
import com.babel88.paycal.models.ResultsOutput;
import com.babel88.paycal.view.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class PaycalContext {

    @Bean
    @DependsOn(value = {"feedBack","paymentFactory"})
    public PaycalApp paycalApp(){
        return new PaycalApp(feedBack(),paymentFactory());
    }

    @Bean
    public FeedBack feedBack(){

        return new FeedBackImpl();
    }

    @Bean
    public PaymentFactory paymentFactory(){

        return new PaymentFactory(businessLogicRouter());
    }

    @Bean
    public Router businessLogicRouter(){

        return new BusinessLogicRouter(partialTaxPaymentController(),
                contractorPaymentsController(),
                withholdingTaxPaymentController(),
                rentalPaymentsController(),
                typicalPaymentsController(),
                ttController());
    }

    @Bean
    public TTController ttController() {

        return new TTControllerImpl(invoiceDetails());
    }

    @Bean
    public InvoiceDetails invoiceDetails() {

        return new Invoice(feedBack());
    }

    @Bean
    public DefaultControllers typicalPaymentsController() {

        return new DefaultTypicalPaymentsController(paymentModel(),
                invoiceDetails(),
                resultsViewer(),
                reportController(),
                prepaymentController(),
                typicalPaymentsLogic(),
                modelViewerVisitor(),
                modelPrecisionVisitor(),
                reportingVisitor());
    }

    @Bean
    public DefaultLogic typicalPaymentsLogic() {

        return new TypicalPaymentsImpl(new PaymentParameters());
    }

    @Bean
    public ReportControllers reportController() {

        return new ReportsController();
    }

    @Bean
    public ResultsViewer resultsViewer() {

        return new ResultsOutput();
    }

    @Bean
    public DefaultControllers rentalPaymentsController() {

        return new RentalPaymentsController();
    }

    @Bean
    public DefaultControllers withholdingTaxPaymentController() {

        return new WithholdingTaxPaymentController(paymentModel(),
                invoiceDetails(),
                withholdingTaxPayments(),
                prepaymentController(),
                modelViewerVisitor(),
                modelPrecisionVisitor(),
                reportingVisitor());
    }

    @Bean
    public DefaultLogic withholdingTaxPayments() {

        return new DefaultTypicalWithholdingTaxPayment();
    }

    @Bean
    public DefaultControllers contractorPaymentsController() {
        return new ContractorPaymentsController( paymentModel(),
                invoiceDetails(),
                contractorLogic(),
                prepaymentController(),
                modelViewerVisitor(),
                modelPrecisionVisitor(),
                reportingVisitor());
    }

    @Bean
    public Visitor reportingVisitor() {

        return new ReportingVisitor(feedBack());
    }

    @Bean
    public Visitor modelPrecisionVisitor() {

        return new ModelPrecisionVisitor();
    }

    @Bean
    public Visitor modelViewerVisitor() {

        return new ModelViewerVisitor();
    }

    @Bean
    public PrepaymentController prepaymentController() {

        return new PrepaymentControllerImpl(prepayable(), feedBack());
    }

    @Bean
    public Prepayable prepayable() {

        return new SimplePrepayments(prepaymentDetails(), new PrepaymentConfigurations());
    }

    @Bean
    public PrepaymentDetails prepaymentDetails() {

        return new Invoice(new FeedBackImpl());
    }

    @Bean
    public DefaultLogic contractorLogic() {

        return new ContractorLogic(new PaymentParameters());
    }

    @Bean
    public DefaultPaymentModel paymentModel() {

        return new PaymentModel();
    }

    @Bean
    public PartialTaxPaymentController partialTaxPaymentController(){

        return new DefaultPartialTaxPaymentController();
    }
}
