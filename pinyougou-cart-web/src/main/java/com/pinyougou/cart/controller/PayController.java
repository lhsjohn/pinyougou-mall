package com.pinyougou.cart.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbPayLog;

import entity.Result;
@RestController
@RequestMapping("/pay")
public class PayController {
	
	@Reference
	private WeixinPayService weixinPayService;
	
	@Reference
	private OrderService orderService;
	
	@RequestMapping("/createNative")
	public Map createNative(){
		//1.获取当前登录用户
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		//2.提取支付日志（从缓存 ）
		TbPayLog payLog = orderService.searchPayLogFromRedis(username);
		//3.调用微信支付接口

		if(payLog!=null){
			return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee()+"");		
		}else{
			return new HashMap<>();
		}		
	}

	
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no){
		Result result=null;
		int x=0;
		//Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);//调用查询

		try {
			//模拟支付过程,等待三秒
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		if("SUCCESS".equals("SUCCESS")){//支付成功
			result=new Result(true, "支付成功");
			orderService.updateOrderStatus(out_trade_no, "123123");//修改订单状态
		}

		return result;
	}
	
	
}
