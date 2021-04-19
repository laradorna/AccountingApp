package com.laraVD;

import dao.ClientDAO;
import dao.InvoiceDAO;
import dao.InvoiceLineDAO;
import dao.ItemDAO;
import model.Client;
import model.Invoice;
import model.InvoiceLine;
import model.Item;
import org.sormula.Database;
import org.sormula.SormulaException;
import org.sormula.Table;
import view.InitialPanel;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class Main {

    public static void main(String[] args) throws SQLException, SormulaException {

        //create y connect DB
        String url = "jdbc:sqlite:facturas.db";
        Connection connection = DriverManager.getConnection(url);
        setup(connection);

        Database database = new Database(connection);


        System.out.println("Lists of data");
        ClientDAO clientDAO = new ClientDAO(database);
        List<Client> clients = clientDAO.selectAll();
        System.out.println(clients);

        Table<Item> itemTable = database.getTable(Item.class);
        List<Item> items = itemTable.selectAll();
        System.out.println(items);

        System.out.println("SIMULATE INVOICE");

        Client client = clients.get(0);
        Invoice invoice = new Invoice(client.getId());
        InvoiceDAO invoiceDAO = new InvoiceDAO(database);
        invoiceDAO.insert(invoice);

        Item item = items.get(0);
        InvoiceLine invoiceLine = new InvoiceLine(5, item.getId(), invoice.getId());
        invoiceLine.calculateTotalPricesLine(item);
        InvoiceLineDAO invoiceLineDAO = new InvoiceLineDAO(database);
        invoiceLineDAO.update(invoiceLine);

        Item item2 = items.get(1);
        InvoiceLine invoiceLine2 = new InvoiceLine(1, item2.getId(), invoice.getId());
        invoiceLine2.calculateTotalPricesLine(item2);
        invoiceLineDAO.update(invoiceLine);

        invoiceDAO.addInvoiceLine(invoice, invoiceLine);
        invoiceDAO.addInvoiceLine(invoice, invoiceLine2);

        invoice.calculateTotalsPrice();
        invoiceDAO.update(invoice);

        System.out.println(invoice);

        System.out.println("LISTS OF INVOICES");

        List<Invoice> invoices = invoiceDAO.selectAll();
        System.out.println(invoices);


        //Form main

        JFrame frame = new JFrame("Facturas");
        frame.setContentPane(new InitialPanel(database).panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);


    }

    /**
     * Method to create database tables
     *
     * @param connection
     */
    public static void setup(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            System.out.println("create table");
            statement.execute("CREATE TABLE IF NOT EXISTS CLIENT(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NIF VARCHAR(32), " +
                    "BUSINESSNAME VARCHAR(64), " +
                    "DIRECTION VARCHAR(64), " +
                    "PHONE VARCHAR(32)" +
                    ")"
            );
            statement.execute("CREATE TABLE IF NOT EXISTS ITEM(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DESCRIPTION VARCHAR(40), " +
                    "AMOUNT REAL, " +
                    "VAT REAL, " +
                    "TOTAL REAL" +
                    ")"
            );
            statement.execute("CREATE TABLE IF NOT EXISTS INVOICE(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DATE TIMESTAMP, " +
                    "CLIENTID INTEGER, " +
                    "BASE REAL, " +
                    "FEETAX REAL, " +
                    "TOTAL REAL, " +
                    "FOREIGN KEY(CLIENTID) REFERENCES CLIENT(ID)" +
                    ")"
            );
            statement.execute("CREATE TABLE IF NOT EXISTS INVOICELINE(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "INVOICEID INTEGER, " +
                    "ITEMID INTEGER, " +
                    "QUANTITY INTEGER, " +
                    "VAT REAL, " +
                    "BASE REAL, " +
                    "FEETAX REAL, " +
                    "TOTAL REAL, " +
                    "FOREIGN KEY(INVOICEID) REFERENCES INVOICE(ID)," +
                    "FOREIGN KEY(ITEMID) REFERENCES ITEM(ID) " +
                    ")"
            );

            System.out.println("Populating Data");

            Database database = new Database(connection);
            ClientDAO clientDAO = new ClientDAO(database);
            ItemDAO itemDAO = new ItemDAO(database);
            InvoiceDAO invoiceDAO = new InvoiceDAO(database);
            InvoiceLineDAO invoiceLineDAO = new InvoiceLineDAO(database);

            clientDAO.deleteAll();
            itemDAO.deleteAll();
            invoiceLineDAO.deleteAll();
            invoiceDAO.deleteAll();

            clientDAO.insert(new Client("1111A", "Alcampo"));
            clientDAO.insert(new Client("2222A", "Mediamarkt"));
            clientDAO.insert(new Client("3333A", "Carrefour"));

            itemDAO.insert(new Item("Portatil Lenovo", 750.99f, 0.21f));
            itemDAO.insert(new Item("Pantalla HP", 175.0f, 0.1f));
            itemDAO.insert(new Item("Cable USB", 1.99f, 0.04f));


        } catch (SQLException | SormulaException e) {
            // assume exception because table already exists
            System.out.println(e);
        }
    }
}

