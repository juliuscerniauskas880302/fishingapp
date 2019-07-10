package domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.base.BaseEntity;
import enums.CommunicationType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQueries(
        @NamedQuery(name = "logbook.findAll", query = "select l from Logbook l")
)
public class Logbook extends BaseEntity {
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

    public Logbook() {
    }

    public Logbook(Arrival arrival, Departure departure, EndOfFishing endOfFishing, List<Catch> catches, String communicationType) {
        this.arrival = arrival;
        this.departure = departure;
        this.endOfFishing = endOfFishing;
        this.catches = catches;
        this.communicationType = CommunicationType.valueOf(communicationType);
    }

    public Logbook build() {
        Logbook logbook = new Logbook();
        logbook.arrival = this.arrival;
        logbook.endOfFishing = this.endOfFishing;
        logbook.departure = this.departure;
        logbook.communicationType = this.getCommunicationType();
        logbook.catches = this.catches;
        return logbook;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public Logbook setArrival(Arrival arrival) {
        this.arrival = arrival;
        return this;
    }

    public Departure getDeparture() {
        return departure;
    }

    public Logbook setDeparture(Departure departure) {
        this.departure = departure;
        return this;
    }

    public EndOfFishing getEndOfFishing() {
        return endOfFishing;
    }

    public Logbook setEndOfFishing(EndOfFishing endOfFishing) {
        this.endOfFishing = endOfFishing;
        return this;
    }

    public List<Catch> getCatches() {
        return catches;
    }

    public Logbook setCatches(List<Catch> catches) {
        this.catches = catches;
        return this;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public Logbook setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
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
}
