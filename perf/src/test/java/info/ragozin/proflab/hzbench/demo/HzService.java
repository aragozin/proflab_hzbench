package info.ragozin.proflab.hzbench.demo;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slf4j.bridge.SLF4JBridgeHandler;

import info.ragozin.labconsole.agent.DemoInitializer;
import info.ragozin.labconsole.agent.GenericStarter;
import info.ragozin.perflab.hazelagg.Client;
import info.ragozin.perflab.hazelagg.Node;
import info.ragozin.perflab.hazelagg.Server;
import info.ragozin.perflab.hazelagg.Service;

public class HzService extends GenericStarter {

    private Server server;

    public static Client client() {
        return new Client("http://127.0.0.1:" + DemoInitializer.propAsInt("hzdemo.http.port", 8080));
    }

    public static HzService control() {
        return new HzService(true);
    }

    @Override
    protected String getProcessTag() {
        return "hzserv";
    }

    protected HzService() {
        super();
    }

    protected HzService(boolean control) {
        super(control);
    }


    public static void main(String... args) throws FileNotFoundException {
        new HzService().run();
    }

    @Override
    protected void run() throws FileNotFoundException {

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Logger.getLogger("").setLevel(Level.FINEST);

        Node node = new Node(DemoInitializer.file("gridapp/node-conf-lite.xml"));
        Service serv = new Service(node);
        server = new Server(serv);
        server.start(DemoInitializer.propAsInt("hzdemo.http.port", 8080));
    }
}
