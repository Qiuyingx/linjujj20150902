/**
 * 
 */
package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.PersistentSupport;

/** 
 * @ClassName: ContentEntityLaud 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月15日 上午10:59:37 
 *  
 */
@Entity
@Table(name = "t_content_laud")
public class ContentEntityLaud extends PersistentSupport {
    	/**
	 * 专题ID
	 */
	private long content_id;
	/**
	 * 点赞用户ID
	 */
	private long user_id;
	
	/**
	 * 院子ID
	 */
	private long courtyard_id;
	/**
	 * 点赞时间
	 */
	private long create_time;
	
	
	public  ContentEntityLaud (){
		
	}
	public ContentEntityLaud(long userId,long courtyardId,long contentEntityId){
		this.content_id =  contentEntityId;
		this.courtyard_id = courtyardId;
		this.create_time = System.currentTimeMillis();
		this.user_id = userId;
	}
	

	
	public long getContent_id() {
	    return content_id;
	}
	public void setContent_id(long content_id) {
	    this.content_id = content_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getCourtyard_id() {
		return courtyard_id;
	}
	public void setCourtyard_id(long courtyard_id) {
		this.courtyard_id = courtyard_id;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	
}
