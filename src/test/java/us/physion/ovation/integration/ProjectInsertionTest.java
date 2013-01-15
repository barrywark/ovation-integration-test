package us.physion.ovation.integration;

import org.joda.time.DateTime;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import us.physion.ovation.DataContext;
import us.physion.ovation.api.OvationApiModule;
import us.physion.ovation.domain.Project;

import static org.junit.Assert.*;

@RunWith(JukitoRunner.class)
public class ProjectInsertionTest extends IntegrationTestBase
{

    public static class Module extends JukitoModule
    {

        @Override
        protected void configureTest()
        {
            new OvationApiModule().configure(binder());
        }
    }

    /**
     * As an authenticated user, when I insert a Project into a DataContext, the project should be
     * persisted and retrievable.
     */
    @Test
    public void should_insert_project()
    {
        DataContext ctx = dsc.getContext();
        ctx.authenticateUser(USER_NAME, PASSWORD);

        String name = "name";
        String purpose = "purpose";
        DateTime start = new DateTime();
        Project p = ctx.insertProject(name, purpose, start);

        Project actual = (Project) ctx.getObjectWithUuid(p.getUuid());

        assertEquals(p, actual);
        assertEquals(name, actual.getName());
        assertEquals(purpose, actual.getPurpose());
        assertEquals(start, actual.getStart());

        assertTrue(p == actual);
    }

    /**
     * As an authenticated user, I should be able to insert multiple projects in a single transaction
     */
    @Test
    public void should_insert_multiple_projects()
    {
        DataContext ctx = dsc.getContext();
        ctx.authenticateUser(USER_NAME, PASSWORD);

        String name = "name";
        String purpose = "purpose";
        DateTime start = new DateTime();

        ctx.beginTransaction();
        Project p1 = ctx.insertProject(name, purpose, start);
        Project p2 = ctx.insertProject(name, purpose, start);
        ctx.commitTransaction();


        assertNotNull(ctx.getObjectWithUuid(p1.getUuid()));
        assertNotNull(ctx.getObjectWithUuid(p2.getUuid()));

    }
}
