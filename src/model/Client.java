package model;

import org.sormula.annotation.Column;

/**
 * Class Client define a customer with their contact details
 *
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class Client {

    /*
     * Define id as mandatory primary key to save to database
     */
    @Column(primaryKey = true, identity = true)
    int id;
    String nif;
    String businessName;
    String direction;
    String phone;

    public Client() {
    }

    public Client(String nif, String businessName) {
        this.nif = nif;
        this.businessName = businessName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nif='" + nif + '\'' +
                ", businessName='" + businessName + '\'' +
                ", direction='" + direction + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }


}
