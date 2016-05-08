package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.PersistentSupport;

/**
 * 置顶帖子
 * 
 * @author : leihc
 * @date : 2015年8月4日
 * @version : 1.0
 */
@Entity
@Table(name = "t_top_post")
public class TopPost extends PersistentSupport {

	/**
	 * 帖子ID
	 */
	private long post_id;
	/**
	 * 社区
	 */
	private long courtyard_id;
	/**
	 * 置顶类型0 本社区置顶 1 周边置顶
	 */
	private int topType;
	/**
	 * 优先级，数字越大，排前面
	 */
	private int priority;
	
	public long getPost_id() {
		return post_id;
	}
	public void setPost_id(long post_id) {
		this.post_id = post_id;
	}
	public long getCourtyard_id() {
		return courtyard_id;
	}
	public void setCourtyard_id(long courtyard_id) {
		this.courtyard_id = courtyard_id;
	}
	public int getTopType() {
		return topType;
	}
	public void setTopType(int topType) {
		this.topType = topType;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
}
