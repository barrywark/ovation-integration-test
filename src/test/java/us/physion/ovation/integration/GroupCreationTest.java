package us.physion.ovation.integration;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import us.physion.ovation.DataContext;
import us.physion.ovation.api.DataStoreCoordinator;
import us.physion.ovation.api.OvationApiModule;
import us.physion.ovation.domain.factories.GroupFactory;

import javax.inject.Inject;

import static org.junit.Assert.fail;

@RunWith(JukitoRunner.class)
public class GroupCreationTest
{

    private static final String USER_NAME = "user";
    private static final String EMAIL = "email@email.com";
    private static final char[] PASSWORD = "password".toCharArray();

    public static class Module extends JukitoModule
    {

        @Override
        protected void configureTest()
        {
            new OvationApiModule().configure(binder());
        }
    }

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

    /**
     * As an authenticated user, I should be able to create write groups.
     */
    @Test
    @Ignore
    public void should_create_write_groups(GroupFactory groupFactory)
    {
        DataContext ctx = dsc.getContext();
        ctx.authenticateUser(USER_NAME, PASSWORD);

        fail();
    }
}
