package cn.dayuanzi.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.dayuanzi.resp.Resp;

/**
 * 
 * @author : leihc
 * @date : 2015年5月4日
 * @version : 1.0
 */
@Controller
public class TradeController {

	
	@RequestMapping(value = "getTradeParam.do")
	public ModelAndView getTradeParam(){
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 支付宝处理结束后异步发送给服务端的支付信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "tradeinfo.do",produces = "text/html;charset=UTF-8")
	public String tradeNotify(){
		return "success";
	}
}
