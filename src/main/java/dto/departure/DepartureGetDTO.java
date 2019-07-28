package dto.departure;

import java.util.Date;

public class DepartureGetDTO {

    private String id;
    private String port;
    private Date date;

    public DepartureGetDTO() {
    }

    public DepartureGetDTO(String id, String port, Date date) {
        this.id = id;
        this.port = port;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
