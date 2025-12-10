package hr.java.projekt.entity.model;


public class Competition<T1 extends Address, T2>{

    private T1 address;
    private T2 date;

    private String time;
    public Competition(T1 address, T2 date, String time){
        this.address = address;
        this.date = date;
        this.time = time;
    }

    public T1 getAddress() {
        return address;
    }

    public void setAddress(T1 address) {
        this.address = address;
    }

    public T2 getDate() {
        return date;
    }

    public void setDate(T2 date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
