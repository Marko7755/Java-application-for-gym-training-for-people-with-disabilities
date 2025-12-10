package hr.java.projekt.entity.model;

import java.io.Serializable;

public abstract class NamedEntity implements Serializable {
    private Integer id;
    private String name;

    public NamedEntity(Integer id, String name){
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
