package info.ragozin.proflab.hzbench.demo;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slf4j.bridge.SLF4JBridgeHandler;

import info.ragozin.labconsole.agent.DemoInitializer;
import info.ragozin.labconsole.agent.GenericStarter;
import info.ragozin.perflab.hazelagg.Node;

public class HzNode2 extends GenericStarter {

    @SuppressWarnings("unused")
    private Node node;

    public static HzNode2 control() {
        return new HzNode2(true);
    }

    @Override
    protected String getProcessTag() {
        return "hznode2";
    }

    protected HzNode2() {
        super();
    }

    protected HzNode2(boolean control) {
        super(control);
    }


    public static void main(String... args) throws FileNotFoundException {
        new HzNode2().run();
    }

    @Override
    protected void run() throws FileNotFoundException {

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Logger.getLogger("").setLevel(Level.FINEST);

        node = new Node(DemoInitializer.file("gridapp/node-conf.xml"));
        LOG.info("Cluster node has been started");
    }
}
