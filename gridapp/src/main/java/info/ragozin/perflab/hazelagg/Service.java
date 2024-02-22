package info.ragozin.perflab.hazelagg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.hazelcast.core.Member;

public class Service {

    Node node;

    public Service(Node node) {
        this.node = node;
    }

    public boolean isClusterReady() {
        // expect 4 members
        return node.getInstantce().getCluster().getMembers().size() == 4;
    }

    public List<String> getClusterMembers() {
        List<String> members = new ArrayList<String>();

        for (Member m: node.getInstantce().getCluster().getMembers()) {
            members.add(m.toString());
        }

        return members;
    }

    public void update(Collection<Position> positions) {
        Map<PositionKey, Position> batch = new HashMap<>();
        for (Position p: positions) {
            batch.put(p.getKey(), p);
        }
        node.getCache().putAll(batch);
    }

    public Map<SliceKey, BigDecimal> snapshotBook(String book) {
        Map<SliceKey, BigDecimal> data = node.getCache().aggregate(PositionAggregation.sliceSupplier(SliceKey.book(book)), new PositionAggregation(Integer.MAX_VALUE));
        return data;
    }

    public Collection<String> getBooks() {
        Set<String> books = new TreeSet<>();
        for (Position p: node.getCache().values()) {
            books.add(p.book);
        }
        return books;
    }

    public int size() {
        return node.getCache().size();
    }

}
