package model;

import org.sormula.annotation.Column;

/**
 * Class InvoiceLine defines the purchase data of a product to add to the invoice
 *
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class InvoiceLine {

    /**
     * Define id as mandatory primary key to save to database
     */
    @Column(primaryKey = true, identity = true)
    private int id;
    private int invoiceId;
    private int itemId;
    private int quantity;
    private float vat;
    private float base;
    private float feeTax;
    private float total;


    public InvoiceLine() {
    }

    public InvoiceLine(int quantity, int itemId, int invoiceId) {
        this.quantity = quantity;
        this.itemId = itemId;
        this.invoiceId = invoiceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getVat() {
        return vat;
    }

    public void setVat(float vat) {
        this.vat = vat;
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

    @Override
    public String toString() {
        return "InvoiceLine{" +
                "id=" + id +
                ", idItem=" + itemId +
                ", quantity=" + quantity +
                ", vat=" + vat +
                ", base=" + base +
                ", feeTax=" + feeTax +
                ", total=" + total +
                '}';
    }

    /**
     * Method that calculates prices by product quantity and loads them in attributes
     */
    public void calculateTotalPricesLine(Item item) {
        base = quantity * item.getAmount();
        vat = item.getVat();
        feeTax = vat * base;
        total = base + feeTax;
    }

}

