/**
 * 
 */
package cn.dayuanzi.resp;

import cn.dayuanzi.model.OpenTrain;

/** 
 * @ClassName: OpenTrain 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年8月16日 上午11:36:45 
 *  
 */

public class OpenTrainResp extends Resp{
	
	 private long categoryId;
	/**
     *类别 
     */
    private String category;
    /**
     * 介绍
     */
    private String introduction;
    /**
     * 手机号
     */
    private String  tel;
    /**
     *申请状态，0已申请 1未通过 2已通过 3
     */
    private int passed;
    /**
     * 邮箱
     */
    private String email;
    /**
     * id
     */
    private long myShopId;
    
    public OpenTrainResp(){
    	
    	
    }
    public OpenTrainResp(OpenTrain op) {
    	
    	this.email = op.getEmail();
    	this.passed = op.getPassed();
    	this.tel = op.getTel();
    	this.introduction = op.getIntroduction();
    	
    	
    	
    }
    
    
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public long getMyShopId() {
		return myShopId;
	}
	public void setMyShopId(long myShopId) {
		this.myShopId = myShopId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
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
	public int getPassed() {
		return passed;
	}
	public void setPassed(int passed) {
		this.passed = passed;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
    
    
}
