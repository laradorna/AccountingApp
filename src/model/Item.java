package model;

import org.sormula.annotation.Column;

/**
 * Class Item define a sales product with its price and vat
 *
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class Item {

    /*
     * Define id as mandatory primary key to save to database
     */
    @Column(primaryKey = true, identity = true)
    private int id;
    private String description;
    private float amount;
    private float vat;

    public Item() {
    }

    public Item(String description, float amount, float vat) {
        this.description = description;
        this.amount = amount;
        this.vat = vat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getVat() {
        return vat;
    }

    public void setVat(float vat) {
        this.vat = vat;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", vat=" + vat +
                '}';
    }
}
