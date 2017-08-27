package com.babel88.paycal.view.reporting;

import com.babel88.paycal.view.ResultsOutput;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.math.BigDecimal;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * Created by edwin.njeru on 8/10/17.
 */
public class PaymentAdvice  {

    private Logger log = LoggerFactory.getLogger(PaymentAdvice.class);

    private Boolean printAdvice;
    private static PaymentAdvice instance = new PaymentAdvice();

    public PaymentAdvice() {

        log.debug("Creating payment advice object");

    }

    public static PaymentAdvice getInstance() {
        return instance;
    }

    public PaymentAdvice setPrintAdvice(Boolean printAdvice) {

        log.debug("Setting print advice as : "+printAdvice.toString());

        this.printAdvice = printAdvice;

        log.debug("Print advice set as : "+printAdvice.toString());
        return this;
    }

    private void build(BigDecimal paid, BigDecimal vatWithheld, BigDecimal withheld) {

        // Styles
        log.debug("Configuring the table styles...");
        StyleBuilder boldStyle = stl.style().bold();
        StyleBuilder boldCenteredSTyle =
                stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        StyleBuilder columnStyle =
                stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                        .setBorder(stl.pen1Point())
                        .setBackgroundColor(Color.lightGray);
        StyleBuilder subtitle1Style =
                stl.style()
                        .bold()
                        .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
                        .setBottomBorder(stl.pen2Point())
                        .setPadding(20)
                        .setFontSize(20);
        StyleBuilder title1Style =
                stl.style()
                        .bold()
                        .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                        .setTopBorder(stl.pen2Point())
                        .setTopBorder(stl.pen2Point())
                        .setTopPadding(40)
                        .setFontSize(20);
        StyleBuilder addressStyle =
                stl.style()
                        .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
                        .setPadding(15);
        StyleBuilder disclaimerStyle =
                stl.style()
                        .setTopPadding(200)
                        .setFontSize(6)
                        .setPadding(15);
        StyleBuilder footerStyle =
                stl.style()
                        .setFontSize(10)
                        .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                        .setPadding(15);
        StyleBuilder columnHeaderStyle =
                stl.style()
                        .setFontSize(12)
                        .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                        .bold();

        // columns
        log.debug("Configuring the data columns");

        TextColumnBuilder<BigDecimal> payAmount =
                col.column("Amount", "amountPayable", type.bigDecimalType())
                        .setTitleStyle(columnHeaderStyle);
        TextColumnBuilder<BigDecimal> withholdingVat =
                col.column("Withholding Vat", "withholdingVat", type.bigDecimalType())
                        .setTitleStyle(columnHeaderStyle);
        TextColumnBuilder<BigDecimal> withholdingTax =
                col.column("Withholding Tax", "withholdingTax", type.bigDecimalType())
                        .setTitleStyle(columnHeaderStyle);
        TextColumnBuilder<String> invoiceNumber =
                col.column("Invoice #", "invoiceNumber", type.stringType())
                        .setTitleStyle(columnHeaderStyle);
        TextColumnBuilder<String> paymentInstrument =
                col.column("Instrument", "instrument", type.stringType())
                        .setTitleStyle(columnHeaderStyle);
        TextColumnBuilder<String> paymentInstrumentNumber =
                col.column("Instrument #", "instrumentNumber", type.stringType())
                        .setStyle(boldStyle)
                        .setTitleStyle(columnHeaderStyle);
        TextColumnBuilder<Integer> rowNumberColumn =
                col.reportRowNumberColumn("No")
                        .setFixedColumns(2)
                        .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                        .setTitleStyle(columnHeaderStyle);

        try {
            log.debug("Creating report object from instance report() method...");
            report()
                    .setColumnStyle(columnStyle)
                    .setSubtotalStyle(boldStyle)
                    .highlightDetailEvenRows()
                    .columns(// adding columns
                            rowNumberColumn, payAmount, withholdingVat, withholdingTax,
                            invoiceNumber, paymentInstrument, paymentInstrumentNumber
                    )
                    .groupBy(paymentInstrumentNumber)
                    .subtotalsAtSummary(
                            sbt.sum(payAmount), sbt.sum(withholdingVat), sbt.sum(withholdingTax)
                    )
                    .title(
                            cmp.text("ABC BANK").setStyle(title1Style)
                    )
                    .title(
                            cmp.text("Payment Advice").setStyle(subtitle1Style)
                    )
                    .title( // return address
                            cmp.text("Return To: \n " +
                                    "Finance Department \n" +
                                    "ABC BANK \n" +
                                    "East Wing, 4th Floor, ABC House \n" +
                                    "Woodvale Grove, Westlands, \n" +
                                    "P.O.BOX 46452 - 00100 \n" +
                                    "Nairobi, Kenya \n" +
                                    "\n" +
                                    "finance@abcthebank.com \n"
                            ).setStyle(addressStyle)
                    )
                    .pageFooter(
                            cmp.text(
                                    "\u00a9 finance department 2017"
                            ).setStyle(footerStyle)
                    )
                    .summary(
                            cmp.text("Thanks for your Services. This advice form is private and confidential. It is " +
                                    "also covered by work product immunity" +
                                    "If you have received it by mistake please mail it back to the return address" +
                                    "and do not make a copy." +
                                    "Please note the form is for informational purposes only and cannot be presented" +
                                    "to a 3rd party as proof of settlement, neither does it bind the bank to any " +
                                    "actual or implied liability"
                            ).setStyle(disclaimerStyle)
                    )
                    .setDataSource(createDataSource(paid, vatWithheld, withheld))
                    .show();
        } catch (DRException e) {

            log.error("A DRException was thrown in the build method of the payment advice :\n" +
                    "");
            e.printStackTrace();
        }
    }

