package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.PersistentSupport;

/**
 * 
 * @author : leihc
 * @date : 2015年7月1日
 * @version : 1.0
 */
@Entity
@Table(name = "t_user_follow")
public class UserFollow extends PersistentSupport {

	/**
	 * 用户id
	 */
	private long user_id;
	/**
	 * 关注对象
	 */
	private long target_id;
	/**
	 * 创建时间
	 */
	private long create_time;
	
	public UserFollow(){
		
	}
	
	public UserFollow(long user_id, long target_id){
		this.user_id = user_id;
		this.target_id = target_id;
		this.create_time = System.currentTimeMillis();
	}
	
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getTarget_id() {
		return target_id;
	}
	public void setTarget_id(long target_id) {
		this.target_id = target_id;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	
	
}
