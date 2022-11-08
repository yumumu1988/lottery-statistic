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

    public static Integer bingoLevel(String code1, String code2) {
        List<Integer> code1List = new ArrayList<>();
        List<Integer> code2List = new ArrayList<>();
        for (String item : code1.split(",")) {
            code1List.add(Integer.valueOf(item));
        }
        for (String item : code2.split(",")) {
            code2List.add(Integer.valueOf(item));
        }
        return bingoLevel(code1List, code2List);
    }

    public static Integer bingoLevel(List<Integer> numList1, List<Integer> numList2) {
        Integer blue1 = numList1.remove(numList1.size() - 1);
        Integer blue2 = numList2.remove(numList2.size() - 1);
        Long bitNum1 = convertToBitNum(numList1);
        Long bitNum2 = convertToBitNum(numList2);
        SameNumBo sameNumBo = compareSameSize(bitNum1, bitNum2);
//    一等奖（6+1）中奖概率为：红球33选6乘以蓝球16选1=1/17721088=0.0000056%；
//    二等奖（6+0）中奖概率为：红球33选6乘以蓝球16选0=15/17721088=0.0000846%；
//    三等奖（5+1）中奖概率为：红球33选5乘以蓝球16选1=162/17721088=0.000914%；
//    四等奖（5+0、4+1）中奖概率为：红球33选5乘以蓝球16选0=7695/17721088=0.0434%；
//    五等奖（4+0、3+1）中奖概率为：红球33选4乘以蓝球16选0=137475/17721088=0.7758%；
//    六等奖（2+1、1+1、0+1）中奖概率为：红球33选2乘以蓝球16选1=1043640/17721088=5.889%；
        boolean blue = false;
        if (blue1.equals(blue2)) {
            blue = true;
        }
        switch (sameNumBo.getSameSize()) {
            case 0:
            case 1:
            case 2:
                if (blue) {
                    return 6;
                }
                return -1;
            case 3:
                if (blue) {
                    return 5;
                }
                return -1;
            case 4:
                if (blue) {
                    return 4;
                }
                return 5;
            case 5:
                if (blue) {
                    return 3;
                }
                return 4;
            case 6:
                if (blue) {
                    return 1;
                }
                return 2;
        }
        return -1;
    }
}