    public void forPayment(ResultsOutput resultsOutput) {

        forPayment(resultsOutput.getToPayee().toString(),
                resultsOutput.getVatWithheld().toString(),
                resultsOutput.getWithholdingTax().toString()
        );
    }

    public void forPayment(String paid, String vatWithhold, String withHold) {

        log.debug("defining payment figures : \n" +
                "Amount to pay : "+paid+"\n" +
                "Vat to withhold: "+vatWithhold+"\n" +
                "Withholding tax: "+withHold+"\n");

        if (printAdvice) {

            log.debug("Conditionally calling the build method to create report : \n" +
                    "The report is supposed to print? :"+printAdvice.toString());
            build(parseBD(paid), parseBD(vatWithhold), parseBD(withHold));
        }
    }

    private BigDecimal parseBD(String stringValue) {

        log.debug("Parsing string value : "+stringValue+" into BigDecimal object");

        return new BigDecimal(Double.parseDouble(stringValue));
    }

    private JRDataSource createDataSource(BigDecimal paid, BigDecimal vatWithheld, BigDecimal withheld) {

        log.debug("Creating the datasource from calculations...");

        DRDataSource dataSource =
                new DRDataSource("amountPayable", "withholdingVat", "withholdingTax", "invoiceNumber",
                        "instrument", "instrumentNumber");

        /*// instrument 0904KF4
        dataSource.add(bd(200000),bd(12000),bd(28000),"xyz-009430","BCHQ","0904KF4");
        dataSource.add(bd(150857),bd(9098),bd(13784),"xyz-009431","BCHQ","0904KF4");
        dataSource.add(bd(23983),bd(3400),bd(6740),"xyz-009432","BCHQ","0904KF4");

        // instrument 0904
        dataSource.add(bd(165900),bd(11789),bd(18579),"bcd-8293","BCHQ","0904");
        dataSource.add(bd(76857),bd(12390),bd(24600),"bcd-8294","BCHQ","0904");
        dataSource.add(bd(45908),bd(9800),bd(0),"bcd-8295","BCHQ","0904");

        dataSource.add(bd(135098),bd(9898),bd(13450),"237","BCHQ","0609");
        dataSource.add(bd(450000),bd(45890),bd(16780),"4394","BCHQ","0503");
        dataSource.add(bd(3200),bd(200),bd(0),"93404","BCHQ","0888");*/

        log.debug("Adding objects to dynamic reports datasource object : \n" +
                "amount paid : "+paid.toString()+"\n" +
                "vat withheld : "+vatWithheld.toString()+"\n" +
                "withholding tax : "+withheld.toString()+"\n");
        dataSource.add(paid, vatWithheld, withheld, "bcd-8293", "BCHQ", "0904");

        log.debug("Data objects added to dynamic reports datasource object, now \n" +
                "creating filler objects to space out the table with 12 lines");
        //TODO :  MAKE BETTER SPACING FOR THE COLUMNS
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");
        dataSource.add(bd(0), bd(0), bd(0), "", "", "");

        return dataSource;
    }

    @NotNull
    private BigDecimal bd(double v) {

        log.debug("Converting {} from double to BigDecimal object...", v);

        return new BigDecimal(v);
    }


}
