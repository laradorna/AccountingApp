package model;

import org.sormula.annotation.Column;
import org.sormula.annotation.cascade.OneToManyCascade;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class InvoiceLine defines the purchase data of  the invoice
 *
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class Invoice {

    /*
     * Define id as mandatory primary key to save to database
     */
    @Column(primaryKey = true, identity = true)
    private int id;
    private LocalDate date;
    private int clientId;
    private float base;
    private float feeTax;
    private float total;

    /*
     * Defines a list where the invoice lines are saved and they are only saved if they belong to an invoice
     */
    @OneToManyCascade(foreignKeyValueFields = "invoiceId")
    private List<InvoiceLine> invoiceLines;

    public Invoice() {
        this.date = LocalDate.now();
    }

    public Invoice(int clientId) {
        this.date = LocalDate.now();
        this.clientId = clientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.date = date;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public float getFeeTax() {
        return feeTax;
    }

    public void setFeeTax(float feeTax) {
        this.feeTax = feeTax;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<InvoiceLine> getInvoiceLines() {
        if (invoiceLines == null) {
            invoiceLines = new ArrayList<>();
        }
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", date=" + date +
                ", clientId=" + clientId +
                ", invoiceLines=" + invoiceLines +
                ", base=" + base +
                ", feeTax=" + feeTax +
                ", total=" + total +
                '}';
    }

    /**
     * Method that collects the base price of each invoice line and calculates the total base price
     *
     * @return (int) totalBase the price base total for the invoice
     */
    public float calculateBaseTotal() {
        float totalBase = 0;
        for (InvoiceLine invoiceLine : this.getInvoiceLines()) {
            totalBase += invoiceLine.getBase();
        }
        return totalBase;
    }

    /**
     * Method that collects the feeTax of each invoice line and calculates the total feeTax price
     *
     * @return (int) totalTax the price fee total for the invoice
     */
    private float calculateFeeTaxTotal() {
        float totalTax = 0;
        for (InvoiceLine invoiceLine : this.getInvoiceLines()) {
            totalTax += invoiceLine.getFeeTax();
        }
        return totalTax;
    }

    /**
     * Method which adds the base price and the tax fee to give the final price of the invoice
     *
     * @return (int) total the price total for the invoice
     */
    private float calcuateFinalTotal() {
        return this.getBase() + this.getFeeTax();
    }

    /**
     * Method that invokes the calculation methods and loads them in the corresponding attributes
     */
    public void calculateTotalsPrice() {
        this.setBase(calculateBaseTotal());
        this.setFeeTax(calculateFeeTaxTotal());
        this.setTotal(calcuateFinalTotal());
    }
}
