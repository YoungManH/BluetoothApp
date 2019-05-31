package com.example.bluetoothapp;

public class PublicMethod {
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString){
        if (hexString == null || hexString.equals("")){
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length()/2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i =0;i<length;i++){
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos+1]));
        }

        return d;
    }
    public static String getBit(byte by){
        StringBuffer sb = new StringBuffer();
        sb.append((by>>7)&0x1)
                .append((by>>6)&0x1)
                .append((by>>5)&0x1)
                .append((by>>4)&0x1)
                .append((by>>3)&0x1)
                .append((by>>2)&0x1)
                .append((by>>1)&0x1)
                .append((by>>0)&0x1);
        return sb.toString();
    }
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    public static int hexStringToInt(String str){
        return Integer.parseInt(str, 16);
    }
}
