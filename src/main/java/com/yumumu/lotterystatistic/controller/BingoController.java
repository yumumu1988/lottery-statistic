package com.yumumu.lotterystatistic.controller;

import com.yumumu.lotterystatistic.service.BingoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author zhanghailin
 * @date 2022/10/28
 */
@RestController
@RequestMapping("/bingo")
public class BingoController {

    @Resource
    private BingoService bingoService;

    @GetMapping("/bang")
    public Object bang(@RequestParam(value = "size", required = false) Integer size,
                       @RequestParam(value = "loop", required = false) Integer loop) {
        return bingoService.bang(Optional.ofNullable(size).orElse(10), Optional.ofNullable(loop).orElse(15));
    }
}
