package com.damon4u.es.client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-17 11:12
 */
public class ClientFactory {

    public static TransportClient getDefaultClient() {
        Settings settings = Settings.builder().put("cluster.name","damon4u-cluster").build();
        try {
            return new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9301));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static TransportClient getClient(String clusterName, InetSocketTransportAddress... addresses) {
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        PreBuiltTransportClient client = new PreBuiltTransportClient(settings);
        for (InetSocketTransportAddress address : addresses) {
            client.addTransportAddress(address);
        }
        return client;
    }
}
