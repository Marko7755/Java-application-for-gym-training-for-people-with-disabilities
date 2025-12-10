package hr.java.projekt.entity.enums;

public enum ConditionsFulfilled {

    YES("Conditions are fulfilled!"),
    NO("Conditions are not fulfilled!");
    private String description;

     ConditionsFulfilled(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
