package domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Objects;

@Entity
@NamedQueries(
        @NamedQuery(name = "arrival.findAll", query = "SELECT a FROM Arrival a")
)
public class Arrival extends BaseEntity {
    private String port;

    @Temporal(TemporalType.DATE)
    private Date date;

    public Arrival() {
    }

    public Arrival(String port, Date date) {
        this.port = port;
        this.date = date;
    }

    public Arrival build() {
        Arrival arrival = new Arrival();
        arrival.date = this.date;
        arrival.port = this.port;
        return arrival;
    }

    public String getPort() {
        return port;
    }

    public Arrival setPort(String port) {
        this.port = port;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Arrival setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public String toString() {
        ObjectMapper mapperObj = new ObjectMapper();
        String json = null;
        try {
            json = mapperObj.writeValueAsString(this);
        } catch (Exception e) {
            //TODO add logger
        }
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Arrival arrival = (Arrival) o;
        return Objects.equals(port, arrival.port) &&
                Objects.equals(date, arrival.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), port, date);
    }
}
