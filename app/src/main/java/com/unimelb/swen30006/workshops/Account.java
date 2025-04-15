package com.unimelb.swen30006.workshops;

import java.util.Date;

public class Account {
    private final long SIX_MONTH_MILLIS = 1000L * 60 * 60 * 24 * 30 * 6;
    private State state = null;
    private Card card = null;
    private Default defaultState = null;
    private boolean hasMissedPayments = false;
    private Date lastAccessed;
    private void onboard() {
        if (state == null) {
            state = State.PENDING;
            card = new Card();
        }
    }
    private void activate() {
        if (state == State.PENDING) {
            state = State.ACTIVE;
        }
        if (state == State.SUSPENDED && card.getFunds() > 0) {
            state = State.ACTIVE;
        }
        if (state == State.INACTIVE) {  // Contacted company
            state = State.ACTIVE;
        }
    }
    private void stolen() {
        if (state == State.ACTIVE) {
            state = State.PENDING;
        }
    }
    private void exceedCardLimit() {
        if (state == State.ACTIVE && card.getSpent() > card.getLimit()) {
            state = State.SUSPENDED;
        }
    }

    private void deactivate() {
        if (state == State.ACTIVE && card.getLastUsed().compareTo(new Date(System.currentTimeMillis() + SIX_MONTH_MILLIS)) > 0) {
            state = State.INACTIVE;
        }
    }

    private void close() {
        if (state == State.INACTIVE && lastAccessed.compareTo(new Date(System.currentTimeMillis() + SIX_MONTH_MILLIS)) > 0) {
            state = State.CLOSED;
        }
    }

    private void processDefault(String condition) {
        if (state == State.ACTIVE) {  // Fail to pay bill on time
            state = State.DEFAULT;
            defaultState = (hasMissedPayments)?Default.PAYMENT_PLAN:Default.GRACE_PERIOD;
        }
        if (state == State.DEFAULT && defaultState == Default.GRACE_PERIOD) {
            if (condition.equals("Fail to respond")) defaultState = Default.UNHEALTHY;
            if (condition.equals("Accept payment plan")) defaultState = Default.PAYMENT_PLAN;
        }
        if (state == State.SUSPENDED && defaultState == Default.UNHEALTHY) {
            // processCollections()
            state = State.CLOSED;
            defaultState = null;
        }
        if (state == State.DEFAULT && defaultState == Default.PAYMENT_PLAN) {
            if (condition.equals("Do not miss payment")) defaultState = Default.HEALTHY;
            if (condition.equals("Refuse or miss payment")) defaultState = Default.UNHEALTHY;
        }
        if (state == State.DEFAULT && defaultState == Default.HEALTHY) {  // Fail to continue repaying
            defaultState = Default.UNHEALTHY;
        }
    }
}
