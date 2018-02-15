package com.expenso.aditya.expenso;

class History {
    private String resultText;
    private String resultFormat;
    private String resultTimeStamp;

    History(String resultText, String resultFormat, String resultTimeStamp) {
        this.resultText = resultText;
        this.resultFormat = resultFormat;
        this.resultTimeStamp = resultTimeStamp;
    }

    String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    String getResultFormat() {
        return resultFormat;
    }

    public void setResultFormat(String resultFormat) {
        this.resultFormat = resultFormat;
    }

    public String getResultTimeStamp() {
        return resultTimeStamp;
    }

    public void setResultTimeStamp(String resultTimeStamp) {
        this.resultTimeStamp = resultTimeStamp;
    }
}
