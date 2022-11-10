package com.yumumu.lotterystatistic.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yumumu.lotterystatistic.dao.domain.CurrentNum;
import com.yumumu.lotterystatistic.dao.domain.HistoryNum;
import com.yumumu.lotterystatistic.dao.domain.LotteryNum;
import com.yumumu.lotterystatistic.dao.service.CurrentNumService;
import com.yumumu.lotterystatistic.dao.service.HistoryNumService;
import com.yumumu.lotterystatistic.dao.service.LotteryNumService;
import com.yumumu.lotterystatistic.model.*;
import com.yumumu.lotterystatistic.util.BitNumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

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
    private CurrentNumService currentNumService;

    @Resource
    private HistoryNumService historyNumService;

    @Resource
    private AnalysisService analysisService;

    @Value("${maxSameNum.threshold:3}")
    private Integer threshold;

    public List<BingoCodeMixBo> bangMix(Integer size, Integer loop) {
        List<BingoCodeMixBo> result = new ArrayList<>();
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
                List<Integer> blueList = new ArrayList<>();
                blueNums.forEach(blue->{
                    blueList.add(blue.getBlue());
                });
                Collections.sort(nums);
                result.add(BingoCodeMixBo.builder().reds(listToString(nums)).blues(listToString(blueList)).build());
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
                List<Integer> blueList = new ArrayList<>();
                blueNums.forEach(blue->{
                    blueList.add(blue.getBlue());
                });
                Collections.sort(nums);
                result.add(BingoCodeMixBo.builder().reds(listToString(nums)).blues(listToString(blueList)).build());
            } else {
                i--;
            }
        }

        return result;
    }

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
                    bingoNums.add(BingoCodeBo.builder().hotCode(true).numList(numsList).code(listToString(numsList)).build());
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
                    bingoNums.add(BingoCodeBo.builder().hotCode(false).numList(numsList).code(listToString(numsList)).build());
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


    public List<BingoCodeMixBo> updateBang(List<String> numList, Integer loop) {
        List<BingoCodeMixBo> result = new ArrayList<>(numList.size());
        //  历史同期
        List<LotteryNum> lotteryNumList = lotteryNumService.list();
        LambdaQueryWrapper<LotteryNum> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(LotteryNum::getId).last("limit 3");
        List<LotteryNum> recentlyLotteryNumList = lotteryNumService.list(queryWrapper);
        List<BlueNumBo> blueNums = analysisService.getBlueNums(1, 1, 1);
        List<String> blueStrList = new ArrayList<>();
        blueNums.forEach(b->{
            blueStrList.add(b.getBlue().toString());
        });
        numList.forEach(e->{
             String code = checkAndUpdateBandNum(e, lotteryNumList, loop, recentlyLotteryNumList);
             result.add(BingoCodeMixBo.builder().reds(code).blues(String.join(",", blueStrList)).build());
        });
        return result;
    }

    private String checkAndUpdateBandNum(String code, List<LotteryNum> lotteryNumList, Integer loop, List<LotteryNum> recentlyLotteryNumList) {
        Long bitNum = BitNumUtils.convertToBitNum(code);
        //  往期数据比较
        while (similarCode(bitNum, lotteryNumList, 3)) {
            List<BingoCodeBo> bingoCodeBos = bang(threshold * 2, loop);
            BingoCodeBo bingoCodeBo = bingoCodeBos.get(new Random().nextInt(bingoCodeBos.size()));
            bingoCodeBo.getNumList().remove(bingoCodeBo.getNumList().size() - 1);
            code = listToString(bingoCodeBo.getNumList());
            bitNum = BitNumUtils.convertToBitNum(code);
        }
        //  近期数据比较
        while (similarCode(bitNum, recentlyLotteryNumList, 2)) {
            List<BingoCodeBo> bingoCodeBos = bang(threshold * 2, loop);
            BingoCodeBo bingoCodeBo = bingoCodeBos.get(new Random().nextInt(bingoCodeBos.size()));
            bingoCodeBo.getNumList().remove(bingoCodeBo.getNumList().size() - 1);
            code = listToString(bingoCodeBo.getNumList());
            bitNum = BitNumUtils.convertToBitNum(code);
        }
        return code;
    }

    private boolean similarCode(Long bitNum, List<LotteryNum> lotteryNumList, int maxSameSize) {
        for (LotteryNum lotteryNum : lotteryNumList) {
            if (BitNumUtils.compareSameSize(bitNum, lotteryNum.getBitNum()).getSameSize() > maxSameSize) {
                return true;
            }
        }
        return false;
    }

    public boolean record(List<BingoCodeMixBo> list) {
        if (currentNumService.list().size() > 0) {
            return false;
        }
        List<CurrentNum> currentNumList = new ArrayList<>();
        list.forEach(e->{
            CurrentNum currentNum = new CurrentNum();
            currentNum.setReds(e.getReds());
            currentNum.setBlues(e.getBlues());
            currentNumList.add(currentNum);
        });
        currentNumService.saveBatch(currentNumList);
        return true;
    }

    public List<BingoCodeMixBo> currentNum() {
        List<CurrentNum> list = currentNumService.list();
        List<BingoCodeMixBo> result = new ArrayList<>();
        list.forEach(e->{
            result.add(BingoCodeMixBo.builder().reds(e.getReds()).blues(e.getBlues()).build());
        });
        return result;
    }

    public List<BingoCodeMixBo> updateCrtNum(Integer loop) {
        HistoryNum one = historyNumService.getOne(Wrappers.lambdaQuery(HistoryNum.class).orderByDesc(HistoryNum::getId).last(" limit 1"));
        if (Objects.isNull(one)) {
            return bangMix(10, loop);
        } else {
            List<HistoryNum> historyNumList = historyNumService.list(Wrappers.lambdaQuery(HistoryNum.class).eq(HistoryNum::getSn, one.getSn()));
            List<String> codeList = new ArrayList<>();
            historyNumList.forEach(e->{
                codeList.add(String.format("%s,%s", e.getReds(), e.getBlue()));
            });
            return updateBang(codeList, loop);
        }
    }
}
