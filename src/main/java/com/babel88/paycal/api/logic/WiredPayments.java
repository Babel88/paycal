package com.babel88.paycal.api.logic;

import java.math.BigDecimal;

@Deprecated
public interface WiredPayments {

    BigDecimal getReverseInvoice(BigDecimal invoiced, BigDecimal withTaxRate, Boolean exclusive);

    BigDecimal getReverseVat(BigDecimal reverseInvoiceAmount, BigDecimal vatRate);

    BigDecimal getwithholdingTaxAmount(BigDecimal reverseInvoiceAmount, BigDecimal withHoldingTaxRate);

    BigDecimal getTotalExpense(BigDecimal reverseInvoice, BigDecimal vatRate, Boolean exclusive, BigDecimal invoiced, BigDecimal vatAmount);

    BigDecimal getPaySupplier(BigDecimal total, BigDecimal WithTax, BigDecimal Vat);
}
