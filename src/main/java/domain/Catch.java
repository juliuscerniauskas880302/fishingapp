package domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.Objects;

@Entity
@NamedQueries(
        @NamedQuery(name = "catch.findAll", query = "SELECT c FROM Catch c")
)
public class Catch extends BaseEntity {
    private String variety;
    private Double weight;

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
        Catch aCatch = (Catch) o;
        return Objects.equals(variety, aCatch.variety) &&
                Objects.equals(weight, aCatch.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), variety, weight);
    }

    public static class Builder {
        private String variety;
        private Double weight;

        public Builder setVariety(String variety) {
            this.variety = variety;
            return this;
        }

        public Builder setWeight(Double weight) {
            this.weight = weight;
            return this;
        }

        public Catch build() {
            Catch aCatch = new Catch();
            aCatch.variety = this.variety;
            aCatch.weight = this.weight;
            return aCatch;
        }

    }

}
