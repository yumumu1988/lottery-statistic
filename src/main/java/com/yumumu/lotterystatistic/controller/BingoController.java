package com.yumumu.lotterystatistic.controller;

import com.yumumu.lotterystatistic.model.BingoCodeBo;
import com.yumumu.lotterystatistic.model.BingoCodeMixBo;
import com.yumumu.lotterystatistic.service.BingoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
                       @RequestParam(value = "loop", required = false) Integer loop,
                       @RequestParam(value = "fm", required = false) Integer showEasy) {
        switch (Optional.ofNullable(showEasy).orElse(0)) {
            case 0:
                return bingoService.bangMix(Optional.ofNullable(size).orElse(10), Optional.ofNullable(loop).orElse(15));
            case 1:
                return bingoService.bang(Optional.ofNullable(size).orElse(10), Optional.ofNullable(loop).orElse(15));
            default:
                List<BingoCodeBo> list = bingoService.bang(Optional.ofNullable(size).orElse(10), Optional.ofNullable(loop).orElse(15));
                List<String> result = new ArrayList<>();
                list.forEach(e->{
                    result.add(e.getCode());
                });
                return result;
        }
    }

    @PostMapping("/updateBang")
    public Object updateBang(@RequestBody List<String> numList, @RequestParam(value = "loop", required = false) Integer loop) {
        loop = Optional.ofNullable(loop).orElse(15);
        return bingoService.updateBang(numList, loop);
    }

    @PostMapping("/updateCrtNum")
    public Object updateCrtNum(@RequestParam(value = "loop", required = false) Integer loop) {
        loop = Optional.ofNullable(loop).orElse(15);
        return bingoService.updateCrtNum(loop);
    }

    @PostMapping("/record")
    public Object record(@RequestBody List<BingoCodeMixBo> list) {
        return bingoService.record(list);
    }

    @GetMapping("/crt")
    public Object currentNums() {
        return bingoService.currentNum();
    }

}
