package domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.base.BaseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        @NamedQuery(name = "arrival.findAll", query = "SELECT a FROM Arrival a")
)
public class Arrival extends BaseEntity {
    private static final Logger LOG = LogManager.getLogger(Arrival.class);

    private String port;

    @Temporal(TemporalType.DATE)
    private Date date;

    public Arrival() {
    }

    public Arrival(String port, Date date) {
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
            LOG.error("Error mapping Arrival {} to json string.", this);
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

    public static class Builder {
        private String port;
        private Date date;

        public Builder setPort(String port) {
            this.port = port;
            return this;
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public Arrival build() {
            Arrival arrival = new Arrival();
            arrival.port = this.port;
            arrival.date = this.date;
            return arrival;
        }
    }

    public String getFormattedDate(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(this.date);
    }

}
