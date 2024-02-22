package info.ragozin.perflab.hazelagg;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoCheck {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Logger.getLogger("").setLevel(Level.FINEST);
    }

    private Node n1;
    private Node n2;
    private Node n3;
    private Node ns;

    private Server server;

    @After
    public void shutdown() {
        if (server != null) {
            server.stop();
        }
        if (n1 != null) {
            n1.shutdown();
        }
        if (n2 != null) {
            n2.shutdown();
        }
        if (n3 != null) {
            n3.shutdown();
        }
        if (ns != null) {
            ns.shutdown();
        }
    }

    private void initServer() throws FileNotFoundException {
        if (server == null) {
            if (ns == null) {
                ns = new Node(true);
            }
            server = new Server(new Service(ns));
            server.start(1278);
        }
    }

    private void initN1() throws FileNotFoundException {
        if (n1 == null) {
            n1 = new Node(false);
        }
    }

    private void initN2() throws FileNotFoundException {
        if (n2 == null) {
            n2 = new Node(false);
        }
    }

    private void initN3() throws FileNotFoundException {
        if (n3 == null) {
            n3 = new Node(false);
        }
    }

    @Test
    public void checkReadyness() throws FileNotFoundException, InterruptedException {
        initServer();

        Client client = new Client("http://127.0.0.1:1278");

        Assertions.assertThat(client.isReady()).isFalse();

        initN1();
        initN2();
        initN3();

        while(ns.getInstantce().getCluster().getMembers().size() < 4) {
            Thread.sleep(100);
        }

        Assertions.assertThat(client.isReady()).isTrue();
    }

    @Test
    public void checkService() throws FileNotFoundException, InterruptedException, JsonProcessingException {
        initN1();
        initN2();
        initN3();
        initServer();

        Client client = new Client("http://127.0.0.1:1278");

        while(ns.getInstantce().getCluster().getMembers().size() < 4) {
            Thread.sleep(100);
        }

        Assertions.assertThat(client.isReady()).isTrue();

        List<Position> batch = Arrays.asList(
                Position.update(1, 100, "A", "X", "SPOT", 10),
                Position.update(2, 100, "A", "X", "SPOT", 11),
                Position.update(3, 100, "B", "X", "SPOT", 12),
                Position.update(1, 200, "A", "X", "SPOT", 13),
                Position.update(4, 400, "C", "X", "SPOT", 14),
                Position.remove(1, 300, "A", "X", "SPOT")
                );

        client.update(batch);

        ObjectMapper mapper = new ObjectMapper();

        System.out.println("[A] -> " + mapper.writeValueAsString(client.snapshotBook("A")));
        System.out.println("[B] -> " + mapper.writeValueAsString(client.snapshotBook("B")));
        System.out.println("[C] -> " + mapper.writeValueAsString(client.snapshotBook("C")));

        assertThat(client.size()).isEqualTo(6);
        assertThat(client.books()).contains("A", "B", "C");
    }

    @Test
    public void checkDataGen() throws FileNotFoundException, InterruptedException, JsonProcessingException {
        initN1();
        initN2();
        initN3();
        initServer();

        Client client = new Client("http://127.0.0.1:1278");

        while(ns.getInstantce().getCluster().getMembers().size() < 4) {
            Thread.sleep(100);
        }

        Assertions.assertThat(client.isReady()).isTrue();
        DataGenerator gen = new DataGenerator();

        gen.generate(client, 10000);

        assertThat(client.size()).isEqualTo(9952);
        assertThat(client.books()).hasSize(100);
    }

}
