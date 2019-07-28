package dto.aCatch;

public class CatchPostDTO {

    private String variety;
    private Double weight;

    public CatchPostDTO() {
    }

    public CatchPostDTO(String variety, Double weight) {
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
}
