package com.funcar.enums;

public enum CSVHeader {

    CODE("code"),
    MAKE_MODEL("make/model"),
    POWER_IN_PS("power-in-ps"),
    YEAR("year"),
    COLOR("color"),
    PRICE("price");


    private String headerName;

    CSVHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return this.headerName;
    }
}
