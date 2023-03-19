package com.easyapp.pix;

public enum BrIds {
    PAYLOAD_FORMAT_INDICATOR("00"),
    POINT_INITIATION_METHOD("01"),
    MERCHANT_ACCOUNT_INFO("26"),
    MERCHANT_ACCOUNT_INFO_GUI("00"),
    MERCHANT_ACCOUNT_INFO_KEY("01"),
    MERCHANT_ACCOUNT_INFO_URL("25"),
    MERCHANT_CATEGORY_CODE("52"),
    TRANSACTION_CURRENCY("53"),
    TRANSACTION_AMOUNT("54"),
    COUNTRY_CODE("58"),
    MERCHANT_NAME("59"),
    MERCHANT_CITY("60"),
    ADDITIONAL_DATA_FIELD("62"),
    ADDITIONAL_DATA_FIELD_TXID("05"),
    CRC16("63");

    public final String id;

    BrIds(String id){
        this.id = id;
    }

}
