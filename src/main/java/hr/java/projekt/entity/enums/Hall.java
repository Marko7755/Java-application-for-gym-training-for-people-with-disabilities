package hr.java.projekt.entity.enums;

public enum Hall{

    HALL1(1, "Hall1"),
    HALL2(2, "Hall2"),
    HALL3(3, "Hall3"),
    HALL4(4, "Hall4"),
    HALL5(5, "Hall5"),
    HALL6(6, "Hall6"),
    HALL7(7, "Hall7");

    private Integer id;
    private String name;


    Hall( Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
