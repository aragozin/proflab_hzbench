package info.ragozin.perflab.hazelagg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private int bookLimit = 100;
    private int underlayingLimit = 100;
    private int contractLimit = 10;

    public int getBookLimit() {
        return bookLimit;
    }

    public void setBookLimit(int bookLimit) {
        this.bookLimit = bookLimit;
    }

    public int getUnderlayingLimit() {
        return underlayingLimit;
    }

    public void setUnderlayingLimit(int underlayingLimit) {
        this.underlayingLimit = underlayingLimit;
    }

    public int getContractLimit() {
        return contractLimit;
    }

    public void setContractLimit(int contractLimit) {
        this.contractLimit = contractLimit;
    }

    public void generate(Client client, int amount) {
        Random rnd = new Random(1);

        List<Position> batch = new ArrayList<Position>();
        for (int i = 0; i != amount; ++i) {

            String book = "B" + rnd.nextInt(bookLimit);
            String under = "U" + rnd.nextInt(underlayingLimit);
            String cornt = "C" + rnd.nextInt(contractLimit);

            long id = rnd.nextInt(110 * amount / 100) % amount;
            long ts = rnd.nextInt(100);

            batch.add(Position.update(id, ts, book, under, cornt, 0.1d * rnd.nextInt(1000)));

            if (batch.size() > 100) {
                client.update(batch);
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            client.update(batch);
        }
    }
}
