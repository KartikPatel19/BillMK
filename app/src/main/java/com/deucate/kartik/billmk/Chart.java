package com.deucate.kartik.billmk;


public class Chart {

    int rs;
    String name;

    public Chart(int rs, String name) {
        this.rs = rs;
        this.name = name;
    }

    public int getRs() {
        return rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
