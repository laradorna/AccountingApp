package view;

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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


/**
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class InvoiceDetail {

    public JPanel panel2;
    private JTextField dateTF;
    private JTextField idInvoiceTF;
    private JComboBox nifClientCB;
    private JTable invoicelinestable;
    private JButton saveButton;
    private JComboBox descriptionCB;
    private JSpinner quantitySp;
    private JButton addButton;
    private JTextField businessNameTF;
    private JPanel panel3;
    private JPanel itemsPanel;
    private JTextField taxTF;
    private JTextField totalTF;
    private JTextField baseTF;
    private DefaultTableModel invoiceLineTableModel;

    private Database database;

    private InvoiceDAO invoiceDAO;
    private InvoiceLineDAO invoiceLineDAO;
    private ClientDAO clientDAO;
    private ItemDAO itemDAO;

    private Invoice invoice;

    /**
     * Builder
     *
     * @param database
     * @param invoice
     * @throws SormulaException
     */
    public InvoiceDetail(Database database, Invoice invoice) throws SormulaException {
        this.database = database;
        this.invoiceDAO = new InvoiceDAO(database);
        this.invoiceLineDAO = new InvoiceLineDAO(database);
        this.clientDAO = new ClientDAO(database);
        this.itemDAO = new ItemDAO(database);
        this.invoice = invoice;

        initComponents();
        fillTable();
        fillItemDescriptionCB();
        fillNifClientCB();

        descriptionCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        quantitySp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });
        nifClientCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = (Client) nifClientCB.getSelectedItem();
                businessNameTF.setText(client.getBusinessName());
            }
        });
        quantitySp.addKeyListener(new KeyAdapter() {
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InvoiceLine invoiceLine = new InvoiceLine();
                Item item = (Item) descriptionCB.getSelectedItem();

                invoiceLine.setItemId(item.getId());
                invoiceLine.setQuantity((Integer) quantitySp.getValue());
                invoiceLine.setVat(item.getVat());
                invoiceLine.calculateTotalPricesLine(item);

                try {
                    invoiceDAO.addInvoiceLine(invoice, invoiceLine);
                    fillTable();
                } catch (SormulaException sormulaException) {
                    sormulaException.printStackTrace();
                }

            }
        });

        invoicelinestable.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = (Client) nifClientCB.getSelectedItem();
                    invoice.setClientId(client.getId());
                    if (invoice.getId() == 0) {
                        invoiceDAO.insert(invoice);
                        idInvoiceTF.setText(String.valueOf(invoice.getId()));
                        dateTF.setText(String.valueOf(invoice.getDate()));
                    } else {
                        invoiceDAO.update(invoice);
                    }


                } catch (SormulaException sormulaException) {
                    sormulaException.printStackTrace();
                }

            }
        });
    }

    /**
     * Starts the components so that they are displayed with initial data
     */
    private void initComponents() throws SormulaException {

        //table
        String[] columns = new String[]{
                "Descripción", "Cantidad", "IVA", "importe"
        };

        //actual data for the table in a 2d array
        Object[][] data = new Object[][]{};

        final Class[] columnClass = new Class[]{
                String.class, Integer.class, Float.class, Float.class
        };
        //create table model with data
        invoiceLineTableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClass[columnIndex];
            }
        };

        invoicelinestable.setModel(invoiceLineTableModel);

        nifClientCB.setSelectedIndex(-1);
        //invoice datas
        // idInvoiceTF.setText(String.valueOf(this.invoice.getId()));
        dateTF.setText(String.valueOf(this.invoice.getDate()));


        //spinner
        SpinnerModel model = new SpinnerNumberModel(1, 0, 20, 1);
        quantitySp.setModel(model);
    }


    /**
     * Populate the table with the database data
     *
     * @throws SormulaException
     */
    public void fillTable() throws SormulaException {
        invoiceLineTableModel.setRowCount(0);

        List<InvoiceLine> invoiceLineList = invoice.getInvoiceLines();

        for (InvoiceLine invoiceLine : invoiceLineList) {
            Item item = invoiceLineDAO.getItem(invoiceLine);
            String description = "";
            if (item != null) {
                description = item.getDescription();
            }

            Object[] row = new Object[]{
                    description,
                    invoiceLine.getQuantity(),
                    invoiceLine.getVat(),
                    invoiceLine.getTotal()
            };
            invoiceLineTableModel.addRow(row);
        }

        invoice.calculateTotalsPrice();
        baseTF.setText(String.valueOf(invoice.getBase()));
        taxTF.setText(String.valueOf(invoice.getFeeTax()));
        totalTF.setText(String.valueOf(invoice.getTotal()));
    }

    /**
     * Populate the combobox Items with the database data
     *
     * @throws SormulaException
     */
    public void fillItemDescriptionCB() throws SormulaException {
        List<Item> items = itemDAO.selectAll();
        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(items.toArray());
        descriptionCB.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Item) {
                    Item item = (Item) value;
                    setText(item.getDescription());
                }
                return this;
            }
        });
        descriptionCB.setModel(defaultComboBoxModel);
    }

    /**
     * Populate the Clients with the database data
     *
     * @throws SormulaException
     */
    public void fillNifClientCB() throws SormulaException {
        List<Client> clients = clientDAO.selectAll();


        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(clients.toArray());
        nifClientCB.addItem("seleccione cliente");
        nifClientCB.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Client) {
                    Client client = (Client) value;
                    setText(client.getNif());
                }
                return this;
            }
        });
        nifClientCB.setModel(defaultComboBoxModel);
        nifClientCB.setSelectedIndex(-1);
    }
}
