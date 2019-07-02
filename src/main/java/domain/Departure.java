package domain;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.time.LocalDate;

@Entity
public class Departure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;
    @NotNull
    private String port;
    @NotNull
    private LocalDate date;

    public Departure() {
    }

    public Departure(String port, LocalDate date) {
        this.port = port;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("port", this.port != null ? this.port : "")
                .add("date", this.date != null ? this.date.toString() : "")
                .build();
    }
}
