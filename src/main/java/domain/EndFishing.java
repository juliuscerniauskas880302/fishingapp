package domain;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;
import java.time.LocalDate;

@Entity
public class EndFishing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;
    private LocalDate date;

    public EndFishing() {
    }

    public EndFishing(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("date", this.date != null ? this.date.toString() : "")
                .build();
    }
}
