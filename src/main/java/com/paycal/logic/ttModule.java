package com.paycal.logic;

import com.paycal.api.FeedBack;
import com.paycal.api.InvoiceDetails;
import com.paycal.models.Invoice;
import com.paycal.view.Notifications;
import com.paycal.api.PayCalView;

import java.util.Scanner;

import static java.lang.System.out;

/**
 * <P>Class calculates the transactions to pass for invoices coming
 * from companies, or individuals who are domiciled beyond the local
 * tax jurisdiction.</P>
 * <p>This means that we have to calculate the taxes chargeable and give
 * them to our local tax authority. There being no way for the supplier
 * to submit VAT for self, we have to calculate the VAT, based on the Invoice
 * but in reverse</p>
 */
class ttModule {

    private PayCalView payCalView;

    ttModule(PayCalView payCalView) {
        this.payCalView = payCalView;
    }

    /**
     * Calculation of the Reverse Invoice Amount
     * > This is the amount from which we will calculate the VAT
     * multiplying against the VAT rate
     * > Calculation of the reverse Invoice amount hinges heavily
     * on whether or not the amount was invoiced inclusive or
     * exclusive of the withholding tax.
     * > If the amount is exclusive then the reverse Invoice amount is
     * calculated as follows:
     * Reverse Invoice = (invoiced/(1-(WithTaxRate/100)))
     * > If the amount invoiced is inclusive, it will construed to be the
     * the same as the Reverse Invoice.
     *
     * @param Invoiced//    "Invoiced" variable in the main method
     * @param WithTaxRate// "WithTax_Rate" variable in the main method
     * @return //returns the reverse Invoice amount
     */
    private static double getReverseInvoice(
            double Invoiced,
            double WithTaxRate,
            String decide) {

        boolean isInclusive = !decide.equals("yes");
        //"no" = That we assume the invoiced amount is exclusive
        //"yes"= That we assume the invoiced amount is inclusive

        /*
         * Now with the understanding of the nature of the invoiced amount
         * we proceed to calculate the ReverseInvoice
         */

        double ReverseInvoice;//Return variable
        if (isInclusive) {

            ReverseInvoice = Invoiced;
        } else {

            ReverseInvoice = Invoiced / (1 - (WithTaxRate / 100));
        }

        return ReverseInvoice;
    }

    /**
     * User Decision
     * This method does one thing and one thing alone.
     * It returns a value that shows what the user has decided;
     * In terms of whether or not the amount invoiced is inclusive of tax
     *
     * @return
     */

    private static String userDecides() {
        FeedBack notice = new Notifications();
        // This objects prints the main prompt
        Scanner keyboard = new Scanner(System.in);
        //initiating user input
        /*
         * We first determine is the amount invoiced in inclusive of the
         * withholding tax
         */
        out.println("Is the amount EXCLUSIVE of withholding");//Prompt
        out.println("tax?");//Prompt
        out.println("Hint: Yes or no");//Prompt

        notice.mainPrompt();

        String str = keyboard.next();
        // User keys in his 'yes' or 'no'
        // The input is captured into a String object
        // Because I want to convert the String values into lower case

        return str.toLowerCase();
    }

    /**
     * Calculating Reverse VAT
     * Here we are now going to use the reverse VAT calculated to
     * calculate the vat amount chargeable
     *
     * @param reverseInvoiceAmount
     * @param vatRate
     * @return //Reverse Vat chargeable
     */

    private static double getReverseVat(
            double reverseInvoiceAmount,
            double vatRate
    ) {
        double ReverseVat;//this is the return variable
        ReverseVat = (reverseInvoiceAmount * (vatRate / 100));/*
         * well, E.G. VAT=NET*16%
         */

        return ReverseVat;
    }

    /**
     * Calculating the withholding Tax amount
     * The idea here is to calculate the actual withholding tax to
     * be paid to the revenue authority based on the withholding tax
     * rate which is to be one of the PaymentParameters here and the calculated
     * reverse Invoice where:
     * Withholding Tax = Reverse Invoice * Withholding Tax Rate
     *
     * @param reverseInvoiceAmount // This is the input for the reverse Invoice amount
     * @param withHoldingTaxRate   //Input for input in whole numbers of the withholding tax rate
     * @return //The formula here gives the withholding tax amount chargeable
     */

    private static double getwithholdingTaxAmount(double reverseInvoiceAmount,
                                                  double withHoldingTaxRate) {
        return reverseInvoiceAmount * withHoldingTaxRate / 100;
    }

    /**
     * Calculating the total expenditure
     * A lot of variables are going to change once the user decides whether
     * or not that the amount invoiced is inclusive of withholding tax...
     * but...
     * One thing always holds true. The Total Expense will always be:
     * Total Expense=Reverse Invoice*(1+Vat Rate)
     *
     * @param ReverseInvoice
     * @param VatRate
     * @param Decision//If   no, the invoiced is not inclusive,
     *                       if yes it is exclusive.
     * @param Invoiced
     * @param VatAmount
     * @return // "Total Expense=Reverse Invoice*(1+Vat Rate)"
     */
    private static double getTotalExpense(double ReverseInvoice,
                                          double VatRate,
                                          String Decision,
                                          double Invoiced,
                                          double VatAmount
    ) {
        if (Decision.equals("yes")) {
            return ReverseInvoice + VatAmount;
        } else {
            return Invoiced * (1 + VatRate / 100);
        }
    }

