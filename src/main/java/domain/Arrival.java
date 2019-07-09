package domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import domain.base.BaseEntity;
import utilities.LocalDateDeserializer;
import utilities.LocalDateSerializer;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@NamedQueries(
        @NamedQuery(name = "arrival.findAll", query = "SELECT a FROM Arrival a")
)
public class Arrival extends BaseEntity {
    private String port;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    public Arrival() {
    }

    public Arrival(String port, LocalDate date) {
        this.port = port;
        this.date = date;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
