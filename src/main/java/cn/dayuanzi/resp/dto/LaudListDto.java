/**
 * 
 */
package cn.dayuanzi.resp.dto;


import cn.dayuanzi.model.User;

/** 
 * @ClassName: LaudListDto 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月27日 下午7:25:05 
 *  
 */

public class LaudListDto {
    	
    	/**
	 * 点赞者ID
	 */
	private long lauder_id;
	/**
	 * 点赞者昵称
	 */
	private String lauder_nickname;
	/**
	 * 点赞者头像
	 */
	private String lauder_head;
	/**
	 * 信息
	 */
	private String info;
	
	public LaudListDto(){
	    
	}
	public LaudListDto(User user){
	    this.lauder_head = user.getHead_icon();
	    this.lauder_id = user.getId();
	    this.lauder_nickname = user.getNickname();
	    
	}
	
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public long getLauder_id() {
	    return lauder_id;
	}
	public void setLauder_id(long lauder_id) {
	    this.lauder_id = lauder_id;
	}
	public String getLauder_nickname() {
	    return lauder_nickname;
	}
	public void setLauder_nickname(String lauder_nickname) {
	    this.lauder_nickname = lauder_nickname;
	}
	public String getLauder_head() {
	    return lauder_head;
	}
	public void setLauder_head(String lauder_head) {
	    this.lauder_head = lauder_head;
	}
	
	
	
	
}
