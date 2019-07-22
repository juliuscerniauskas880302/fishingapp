package domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.base.BaseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import java.util.Objects;

@Entity
@NamedQueries(
        @NamedQuery(name = "catch.findAll", query = "SELECT c FROM Catch c")
)
public class Catch extends BaseEntity {
    private static final Logger LOG = LogManager.getLogger(Catch.class);

    private String variety;
    private Double weight;
    @Version
    private Integer version;

    public Catch() {
    }

    public Catch(String variety, Double weight) {
        this.variety = variety;
        this.weight = weight;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        ObjectMapper mapperObj = new ObjectMapper();
        String json = null;
        try {
            json = mapperObj.writeValueAsString(this);
        } catch (Exception e) {
            LOG.error("Error mapping Catch {} to json string.", this);
        }
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Catch aCatch = (Catch) o;
        return Objects.equals(getId(), aCatch.getId()) &&
                Objects.equals(weight, aCatch.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), variety, weight);
    }
}
