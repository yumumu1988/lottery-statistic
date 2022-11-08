package com.yumumu.lotterystatistic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhanghailin
 * @date 2022/10/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BingoCodeBo implements Serializable {
    private static final long serialVersionUID = -283853133746176057L;

    private boolean hotCode;
    private List<Integer> numList;
    private String code;
    private Integer bingoLevel;
}
