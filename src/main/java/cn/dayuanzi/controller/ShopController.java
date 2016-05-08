package cn.dayuanzi.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.dayuanzi.config.ShopConfig;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.OpenTrainResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.ShopGoodsInfoResp;
import cn.dayuanzi.resp.ShopListResp;
import cn.dayuanzi.resp.ShopResp;
import cn.dayuanzi.resp.TrainDetailResp;
import cn.dayuanzi.resp.TrainListResp;
import cn.dayuanzi.resp.UserOrderListResp;
import cn.dayuanzi.resp.UserOrderResp;
import cn.dayuanzi.service.IShopService;
import cn.dayuanzi.util.YardUtils;

/**
 * 商店相关
 * 
 * @author : leihc
 * @date : 2015年6月9日
 * @version : 1.0
 */
@Controller
public class ShopController {
	
	
	@Autowired
	private IShopService shopService;
	
	/**
	 * 下订单
	 * 
	 * @param name
	 * @param tel
	 * @param address
	 * @param goodsId
	 * @param amount
	 * @return
	 */
	@RequestMapping(value = "order.do")
	public ModelAndView order(String name, String tel, String address,long goodsId, int amount){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客无法操作哦，快来加入我们把");
		}
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		if(amount<=0){
			throw new GeneralLogicException("参数错误");
		}
		if(StringUtils.isBlank(name)){
			throw new GeneralLogicException("请输入收货人姓名");
		}
		if(StringUtils.isBlank(address)){
			throw new GeneralLogicException("请输入收货地址");
		}
		if(StringUtils.isBlank(tel)){
			throw new GeneralLogicException("请输入联系电话");
		} 
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[6-8])|(14[5-7]))\\d{8}$");
		Matcher matcher = pattern.matcher(tel);
		if(!matcher.matches()){
		    throw new GeneralLogicException("请输入正确的手机号码");
		}
		UserOrderResp  resp = this.shopService.placeOrder(userSession.getUserId(), userSession.getCourtyardId(), name, tel, address, goodsId, amount);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 查询我的兑换
	 * @return
	 */
	@RequestMapping(value = "myOrders.do")
	public ModelAndView getOrders(){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客无法操作哦，快来加入我们把");
		}
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		UserOrderListResp resp = this.shopService.findMyOrders(userSession.getUserId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 我的兑换详情
	 */
	@RequestMapping(value = "myOrdersDetail.do")
	public ModelAndView getOrdersDetail(long ordersId){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客无法操作哦，快来加入我们把");
		}
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		if(ordersId<=0){
			throw new GeneralLogicException("参数错误");
		}
		UserOrderResp resp = this.shopService.findMyOrdersDetail(ordersId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 获取商品详情
	 * @return
	 */
	@RequestMapping(value = "getShopGoodsDetail.do")
	public ModelAndView getShopGoodsDetail(long goodsId){
		if(goodsId<=0){
			throw new GeneralLogicException("参数错误");
		}
		ShopResp  resp =this.shopService.getShopDetail(goodsId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 获取商店列表
	 */
	@RequestMapping(value = "getShopGoodsList.do")
	public ModelAndView getShopGoodsList(Integer current_page, Integer max_num){
	    	UserSession userSession = UserSession.get();
	    	if(userSession.isVisitor()){
	    	    throw new GeneralLogicException("访客无法操作哦，快来加入我们把");
	    	}
	    	ShopListResp resp = this.shopService.getShopList(current_page,max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	
	}
	
	/**
	 *  学堂列表
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	@RequestMapping(value = "trainList.do")
	public ModelAndView trainList (int current_page, int max_num){
		 UserSession userSession = UserSession.get();
		 if(userSession.isVisitor()){
				throw new GeneralLogicException("访客无法操作哦，快来加入我们把");
			}
		TrainListResp resp = this.shopService.trainList(current_page, max_num,userSession.getCourtyardId());
	    ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model",resp);
		return mav;
	}
	
	/**
	 * 学堂详情
	 * @param rainId
	 * @return
	 */
	@RequestMapping(value = "trainDetail.do")
	public ModelAndView trainDetail (long trainId){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		TrainDetailResp resp = this.shopService.traindetail(trainId,userSession.getUserId());
	    ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model",resp);
		return mav;
	}
	/**
	 * 感谢学堂
	 * @param trainId
	 * @return
	 */
	@RequestMapping(value = "trainLaud.do")
	public ModelAndView trainLaud (long trainId){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		this.shopService.trainLaud(trainId,userSession.getUserId());
	    ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model",new Resp());
		return mav;
	}
	
	/**
	 * 申请开店
	 * @param category 类别
	 * @param introduction  介绍
	 * @param tel
	 * @param mail
	 * @param 0 是申请  1 被拒绝 2 我的学堂草稿
	 * @return
	 */
	@RequestMapping(value = "openShop.do")
	public ModelAndView openShop (@RequestParam(required=false)Integer category, @RequestParam(required=false)String introduction,@RequestParam(required=false)String  tel,@RequestParam(required=false)String mail){
	    UserSession userSession = UserSession.get();
	    if(userSession.isVisitor()){
	    	throw new GeneralLogicException("访客无法操作哦，快来加入我们把");
	    }
	    if(!userSession.isValidatedUser()){
	    	throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
	    }
	    if(category<=0){
	    	throw new GeneralLogicException("请选择您的类别");
	    }	
	    if(StringUtils.isBlank(introduction)){
	    	throw new GeneralLogicException("请介绍您的店铺");
	    }
	   	if(StringUtils.isBlank(tel)){
	    	throw new GeneralLogicException("请输入您的手机号码");
	    }
	    Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[6-8])|(14[5-7]))\\d{8}$");
	    Matcher matcher = pattern.matcher(tel);
	    if(!matcher.matches()){
	    	throw new GeneralLogicException("请输入正确的手机号码");
	    }
	    if(StringUtils.isBlank(mail)){
	    	throw new GeneralLogicException("请输入邮箱");
	    }  
	    if(!YardUtils.isValidMail(mail)){
			throw new GeneralLogicException("请输入正确的邮箱");
		}
	    this.shopService.openShop(category,introduction,tel,userSession.getCourtyardId(),userSession.getUserId(),mail);
	    ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 获取开店铺的信息
	 * @return
	 */
	@RequestMapping(value = "getTraininfo.do")
	public ModelAndView getTraininfo(){
		UserSession userSession = UserSession.get();
	    if(userSession.isVisitor()){
	    	throw new GeneralLogicException("访客无法操作哦，快来加入我们把");
	    }
		OpenTrainResp resp = this.shopService.getTraininfo(userSession.getUserId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model",resp);
		return mav;
	}
	
	
	
	
	
	/**
	 * 返回商品信息 
	 */
//	@RequestMapping(value = "getShopGoodsInfo.do")
	public ModelAndView getShopGoodsInfo(){
		ShopGoodsInfoResp resp = new ShopGoodsInfoResp();
		resp.getDatas().addAll(ShopConfig.getDatas().values());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
}


