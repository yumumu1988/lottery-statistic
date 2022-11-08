package com.yumumu.lotterystatistic.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhanghailin
 * @date 2022/11/8
 */
@Data
public class NumCollectionBo implements Serializable {
    private static final long serialVersionUID = -166804587864019327L;
    private List<BlueNumBo> blueNumList;
    private List<Integer> redNumList;
}
