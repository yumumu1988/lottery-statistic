package com.yumumu.lotterystatistic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yumumu.lotterystatistic.dao.domain.LotteryNum;
import com.yumumu.lotterystatistic.dao.domain.LotteryRelationship;
import com.yumumu.lotterystatistic.dao.service.LotteryNumService;
import com.yumumu.lotterystatistic.dao.service.LotteryRelationshipService;
import com.yumumu.lotterystatistic.model.SameNumBo;
import com.yumumu.lotterystatistic.util.BitNumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanghailin
 * @date 2022/10/26
 */
@Service
@Slf4j
public class LoadHistoryDataService {

    @Resource
    private LotteryNumService lotteryNumService;

    @Resource
    private LotteryRelationshipService lotteryRelationshipService;

    public void initialData() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/zhanghailin/Desktop/git/yumumu/lottery-statistic/src/main/resources/lottery.txt"));
        String readLine = bufferedReader.readLine();
        List<LotteryNum> list = new ArrayList<>();
        while (StringUtils.hasText(readLine)) {
            String[] split = readLine.split(",");
            Integer num1 = Integer.valueOf(split[1]);
            Integer num2 = Integer.valueOf(split[2]);
            Integer num3 = Integer.valueOf(split[3]);
            Integer num4 = Integer.valueOf(split[4]);
            Integer num5 = Integer.valueOf(split[5]);
            Integer num6 = Integer.valueOf(split[6]);
            Integer blue = Integer.valueOf(split[7]);
            String code = split[1] + "," + split[2] + "," + split[3] + "," + split[4] + "," + split[5] + "," + split[6];
            LotteryNum lotteryNum = LotteryNum.builder().num1(num1).num2(num2).num3(num3).num4(num4).num5(num5).num6(num6).blue(blue)
                    .code(code + "," + split[7]).sn(split[0]).bitNum(BitNumUtils.convertToBitNum(code)).build();
            list.add(0, lotteryNum);
            log.info(lotteryNum.toString());
            readLine = bufferedReader.readLine();
        }
        log.info("prepare to save history data");
        lotteryNumService.saveBatch(list);
        log.info("history data initialed");
    }

    public void initialRelationshipData() {
        List<LotteryNum> list = lotteryNumService.getBaseMapper().selectList(Wrappers.emptyWrapper());
        System.out.println(list.size());
        for (int i = 36; i < list.size(); i++) {
            LotteryNum lotteryNum = list.get(i);
            List<LotteryRelationship> items = new ArrayList<>();
            for (int j = i - 36; j < i; j++) {
                LotteryNum item = list.get(j);
                SameNumBo sameNumBo = BitNumUtils.compareSameSize(lotteryNum.getBitNum(), item.getBitNum());
                LotteryRelationship lotteryRelationship = LotteryRelationship.builder().snBig(lotteryNum.getSn())
                        .snSmall(item.getSn()).sameNums(sameNumBo.getSameNums())
                        .idBig(lotteryNum.getId()).idSmall(item.getId()).sameSize(sameNumBo.getSameSize()).build();
                items.add(lotteryRelationship);
            }
            log.info("initialRelationshipData: " + lotteryNum.getSn() + "; process: " + items.size());
            lotteryRelationshipService.saveBatch(items);
        }
    }

    @Transactional
    public void addNewData(String sn, String fullCode) {
        String[] split = fullCode.split(",");
        Integer num1 = Integer.valueOf(split[0]);
        Integer num2 = Integer.valueOf(split[1]);
        Integer num3 = Integer.valueOf(split[2]);
        Integer num4 = Integer.valueOf(split[3]);
        Integer num5 = Integer.valueOf(split[4]);
        Integer num6 = Integer.valueOf(split[5]);
        Integer blue = Integer.valueOf(split[6]);
        String code = split[0] + "," + split[1] + "," + split[2] + "," + split[3] + "," + split[4] + "," + split[5];
        LotteryNum lotteryNum = LotteryNum.builder().num1(num1).num2(num2).num3(num3).num4(num4).num5(num5).num6(num6).blue(blue)
                .code(fullCode).sn(sn).bitNum(BitNumUtils.convertToBitNum(code)).build();
        lotteryNumService.save(lotteryNum);
        LambdaQueryWrapper<LotteryNum> wrapper = Wrappers.lambdaQuery();
        wrapper.lt(LotteryNum::getId, lotteryNum.getId()).orderByDesc(LotteryNum::getId);
        Page<LotteryNum> lotteryNumPage = new Page<>(1, 36);
        Page<LotteryNum> result = lotteryNumService.getBaseMapper().selectPage(lotteryNumPage, wrapper);
        List<LotteryNum> lotteryNumList = result.getRecords();
        List<LotteryRelationship> items = new ArrayList<>();
        for (int i = lotteryNumList.size() - 1; i >= 0; i--) {
            LotteryNum item = lotteryNumList.get(i);
            SameNumBo sameNumBo = BitNumUtils.compareSameSize(lotteryNum.getBitNum(), item.getBitNum());
            LotteryRelationship lotteryRelationship = LotteryRelationship.builder().snBig(lotteryNum.getSn())
                    .snSmall(item.getSn()).sameNums(sameNumBo.getSameNums())
                    .idBig(lotteryNum.getId()).idSmall(item.getId()).sameSize(sameNumBo.getSameSize()).build();
            items.add(lotteryRelationship);
        }
        lotteryRelationshipService.saveBatch(items);
    }
}
