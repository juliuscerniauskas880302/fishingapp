package dto.departure;

import java.util.Date;

public class DeparturePostDTO {

    private String port;
    private Date date;

    public DeparturePostDTO() {
    }

    public DeparturePostDTO(String port, Date date) {
        this.port = port;
        this.date = date;
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
