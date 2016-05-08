/**
 * 
 */
package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.VersionPersistentSupport;

/** 
 * @ClassName: OpenShop 
 * @Description:  商家申请开店信息
 * @author qiuyingxiang
 * @date 2015年8月11日 下午2:17:46 
 *  
 */
@Entity
@Table(name = "t_train_open")
public class OpenTrain extends VersionPersistentSupport{
    
    /**
     * 申请用户id
     */
    private long userId;
    /**
     * 所属院子
     */
    private long courtyardId;
    /**
     *类别 
     */
    private int category;
    /**
     * 介绍
     */
    private String introduction;
    /**
     * 手机号
     */
    private String  tel;
    /**
     *申请状态，0申请 1通过 2未通过 
     */
    private int passed;
    /**
     * 邮箱
     */
    private String email;
	/**
	 * 创建时间
	 */
	private long create_time;
    
    public OpenTrain(){
	
    }
    public OpenTrain(int category, String introduction,String  tel,long courtyardId,long userId,String mail){
    	this.category = category;
    	this.introduction = introduction;
    	this.tel = tel ;
    	this.courtyardId = courtyardId ;
    	this.userId = userId ;
    	this.email = mail;
    	this.create_time = System.currentTimeMillis();
    }

    
  
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getPassed() {
		return passed;
	}
	public void setPassed(int passed) {
		this.passed = passed;
	}
	
    
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getCourtyardId() {
        return courtyardId;
    }

    public void setCourtyardId(long courtyardId) {
        this.courtyardId = courtyardId;
    }

   

    public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    
    
}
