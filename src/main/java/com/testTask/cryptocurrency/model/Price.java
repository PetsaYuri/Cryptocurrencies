package com.testTask.cryptocurrency.model;

import javax.persistence.*;

@Entity
@Table(name = "price")
public class Price {

    public Price()  {}

    public Price(double btc, double eth, double xrp)  {
        this.btc = btc;
        this.eth = eth;
        this.xrp = xrp;
    }

    @Id
    private String id;

    @Column
    private double btc, eth, xrp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBtc() {
        return btc;
    }

    public void setBtc(double btc) {
        this.btc = btc;
    }

    public double getEth() {
        return eth;
    }

    public void setEth(double eth) {
        this.eth = eth;
    }

    public double getXrp() {
        return xrp;
    }

    public void setXrp(double xrp) {
        this.xrp = xrp;
    }
}
