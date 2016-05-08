package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.VersionPersistentSupport;

/**
 * 栏目
 * @author : xiaym
 * @date : 2015年7月3日 下午4:30:53
 * @version : 1.0
 */
@Entity
@Table(name = "t_channel_entity")
public class ChannelEntity extends VersionPersistentSupport{

	/**
	 * 创建者ID
	 */
	private long managerId;
	/**
	 * 栏目名称
	 */
	private int channel_name;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 创建时间
	 */
	private long create_time;
	
	public long getManagerId() {
		return managerId;
	}
	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(int channel_name) {
		this.channel_name = channel_name;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	
	
}
