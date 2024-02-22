package info.ragozin.proflab.hzbench.demo;

import info.ragozin.labconsole.agent.DemoInitializer;
import info.ragozin.perflab.hazelagg.DataGenerator;

public class DemoActions {

    static {
        DemoInitializer.initConfiguration();
    }

    public static void start() {
        if (!clusterIsReady()) {
            System.out.println("Starting cluster ...");
            stopCluster();

            HzNode1.control().start();
            HzNode2.control().start();
            HzNode3.control().start();
            HzService.control().start();

            System.out.println("Waiting for service ...");

            waitForService();

            System.out.println("HTTP end point is read at " + HzService.client().toString());

            generateData();

            System.out.println("Cluster is ready\n");

        } else {
            System.out.println("Cluster is ready\n");
        }

    }

    private static void generateData() {
        DataGenerator gen = new DataGenerator();
        gen.setBookLimit(DemoInitializer.propAsInt("hzdemo.data.bookCount", 100));
        gen.setUnderlayingLimit(DemoInitializer.propAsInt("hzdemo.data.underlayingLimit", 100));
        gen.setContractLimit(DemoInitializer.propAsInt("hzdemo.data.contractLimit", 10));

        int volume = DemoInitializer.propAsInt("hzdemo.data.volume", 10000);

        gen.generate(HzService.client(), volume);

        System.out.println("Data generated, cache size is " + HzService.client().size());
    }

    private static void waitForService() {
        while(true) {
            if (!HzService.control().check()) {
                System.err.println("Service is down");
                throw new RuntimeException("Service is down");
            }

            try {
                if (HzService.client().isReady()) {
                    return;
                }

                Thread.sleep(200);
            } catch (InterruptedException e) {
                // ignore errors
            }
        }
    }

    private static boolean clusterIsReady() {
        if (!HzNode1.control().check()) {
            return false;
        }
        if (!HzNode2.control().check()) {
            return false;
        }
        if (!HzNode3.control().check()) {
            return false;
        }
        if (!HzService.control().check()) {
            return false;
        }
        if (!HzService.client().isReady()) {
            return false;
        }

        int bookCount = HzService.client().books().size();
        if (bookCount != DemoInitializer.propAsInt("hzdemo.data.bookCount", 100)) {
            System.out.println("Cluster is missing the data");
            return false;
        }

        return true;
    }

    public static void load() {
        System.out.println("Starting the load ...");

        HzLoadGen.control().start();

        System.out.println("");
    }

    public static void info() {
        System.out.println("Environment information ...\n");

        System.out.println("HzNode1 - " + (HzNode1.control().check() ? "live" : "down"));
        System.out.println("HzNode2 - " + (HzNode2.control().check() ? "live" : "down"));
        System.out.println("HzNode3 - " + (HzNode3.control().check() ? "live" : "down"));
        System.out.println("HzService - " + (HzService.control().check() ? "live" : "down"));
        System.out.println("LoadGen - " + (HzLoadGen.control().check() ? "live" : "down"));

        System.out.println();
    }

    public static void stop() {
        System.out.println("Stopping demo envirnment");
        stopCluster();
        HzLoadGen.control().kill();
    }

    private static void stopCluster() {
        HzNode1.control().kill();
        HzNode2.control().kill();
        HzNode3.control().kill();
        HzService.control().kill();
    }
}
