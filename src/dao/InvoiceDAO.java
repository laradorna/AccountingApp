package dao;

import model.Client;
import model.Invoice;
import model.InvoiceLine;
import org.sormula.Database;
import org.sormula.SormulaException;
import org.sormula.Table;

import java.util.List;

/**
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class InvoiceDAO extends Table<Invoice> {

    /**
     * Builder
     *
     * @param database
     * @throws SormulaException
     */
    public InvoiceDAO(Database database) throws SormulaException {
        super(database, Invoice.class);
    }

    /**
     * Method add invoice line in invoice
     *
     * @param invoice
     * @param invoiceLine
     * @throws SormulaException
     */
    public void addInvoiceLine(Invoice invoice, InvoiceLine invoiceLine) throws SormulaException {
        invoiceLine.setInvoiceId(invoice.getId());
        InvoiceLineDAO invoiceLineDAO = new InvoiceLineDAO(this.getDatabase());
        invoiceLineDAO.insert(invoiceLine);
        List<InvoiceLine> invoiceLines = invoice.getInvoiceLines();
        invoiceLines.add(invoiceLine);
        invoice.setInvoiceLines(invoiceLines);
    }

    /**
     * Method selected the last invoice line of the database
     *
     * @return Invoice
     * @throws SormulaException
     */
    private Invoice selectLastInvoice() throws SormulaException {
        List<Invoice> invoices = this.selectAll();
        Invoice invoice = invoices.get(invoices.size() - 1);
        return invoice;
    }

    /**
     * Method deleted line invoice if it's the last add of the database
     *
     * @param invoice
     * @return int
     * @throws SormulaException
     */
    @Override
    public int delete(Invoice invoice) throws SormulaException {
        Invoice lastInvoice = this.selectLastInvoice();
        if (invoice.getId() != lastInvoice.getId())
            return -1;

        return super.delete(invoice);
    }

    /**
     * Method get client from invoice
     *
     * @param invoice
     * @return Client
     * @throws SormulaException
     */
    public Client getClient(Invoice invoice) throws SormulaException {
        ClientDAO clientDAO = new ClientDAO(this.getDatabase());
        List<Client> clients = clientDAO.selectAll();
        for (Client client : clients) {
            if (client.getId() == invoice.getClientId()) {
                return client;
            }
        }
        return null;
    }
}

