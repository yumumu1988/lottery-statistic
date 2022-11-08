package com.yumumu.lotterystatistic.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yumumu.lotterystatistic.dao.domain.LotteryNum;
import com.yumumu.lotterystatistic.dao.service.LotteryNumService;
import com.yumumu.lotterystatistic.model.*;
import com.yumumu.lotterystatistic.util.BitNumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * @author zhanghailin
 * @date 2022/10/28
 */
@Service
@Slf4j
public class BingoService {

    @Resource
    private LotteryNumService lotteryNumService;

    @Resource
    private AnalysisService analysisService;

    @Value("${maxSameNum.threshold:3}")
    private Integer threshold;

//    public List<BingoCodeMixBo> bangMix(Integer size, Integer loop) {
//        Function<NumCollectionBo, List<BingoCodeMixBo>> function = new Function<NumCollectionBo, List<BingoCodeMixBo>>() {
//            @Override
//            public List<BingoCodeMixBo> apply(NumCollectionBo numCollectionBo) {
//                List<BingoCodeMixBo> result = new ArrayList<>();
//                List<Integer> redNumList = numCollectionBo.getRedNumList();
//                List<BlueNumBo> blueNumList = numCollectionBo.getBlueNumList();
//                List<Integer> blueList = new ArrayList<>();
//                blueNumList.forEach(b-> blueList.add(b.getBlue()));
//                result.add(BingoCodeMixBo.builder()
//                        .reds(listToString(redNumList))
//                        .blues(listToString(blueList)).build());
//                return result;
//            }
//        };
//
//
//    }

    private String listToString(List<Integer> list) {
        return JSONArray.toJSONString(list).replace("[", "").replace("]","");
    }


    public List<BingoCodeBo> bang(Integer size, Integer loop) {
        List<BingoCodeBo> bingoNums = new ArrayList<>();
        //  step0：获取blue数组
        List<BlueNumBo> blueNums = analysisService.getBlueNums(1, 1, 1);
        //  step1：统计前N期数据
        LambdaQueryWrapper<LotteryNum> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(LotteryNum::getId).last(String.format(" limit %s", loop));
        List<LotteryNum> lotteryNumList = lotteryNumService.list(wrapper);
        AnalysisDataBo analysisDataBo = analysisService.analysisHistoryData(lotteryNumList);
        //  step2：找出高频的数字
        List<Integer> hotNums = analysisDataBo.getHotNums(threshold);
        //  step3：找出低频的数组
        List<Integer> coldNums = analysisDataBo.getColdNums(threshold);
        //  step4：生产随机数
        //  step5：和往期数据对比，去除高相似度数据(>=3)
        for (int i = 0; i < size - threshold; i++) {
            List<Integer> nums = getBingoNums(hotNums);
            Long bitNum = BitNumUtils.convertToBitNum(nums);
            boolean passNums = false;
            for (LotteryNum lotteryNum : lotteryNumList) {
                SameNumBo sameNumBo = BitNumUtils.compareSameSize(bitNum, lotteryNum.getBitNum());
                if (sameNumBo.getSameSize() >= threshold) {
                    break;
                }
                passNums = true;
            }
            if (passNums) {
                blueNums.forEach(blue->{
                    List<Integer> numsList = new ArrayList<>(nums);
                    Collections.sort(numsList);
                    numsList.add(blue.getBlue());
                    bingoNums.add(BingoCodeBo.builder().hotCode(true).numList(numsList).code(JSONArray.toJSONString(numsList)).build());
                });
            } else {
                i--;
            }
        }

        for (int i = 0; i < threshold; i++) {
            List<Integer> nums = getBingoNums(hotNums, coldNums);
            Long bitNum = BitNumUtils.convertToBitNum(nums);
            boolean passNums = false;
            for (LotteryNum lotteryNum : lotteryNumList) {
                SameNumBo sameNumBo = BitNumUtils.compareSameSize(bitNum, lotteryNum.getBitNum());
                if (sameNumBo.getSameSize() >= threshold) {
                    break;
                }
                passNums = true;
            }
            if (passNums) {
                blueNums.forEach(blue->{
                    List<Integer> numsList = new ArrayList<>(nums);
                    Collections.sort(numsList);
                    numsList.add(blue.getBlue());
                    bingoNums.add(BingoCodeBo.builder().hotCode(false).numList(numsList).code(JSONArray.toJSONString(numsList)).build());
                });
            } else {
                i--;
            }
        }

        //  step6：返回数据集合
        return bingoNums;
    }

    private List<Integer> getBingoNums(List<Integer> hotNums, List<Integer> coldNumList) {
        List<Integer> coldNums = new ArrayList<>(coldNumList);
        int coldNumsSize = coldNums.size();
        List<Integer> numList = new ArrayList<>();
        while (numList.size() != 6) {
            int num = new Random().nextInt(34);
            if (num == 0) {
                continue;
            }
            if (numList.contains(num)) {
                continue;
            }
            if (!hotNums.contains(num)) {
                numList.add(0, num);
            }
            if (coldNums.contains(num)) {
                coldNums.remove(new Integer(num));
            }
            if (numList.size() >= 5 && coldNums.size() == coldNumsSize) {
                numList.remove(0);
            }
        }
        return numList;
    }

    private List<Integer> getBingoNums(List<Integer> hotNums) {
        List<Integer> numList = new ArrayList<>();
        while (numList.size() != 6) {
            int num = new Random().nextInt(34);
            if (num == 0) {
                continue;
            }
            if (numList.contains(num)) {
                continue;
            }
            if (!hotNums.contains(num)) {
                numList.add(num);
            }
        }
        return numList;
    }


}
