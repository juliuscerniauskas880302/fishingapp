package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Logbook {
    private Long id;
    private Arrival arrival;
    private Catch aCatch;
    private Departure departure;
    private EndFishing endFishing;

    public Logbook() {
    }

    public Logbook(Long id, Arrival arrival, Catch aCatch, Departure departure, EndFishing endFishing) {
        this.id = id;
        this.arrival = arrival;
        this.aCatch = aCatch;
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

    public Catch getaCatch() {
        return aCatch;
    }

    public void setaCatch(Catch aCatch) {
        this.aCatch = aCatch;
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
}
