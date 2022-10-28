package com.yumumu.lotterystatistic.service;

import com.yumumu.lotterystatistic.dao.domain.LotteryNum;
import com.yumumu.lotterystatistic.dao.service.LotteryNumService;
import com.yumumu.lotterystatistic.model.AnalysisDataBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanghailin
 * @date 2022/10/28
 */
@Service
@Slf4j
public class AnalysisService {

    @Resource
    private LotteryNumService lotteryNumService;

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

}
