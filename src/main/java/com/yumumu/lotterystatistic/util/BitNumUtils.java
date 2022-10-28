package com.yumumu.lotterystatistic.util;

import com.yumumu.lotterystatistic.model.SameNumBo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanghailin
 * @date 2022/10/28
 */
public class BitNumUtils {

    public static Long convertToBitNum(String code) {
        String[] split = code.split(",");
        Double value = (double) 0;
        for (String item : split) {
            value += power(Integer.valueOf(item));
        }
        return value.longValue();
    }

    private static double power(Integer num) {
        return Math.pow(2, num - 1);
    }

    public static Long convertToBitNum(List<Integer> numList) {
        Double value = (double) 0;
        for (Integer num : numList) {
            value += power(num);
        }
        return value.longValue();
    }

    public static SameNumBo compareSameSize(Long bitNum1, Long bitNum2) {
        char[] chars = Long.toBinaryString(bitNum1 & bitNum2).toCharArray();
        int length = chars.length;
        List<String> sameNumList = new ArrayList<>();
        for (int i = length - 1; i >= 0; i--) {
            if (chars[i] == '1') {
                sameNumList.add(String.valueOf(length - i));
            }
        }
        String sameNums = "";
        if (!CollectionUtils.isEmpty(sameNumList)) {
            sameNums = String.join(",", sameNumList);
        }
        return SameNumBo.builder().sameSize(sameNumList.size()).sameNums(sameNums).build();
    }
//
//    04,10,18,19,25,27
//            03,17,21,23,27,28
//            05,06,08,21,31,33
//            04,09,11,17,26,27

    public static void main(String[] args) {
//        167779840
//        35389704
//        2285896257
//        16781928
//        1610694696
//        System.out.println(compareSameSize(167779840l,35389704l));
//        Long aLong = convertToBitNum("04,10,18,19,25,27");
//        Long aLong1 = convertToBitNum("03,17,21,23,27,28");
//        Long aLong2 = convertToBitNum("05,06,08,21,31,33");
//        Long aLong3 = convertToBitNum("04,10,11,17,25,27");
//        System.out.println(aLong & aLong1);
//        System.out.println("  " + Long.toBinaryString(aLong & aLong1));
//        System.out.println("    " + Long.toBinaryString(aLong & aLong1).replace("0", "").length());
//        System.out.println(aLong & aLong2);
//        System.out.println("  " + Long.toBinaryString(aLong & aLong2));
//        System.out.println("    " + Long.toBinaryString(aLong & aLong2).replace("0", "").length());
//        System.out.println(aLong & aLong3);
//        System.out.println("  " + Long.toBinaryString(aLong & aLong3));
//        System.out.println("    " + Long.toBinaryString(aLong & aLong3).replace("0", "").length());
//        System.out.println(aLong);
//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(0);
//        list.add(2);
//        System.out.println(list);
//        list.remove(new Integer(0));
//        System.out.println(list);
        Long aLong = convertToBitNum("10,13,16,20,21,25");
        Long aLong1 = convertToBitNum("21,12,7,8,15,23");
        Long aLong2 = convertToBitNum("3,2,26,33,10,29");
        Long aLong3 = convertToBitNum("2,12,22,1,25,6");
        Long aLong4 = convertToBitNum("21,24,31,3,12,30");
        Long aLong5 = convertToBitNum("8,0,22,31,33,2");
        Long aLong6 = convertToBitNum("29,16,6,23,20,8");
        Long aLong7 = convertToBitNum("11,10,15,22,9,12");
        Long aLong8 = convertToBitNum("33,10,0,8,16,32");
        Long aLong9 = convertToBitNum("5,23,9,22,19,30");
        Long aLong10 = convertToBitNum("25,9,23,16,12,30");
        System.out.println(compareSameSize(aLong, aLong1).getSameSize());
        System.out.println(compareSameSize(aLong, aLong2).getSameSize());
        System.out.println(compareSameSize(aLong, aLong3).getSameSize());
        System.out.println(compareSameSize(aLong, aLong4).getSameSize());
        System.out.println(compareSameSize(aLong, aLong5).getSameSize());
        System.out.println(compareSameSize(aLong, aLong6).getSameSize());
        System.out.println(compareSameSize(aLong, aLong7).getSameSize());
        System.out.println(compareSameSize(aLong, aLong8).getSameSize());
        System.out.println(compareSameSize(aLong, aLong9).getSameSize());
        System.out.println(compareSameSize(aLong, aLong10).getSameSize());
    }

}
