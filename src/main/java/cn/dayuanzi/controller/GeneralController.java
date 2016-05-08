package cn.dayuanzi.controller;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.dayuanzi.config.CareerConfig;
import cn.dayuanzi.config.GainExpConfig;
import cn.dayuanzi.config.GainLindouConfig;
import cn.dayuanzi.config.InterestConf;
import cn.dayuanzi.config.InvitationConfig;
import cn.dayuanzi.config.LevelUpConfig;
import cn.dayuanzi.config.ReportConfig;
import cn.dayuanzi.config.Settings;
import cn.dayuanzi.config.TagConfig;
import cn.dayuanzi.config.TopicTagConfig;
import cn.dayuanzi.config.TrainTagConfig;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.HouseOwners;
import cn.dayuanzi.model.User;
import cn.dayuanzi.pojo.ExternalType;
import cn.dayuanzi.pojo.PlatForm;
import cn.dayuanzi.pojo.SessionAttribute;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.BlackNameListResp;
import cn.dayuanzi.resp.BuildingsResp;
import cn.dayuanzi.resp.CareersResp;
import cn.dayuanzi.resp.CourtyardInfoResp;
import cn.dayuanzi.resp.ExpInfoResp;
import cn.dayuanzi.resp.HouseResp;
import cn.dayuanzi.resp.InterestsResp;
import cn.dayuanzi.resp.InvitationTagResp;
import cn.dayuanzi.resp.LevelUpInfoResp;
import cn.dayuanzi.resp.LinDouInfoResp;
import cn.dayuanzi.resp.PersonalDataResp;
import cn.dayuanzi.resp.ReportTagResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.TagResp;
import cn.dayuanzi.resp.TermResp;
import cn.dayuanzi.resp.TopicTagResp;
import cn.dayuanzi.resp.TrainTagResp;
import cn.dayuanzi.resp.UnitResp;
import cn.dayuanzi.resp.dto.BuildingsDto;
import cn.dayuanzi.resp.dto.CourtyardDto;
import cn.dayuanzi.resp.dto.HouseDto;
import cn.dayuanzi.resp.dto.TermDto;
import cn.dayuanzi.resp.dto.UnitDto;
import cn.dayuanzi.service.ICourtYardService;
import cn.dayuanzi.service.IHouseOwnersService;
import cn.dayuanzi.service.IRedisService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.service.impl.ServiceRegistry;
import cn.dayuanzi.util.DateTimeUtil;
import cn.dayuanzi.util.GeoRange;
import cn.dayuanzi.util.LogUtil;
import cn.dayuanzi.util.YardUtils;

/**
 * @author : lhc
 * @date : 2015年4月7日 下午12:57:34
 * @Description :
 *
 */
@Controller
public class GeneralController {
	

