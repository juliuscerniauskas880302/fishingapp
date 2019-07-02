package domain;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Logbook {
    private Long id;
    private Arrival arrival;
    private List<Catch> catches = new ArrayList<>();
    private Departure departure;
    private EndFishing endFishing;

    public Logbook() {
    }

    public Logbook(Arrival arrival, List<Catch> catches, Departure departure, EndFishing endFishing) {
        this.arrival = arrival;
        this.catches = catches;
        this.departure = departure;
        this.endFishing = endFishing;
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

    public JsonObject toJson() {

        JsonArrayBuilder catchList = Json.createArrayBuilder();
        if (catches != null) {
            catches.stream().forEach(c -> catchList.add(c.toJson()));
        } else {
            catches = new ArrayList<>();
        }

        return Json.createObjectBuilder()
                .add("LogBook", Json.createObjectBuilder()
                        .add("departure", departure != null ? departure.toJson() : new Departure().toJson())
                        .add("arrival", arrival != null ? arrival.toJson() : new Arrival().toJson())
                        .add("catches", catchList)
                        .add("endOfFishing", endFishing != null ? endFishing.toJson() : new EndFishing().toJson())
                        .build()).build();
    }
}
