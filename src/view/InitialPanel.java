package view;

import dao.ClientDAO;
import dao.InvoiceDAO;
import model.Client;
import model.Invoice;
import org.sormula.Database;
import org.sormula.SormulaException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class InitialPanel {

    public JPanel panel;
    private JButton bNueva;
    private JButton bBuscar;
    public JTable tableInv;
    private JTextField JTnfactura;
    private JButton bBorrar;
    private JScrollPane jscrollPane;
    private DefaultTableModel invoiceTableModel;

    private Database database;

    private InvoiceDAO invoiceDAO;
    private ClientDAO clientDAO;

    /**
     * Builder
     * @param database
     * @throws SormulaException
     */
    public InitialPanel(Database database) throws SormulaException {
        this.database = database;
        this.invoiceDAO = new InvoiceDAO(database);
        this.clientDAO = new ClientDAO(database);

        initComponents();
        fillTable();
        JTnfactura.requestFocus();

        tableInv.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (tableInv.getSelectedRow() != -1) {
                    bBorrar.setEnabled(true);
                }
            }
        });

        bBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fillTable();
                } catch (SormulaException sormulaException) {
                    sormulaException.printStackTrace();
                }
            }
        });

        bNueva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame  frame = new JFrame("Nueva Factura");
                try {
                    Invoice invoice= new Invoice();
//                    invoiceDAO.insert(invoice);
                    frame.setContentPane(new InvoiceDetail(database,invoice).panel2);

                } catch (SormulaException sormulaException) {
                    sormulaException.printStackTrace();
                }
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                frame.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        try {
                            fillTable();
                        } catch (SormulaException sormulaException) {
                            sormulaException.printStackTrace();
                        }
                    }
                });

            }
        });

        bBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int invoiceId = (int) tableInv.getModel().getValueAt(tableInv.getSelectedRow(), 0);
                try {
                    List<Invoice> invoices = invoiceDAO.selectAll();

                    for (Invoice invoice : invoices) {
                        if (invoice.getId() == invoiceId) {
                            int result = invoiceDAO.delete(invoice);
                            if (result == -1) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "La factura no se puede borrar"
                                );
                            }
                            break;
                        }
                    }
                    fillTable();

                } catch (SormulaException sormulaException) {
                    sormulaException.printStackTrace();
                }
            }
        });

        JTnfactura.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
        });
    }

    /**
     * Starts the components so that they are displayed with initial data
     */
    private void initComponents() {

        bBorrar.setEnabled(false);

        //TABLE
        String[] columns = new String[]{
                "NºFactura", "Razon Social", "Fecha"
        };


        Object[][] data = new Object[][]{};

        final Class[] columnClass = new Class[]{
                Integer.class, String.class, LocalDate.class
        };
        //create table model with data
        invoiceTableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClass[columnIndex];
            }
        };

        tableInv.setModel(invoiceTableModel);

    }

    /**
     * Populate the table with the database data
     * @throws SormulaException
     */
    public void fillTable() throws SormulaException {
        invoiceTableModel.setRowCount(0);
        JTnfactura.requestFocus();
        List<Invoice> invoiceList = invoiceDAO.selectAll();
        List<Invoice> invoiceFilteredList = null;

        if (JTnfactura.getText().isEmpty()) {
            invoiceFilteredList = invoiceList;
        } else {
            invoiceFilteredList = invoiceList.stream().filter(
                    invoice -> String.valueOf(invoice.getId()).contains(JTnfactura.getText())
            ).collect(Collectors.toList());
        }

        for (Invoice invoice : invoiceFilteredList) {
            Client client = invoiceDAO.getClient(invoice);
            String businessName = "";
            if (client != null) {
                businessName = client.getBusinessName();
            }

            Object[] row = new Object[]{
                    invoice.getId(),
                    businessName,
                    invoice.getDate()
            };
            invoiceTableModel.addRow(row);
        }
    }
}
