package dto.logbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Arrival;
import domain.Catch;
import domain.CommunicationType;
import domain.Departure;
import domain.EndOfFishing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LogbookPostDTO {
    private static final Logger LOG = LogManager.getLogger(LogbookPostDTO.class);

    private Arrival arrival;
    private Departure departure;
    private EndOfFishing endOfFishing;
    private List<Catch> catches;
    private CommunicationType communicationType;

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

    public void addCatch(Catch aCatch) {
        if(catches == null) {
            catches = new ArrayList<>();
        }
        catches.add(aCatch);
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
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

    public static class Builder {
        private Arrival arrival;
        private Departure departure;
        private EndOfFishing endOfFishing;
        private List<Catch> catches;
        private CommunicationType communicationType;

        public Builder withArrival(Arrival arrival) {
            this.arrival = arrival;
            return this;
        }

        public Builder withDeparture(Departure departure) {
            this.departure = departure;
            return this;
        }

        public Builder withEndOfFishing(EndOfFishing eendOfFishing) {
            this.endOfFishing = eendOfFishing;
            return this;
        }

        public Builder withCatches(List<Catch> catches) {
            this.catches = catches;
            return this;
        }

        public Builder withCatch(Catch aCatch) {
            if(catches == null) {
                catches = new ArrayList<>();
            }
            catches.add(aCatch);
            return this;
        }

        public Builder withCommunicationType(CommunicationType communicationType) {
            this.communicationType = communicationType;
            return this;
        }

        public LogbookPostDTO build() {
            LogbookPostDTO dto = new LogbookPostDTO();
            dto.arrival = this.arrival;
            dto.departure = this.departure;
            dto.endOfFishing = this.endOfFishing;
            dto.catches = this.catches;
            dto.communicationType = this.communicationType;
            return dto;
        }
    }
}
