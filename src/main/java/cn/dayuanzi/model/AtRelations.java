package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.dayuanzi.pojo.ContentType;
import cn.framework.persist.db.PersistentSupport;

/**
 * 
 * @author : leihc
 * @date : 2015年7月26日
 * @version : 1.0
 */
@Entity
@Table(name = "t_at_relations")
public class AtRelations extends PersistentSupport{

	/**
	 * 发艾特的用户
	 */
	private long user_id;
	/**
	 * 艾特对象的ID
	 */
	private long at_target_id;
	/**
	 * 艾特对象的昵称
	 */
	private String at_nickname;
	/**
	 * 发艾特的场景，{@link ContentType}
	 */
	private int scene;
	/**
	 * 附加信息，可能是话题，邀约等的ID
	 */
	private long append;
	/**
	 * 发艾特的时间
	 */
	private long create_time;
	
	public AtRelations(){
		this.create_time = System.currentTimeMillis();
	}
	
	public AtRelations(long user_id,User at_target){
		this.user_id = user_id;
		this.at_target_id = at_target.getId();
		this.at_nickname = at_target.getNickname();
		this.create_time = System.currentTimeMillis();
	}
	
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getAt_target_id() {
		return at_target_id;
	}
	public void setAt_target_id(long at_target_id) {
		this.at_target_id = at_target_id;
	}
	public int getScene() {
		return scene;
	}
	public void setScene(int scene) {
		this.scene = scene;
	}
	public long getAppend() {
		return append;
	}
	public void setAppend(long append) {
		this.append = append;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getAt_nickname() {
		return at_nickname;
	}

	public void setAt_nickname(String at_nickname) {
		this.at_nickname = at_nickname;
	}
	
	
}
