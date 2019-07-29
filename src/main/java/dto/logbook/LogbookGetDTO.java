package dto.logbook;

import domain.Arrival;
import domain.Catch;
import domain.enums.CommunicationType;
import domain.Departure;
import domain.EndOfFishing;

import java.util.Date;
import java.util.List;

public class LogbookGetDTO {

    private String id;
    private Arrival arrival;
    private Departure departure;
    private EndOfFishing endOfFishing;
    private List<Catch> catches;
    private CommunicationType communicationType;
    private Integer version;
    private boolean enabled;
    private Date lastUpdate;

    public LogbookGetDTO() {
    }

    public LogbookGetDTO(String id, Arrival arrival, Departure departure,
                         EndOfFishing endOfFishing, List<Catch> catches,
                         CommunicationType communicationType, boolean enabled, Date lastUpdate) {
        this.id = id;
        this.arrival = arrival;
        this.departure = departure;
        this.endOfFishing = endOfFishing;
        this.catches = catches;
        this.communicationType = communicationType;
        this.enabled = enabled;
        this.lastUpdate = lastUpdate;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
