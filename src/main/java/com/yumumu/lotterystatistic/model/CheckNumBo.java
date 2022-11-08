package com.yumumu.lotterystatistic.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhanghailin
 * @date 2022/11/4
 */
@Data
public class CheckNumBo implements Serializable {
    private static final long serialVersionUID = 6807073705546245191L;

    private List<String> codeList;
    private String bingoCode;
}
