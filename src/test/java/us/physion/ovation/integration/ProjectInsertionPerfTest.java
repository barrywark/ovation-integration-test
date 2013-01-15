package us.physion.ovation.integration;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.joda.time.DateTime;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import us.physion.ovation.DataContext;
import us.physion.ovation.api.OvationApiModule;
import us.physion.ovation.domain.Project;

@RunWith(JukitoRunner.class)

public class ProjectInsertionPerfTest extends IntegrationTestBase
{
    @Rule
    public MethodRule benchmarkRun = new BenchmarkRule();

    public static class Module extends JukitoModule
    {

        @Override
        protected void configureTest()
        {
            new OvationApiModule().configure(binder());
        }
    }


    /* ** This just takes too long ** (~20s/run)
    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0)
    public void individual_entity_insertion_performance_benchmark()
    {
        DataContext ctx = dsc.getContext();
        ctx.authenticateUser(USER_NAME, PASSWORD);

        String name = "name";
        String purpose = "purpose";
        DateTime start = new DateTime();
        for (int i = 0; i < 1000; i++) {
            Project p = ctx.insertProject(name, purpose, start);
        }
    }
    */


    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0)
    public void bulk_entity_insertion_performance_benchmark()
    {
        DataContext ctx = dsc.getContext();
        ctx.authenticateUser(USER_NAME, PASSWORD);

        String name = "name";
        String purpose = "purpose";
        DateTime start = new DateTime();
        ctx.beginTransaction();
        try {
            for (int i = 0; i < 100; i++) {
                Project p = ctx.insertProject(name, purpose, start);
            }
        } finally {
            ctx.commitTransaction();
        }
    }
}
