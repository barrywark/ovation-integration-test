package us.physion.ovation.integration;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.google.common.collect.Sets;
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

import java.util.Set;
import java.util.UUID;

@RunWith(JukitoRunner.class)
public class ProjectRetrievalPerfTest extends IntegrationTestBase
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

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0)
    public void individual_cached_entity_retrieval_performance_benchmark()
    {
        DataContext ctx = dsc.getContext();
        ctx.authenticateUser(USER_NAME, PASSWORD);

        String name = "name";
        String purpose = "purpose";
        DateTime start = new DateTime();
        Set<UUID> uuidSet = Sets.newHashSet();
        ctx.beginTransaction();
        try {
            for (int i = 0; i < 100; i++) {
                Project p = ctx.insertProject(name, purpose, start);
                uuidSet.add(p.getUuid());
            }
        } finally {
            ctx.commitTransaction();
        }

        for (UUID uuid : uuidSet) {
            ctx.getObjectWithUuid(uuid);
        }
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0)
    public void individual_uncached_entity_retrieval_performance_benchmark()
    {
        DataContext ctx = dsc.getContext();
        ctx.authenticateUser(USER_NAME, PASSWORD);

        String name = "name";
        String purpose = "purpose";
        DateTime start = new DateTime();
        Set<UUID> uuidSet = Sets.newHashSet();
        ctx.beginTransaction();
        try {
            for (int i = 0; i < 100; i++) {
                Project p = ctx.insertProject(name, purpose, start);
                uuidSet.add(p.getUuid());
            }
        } finally {
            ctx.commitTransaction();
        }

        ctx.getProjectRepository().clear();

        for (UUID uuid : uuidSet) {
            ctx.getObjectWithUuid(uuid);
        }
    }
}
