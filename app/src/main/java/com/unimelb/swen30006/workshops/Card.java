package com.unimelb.swen30006.workshops;

import java.util.Date;

public class Card {
    private double spent;
    private double limit;
    private Date lastUsed;
    Card() {
        this.spent = 0;
        this.limit = 1000;
    }

    public double getSpent() {
        return spent;
    }
    public double getLimit() {
        return limit;
    }
    public double getFunds() {
        return limit - spent;
    }
    public Date getLastUsed() {
        return lastUsed;
    }
}
