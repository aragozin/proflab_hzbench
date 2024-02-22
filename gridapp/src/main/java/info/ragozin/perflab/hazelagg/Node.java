package info.ragozin.perflab.hazelagg;

import java.io.File;
import java.io.FileNotFoundException;

import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class Node {

    final Config cfg;
    final HazelcastInstance node;
    final IMap<PositionKey, Position> cache;

    public Node(boolean lite) throws FileNotFoundException {
        if (lite) {
            cfg = new FileSystemXmlConfig("node-conf-lite.xml");
        } else {
            cfg = new FileSystemXmlConfig("node-conf.xml");
        }

        node = Hazelcast.newHazelcastInstance(cfg);

        cache = node.getMap("positions");
    }

    public Node(File config) throws FileNotFoundException {
        cfg = new FileSystemXmlConfig(config);

        node = Hazelcast.newHazelcastInstance(cfg);

        cache = node.getMap("positions");
    }

    public HazelcastInstance getInstantce() {
        return node;
    }

    public IMap<PositionKey, Position> getCache() {
        return cache;
    }

    public void shutdown() {
        node.shutdown();
    }
}
