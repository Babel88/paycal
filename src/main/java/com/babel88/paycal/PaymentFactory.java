package com.babel88.paycal;

import com.babel88.paycal.api.InvoiceDetails;
import com.babel88.paycal.api.Router;
import com.babel88.paycal.api.view.PaymentModelViewInterface;
import com.babel88.paycal.config.factory.LogicFactory;
import com.babel88.paycal.config.factory.ModelViewFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Scanner;

import static java.lang.System.out;

/**
 * Created by edwin.njeru on 18/07/2016.
 * Class contains relevant switch statements for various areas in the program
 */
public class PaymentFactory {
    private final Logger log = LoggerFactory.getLogger(PaymentFactory.class);

    private PaymentModelViewInterface display;

    private InvoiceDetails invoiceDetails;

    private Router businessLogicRouter;

    public PaymentFactory() {

        log.debug("Creating an instance of the PaymentFactory : {}",this);
    }

    @NotNull
    private String usrChoice(){

        Scanner keyboard = new Scanner(System.in);
        String choice = keyboard.next();

        return choice.toLowerCase();
    }

    void mainSwitch() {

        mainOptions options;
        // initiating the enum class
        options = mainOptions.valueOf(usrChoice());

        switch (options){
            case a:
                out.println();
                out.println("Normal transaction:");
                out.println("-------------------");
                businessLogicRouter.normal();
                break;
            case b:
                out.println();
                out.println("Normal trnx with withholding taxes:");
                out.println("----------------------------------");
                businessLogicRouter.taxToWithhold();
                break;
            case c:
                out.println();
                out.println("Rental Expenses Payment:");
                out.println("-----------------------------");
                businessLogicRouter.rentalPayments();
                break;
            case d:
                out.println();
                out.println("PaycalApp paramenter adjustment protocol:");
                out.println("-------------------------------------");
                out.println(" Work in progress on this tool & other complex transactions");
                break;
            case e:
                out.println();
                out.println("Foreign transactions (Telegraphic transfers)");
                out.println("--------------------------------------------");
                businessLogicRouter.telegraphicTransfer();
                break;
            case f:
                out.println();
                out.println("Partially non-taxable payment");
                out.println("--------------------------------------------");
                businessLogicRouter.vatGiven();
                break;
            case g:
                out.println();
                out.println("Contractor payments");
                out.println("--------------------------------------------");
                businessLogicRouter.contractor();
                break;
            default:
                out.println();
                out.println("Kindly input a choice from the above menu");
                break;
        }


    }

    /**
     * mainOptions is a helper class providing enumeration
     * for switching between the main options when the applications
     * starts running
     */
    enum mainOptions{
        a,// Normal payment
        b,// Normal payment with withholding taxes
        c,// Payment with prepayments component
        d,// Adjust main paramenters
        e,// Foreign transactions(Telegraphic Transfers)
        f,// Partially non taxable payment
        g // 3% withholding tax
    }

    public PaymentFactory setBusinessLogicRouter(Router businessLogicRouter) {
        this.businessLogicRouter = businessLogicRouter;
        return this;
    }

    public PaymentFactory setDisplay(PaymentModelViewInterface display) {
        this.display = display;
        return this;
    }

    public PaymentFactory setInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
        return this;
    }
}
