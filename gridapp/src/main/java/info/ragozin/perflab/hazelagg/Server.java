package info.ragozin.perflab.hazelagg;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Server {

    private final Service service;
    private final Javalin server;

    public Server(Service service) {
        this.service = service;
        server = Javalin.create();
        server.get ("status", this::handleStatus);
        server.get ("size", this::handleSize);
        server.get ("books", this::handleBooks);
        server.post("update", this::handleUpdate);
        server.get ("snapshot/{book}", this::handleSnapshot);
    }

    public void start(int port) {
        server.start(port);
    }

    public void stop() {
        server.stop();
    }

    private ObjectMapper mapper() {
        return new ObjectMapper();
    }

    private void handleStatus(Context ctx) throws JsonProcessingException {
        List<String> members = service.getClusterMembers();
        ctx.status(members.size() == 4 ? 200 : 525);
        ctx.result(mapper().writeValueAsString(members));
    }

    private void handleSize(Context ctx) throws JsonProcessingException {
        ctx.result(String.valueOf(service.size()));
    }

    private void handleUpdate(Context ctx) throws StreamReadException, DatabindException, IOException {
        List<Position> positions = mapper().readValue(ctx.bodyAsBytes(), mapper().getTypeFactory().constructCollectionLikeType(ArrayList.class, Position.class));
        service.update(positions);
        ctx.status(200);
    }

    private void handleSnapshot(Context ctx) throws JsonProcessingException {
        String book = ctx.pathParam("book");
        Map<SliceKey, BigDecimal> snapshot = service.snapshotBook(book);

        ctx.result(mapper().writeValueAsBytes(snapshot));
    }

    private void handleBooks(Context ctx) throws JsonProcessingException {
        ctx.result(mapper().writeValueAsString(service.getBooks()));
    }

}
