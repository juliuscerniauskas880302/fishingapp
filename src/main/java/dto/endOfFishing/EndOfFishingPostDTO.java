package dto.endOfFishing;

import java.util.Date;

public class EndOfFishingPostDTO {

    private Date date;

    public EndOfFishingPostDTO() {
    }

    public EndOfFishingPostDTO(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
