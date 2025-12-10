package hr.java.projekt.entity.model;


public record Day(Integer dayId, String dayName) {
    public Integer getDayId() {
        return dayId;
    }
    public String getDayName() {
        return dayName;
    }

    @Override
    public String toString() {
        return dayName;
    }
}


