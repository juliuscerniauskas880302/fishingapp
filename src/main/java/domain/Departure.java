package domain;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@XmlRootElement
public class Departure {
    @Id
    private Long id;
    private String port;
    private LocalDate date;

    public Departure() {
    }

    public Departure(Long id, String port, LocalDate date) {
        this.id = id;
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
}
