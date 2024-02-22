package info.ragozin.proflab.hzbench.demo;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slf4j.bridge.SLF4JBridgeHandler;

import info.ragozin.labconsole.agent.GenericStarter;
import info.ragozin.proflab.hzbench.load.HzLoadRunner;

public class HzLoadGen extends GenericStarter {

    public static HzLoadGen control() {
        return new HzLoadGen(true);
    }

    @Override
    protected String getProcessTag() {
        return "loadgen";
    }

    protected HzLoadGen() {
        super();
    }

    protected HzLoadGen(boolean control) {
        super(control);
    }


    public static void main(String... args) throws FileNotFoundException {
        new HzLoadGen().run();
    }

    @Override
    protected void run() throws FileNotFoundException {

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Logger.getLogger("").setLevel(Level.FINEST);

        HzLoadRunner runner = new HzLoadRunner(HzService.client());
        runner.run();
    }
}
