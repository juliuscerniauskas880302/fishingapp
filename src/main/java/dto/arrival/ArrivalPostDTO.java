package dto.arrival;

import java.util.Date;

public class ArrivalPostDTO {

    private String port;
    private Date date;

    public ArrivalPostDTO(String port, Date date) {
        this.port = port;
        this.date = date;
    }

    public ArrivalPostDTO() {
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
