package com.tangjd.common.bluetooth;

/**
 * Created by Tangjd on 2015/7/7
 */
public class ByteUtil {
    /**
     * Convert byte[] to hex string.将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param buffer byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] buffer) {
        if (buffer == null || buffer.length <= 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < buffer.length; i++) {
            int value = buffer[i] & 0xFF;
            String hexStr = Integer.toHexString(value);
            if (hexStr.length() < 2) {
                builder.append(0);
            }
            builder.append(hexStr);
            if (i != buffer.length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
