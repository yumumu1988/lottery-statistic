package com.yumumu.lotterystatistic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhanghailin
 * @date 2022/10/31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlueNumBo implements Serializable {
    private static final long serialVersionUID = 1111441947670091350L;

    private Integer blue;
    private Integer level;
    private Integer lastId;
}
