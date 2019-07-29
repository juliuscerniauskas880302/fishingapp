package domain.enums;

public enum Interval {
    YEAR("year"),
    QUATER("quater"),
    MONTH("month"),
    DAY_OF_YEAR("dayofyear"),
    DAY("day"),
    WEEK("week"),
    WEEK_DAY("weekday");

    private String name;

    Interval(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
