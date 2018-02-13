package com.expenso.aditya.expenso;

class Expense {
    private String description;
    private String type;
    private int amount;
    private String date;
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
