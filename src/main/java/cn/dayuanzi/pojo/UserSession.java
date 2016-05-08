package cn.dayuanzi.pojo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dayuanzi.model.User;
import cn.dayuanzi.service.impl.ServiceRegistry;

/**
 *
 * @author : lhc
 * @date : 2015年4月9日 上午11:07:23
 * @version : 1.0
 */
public class UserSession {
	
	private static ThreadLocal<UserSession> local = new ThreadLocal<UserSession>();
	
	public static void set(UserSession sessionUser){
		local.set(sessionUser);
	}
	
	public static UserSession get(){
		return local.get();
	}
	
	public static void clear(){
		local.remove();
	}
	
	/**
	 * 当前所在院子ID
	 */
	private long courtyardId;
	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 用户电话
	 */
	private String tel;
	/**
	 * 注册时间
	 */
	private long registerTime;
	
	/**
	 * 是否是验证用户
	 */
	private boolean validate;
	/**
	 * 每个距离范围内的社区集合
	 */
	private Map<Integer, List<Long>> courtyardsPerDistance = new HashMap<Integer, List<Long>>();
	
	public UserSession(User user){
		this.userId = user.getId();
		this.tel = user.getTel();
		this.courtyardId = user.getCourtyard_id();
		this.registerTime = user.getRegister_time();
		this.validate = ServiceRegistry.getValidateUserService().isValidate(userId, courtyardId);
	}
	
	/**
	 * 
	 * @param courtyardId
	 */
	public UserSession(long courtyardId){
		this.courtyardId = courtyardId;
	}

	/**
	 * 判断是否是访客
	 * @return
	 */
	public boolean isVisitor(){
		return this.userId==0;
	}
	
	/**
	 * 判断用户是否通过验证
	 * @return
	 */
	public boolean isValidatedUser(){
		return validate;
	}
	
	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	public long getCourtyardId() {
		return courtyardId;
	}

	public void setCourtyardId(long courtyardId) {
		this.courtyardId = courtyardId;
	}

	public Map<Integer, List<Long>> getCourtyardsPerDistance() {
		return courtyardsPerDistance;
	}

	public void setCourtyardsPerDistance(
			Map<Integer, List<Long>> courtyardsPerDistance) {
		this.courtyardsPerDistance = courtyardsPerDistance;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	
	
	
}
