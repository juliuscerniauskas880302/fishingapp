package dto.logbook;

import domain.Arrival;
import domain.Catch;
import domain.CommunicationType;
import domain.Departure;
import domain.EndOfFishing;

import java.util.List;

public class LogbookGetDTO {

    private String id;
    private Arrival arrival;
    private Departure departure;
    private EndOfFishing endOfFishing;
    private List<Catch> catches;
    private CommunicationType communicationType;

    public LogbookGetDTO() {
    }

    public LogbookGetDTO(String id, Arrival arrival, Departure departure,
                         EndOfFishing endOfFishing, List<Catch> catches, CommunicationType communicationType) {
        this.id = id;
        this.arrival = arrival;
        this.departure = departure;
        this.endOfFishing = endOfFishing;
        this.catches = catches;
        this.communicationType = communicationType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
