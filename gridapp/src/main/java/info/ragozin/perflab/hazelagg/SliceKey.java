package info.ragozin.perflab.hazelagg;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class SliceKey implements Serializable {

    public static final SliceKey ANY = new SliceKey(null, null, null);

    private String book;
    private String underlaying;
    private String contract;

    public static SliceKey book(String book) {
        return new SliceKey(book, null, null);
    }

    public static SliceKey bookUnderlaying(String book, String underlaying) {
        return new SliceKey(book, underlaying, null);
    }

    public static SliceKey underlaying(String underlaying) {
        return new SliceKey(null, underlaying, null);
    }

    SliceKey() {
    }

    @JsonCreator
    public SliceKey(@JsonProperty("book") String book, @JsonProperty("underlaying") String underlaying, @JsonProperty("contract") String contract) {
        this.book = book;
        this.underlaying = underlaying;
        this.contract = contract;
    }

    public String getBook() {
        return book;
    }

    public String getUnderlaying() {
        return underlaying;
    }

    public String getContract() {
        return contract;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((book == null) ? 0 : book.hashCode());
        result = prime * result + ((contract == null) ? 0 : contract.hashCode());
        result = prime * result + ((underlaying == null) ? 0 : underlaying.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SliceKey other = (SliceKey) obj;
        if (book == null) {
            if (other.book != null)
                return false;
        } else if (!book.equals(other.book))
            return false;
        if (contract == null) {
            if (other.contract != null)
                return false;
        } else if (!contract.equals(other.contract))
            return false;
        if (underlaying == null) {
            if (other.underlaying != null)
                return false;
        } else if (!underlaying.equals(other.underlaying))
            return false;
        return true;
    }

    public static SliceKey fromString(String text) {
        if ("ANY".equals(text)) {
            return new SliceKey(null, null, null);
        } else {
            String[] parts = text.split("[|]");
            assert parts.length == 3;
            return new SliceKey(
                    "*".equals(parts[0]) ? null : parts[0],
                    "*".equals(parts[1]) ? null : parts[1],
                    "*".equals(parts[2]) ? null : parts[2]);
        }
    }

    @Override
    public String toString() {

        if (book == null && underlaying == null && contract == null) {
            return "ANY";
        }

        StringBuilder sb = new StringBuilder();
        if (book != null) {
            sb.append(book);
        }
        else {
            sb.append("*");
        }
        sb.append('|');
        if (underlaying != null) {
            sb.append(underlaying);
        }
        else {
            sb.append("*");
        }
        sb.append('|');
        if (contract != null) {
            sb.append(contract);
        }
        else {
            sb.append("*");
        }

        return sb.toString();
    }
}
