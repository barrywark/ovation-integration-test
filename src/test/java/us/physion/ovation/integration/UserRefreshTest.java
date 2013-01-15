package us.physion.ovation.integration;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import us.physion.ovation.DataContext;
import us.physion.ovation.api.OvationApiModule;
import us.physion.ovation.domain.User;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class UserRefreshTest extends IntegrationTestBase
{
    public static class Module extends JukitoModule
    {

        @Override
        protected void configureTest()
        {
            new OvationApiModule().configure(binder());
        }
    }

    @Test
    public void should_refresh_password_from_separate_context() throws InterruptedException
    {
        DataContext ctx = dsc.getContext();
        ctx.authenticateUser(USER_NAME, PASSWORD);

        final String username = "newuser";
        final String email = "email@test.com";
        final char[] password = "password".toCharArray();

        User u = ctx.addUser(username, email, password);
        URI userUri = u.getURI();

        DataContext ctx2 = dsc.getContext();
        ctx2.authenticateUser(username, password);
        assertEquals(u.getUuid(), ctx2.getAuthenticatedUser().getUuid());

        User u2 = (User) ctx2.getObjectWithURI(userUri.toString());
        final String newEmail = "new@test.com";
        u2.updateEmail(newEmail);
        assertEquals(newEmail, u2.getEmail());

        u.refresh(true);

        assertEquals(newEmail, u.getEmail());
    }
}
