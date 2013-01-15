package us.physion.ovation.integration;

import com.google.inject.Inject;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.junit.After;
import org.junit.Before;
import us.physion.ovation.DataContext;
import us.physion.ovation.api.DataStoreCoordinator;

/**
 * Created with IntelliJ IDEA.
 * User: barry
 * Date: 1/12/13
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class IntegrationTestBase
{
    protected static final String USER_NAME = "user";
    protected static final char[] PASSWORD = "password".toCharArray();
    private static final String EMAIL = "email@email.com";
    @Inject
    DataStoreCoordinator dsc;

    @Before
    public void create_user()
    {
        DataContext ctx = dsc.getContext();
        ctx.addUser(USER_NAME, EMAIL, PASSWORD);
    }

    @After
    public void clean_up(CouchDbInstance server,
                         CouchDbConnector db)
    {
        server.deleteDatabase(db.path());
    }
}
