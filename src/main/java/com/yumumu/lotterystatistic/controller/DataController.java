package com.yumumu.lotterystatistic.controller;

import com.yumumu.lotterystatistic.service.LoadHistoryDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author zhanghailin
 * @date 2022/10/28
 */
@RestController
@RequestMapping("/data")
public class DataController {

    @Resource
    private LoadHistoryDataService loadHistoryDataService;

    @GetMapping("/initial")
    public String initialData() {
        try {
            loadHistoryDataService.initialData();
            return "OK";
        } catch (IOException e) {
            e.printStackTrace();
            return "FAILED";
        }
    }

    @GetMapping("/initialRsData")
    public String initialRsData() {
        loadHistoryDataService.initialRelationshipData();
        return "OK";
    }

    @GetMapping("/newData")
    public String newData(@RequestParam("code") String code, @RequestParam("sn") String sn) {
        loadHistoryDataService.addNewData(sn, code);
        return "OK";
    }
}
