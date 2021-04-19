package dao;

import model.Client;
import org.sormula.Database;
import org.sormula.SormulaException;
import org.sormula.Table;

/**
 * @author Lara Vázquez Dorna
 * @see /github.com/laradorna
 */
public class ClientDAO extends Table<Client> {
    public ClientDAO(Database database) throws SormulaException {
        super(database, Client.class);
    }
}

