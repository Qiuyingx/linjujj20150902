package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.VersionPersistentSupport;

/**
 * 内容，邻聚专题
 * @author : xiaym
 * @date : 2015年7月3日 下午4:31:01
 * @version : 1.0
 */
@Entity
@Table(name = "t_content")
public class ContentEntity extends VersionPersistentSupport{

	/**
	 * 创建者ID
	 */
	private long managerId;
	/**
	 * 作者ID
	 */
	private int authorId;
	/**
	 * 城市ID (0表示所有城市)
	 */
	private int cityId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 简述
	 */
	private String description;
	/**
	 * 封面图
	 */
	private String title_img;
	/**
	 * banner 图
	 */
	private String banner_img;
	
	/**
	 * 发布时间
	 */
	private long create_time;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 栏目ID
	 */
	private int channel_id;
	/**
	 * 状态 (0草稿 1发布 2删除)
	 */
	private int status;
	/**
	 * 浏览量
	 */
	private long views;
	/**
	 * 点赞数
	 */
	private long   ups;
	/**
	 * 评论数
	 */
	private long comments;
	
	
	public String getBanner_img() {
	    return banner_img;
	}
	public void setBanner_img(String banner_img) {
	    this.banner_img = banner_img;
	}
	public long getManagerId() {
		return managerId;
	}
	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle_img() {
		return title_img;
	}
	public void setTitle_img(String title_img) {
		this.title_img = title_img;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public int getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}
	public long getViews() {
	    return views;
	}
	public void setViews(long views) {
	    this.views = views;
	}
	public long getUps() {
	    return ups;
	}
	public void setUps(long ups) {
	    this.ups = ups;
	}
	public long getComments() {
	    return comments;
	}
	public void setComments(long comments) {
	    this.comments = comments;
	}
	
}
