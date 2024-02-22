package info.ragozin.proflab.hzbench.load;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import info.ragozin.perflab.hazelagg.Client;
import info.ragozin.perflab.hazelagg.SliceKey;

public class HzLoadRunner {

    private final Client client;
    private final List<String> books;

    public HzLoadRunner(Client client) {
        this.client = client;
        this.books = new ArrayList<>(client.books());
    }

    public void run() {
        Random rnd = new Random(1);
        int count = 0;
        double total = 0;
        while(true) {
            String book = books.get(rnd.nextInt(books.size()));
            long ts = System.nanoTime();
            Map<SliceKey, BigDecimal> result = client.snapshotBook(book);
            double timeMS = 1d * (System.nanoTime() - ts) / TimeUnit.MILLISECONDS.toNanos(1);

            System.out.println("Book [" + book + "], positions: " + result.size() + String.format(", snapshot time: %.2fms", timeMS));

            count++;
            total += timeMS;

            if (count == 20) {
                double avgMs = total / count;
                System.out.println(String.format("\nAverage over last %d calls is %.2fms\n", count, avgMs));
                count = 0;
                total = 0;
            }
        }
    }
}
