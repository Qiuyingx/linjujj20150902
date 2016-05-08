package cn.dayuanzi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.LaudListResp;
import cn.dayuanzi.resp.RecommendResp;
import cn.dayuanzi.service.IUserService;

/**
 * 发现模块相关
 * 
 * @author : leihc
 * @date : 2015年5月3日
 * @version : 1.0
 */
@Controller
public class FindController {


	@Autowired
	private IUserService userService;

	/**
	 * 获得推荐的邻居
	 * 
	 * @return
	 */
	@RequestMapping(value = "getRecommend.do")
	public ModelAndView getRecommend(int current_page, int max_num){
		UserSession userSession = UserSession.get();
		RecommendResp resp = userService.getRecommends(userSession.getUserId(), userSession.getCourtyardId(),current_page,max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 搜索邻居
	 * @param nickname
	 * @param type 1 搜索邻居 2 所有小区用户
	 * @return
	 */
	@RequestMapping(value = "searchNeighbor.do")
	public ModelAndView searchNeighbor(String nickname,int type){
		UserSession userSession = UserSession.get();
		RecommendResp resp = userService.searchNeighbor(userSession.getUserId(), userSession.getCourtyardId(),nickname,type);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 可以At的用户列表 (本小区邻居，我关注的人)
	 */
	@RequestMapping(value = "searchUser.do")
	public ModelAndView searchUser(int current_page, int max_num){
	    UserSession userSession = UserSession.get();
	    LaudListResp resp = this.userService.findUserfollow(current_page,max_num,userSession.getUserId(),userSession.getCourtyardId());
	    ModelAndView mav = new ModelAndView("jsonView");
	    mav.addObject("model", resp);
	    return mav;
	}
	
	
}
