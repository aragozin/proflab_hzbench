package info.ragozin.perflab.hazelagg;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class Client {

    private final ObjectMapper mapper = new ObjectMapper();
    private final String url;

    public Client(String url) {
        this.url = normalize(url);
        initMapper(mapper);
    }

    private void initMapper(ObjectMapper mapper2) {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addKeyDeserializer(SliceKey.class, new KeyDeserializer() {

            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return SliceKey.fromString(key);
            }
        });

    }

    private String normalize(String url) {
        return url.endsWith("/") ? url : url + "/";
    }

    public boolean isReady()  {
        try {
            @SuppressWarnings("unused")
            String result = get(url + "status");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public int size() {
        try {
            @SuppressWarnings("unused")
            String result = get(url + "size");
            return Integer.parseInt(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Collection<Position> positions) {
        try {
            @SuppressWarnings("unused")
            String result = post(url + "update", mapper.writeValueAsString(positions));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<SliceKey, BigDecimal> snapshotBook(String book) {
        try {
            String result = get(url + "snapshot/" + book);
            Map<SliceKey, BigDecimal> resp = mapper.readValue(result, mapper.getTypeFactory().constructMapLikeType(LinkedHashMap.class, SliceKey.class, BigDecimal.class));
            return resp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<String> books() {
        try {
            String result = get(url + "books");
            return mapper.readValue(result, mapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, String.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String get(String url) throws MalformedURLException, IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int code = conn.getResponseCode();
        if (code != 200) {
            throw new IOException("HTTP status " + code);
        }
        return IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8);
    }

    private String post(String url, String body) throws MalformedURLException, IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.connect();
        conn.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        int code = conn.getResponseCode();
        if (code != 200) {
            throw new IOException("HTTP status " + code);
        }
        return IOUtils.toString(conn.getInputStream(), StandardCharsets.UTF_8);
    }

    @Override
	public String toString() {
        return url;
    }
}
