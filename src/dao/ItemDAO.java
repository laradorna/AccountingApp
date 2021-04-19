package dao;

import model.Item;
import org.sormula.Database;
import org.sormula.SormulaException;
import org.sormula.Table;

/**
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class ItemDAO extends Table<Item> {

    /**
     * Builder
     * @param database
     * @throws SormulaException
     */
    public ItemDAO(Database database) throws SormulaException {
        super(database, Item.class);
    }
}

