package com.yumumu.lotterystatistic.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yumumu.lotterystatistic.dao.domain.LotteryNum;
import com.yumumu.lotterystatistic.dao.service.LotteryNumService;
import com.yumumu.lotterystatistic.model.AnalysisDataBo;
import com.yumumu.lotterystatistic.model.SameNumBo;
import com.yumumu.lotterystatistic.util.BitNumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    public List<String> bang(Integer size, Integer loop) {
        List<String> bingoNums = new ArrayList<>();
        //  step1：统计前N期数据
        LambdaQueryWrapper<LotteryNum> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(LotteryNum::getId).last(String.format(" limit %s", loop));
        List<LotteryNum> lotteryNumList = lotteryNumService.list(wrapper);
        AnalysisDataBo analysisDataBo = analysisService.analysisHistoryData(lotteryNumList);
        System.out.println(analysisDataBo.toString());
        analysisDataBo.output();
        //  step2：找出高频的数字
        List<Integer> hotNums = analysisDataBo.getHotNums(3);
        //  step3：找出低频的数组
        List<Integer> coldNums = analysisDataBo.getColdNums(3);
        //  step4：生产随机数
        //  step5：和往期数据对比，去除高相似度数据(>=3)
        for (int i = 0; i < size - 3; i++) {
            List<Integer> nums = getBingoNums(hotNums);
            Long bitNum = BitNumUtils.convertToBitNum(nums);
            boolean passNums = false;
            for (LotteryNum lotteryNum : lotteryNumList) {
                SameNumBo sameNumBo = BitNumUtils.compareSameSize(bitNum, lotteryNum.getBitNum());
                if (sameNumBo.getSameSize() >= 3) {
                    break;
                }
                passNums = true;
            }
            if (passNums) {
                bingoNums.add(JSONArray.toJSONString(nums));
            } else {
                i--;
            }
        }

        for (int i = 0; i < 3; i++) {
            List<Integer> nums = getBingoNums(hotNums, coldNums);
            Long bitNum = BitNumUtils.convertToBitNum(nums);
            boolean passNums = false;
            for (LotteryNum lotteryNum : lotteryNumList) {
                SameNumBo sameNumBo = BitNumUtils.compareSameSize(bitNum, lotteryNum.getBitNum());
                if (sameNumBo.getSameSize() >= 3) {
                    break;
                }
                passNums = true;
            }
            if (passNums) {
                bingoNums.add(JSONArray.toJSONString(nums));
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
