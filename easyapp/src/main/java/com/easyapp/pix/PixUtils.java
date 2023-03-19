package com.easyapp.pix;

public final class PixUtils {

    public static String createBrCode(Payload payload) throws PixException {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null.");
        }
        payload.validate();
        StringBuilder sb = new StringBuilder();
        //Payload Format
        sb.append(toEMV(BrIds.PAYLOAD_FORMAT_INDICATOR, "01"));
        //PointInitiationMethod
        if(payload.isDynamic()){
            sb.append(toEMV(BrIds.POINT_INITIATION_METHOD, payload.getPointMethod()));
        }
        //Merchant Account
        final String gui = toEMV(BrIds.MERCHANT_ACCOUNT_INFO_GUI, "br.gov.bcb.pix");
        final String key = toEMV(BrIds.MERCHANT_ACCOUNT_INFO_KEY, payload.getKey());
        final String url = toEMV(BrIds.MERCHANT_ACCOUNT_INFO_URL, payload.getUrl());
        sb.append(toEMV(
                      BrIds.MERCHANT_ACCOUNT_INFO, 
                      payload.isStatic() ?  (gui + key) : (gui + key + url)
                  ));
        //Merchant Category
        sb.append(toEMV(BrIds.MERCHANT_CATEGORY_CODE, payload.getCategory()));
        //Currency Obrigatorio
        sb.append(toEMV(BrIds.TRANSACTION_CURRENCY, payload.getCurrency()));
        //Ammount
        if (payload.hasAmount()) {
            sb.append(toEMV(BrIds.TRANSACTION_AMOUNT, payload.getAmount()));
        }
        //Country
        sb.append(toEMV(BrIds.COUNTRY_CODE, payload.getCountry()));
        //Merchant Name
        sb.append(toEMV(BrIds.MERCHANT_NAME, payload.getName()));
        //Merchant City
        sb.append(toEMV(BrIds.MERCHANT_CITY, payload.getCity()));
        //Additional Data
        String txid = toEMV(BrIds.ADDITIONAL_DATA_FIELD_TXID, payload.getTxid());
        sb.append(toEMV(BrIds.ADDITIONAL_DATA_FIELD, txid));
        //CRC1623
        sb.append(toEMV(BrIds.CRC16, sb + BrIds.CRC16.id + "04"));
        return sb.toString();
    }

    private static String toEMV(BrIds brIds, String value) {
        if (brIds == BrIds.CRC16) {
            final int polynomial = 0x1021;
            short crc = (short) 0xFFFF;
            for (int i = 0; i < value.length(); i++) {
                byte b = (byte) value.charAt(i);
                for (int j = 0; j < 8; j++) {
                    boolean bit = ((b >> (7 - j) & 1) == 1);
                    boolean c15 = (((crc >> 15) & 1) == 1);
                    crc <<= 1;
                    if (c15 ^ bit) {
                        crc ^= polynomial;
                    }
                }
            }
            value = String.format("%04X", (crc & 0xFFFF));
        }
        String length = String.format("%02d", (value == null) ? 0 : value.length());
        return brIds.id + length + value;
    }

}