	@Autowired
	private IHouseOwnersService houseOwnersService;
	@Autowired
	private ICourtYardService courtYardService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IRedisService redisService;

	
	/**
	 * 获取验证码(注册，忘记密码)
	 * @param session  
	 * @param tel  电话号码
	 * @param type 0 注册   1 忘记密码
	 * @return
	 */
	@RequestMapping(value = "sendCode.do")
	public ModelAndView sendCode(HttpSession session, String tel,int type){
		if(StringUtils.isBlank(tel)){
			throw new GeneralLogicException("手机号码不能为空");
		}
		long sendCountToday = redisService.addValidateCodeGnerateAmount(tel);
		if(Settings.VALIDATECODE_LIMIT>0 && sendCountToday > Settings.VALIDATECODE_LIMIT){
			throw new GeneralLogicException("每天发送验证码不能超过"+Settings.VALIDATECODE_LIMIT+"次");
		}
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[6-8])|(14[5-7]))\\d{8}$");
		Matcher matcher = pattern.matcher(tel);
		if(!matcher.matches()){
			throw new GeneralLogicException("请输入正确的手机号码");
		}
		if(type == 0){
			if(userService.findUserByTel(tel)!=null){
				throw new GeneralLogicException("该手机号码已经被注册,请重新输入");
			}
		}else{
			if(userService.findUserByTel(tel)==null){
				throw new GeneralLogicException("该手机号码未被注册,请重新输入");
			}
		}
		String validateCode = ServiceRegistry.getSmsSenderService().sendMsg(tel);
		if(StringUtils.isBlank(validateCode)){
			throw new GeneralLogicException("请重新发送验证码");
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		session.setAttribute(SessionAttribute.USER_TEL, tel);
		session.setAttribute(SessionAttribute.VALIDATE_CODE, validateCode);
		return mav;
	}
	
	/**
	 * 手机验证
	 * @param session
	 * @param validateCode
	 * @return
	 */
	@RequestMapping(value = "validateTel.do")
	public ModelAndView validateTel(HttpSession session ,String validateCode){
		String sessionValidateCode = (String)session.getAttribute(SessionAttribute.VALIDATE_CODE);
		String tel = (String)session.getAttribute(SessionAttribute.USER_TEL);
		if(StringUtils.isBlank(sessionValidateCode) || StringUtils.isBlank(tel)){
			throw new GeneralLogicException("请先获取验证码");
		}
		if(StringUtils.isBlank(validateCode)){
			throw new GeneralLogicException("验证码不能为空");
		}
		if(!sessionValidateCode.equals(validateCode)){
			throw new GeneralLogicException("验证码不正确，请重新输入");
		}
		UserSession userSession = UserSession.get();
		this.userService.validateTel(tel, userSession.getUserId());
		
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	
	/**
	 * 用户注册   
	 * @param session
	 * @param validateCode 验证码
	 * @param password   密码
	 * @param pf  1 是安卓  2是苹果 
	 * @param token    
	 * @param inviteCode 邀请码 可空
	 * @return
	 */
	@RequestMapping(value = "register.do")
	public ModelAndView nextStep(HttpSession session, String validateCode, String password, int pf,String token,@RequestParam(required=false) Long inviteCode){
		String sessionValidateCode = (String)session.getAttribute(SessionAttribute.VALIDATE_CODE);
		String tel = (String)session.getAttribute(SessionAttribute.USER_TEL);
		if(StringUtils.isBlank(sessionValidateCode) || StringUtils.isBlank(tel)){
			throw new GeneralLogicException("请先获取验证码");
		}
		if(StringUtils.isBlank(validateCode)){
			throw new GeneralLogicException("验证码不能为空");
		}
		if(!sessionValidateCode.equals(validateCode)){
			throw new GeneralLogicException("验证码不正确，请重新输入");
		}
		if(StringUtils.isBlank(password)){
			throw new GeneralLogicException("密码不能为空");
		}
		if(pf!=PlatForm.ANDROID.ordinal()&&pf!=PlatForm.IOS.ordinal()){
			throw new GeneralLogicException("参数错误");
		}
		User user = this.userService.register(tel, password, pf, token,inviteCode);
		LogUtil.logRegister(user.getId(), ExternalType.邻聚, pf, token,inviteCode);
		session.removeAttribute(SessionAttribute.VALIDATE_CODE);
		session.removeAttribute(SessionAttribute.USER_TEL);
//		session.setAttribute(SessionAttribute.PASSWORD, password);
		UserSession userSession = new UserSession(user);
		session.setAttribute(SessionAttribute.SESSION_USER, userSession);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 *  第三方平台注册
	 * @param session
	 * @param pf    1 是安卓  2是苹果 
	 * @param token   设备token,注册用户时的
	 * @param externalType      第三方代号0 表示本平台 1 表示QQ平台 2 表示微信 3 表示微博
	 * @param openId   第三方返回的代号
	 * @param inviteCode     邀请码
	 * @return
	 */
	@RequestMapping(value = "registerExternal.do")
	public ModelAndView registerExternal(HttpSession session,int pf,String token,int externalType, String openId, @RequestParam(required=false) Long inviteCode){
		if(StringUtils.isBlank(openId)){
			throw new GeneralLogicException("出错了");
		}
//		Pattern pattern = Pattern.compile("^([0-9A-F]{32})$");
//		Matcher matcher = pattern.matcher(openId);
//		if(!matcher.matches()){
			//throw new GeneralLogicException("出错了哦");
//		}
		ExternalType external = ExternalType.values()[externalType];
		
		if(external==ExternalType.邻聚 || !external.isOpen()){
			throw new GeneralLogicException("还不能通过该平台注册哦");
		}
//		String sessionValidateCode = (String)session.getAttribute(SessionAttribute.VALIDATE_CODE);
//		String tel = (String)session.getAttribute(SessionAttribute.USER_TEL);
//		if(StringUtils.isBlank(sessionValidateCode) || StringUtils.isBlank(tel)){
//			throw new GeneralLogicException("请先获取验证码");
//		}
		
//		if(StringUtils.isBlank(validateCode)){
//			throw new GeneralLogicException("验证码不能为空");
//		}
//		if(!sessionValidateCode.equals(validateCode)){
//			throw new GeneralLogicException("验证码不正确，请重新输入");
//		}
		if(pf!=PlatForm.ANDROID.ordinal()&&pf!=PlatForm.IOS.ordinal()){
			throw new GeneralLogicException("参数错误");
		}
		User user = this.userService.register(externalType, openId, pf, token,inviteCode);
		LogUtil.logRegister(user.getId(), external, pf, token,inviteCode);
		session.removeAttribute(SessionAttribute.VALIDATE_CODE);
		session.removeAttribute(SessionAttribute.USER_TEL);
		
		UserSession userSession = new UserSession(user);
		session.setAttribute(SessionAttribute.SESSION_USER, userSession);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 *  完善资料
	 * @param session
	 * @param headIcons 头像
	 * @param nickName 昵称
	 * @param gender  性别 1 男 2 女
	 * @param birthday 生日 格式: yyyy-MM-dd
	 * @param careerId 职业ID,可不传
	 * @param interests 兴趣 格式：兴趣1ID|兴趣2ID|兴趣3ID 可不传
	 * @param skill 技能
	 * @return
	 */
	@RequestMapping(value = "addinfo.do")
	public ModelAndView register(HttpSession session,@RequestParam(value="headIcons")CommonsMultipartFile[] headIcons,String nickName,Integer gender,String birthday,@RequestParam(required=false)Integer careerId,@RequestParam(required=false)String interests,@RequestParam(required=false)String signature,@RequestParam(required=false)String skill){
		if(gender!=1 && gender!=2){
			throw new GeneralLogicException("参数错误。");
		}
		if(StringUtils.isBlank(nickName)){
			throw new GeneralLogicException("请输入昵称");
		}
		Pattern pattern = Pattern.compile("[-\\w\u4e00-\u9fa5]{2,10}");
		Matcher m = pattern.matcher(nickName);
		if(!m.matches()){
			throw new GeneralLogicException("昵称支持中英文，数字，下划线或减号，长度2到10个字符");
		}
		if(careerId!=null&&CareerConfig.getCareers().get(careerId)==null){
			throw new GeneralLogicException("系统没有设置这个职业");
		}
		if(careerId == null){
			careerId = 0;
		}
		if(signature!=null&&signature.length()>24){
			throw new GeneralLogicException("签名太长了");
		}
		if(skill!=null&&skill.length()>10){
			throw new GeneralLogicException("技能名称不能超过10个字");
		}
		
		Timestamp birthdayTimestamp = new Timestamp(DateTimeUtil.getTime(birthday,"yyyy-MM-dd"));
		UserSession userSession = UserSession.get();
		PersonalDataResp resp = ServiceRegistry.getUserService().addInfo(userSession.getUserId(), headIcons, nickName, gender, birthdayTimestamp, careerId, interests,signature,skill);
		resp.setTel(userSession.getTel());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}

	/**
	 * 修改资料
	 * 
	 * @param headIcons 头像
	 * @param nickName 昵称
	 * @param gender  性别 1 男 2 女
	 * @param birthday 生日 格式: yyyy-MM-dd
	 * @param careerId 职业ID,可不传
	 * @param interests 兴趣 格式：兴趣1ID|兴趣2ID|兴趣3ID 可不传
	 * @param signature 签名
	 * @param skill 技能
	 * @return
	 */
	@RequestMapping(value = "modifyinfo.do")
	public ModelAndView modinfo(@RequestParam(value="headIcons",required=false)CommonsMultipartFile[] headIcons,@RequestParam(required=false)String nickName,@RequestParam(required=false)Integer gender,@RequestParam(required=false)String birthday,@RequestParam(required=false)Integer careerId,@RequestParam(required=false)String interests,@RequestParam(required=false)String signature,@RequestParam(required=false)String skill){
		if(gender!=1 && gender!=2){
			throw new GeneralLogicException("参数错误");
		}
		if(nickName.length()>10){
			throw new GeneralLogicException("昵称太长了");
		}
		if(StringUtils.isNotBlank(nickName)){
			Pattern pattern = Pattern.compile("[-\\w\u4e00-\u9fa5]{2,10}");
			Matcher m = pattern.matcher(nickName);
			if(!m.matches()){
				throw new GeneralLogicException("昵称支持中英文，数字，下划线或减号，长度4到10个字符");
			}
		}
		if(careerId == null){
			careerId = 0;
		}
		if(signature!=null&&signature.length()>24){
			throw new GeneralLogicException("签名太长了");
		}
		if(skill!=null&&skill.length()>10){
			throw new GeneralLogicException("技能名称不能超过10个字");
		}
		Timestamp birthdayTimestamp = new Timestamp(DateTimeUtil.getTime(birthday,"yyyy-MM-dd"));
		UserSession userSession = UserSession.get();
		PersonalDataResp resp = userService.modify(userSession.getUserId(), headIcons, nickName, gender, birthdayTimestamp, userSession.getCourtyardId(), careerId, interests,signature,skill);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * (已弃用,选择兴趣)
	 * @param session
	 * @param interests
	 * @return
	 */
//	@RequestMapping(value = "selectInterest.do")
	public ModelAndView selectInterest(HttpSession session, String interests){
		String tel = (String)session.getAttribute(SessionAttribute.USER_TEL);
		if(StringUtils.isBlank(tel)){
			throw new GeneralLogicException("请先通过短信验证");
		}
		User user = ServiceRegistry.getUserService().findUserByTel(tel);
		if(user == null){
			throw new GeneralLogicException("请先通过短信验证");
		}
		if(StringUtils.isNotBlank(interests)){
			String[] tokens = interests.split("\\|");
			ServiceRegistry.getUserInterestService().saveUserInterest(user.getId(), tokens);
		}
		UserSession userSession = new UserSession(user);
		session.setAttribute(SessionAttribute.SESSION_USER, userSession);
		session.removeAttribute(SessionAttribute.USER_TEL);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 业主验证
	 * @param courtyardId   院子id
	 * @param validateType   1图片验证    3邀请码验证
	 * @param inviteCode   邀请码 (可选参数）
	 * @param image  图片 
	 * @return
	 */
	@RequestMapping(value = "validateUser.do")
	public ModelAndView validateUser(long courtyardId, Integer validateType,@RequestParam(required = false) Long inviteCode,@RequestParam(required = false) CommonsMultipartFile image){
		UserSession userSession = UserSession.get();
//		if(term<=0 || building<=0 || unit<=0 || houseId<=0){
//			throw new GeneralLogicException("请不要输入小于零的数字");
//		}
		boolean success = houseOwnersService.validateHouseOwner(userSession.getUserId(), courtyardId, inviteCode,validateType,image);
		if(!success){
			throw new GeneralLogicException("验证失败");
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 通过地图位置验证
	 * @param courtyardId
	 * @param lgt 用户当前位置的经度
	 * @param lat 用户当前位置的纬度
	 * @return
	 */
	@RequestMapping(value = "validateUserViaMap.do")
	public ModelAndView validateUserViaMap(long courtyardId, double lgt, double lat){
		// 经纬度有效判断
		if(lgt<0||lgt>180||lat<0||lat>90){
			throw new GeneralLogicException("错误的经纬度");
		}
		if(lgt==0&&lat==0){
			throw new GeneralLogicException("定位失败");
		}
		UserSession userSession = UserSession.get();
		houseOwnersService.validateHouseOwner(userSession.getUserId(), courtyardId, lgt, lat);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * code码认证
	 * @param code  验证码
	 * @return
	 */
//	@RequestMapping(value = "userCodeValidate.do")
	public ModelAndView usercodevalidate( String code){
//		UserSession userSession = UserSession.get();
//		boolean success = houseOwnersService.codevalidate( userSession.getUserId(),code);
//		if(!success){
//			throw new GeneralLogicException("验证失败");
//		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
		
	}
	
	
	/**
	 * 用户认证报错
	 * @param name  名字 
	 * @param code    身份证号码
	 * @param tel   电话
	 * @param content  内容
	 * @param address  地址
	 * @return
	 */
	@RequestMapping(value = "userError.do")
	public ModelAndView userError(String name,String code,String tel,String content,String address){
		if(StringUtils.isBlank(content)){
			throw new GeneralLogicException("请输入内容");
		}
		if(StringUtils.isBlank(code)){
			throw new GeneralLogicException("请输入身份证号");
		}
		if(StringUtils.isBlank(tel)){
			throw new GeneralLogicException("请输入手机号码");
		}
		if(StringUtils.isBlank(name)){
			throw new GeneralLogicException("请输入姓名");
		}
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		userService.setUserError(userSession.getUserId(),name, code, address, tel, content);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 获取指定城市的院子信息(选择社区)  
	 * @param cityId
	 * @param lgt 用户当前位置经度
	 * @param lat 用户当前位置纬度
	 * @param range 范围 单位公里
	 * @return
	 */
	@RequestMapping(value = "getCourtyardList.do")
	public ModelAndView getCourtyardList(int cityId, double lgt, double lat, int range){
		if(lgt<0||lgt>180||lat<0||lat>90){
			throw new GeneralLogicException("错误的经纬度");
		}
		if(range<0){
			throw new GeneralLogicException("参数错误");
		}
		// 定位成功
		boolean located = lgt!=0&&lat!=0;
		List<CourtYard> courtyards = null;
		if(located&&range>0){
			GeoRange gr = YardUtils.getGetRange(lgt, lat, range);
			courtyards = courtYardService.findCourtyardByCityId(cityId, gr);
		}else{
			courtyards = courtYardService.findCourtyardByCityId(cityId);
		}
		CourtyardInfoResp resp = new CourtyardInfoResp();
		for(CourtYard courtyard : courtyards){
			if(courtyard.getCourtyard_name().contains("邻聚")){
				continue;
			}
			// range=0返回该城市所有社区  >0返回距离该用户指定范围内的社区
//			if(located){
//				if(range==0 || courtyard.atRange(range*1000, lgt, lat)){
//					CourtyardDto dto = new CourtyardDto(courtyard);
//					BigDecimal bg = new BigDecimal(YardUtils.getDistance(courtyard.getLongitude(), courtyard.getLatitude(), lgt, lat)/1000);
//					dto.setDistance(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//					resp.getDatas().add(dto);
//				}
//			}else{
//				CourtyardDto dto = new CourtyardDto(courtyard);
//				resp.getDatas().add(dto);
//			}
			CourtyardDto dto = new CourtyardDto(courtyard);
			if(located&&range>0){
				BigDecimal bg = new BigDecimal(YardUtils.getDistance(courtyard.getLongitude(), courtyard.getLatitude(), lgt, lat)/1000);
				dto.setDistance(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			}
			resp.getDatas().add(dto);
		}
		if(located&&range>0){
			Collections.sort(resp.getDatas());
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	///**
	// * 获取指定院子的楼栋信息
	// * 
	// * @param courtyardId
	// * @return
	// */
	//@RequestMapping(value = "getBuildingList.do")
	//public ModelAndView getBuildingList(long courtyardId){
	//	List<HouseOwners> houseowners = courtYardService.findBuildingsByCourtyardId(courtyardId);
	//	BuildingsResp resp = new BuildingsResp();
	//	for(HouseOwners houseOwner : houseowners){
	//		resp.getDatas().add(new BuildingsDto(houseOwner));
	//	}
	//	ModelAndView mav = new ModelAndView("jsonView");
	//	mav.addObject("model", resp);
	//	return mav;
	//}
	/**
	 * 获取指定院子的期数信息
	 * @param courtyardId  院子id
	 * @return
	 */
	
	//@RequestMapping(value = "getTermList.do")
	public ModelAndView getTermList(long courtyardId){
		List<HouseOwners> houseowners = courtYardService.findTermByCourtyardId(courtyardId);
		TermResp resp = new TermResp();
		for(HouseOwners houseOwner : houseowners){
			TermDto dto = new TermDto(houseOwner.getTerm_id(),houseOwner.getTerm());
			if(!resp.getDatas().contains(dto)){
				resp.getDatas().add(dto);
			}
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 获取指定院子指定期数的楼栋信息
	 * @param courtyardId  院子id 
	 * @param termId  期数id
	 * @return
	 */
	
	//@RequestMapping(value = "getBuildingList.do")
	public ModelAndView getBuildingList(long courtyardId,int termId){
		List<HouseOwners> houseowners = courtYardService.findBuildingsByTermId(courtyardId,termId);
		BuildingsResp resp = new BuildingsResp();
		for(HouseOwners houseOwner : houseowners){
			BuildingsDto dto = new BuildingsDto(houseOwner.getBuilding_id(),houseOwner.getBuilding());
			if(!resp.getDatas().contains(dto)){
				resp.getDatas().add(dto);
			}
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 *
	 */
	/**
	 *  获取指定院子指定期数指定楼栋 单元信息
	 * @param courtyardId  院子id
	 * @param termId  期数id
	 * @param buildingId  楼栋id
	 * @return
	 */
	//@RequestMapping(value = "getUnitList.do")
	public ModelAndView getUnitList(long courtyardId,int termId,int buildingId){
		List<HouseOwners> houseowners = courtYardService.findUnitByBuildings(courtyardId, termId, buildingId);
		UnitResp resp = new UnitResp();
		for(HouseOwners houseOwner : houseowners){
			UnitDto dto = new UnitDto(houseOwner.getUnit_id(),houseOwner.getUnit());
			if(!resp.getDatas().contains(dto)){
				resp.getDatas().add(dto);
			}
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 *  获取指定院子指定期数指定楼栋指定单元房号信息(还有对应的手机号
	 * @param courtyardId
	 * @param termId
	 * @param buildingId
	 * @param unitId
	 * @return
	 */
	//@RequestMapping(value = "getHouList.do")
	public ModelAndView gethouseList(long courtyardId,int termId,int buildingId,int unitId){
		List<HouseOwners> houseowners = courtYardService.findHouseByUnit(courtyardId, termId, buildingId,unitId);
		HouseResp resp = new HouseResp();
		for(HouseOwners houseOwner : houseowners){ 
			String telf = houseOwner.getTel().substring(0,3);
			String tell = houseOwner.getTel().substring(7,11);
			HouseDto dto = new HouseDto(houseOwner.getHouse_id(),houseOwner.getHouse(),telf,tell);
			if(!resp.getDatas().contains(dto)){
				resp.getDatas().add(dto);
			}
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 返回系统设置的职业
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCareers.do")
	public ModelAndView getCareers(){
		CareersResp resp = new CareersResp();
		resp.getDatas().addAll(CareerConfig.getCareers().values());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 返回系统设置兴趣
	 * 
	 * @return
	 */
	@RequestMapping(value = "getInterests.do")
	public ModelAndView getInterests(){
		InterestsResp resp = new InterestsResp();
		resp.getDatas().addAll(InterestConf.getInterests().values());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 返回系统设置的帮帮标签
	 * @return
	 */
	
	@RequestMapping(value = "getTag.do")
	public ModelAndView getTag(){
		TagResp resp = new TagResp();
		resp.getDatas().addAll(TagConfig.getTags().values());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 返回系统设置的举报标签 
	 */
	@RequestMapping(value = "getReportTag.do")
	public ModelAndView getReportTag(){
		ReportTagResp resp = new ReportTagResp();
		resp.getDatas().addAll(ReportConfig.getReports().values());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 返回邀约标签
	 * @return
	 */
	@RequestMapping(value = "getInvitationTag.do")
	public ModelAndView getInvitationTag(){
		InvitationTagResp resp = new InvitationTagResp();
		resp.getDatas().addAll(InvitationConfig.getInterests().values());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 返回话题标签
	 */
	@RequestMapping(value = "getTopicTag.do")
	public ModelAndView getTopicTag(){
	    TopicTagResp resp = new TopicTagResp();
		resp.getDatas().addAll(TopicTagConfig.getTopicTag().values());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 如何获得经验
	 * @return
	 */
	@RequestMapping(value = "getExpInfo.do")
	public ModelAndView getExpInfo(){
		ExpInfoResp  resp = new ExpInfoResp();
		resp.getDatas().addAll(GainExpConfig.getDatas().values());
		resp.setContent(Settings.WORD_MARK_EXP);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 获得学堂的类别标签
	 */
	@RequestMapping(value = "getTrainTag.do")
	public ModelAndView getTrainTag(){
		TrainTagResp resp = new TrainTagResp();
		resp.getDatas().addAll(TrainTagConfig.getTrainTag().values());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 如何赚取领豆
	 */
	@RequestMapping(value = "getLinDouInfo.do")
	public ModelAndView getLinDouInfo(){
		LinDouInfoResp  resp = new LinDouInfoResp();
		resp.getDatas().addAll(GainLindouConfig.getDatas().values());
		resp.setContent(Settings.WORD_MARK_BEANS);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 等级规则
	 *  
	 *  */
	@RequestMapping(value = "getLevelUpInfo.do")
	public ModelAndView geLevelUpInfo(){
		LevelUpInfoResp  resp = new LevelUpInfoResp();
		resp.getDatas().addAll(LevelUpConfig.getDatas().values());
		resp.setContent(Settings.WORD_MARK_RANK);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 修改密码
	 * 
	 * @param currentpw  当前密码
	 * @param newpw   新密码
	 * @param confirmpw   确认密码
	 * @return
	 */
	@RequestMapping(value = "modifypw.do")
	public ModelAndView modifypw(String currentpw, String newpw, String confirmpw){
		if(StringUtils.isBlank(currentpw)){
			throw new GeneralLogicException("请输入当前密码");
		}
		if(StringUtils.isBlank(newpw)){
			throw new GeneralLogicException("请输入新密码");
		}
		if(StringUtils.isBlank(confirmpw)){
			throw new GeneralLogicException("请确认密码");
		}
		if(!newpw.equals(confirmpw)){
			throw new GeneralLogicException("两次输入的密码不一致，请重新输入");
		}
		UserSession userSession = UserSession.get();
		ServiceRegistry.getUserService().modifypw(userSession.getUserId(), 1, currentpw, newpw);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 忘记密码，重设密码
	 * 
	 * @param session
	 * @param validateCode   验证码
	 * @param password   新密码
	 * @return
	 */
	@RequestMapping(value = "forgetpw.do")
	public ModelAndView forgetpw(HttpSession session, String validateCode, String password){
		String sessionValidateCode = (String)session.getAttribute(SessionAttribute.VALIDATE_CODE);
		String tel = (String)session.getAttribute(SessionAttribute.USER_TEL);
		if(StringUtils.isBlank(sessionValidateCode) || StringUtils.isBlank(tel)){
			throw new GeneralLogicException("请先获取验证码");
		}
		if(StringUtils.isBlank(validateCode)){
			throw new GeneralLogicException("验证码不能为空");
		}
		if(!sessionValidateCode.equals(validateCode)){
			throw new GeneralLogicException("验证码不正确，请重新输入");
		}
		if(StringUtils.isBlank(password)){
			throw new GeneralLogicException("密码不能为空");
		}
		User user = ServiceRegistry.getUserService().findUserByTel(tel);
		if(user == null){
			throw new GeneralLogicException("请先注册");
		}
		if(StringUtils.isBlank(user.getPassword())){
			throw new GeneralLogicException("修改密码出错");
		}
		ServiceRegistry.getUserService().modifypw(user.getId(), 2, validateCode, password);
		session.removeAttribute(SessionAttribute.VALIDATE_CODE);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 切换社区
	 */
	@RequestMapping(value = "changeCommunity.do")
	public ModelAndView selectCommunity(long courtyardId){
		UserSession userSession = UserSession.get();
		if(userSession.getUserId()>0){
			 userSession.setCourtyardId(courtyardId);
			 userService.selectCommunity(courtyardId,userSession.getUserId());
			 userSession.setValidate(ServiceRegistry.getValidateUserService().isValidate(userSession.getUserId(), courtyardId));
			 userSession.getCourtyardsPerDistance().clear();
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 黑名单列表
	 *
	 */
	@RequestMapping(value = "blackNameList.do")
	public ModelAndView blackNameList(int current_page, int max_num){
	    BlackNameListResp resp = this.userService.BlackNameList(current_page, max_num);
	    ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model",resp);
		return mav;
	}
	
	
	
	
//	@RequestMapping(value = "upload.do")
	public ModelAndView upload(@RequestParam(required=false)CommonsMultipartFile[] files){
		
		if(files!=null){
//			if(headIcons.length > 1){
//				throw new GeneralLogicException("只能上传一张头像。");
//			}
			System.out.println(files.length+">>>>>>>>>>");
			for(int i=0;i<files.length;i++){
			if(!files[i].isEmpty()){
				String fileName = files[i].getOriginalFilename();
//				fileName = String.valueOf(System.currentTimeMillis());
				File localFile = new File("e:\\test", fileName);
				try{
					files[i].transferTo(localFile);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			}
		}
		
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
//	@RequestMapping(value = "downHead.do")
//	public void download(Long userId,HttpServletRequest request, HttpServletResponse response){
//		response.reset();
//		response.setCharacterEncoding("utf-8");
//        response.setContentType("application/octet-stream");
//        try {
//        	User user = ServiceRegistry.getUserService().findUserById(userId);
//        	if(StringUtils.isBlank(user.getHead_icon())){
//        		throw new GeneralLogicException("没有头像。");
//        	}
//        	response.setHeader("Content-Disposition", "attachment;fileName=\""+user.getHead_icon()+"\"");
//        	File userDire = new File(Settings.HEAD_ICON_DIRE, String.valueOf(user.getId()));
//			if(!userDire.exists()){
//				throw new GeneralLogicException("没有头像。");
//			}
////			fileName = headIcons[0].getOriginalFilename();
//			File localFile = new File(userDire, user.getHead_icon());
//            InputStream inputStream = new FileInputStream(localFile);
// 
//            OutputStream os = response.getOutputStream();
//            byte[] b = new byte[2048];
//            int length;
//            while ((length = inputStream.read(b)) > 0) {
//                os.write(b, 0, length);
//            }
// 
//            os.flush();
//            os.close();
// 
//            inputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//	}
	
}
