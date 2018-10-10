package com.tangdou.panda.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class HostNameUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HostNameUtils.class);

    private static final Pattern HOSTNAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-]*(\\.[a-zA-Z0-9][a-zA-Z0-9-]*)*$");

    public static String localHost() {
        try {
            return simplify(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            LOG.error("Could not determine hostname");  // TODO add to metrics
            return "unknown-host";
        }
    }

    public static String resolve(String name) {
        try {
            return simplify(InetAddress.getByName(name).getCanonicalHostName());
        } catch (UnknownHostException e) {
            LOG.error("Could not resolve host name: {}", name);  // TODO add to metrics
            return "unknown-host";
        }
    }

    private static String simplify(String fqdn) {
        if (HOSTNAME_PATTERN.matcher(fqdn).matches()) {
            if (fqdn.contains(".")) {
                return fqdn.split("\\.")[0];
            } else {
                return fqdn;
            }
        } else {
            return fqdn;
        }
    }
}
