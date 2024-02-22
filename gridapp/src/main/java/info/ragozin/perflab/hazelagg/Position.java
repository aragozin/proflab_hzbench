package info.ragozin.perflab.hazelagg;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class Position implements Serializable {

    public static Position update(long id, long timestamp, String book, String underlying, String contract, double qty) {
        Position p = new Position(id, timestamp);
        p.setBook(book);
        p.setUnderlying(underlying);
        p.setContract(contract);
        p.setQty(qty);
        p.setActive(true);
        return p;
    }

    public static Position remove(long id, long timestamp, String book, String underlying, String contract) {
        Position p = new Position(id, timestamp);
        p.setActive(false);
        p.setBook(book);
        p.setUnderlying(underlying);
        p.setContract(contract);
        return p;
    }

    protected long positionId;
    protected long timestamp;

    protected String book = "";
    protected String underlying = "";
    protected String contract = "";

    protected double qty = 0d;
    protected boolean active = false;

    Position() {
        // for serialization
    }

    public Position(long positionId, long timestamp) {
        this.positionId = positionId;
        this.timestamp = timestamp;
    }

    public long getPositionId() {
        return positionId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @JsonIgnore
    public PositionKey getKey() {
        return new PositionKey(positionId, timestamp);
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getUnderlying() {
        return underlying;
    }

    public void setUnderlying(String underlying) {
        this.underlying = underlying;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result + ((book == null) ? 0 : book.hashCode());
        result = prime * result + ((contract == null) ? 0 : contract.hashCode());
        result = prime * result + (int) (positionId ^ (positionId >>> 32));
        long temp;
        temp = Double.doubleToLongBits(qty);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
        result = prime * result + ((underlying == null) ? 0 : underlying.hashCode());
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
        Position other = (Position) obj;
        if (active != other.active)
            return false;
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
        if (positionId != other.positionId)
            return false;
        if (Double.doubleToLongBits(qty) != Double.doubleToLongBits(other.qty))
            return false;
        if (timestamp != other.timestamp)
            return false;
        if (underlying == null) {
            if (other.underlying != null)
                return false;
        } else if (!underlying.equals(other.underlying))
            return false;
        return true;
    }
}
