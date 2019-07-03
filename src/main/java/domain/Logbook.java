package domain;

import enums.CommunicationType;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@Entity
public class Logbook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;
    @OneToOne
    private Arrival arrival;
    @OneToMany
    private List<Catch> catches = new ArrayList<>();
    @OneToOne
    private Departure departure;
    @OneToOne
    private EndFishing endFishing;

    @Transient
    @Enumerated(EnumType.STRING)
    private CommunicationType communicationType;

    public Logbook() {
    }

    public Logbook(Arrival arrival, List<Catch> catches, Departure departure, EndFishing endFishing, String communicationType) {
        this.arrival = arrival;
        this.catches = catches;
        this.departure = departure;
        this.endFishing = endFishing;
        this.communicationType = CommunicationType.valueOf(communicationType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    public List<Catch> getCatches() {
        return catches;
    }

    public void setCatches(List<Catch> catches) {
        this.catches = catches;
    }

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    public EndFishing getEndFishing() {
        return endFishing;
    }

    public void setEndFishing(EndFishing endFishing) {
        this.endFishing = endFishing;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    public JsonObject toJson() {

        JsonArrayBuilder catchList = Json.createArrayBuilder();
        if (catches != null) {
            catches.stream().forEach(c -> catchList.add(c.toJson()));
        } else {
            catches = new ArrayList<>();
        }

        return Json.createObjectBuilder()
                .add("LogBook", Json.createObjectBuilder()
                        .add("communication", this.communicationType.toString())
                        .add("departure", departure != null ? departure.toJson() : new Departure().toJson())
                        .add("arrival", arrival != null ? arrival.toJson() : new Arrival().toJson())
                        .add("catches", catchList)
                        .add("endOfFishing", endFishing != null ? endFishing.toJson() : new EndFishing().toJson())
                        .build()).build();
    }
}
