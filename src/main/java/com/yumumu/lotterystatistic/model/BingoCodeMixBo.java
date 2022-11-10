package com.yumumu.lotterystatistic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhanghailin
 * @date 2022/11/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BingoCodeMixBo implements Serializable {
    private static final long serialVersionUID = -2424273336498241775L;
    private String reds;
    private String blues;

    @Override
    public String toString() {
        return reds + " " + blues;
    }
}
