package dto.aCatch;

public class CatchGetDTO {

    private String id;
    private String variety;
    private Double weight;

    public CatchGetDTO() {
    }

    public CatchGetDTO(String id, String variety, Double weight) {
        this.id = id;
        this.variety = variety;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
