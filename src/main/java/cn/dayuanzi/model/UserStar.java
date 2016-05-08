package cn.dayuanzi.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import cn.framework.persist.db.VersionPersistentSupport;

/**
 * 达人
 * @author : leihc
 * @date : 2015年7月28日
 * @version : 1.0
 */
@Entity
@Table(name = "t_user_star")
public class UserStar extends VersionPersistentSupport {

	/**
	 * 用户ID
	 */
	private long user_id;
	/**
	 * 联系电话
	 */
	private String tel;
	/**
	 * 申请内容
	 */
	private String content;
	/**
	 * 发布的图片保存本地路径
	 */
	private List<String> image_names;
	/**
	 * 职业技能
	 */
	private String skill;
	/**
	 * 0 申请 1 通过 2 审核不通过
	 */
	private int status;
	/**
	 * 申请时间
	 */
	private long create_time;
	
	public UserStar(){
		this.create_time = System.currentTimeMillis();
	}
	
	public UserStar(long userId,String tel,String content){
		this.user_id = userId;
		this.tel = tel;
		this.content = content;
		this.create_time = System.currentTimeMillis();
	}
	
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Type(type = "cn.framework.persist.db.type.StringList")
	public List<String> getImage_names() {
		return image_names;
	}
	public void setImage_names(List<String> image_names) {
		this.image_names = image_names;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}
	
}
