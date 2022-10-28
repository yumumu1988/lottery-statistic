package com.yumumu.lotterystatistic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yumumu.lotterystatistic.dao.mapper.LotteryNumMapper;
import com.yumumu.lotterystatistic.service.LoadHistoryDataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class LotteryStatisticApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LotteryStatisticApplication.class, args);
//		LoadHistoryDataService bean = context.getBean(LoadHistoryDataService.class);
//		try {
//			bean.loadData();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		LotteryNumMapper bean1 = context.getBean(LotteryNumMapper.class);
//		Long aLong = bean1.selectCount(Wrappers.emptyWrapper());
//		System.out.println(aLong);
	}

}
