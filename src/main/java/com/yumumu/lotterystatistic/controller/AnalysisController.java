package com.yumumu.lotterystatistic.controller;

import com.yumumu.lotterystatistic.service.AnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
