package com.expenso.aditya.expenso;

class Expense {
    private String description;
    private String type;
    private int amount;
    private String date;

    Expense (String description, String type, String date, int amount) {
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    int getAmount() {
        return amount;
    }

    String getDate() {
        return date;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setType(String type) {
        this.type = type;
    }

    void setAmount(int amount) {
        this.amount = amount;
    }

    void setDate(String date) {
        this.date = date;
    }
}
