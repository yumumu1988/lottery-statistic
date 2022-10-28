package com.yumumu.lotterystatistic.model;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

/**
 * @author zhanghailin
 * @date 2022/10/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDataBo implements Serializable {
    private static final long serialVersionUID = 5725690715222191823L;

    private List<NumData> numList;

    public List<Integer> getHotNums() {
        return getHotNums(-1);
    }

    public List<Integer> getHotNums(Integer size) {
        Collections.sort(numList, (o1, o2) -> o2.getCount() - o1.getCount());
        List<Integer> result = new ArrayList<>();
        for (NumData item : numList) {
            if (size-- == 0) {
                break;
            }
            result.add(item.getNum());
        }
        return result;
    }

    public List<Integer> getColdNums() {
        return getColdNums(-1);
    }

    public List<Integer> getColdNums(Integer size) {
        Collections.sort(numList, Comparator.comparingInt(NumData::getCount));
        List<Integer> result = new ArrayList<>();
        for (NumData item : numList) {
            if (size-- == 0) {
                break;
            }
            result.add(item.getNum());
        }
        return result;
    }

    @Override
    public String toString() {
        return "AnalysisDataBo{" +
                "numList=" + JSONArray.toJSONString(numList) +
                '}';
    }

    public void output() {
        Collections.sort(numList, (o1, o2) -> o2.getCount() - o1.getCount());
        System.out.println(JSONArray.toJSONString(numList));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NumData implements Serializable {
        private static final long serialVersionUID = 7109995618922370943L;

        private Integer num;
        private Integer count;
    }
}
