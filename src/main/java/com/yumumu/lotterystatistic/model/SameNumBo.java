package com.yumumu.lotterystatistic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhanghailin
 * @date 2022/10/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SameNumBo implements Serializable {
    private static final long serialVersionUID = 7588400422215852408L;

    private Integer sameSize;
    private String sameNums;
}
