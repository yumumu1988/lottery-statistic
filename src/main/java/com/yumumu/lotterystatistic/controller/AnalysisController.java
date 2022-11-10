package com.yumumu.lotterystatistic.controller;

import com.alibaba.fastjson.JSONArray;
import com.yumumu.lotterystatistic.model.BingoCodeBo;
import com.yumumu.lotterystatistic.model.CheckNumBo;
import com.yumumu.lotterystatistic.model.SameNumBo;
import com.yumumu.lotterystatistic.service.AnalysisService;
import com.yumumu.lotterystatistic.service.BingoService;
import com.yumumu.lotterystatistic.util.BitNumUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhanghailin
 * @date 2022/10/28
 */
@RestController
@RequestMapping("/a")
public class AnalysisController {

    @Resource
    private AnalysisService analysisService;

    @GetMapping("/historyData")
    public String aHistoryData() {
        analysisService.analysisHistoryData();
        return "OK";
    }

    @PostMapping("/check")
    public List<BingoCodeBo> check(@RequestBody CheckNumBo checkNumBo) {
        return analysisService.check(checkNumBo);
    }

    @GetMapping("/sn/{sn}")
    public Object checkNumInSn(@PathVariable("sn") String sn) {
        return analysisService.checkNumInSn(sn);
    }

    @Resource
    private BingoService bingoService;

    @GetMapping("/test")
    public String test() {
        Long aLong = BitNumUtils.convertToBitNum("10,13,16,20,21,25");
        int index = 1;
        int loop = 10000;
        int s3 = 0;
        int s4 = 0;
        int s5 = 0;
        int s6 = 0;
        for (int i = 0; i < loop; i ++) {
            List<BingoCodeBo> bang = bingoService.bang(10, 15);
            for (BingoCodeBo bingoCodeBo : bang) {
                String code = bingoCodeBo.getCode();
                code = code.replace("[", "").replace("]", "");
                SameNumBo sameNumBo = BitNumUtils.compareSameSize(aLong, BitNumUtils.convertToBitNum(code));
                if (sameNumBo.getSameSize() >= 3) {
                    switch (sameNumBo.getSameSize()) {
                        case 3:
                            s3++;
                            break;
                        case 4:
                            s4++;
                            break;
                        case 5:
                            s5++;
                            break;
                        case 6:
                            s6++;
                            break;
                    }
                    System.out.println(index + ": " + code + " ===> " + sameNumBo.getSameSize());
                }
                index++;
            }
        }
        return "OK 3:" + s3*10/loop + "; 4:" + s4*10/loop + "; 5:" + s5*10/loop + "; 6:" + s6*10/loop;
    }
//    三等奖：单注奖金固定为3000元。
//    四等奖：单注奖金固定为200元。
//    五等奖：单注奖金固定为10元。
//    六等奖：单注奖金固定为5元。
//    一等奖（6+1）中奖概率为：红球33选6乘以蓝球16选1=1/17721088=0.0000056%；
//    二等奖（6+0）中奖概率为：红球33选6乘以蓝球16选0=15/17721088=0.0000846%；
//    三等奖（5+1）中奖概率为：红球33选5乘以蓝球16选1=162/17721088=0.000914%；
//    四等奖（5+0、4+1）中奖概率为：红球33选5乘以蓝球16选0=7695/17721088=0.0434%；
//    五等奖（4+0、3+1）中奖概率为：红球33选4乘以蓝球16选0=137475/17721088=0.7758%；
//    六等奖（2+1、1+1、0+1）中奖概率为：红球33选2乘以蓝球16选1=1043640/17721088=5.889%；

}
