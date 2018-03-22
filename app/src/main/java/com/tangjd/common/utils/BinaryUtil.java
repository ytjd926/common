package com.tangjd.common.utils;

/**
 * Author: Administrator
 * Date: 2018/3/22
 */

public class BinaryUtil {
    /**
     * 计算byte[]的int型数值
     * 如{0xFF, 0xFF, 0xFF, 0xFF}
     * 11111111 11111111 11111111 11111111
     * 其中第一位为符号位：1 = 负数（负数用补码表示），0 = 正数
     */
    public static int calculate(byte[] data) {
        int result;
        String binary = BinaryUtil.hexString2BinaryString(ByteUtil.bytesToHexStringWithoutSpace(data));
        if (binary.startsWith("1")) {
            StringBuilder sb = new StringBuilder("0");
            for (int i = 1; i < binary.length(); i++) {
                if (binary.charAt(i) == '1') {
                    sb.append("0");
                } else {
                    sb.append("1");
                }
            }
            result = (BinaryUtil.binaryString2Int(sb.toString()) + 1) * -1;
        } else {
            result = BinaryUtil.binaryString2Int(binary);
        }
        return result;
    }

    /**
     * 二进制String转十六进制String
     *
     * @param binaryString
     * @return
     */
    public static String binaryString2HexString(String binaryString) {
        if (binaryString == null || binaryString.equals("") || binaryString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < binaryString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(binaryString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }


    /**
     * 十六进制String转二进制String
     */
    public static String hexString2BinaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * 十六进制String转二进制String
     */
    public static String bytes2BinaryString(byte[] bytes) {
        String hexString = ByteUtil.bytesToHexStringWithoutSpace(bytes);
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * int型数据转十六进制String
     */
    static String int2BinaryString(int integer) {
        String str = "";
        while (integer != 0) {
            str = integer % 2 + str;
            integer = integer / 2;
        }
        str = "00000000000000000000000000000000" + str;
        return str.substring(str.length() - 32);
    }

    public static int binaryString2Int(String binary) throws NumberFormatException {
        return Integer.parseInt(binary, 2);
    }

    /**
     * 返回8位二进制1111 1111
     */
    public static String byte2BinaryString(byte singleByte) {
        return Integer.toBinaryString(singleByte & 0xFF);
    }
}
