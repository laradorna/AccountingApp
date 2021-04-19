package dao;

import model.InvoiceLine;
import model.Item;
import org.sormula.Database;
import org.sormula.SormulaException;
import org.sormula.Table;

import java.util.List;

/**
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class InvoiceLineDAO extends Table<InvoiceLine> {

    /**
     * Builder
     *
     * @param database
     * @throws SormulaException
     */
    public InvoiceLineDAO(Database database) throws SormulaException {
        super(database, InvoiceLine.class);
    }

    /**
     * Method get item object from invoice line itemId
     *
     * @param invoiceLine
     * @return
     * @throws SormulaException
     */
    public Item getItem(InvoiceLine invoiceLine) throws SormulaException {
        ItemDAO itemDAO = new ItemDAO(this.getDatabase());
        List<Item> items = itemDAO.selectAll();
        for (Item item : items) {
            if (item.getId() == invoiceLine.getItemId()) {
                return item;
            }
        }
        return null;
    }
}

