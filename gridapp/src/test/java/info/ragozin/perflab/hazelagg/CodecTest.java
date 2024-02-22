package info.ragozin.perflab.hazelagg;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CodecTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPosition() throws JsonProcessingException {

        Position p1 = Position.update(1, 10, "x100", "U124", "C15", 25);

        Assertions.assertThat(recode(p1)).isEqualTo(p1);

        Position p2 = Position.remove(2, 11, "x100", "U124", "C15");

        Assertions.assertThat(recode(p2)).isEqualTo(p2);
    }

    @Test
    public void testSliceKey() throws JsonProcessingException {

        SliceKey k1 = SliceKey.book("x100");

        Assertions.assertThat(recode(k1)).isEqualTo(k1);

        SliceKey k2 = SliceKey.bookUnderlaying("x101", "U122");

        Assertions.assertThat(recode(k2)).isEqualTo(k2);

        SliceKey k3 = new SliceKey("x102", "U99", "C10");

        Assertions.assertThat(recode(k3)).isEqualTo(k3);
    }

    @Test
    public void testBigDecimal() throws JsonProcessingException {

        BigDecimal dec = new BigDecimal(1201d);

        Assertions.assertThat(recode(dec)).isEqualTo(dec);
    }

    @SuppressWarnings("unchecked")
    public <T> T recode(T object) throws JsonProcessingException {
        String text = mapper.writeValueAsString(object);
        T copy = (T) mapper.readValue(text, object.getClass());
        return copy;
    }

}