    /**
     * Calculating amounts owed to supplier
     * The amount is obviously the stated amount that has been invoiced
     * unless...
     * Well, unless the amount invoiced is inclusive of withholding Tax
     * Getting the right figure here is all the rage...
     *
     * @param total//      This is the figure for total Expense
     * @param WithTax//The withholding tax chargeable
     * @param Vat//The     reverse VAT amount chargeable
     * @return // The amounts due to supplier, will always be the balance after
     * taxes are withheld on the total expense incurred by the company
     */

    private static double getPaySupplier(double total,
                                         double WithTax,
                                         double Vat
    ) {
        return total - WithTax - Vat;
    }

    /**
     * PaycalApp Looper
     * This function will be employed in user looping decision
     *
     * @return "yes" or "no" answer to the request for repetition
     */

    private static String repeat() {
        //check if user wants to recalculate
        //Check if user wants to recalculate
        Scanner keyboard = new Scanner(System.in);//initiating user input
        out.println("Do you want to recalculate?");

        String str = keyboard.next();//user types yes or no

        out.println("------");

        return str.toLowerCase();
    }

    private static double getVat() {

        Scanner keyboard = new Scanner(System.in);

        out.println("What is the Vat rate applied?\n" +
                "Hint: Use whole numbers");
        out.println();

        FeedBack notice = new Notifications();

        notice.mainPrompt();

        double Vat = keyboard.nextDouble();

        out.println();

        return Vat;
    }

    /**
     * main method for the ttmodule
     * This module needs to be methodical in order not to distort values
     * of the variables which will be changed based on user's decisions
     */

    public void telegraphic() {
        /*Looping logic
         * The user will at end of everything decide whether to repeat
         * the computation or forteit the opportunity.
         * We therefore create a boolean variable (doAgain)
         * and a string variable (d) to be able to indirectly use the user
         * input the above boolean. Apparently the user would find it easier
         * to type "yes", or "no", than "true or false"
         */
        boolean doAgain;
        // We will use this to loop this part
        // We will use this to loop this part
        // We will use this to loop this part
        // We will use this to loop this part


        /*
        * First we create the "invoiced" object safely outside the loop
        * Otherwise this could lead to leakage in memory created everytime
        * the object is created
         */

        InvoiceDetails invoiced = new Invoice();
        // Done.
        // Good Edwin

        do {//This is the looper

        /*It's also the beginning of the main program*/

        /*start by getting VAT rate*/
            double Vat_Rate = getVat();

        /*Step2 Get withholding tax rate*/
            double WithTax_Rate = invoiced.withHoldingTaxRate();

        /*Step3 Now we get the Invoice amount*/
            double Invoiced = invoiced.invoiceAmount();

        /*Step4 We are calculating ReverseInvoice in*/

        /*Step5 user decides whether or not the invoiced amount includes tax*/
            String Decision = userDecides();


        /*Step6 calculate the reverse Invoice amount*/
            double ReverseInvoice = getReverseInvoice(Invoiced,
                    WithTax_Rate,
                    Decision
            );

        /*Step6 we calculate the amount of reverse VAT*/
            double ReverseVat = getReverseVat(ReverseInvoice, Vat_Rate);/*


        /*Step 7 Calling function 9 calculate withholding tax amount*/
            double WithTax = getwithholdingTaxAmount(ReverseInvoice, WithTax_Rate);/*

        /*Step 8 Calculate TotalExpense*/
            double TotalExpense;
            TotalExpense = getTotalExpense(ReverseInvoice,
                    Vat_Rate,
                    Decision,
                    Invoiced,
                    ReverseVat
            );

        /*Step 9 Calculate Amount owed supplier*/
            double forSupplier = getPaySupplier(TotalExpense,
                    WithTax,
                    ReverseVat
            );


            /*
            * The following are values for the Display class to print
             */

            double withHoldingVat = ReverseVat;
            // Amount to be withheld by us

            double withHoldingTax = WithTax;
            // Amount withheld on consultancy

            double total = TotalExpense;
            // Amount for the expense/ wip account

            double toPayee = forSupplier;
            // Amounts due for immediate transfer

            double toPrepay = 0.00;
            // This variable is there for compliance with the
            // Display class methods

            payCalView.displayResults(total, withHoldingVat, withHoldingTax, toPrepay, toPayee);
            // Results submitted for view



            /*chooses the variable d as to whether the user
         would want to repeat computation or otherwise*/
            String d = repeat();//Calling Function#3
            doAgain = !d.equals("no");
        } while (doAgain);
    }

}
