package com.yumumu.lotterystatistic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yumumu.lotterystatistic.dao.domain.CurrentNum;
import com.yumumu.lotterystatistic.dao.domain.HistoryNum;
import com.yumumu.lotterystatistic.dao.domain.LotteryNum;
import com.yumumu.lotterystatistic.dao.service.CurrentNumService;
import com.yumumu.lotterystatistic.dao.service.HistoryNumService;
import com.yumumu.lotterystatistic.dao.service.LotteryNumService;
import com.yumumu.lotterystatistic.model.AnalysisDataBo;
import com.yumumu.lotterystatistic.model.BingoCodeBo;
import com.yumumu.lotterystatistic.model.BlueNumBo;
import com.yumumu.lotterystatistic.model.CheckNumBo;
import com.yumumu.lotterystatistic.util.BitNumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanghailin
 * @date 2022/10/28
 */
@Service
@Slf4j
public class AnalysisService {

    @Resource
    private LotteryNumService lotteryNumService;

    @Resource
    private CurrentNumService currentNumService;

    @Resource
    private HistoryNumService historyNumService;

    public void analysisHistoryData() {
        Map<Integer, Integer> map = new HashMap<>();
        List<LotteryNum> lotteryNumList = lotteryNumService.list();
        lotteryNumList.forEach(e->{
            analysisNums(e.getNum1(), map);
            analysisNums(e.getNum2(), map);
            analysisNums(e.getNum3(), map);
            analysisNums(e.getNum4(), map);
            analysisNums(e.getNum5(), map);
            analysisNums(e.getNum6(), map);
        });
        Integer sum = 0;
        for (Integer value : map.values()) {
            sum += value;
        }
        int avg = sum / 33;
        System.out.println("AVG: " + avg);
        List<String> ltList = new ArrayList<>();
        List<String> gtList = new ArrayList<>();
        for (int i = 1; i <= 33; i++) {
            String str = i + ": " + map.get(i);
            if (map.get(i) >= avg) {
                gtList.add(str);
            } else {
                ltList.add(str);
            }
            System.out.println(str);
        }
        System.out.println("LT: " + String.join(" ", ltList));
        System.out.println("GT: " + String.join(" ", gtList));
    }

    private void analysisNums(Integer num, Map<Integer, Integer> map) {
        Integer value = map.computeIfAbsent(num, key -> 0);
        map.put(num, ++value);
    }

    public AnalysisDataBo analysisHistoryData(List<LotteryNum> lotteryNumList) {
        Map<Integer, Integer> map = new HashMap<>();
        lotteryNumList.forEach(e->{
            analysisNums(e.getNum1(), map);
            analysisNums(e.getNum2(), map);
            analysisNums(e.getNum3(), map);
            analysisNums(e.getNum4(), map);
            analysisNums(e.getNum5(), map);
            analysisNums(e.getNum6(), map);
        });
        AnalysisDataBo analysisDataBo = new AnalysisDataBo();

        List<AnalysisDataBo.NumData> numDataList = new ArrayList<>();

        map.forEach((k,v)->{
            numDataList.add(new AnalysisDataBo.NumData(k, v));
        });

        analysisDataBo.setNumList(numDataList);

        return analysisDataBo;
    }

    @Value("${blue.loop:16}")
    private Integer blueLoop;

