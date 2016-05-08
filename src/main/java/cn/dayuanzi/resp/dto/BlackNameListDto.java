/**
 * 
 */
package cn.dayuanzi.resp.dto;


import cn.dayuanzi.model.BlackList;
import cn.dayuanzi.util.DateTimeUtil;

/** 
 * @ClassName: BlackNameListDto 
 * @Description: 黑名单信息
 * @author qiuyingxiang
 * @date 2015年7月9日 上午10:23:10 
 *  
 */


public class BlackNameListDto {
    
    /**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 联系人昵称
	 */
	private String userName;
	
	/**
	 * 联系人头像
	 */
	private String userHead;
	/**
	 * 拉黑时间
	 */
	private String create_time;
	
	public  BlackNameListDto(){
	    
	}
	public BlackNameListDto(BlackList blackList,String head,String name){
	    this.userId = blackList.getTarget_id();
	    this.create_time = DateTimeUtil.formatDateTime(blackList.getCreate_time());
	    this.userHead = head ;
	    this.userName =name ;
	}
	public long getUserId() {
	    return userId;
	}
	public void setUserId(long userId) {
	    this.userId = userId;
	}
	public String getUserName() {
	    return userName;
	}
	public void setUserName(String userName) {
	    this.userName = userName;
	}
	public String getUserHead() {
	    return userHead;
	}
	public void setUserHead(String userHead) {
	    this.userHead = userHead;
	}
	public String getCreate_time() {
	    return create_time;
	}
	public void setCreate_time(String create_time) {
	    this.create_time = create_time;
	}
	
}
