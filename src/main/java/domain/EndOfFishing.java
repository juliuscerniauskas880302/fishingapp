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
        @NamedQuery(name = "endOfFishing.findAll", query = "SELECT e FROM EndOfFishing e")
)
public class EndOfFishing extends BaseEntity {
    private static final Logger LOG = LogManager.getLogger(EndOfFishing.class);

    @Temporal(TemporalType.DATE)
    private Date date;

    public EndOfFishing() {
    }

    public EndOfFishing(Date date) {
        this.date = date;
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
            LOG.error("Error mapping EndOfFishing {} to json string.", this);
        }
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EndOfFishing that = (EndOfFishing) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date);
    }

    public String getFormattedDate(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(this.date);
    }

}