    public List<BlueNumBo> getBlueNums(Integer highLevelCount, Integer middleLevelCount, Integer lowLevelCount) {
        List<BlueNumBo> result = new ArrayList<>();
        LambdaQueryWrapper<LotteryNum> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(LotteryNum::getId).last(String.format(" limit %s", blueLoop));
        List<LotteryNum> lotteryNumList = lotteryNumService.list(wrapper);
        //  找出不存在的数字
        List<Integer> numList = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            numList.add(i);
        }
        lotteryNumList.forEach(e->{
            numList.remove(e.getBlue());
        });
        if (numList.size() > 0) {
            List<BlueNumBo> highLevelBlueNumList = new ArrayList<>();
            numList.forEach(e->{
                LambdaQueryWrapper<LotteryNum> lambdaQuery = Wrappers.lambdaQuery();
                lambdaQuery.eq(LotteryNum::getBlue, e).orderByDesc(LotteryNum::getId).last(" limit 1");
                List<LotteryNum> list = lotteryNumService.list(lambdaQuery);
                if (!CollectionUtils.isEmpty(list)) {
                    highLevelBlueNumList.add(BlueNumBo.builder().blue(e).level(1).lastId(list.get(0).getId()).build());
                }
            });
            Collections.sort(highLevelBlueNumList, Comparator.comparingInt(BlueNumBo::getLastId));
            for (int i = 0; i < Math.min(highLevelCount, highLevelBlueNumList.size()); i++) {
                BlueNumBo blueNumBo = highLevelBlueNumList.get(i);
                blueNumBo.setLastId(0);
                result.add(blueNumBo);
            }
        }
        //  存在的数组排序
        Map<Integer, List<LotteryNum>> collect = lotteryNumList.stream().collect(Collectors.groupingBy(LotteryNum::getBlue));
        TreeMap<Integer, List<Integer>> blueNumRateTreeMap = new TreeMap<>();
        collect.forEach((k,v)->{
            List<Integer> list = blueNumRateTreeMap.computeIfAbsent(v.size(), key -> new ArrayList<>());
            list.add(k);
        });

        for (Integer key: blueNumRateTreeMap.keySet()) {
            if (middleLevelCount <= 0) {
                break;
            }
            List<Integer> list = blueNumRateTreeMap.get(key);
            Collections.sort(list, (o1, o2) -> new Random().nextInt()/2);
            for (Integer blue : list) {
                if (middleLevelCount-- <= 0) {
                    break;
                }
                result.add(BlueNumBo.builder().blue(blue).level(0).build());
            }
        }

        while (lowLevelCount > 0) {
            int blue = new Random().nextInt(17);
            if (blue == 0 || result.stream().map(BlueNumBo::getBlue).collect(Collectors.toSet()).contains(blue)) {
                continue;
            }
            result.add(BlueNumBo.builder().blue(blue).level(-1).build());
            lowLevelCount--;
        }

        return result;
    }

    public List<BingoCodeBo> check(CheckNumBo checkNumBo) {
        List<BingoCodeBo> result = new ArrayList<>(checkNumBo.getCodeList().size());
        String bingoCode = checkNumBo.getBingoCode();
        checkNumBo.getCodeList().forEach(e->{
            Integer bingoLevel = BitNumUtils.bingoLevel(e, bingoCode);
            result.add(BingoCodeBo.builder().code(e).bingoLevel(bingoLevel).build());
        });
        return result;
    }

    @Transactional
    public Object checkNumInSn(String sn) {
        List<CurrentNum> list = currentNumService.list();
        if (CollectionUtils.isEmpty(list)) {
            return historyNumService.list(Wrappers.lambdaQuery(HistoryNum.class).eq(HistoryNum::getSn, sn));
        }

        LotteryNum one = lotteryNumService.getOne(Wrappers.lambdaQuery(LotteryNum.class).eq(LotteryNum::getSn, sn));
        if (Objects.isNull(one)) {
            return new ArrayList<>(0);
        }

        List<HistoryNum> result = new ArrayList<>();

        for (CurrentNum currentNum : list) {
            String reds = currentNum.getReds();
            String blues = currentNum.getBlues();
            for (String blue : blues.split(",")) {
                String code = String.format("%s,%s", reds, blue);
                Integer bingoLevel = BitNumUtils.bingoLevel(code, one.getCode());
                HistoryNum historyNum = new HistoryNum();
                historyNum.setSn(sn);
                historyNum.setBingoLevel(bingoLevel);
                historyNum.setReds(reds);
                historyNum.setBlue(blue);
                result.add(historyNum);
            }
        }

        currentNumService.getBaseMapper().delete(Wrappers.emptyWrapper());
        historyNumService.saveBatch(result);
        return result;
    }
}
