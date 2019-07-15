package domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.base.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@NamedQueries(
        @NamedQuery(name = "departure.findAll", query = "SELECT d FROM Departure d")
)
@Slf4j
public class Departure extends BaseEntity {

    private String port;

    @Temporal(TemporalType.DATE)
    private Date date;

    public Departure() {
    }

    public Departure(String port, Date date) {
        this.port = port;
        this.date = date;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        ObjectMapper mapperObj = new ObjectMapper();
        String json = null;
        try {
            json = mapperObj.writeValueAsString(this);
        } catch (Exception e) {
            log.error("Error mapping Departure {} to json string.", this);
        }
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Departure departure = (Departure) o;
        return Objects.equals(port, departure.port) &&
                Objects.equals(date, departure.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), port, date);
    }

    public String getFormattedDate(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(this.date);
    }

}
