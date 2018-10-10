package com.tangdou.panda.utils;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static com.tangdou.panda.utils.HostNameUtils.localHost;

public class MetricRegistryUtils {
    private static final Logger LOG = LoggerFactory.getLogger(MetricRegistryUtils.class);

    private static final String GRAPHITE_METRICS_NAMESPACE_PREFIX = "apps.salmon";
    private static final String HOSTNAME;

    private static final MetricRegistry METRICS = new MetricRegistry();

    static {
        HOSTNAME = localHost();
        reportViaGraphite();
        reportViaConsole();
    }

    public static MetricRegistry getMetricRegistry() {
        return METRICS;
    }

    private static void reportViaGraphite() {
        Graphite graphite = new Graphite(new InetSocketAddress("10.19.61.77", 2003));
//        Graphite graphite = new Graphite(new InetSocketAddress("10.19.61.77", 2023));
        GraphiteReporter reporter = GraphiteReporter.forRegistry(METRICS)
                .prefixedWith(GRAPHITE_METRICS_NAMESPACE_PREFIX)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build(graphite);
        reporter.start(10, TimeUnit.SECONDS);
    }

    private static void reportViaConsole() {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(METRICS)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(10, TimeUnit.SECONDS);
    }

    public static String name(String name, String... names) {
        return MetricRegistry.name(MetricRegistry.name(name, names), HOSTNAME);
    }

    public static String avgGaugeName(String name, String... names) {
        return MetricRegistry.name(name(name, names), "gauge-avg");
    }

    public static String sumGaugeName(String name, String... names) {
        return MetricRegistry.name(name(name, names), "gauge-sum");
    }
}
