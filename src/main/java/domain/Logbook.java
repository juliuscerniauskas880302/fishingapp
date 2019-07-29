package domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.base.BaseEntity;
import domain.enums.CommunicationType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(
        @NamedQuery(name = "logbook.findAll", query = "select l from Logbook l")
)
public class Logbook extends BaseEntity {
    private static final Logger LOG = LogManager.getLogger(Logbook.class);

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Arrival arrival;
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Departure departure;
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private EndOfFishing endOfFishing;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<Catch> catches = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private CommunicationType communicationType;
    @Version
    private Integer version;
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled = true;
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;

    public Logbook() {
    }

    public Logbook(Arrival arrival, Departure departure, EndOfFishing endOfFishing,
                   List<Catch> catches, String communicationType, boolean enabled, Date lastUpdate) {
        this.arrival = arrival;
        this.departure = departure;
        this.endOfFishing = endOfFishing;
        this.catches = catches;
        this.communicationType = CommunicationType.valueOf(communicationType);
        this.enabled = enabled;
        this.lastUpdate = lastUpdate;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    public EndOfFishing getEndOfFishing() {
        return endOfFishing;
    }

    public void setEndOfFishing(EndOfFishing endOfFishing) {
        this.endOfFishing = endOfFishing;
    }

    public List<Catch> getCatches() {
        return catches;
    }

    public void setCatches(List<Catch> catches) {
        this.catches = catches;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        ObjectMapper mapperObj = new ObjectMapper();
        String json = null;
        try {
            json = mapperObj.writeValueAsString(this);
        } catch (Exception e) {
            LOG.error("Error mapping Logbook {} to json string.", this);
        }
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Logbook logbook = (Logbook) o;
        return Objects.equals(arrival, logbook.arrival) &&
                Objects.equals(departure, logbook.departure) &&
                Objects.equals(endOfFishing, logbook.endOfFishing) &&
                Objects.equals(catches, logbook.catches) &&
                communicationType == logbook.communicationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), arrival, departure, endOfFishing, catches, communicationType);
    }

    public static class Builder {

        private String id;
        private Arrival arrival;
        private Departure departure;
        private EndOfFishing endOfFishing;
        private List<Catch> catches = new ArrayList<>();
        private CommunicationType communicationType;
        private boolean enabled;
        private Date lastUpdate;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withArrival(Arrival arrival) {
            this.arrival = arrival;
            return this;
        }

        public Builder withDeparture(Departure departure) {
            this.departure = departure;
            return this;
        }

        public Builder withEndOfFishing(EndOfFishing endOfFishing) {
            this.endOfFishing = endOfFishing;
            return this;
        }

        public Builder withCatches(List<Catch> catches) {
            this.catches = catches;
            return this;
        }

        public Builder withCommunicationType(CommunicationType communicationtype) {
            this.communicationType = communicationtype;
            return this;
        }

        public Builder withLastUpdate(Date lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Logbook build() {
            Logbook logbook = new Logbook();
            logbook.arrival = this.arrival;
            logbook.endOfFishing = this.endOfFishing;
            logbook.departure = this.departure;
            logbook.communicationType = this.communicationType;
            logbook.catches = this.catches;
            logbook.lastUpdate = this.lastUpdate;
            logbook.enabled = this.enabled;
            logbook.setId(this.id);
            return logbook;
        }
    }
}



