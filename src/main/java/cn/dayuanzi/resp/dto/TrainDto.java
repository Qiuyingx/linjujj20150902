/**
 * 
 */
package cn.dayuanzi.resp.dto;

import cn.dayuanzi.model.TrainInfo;

/** 
 * @ClassName: TrainDto 
 * @Description: 学堂
 * @author qiuyingxiang
 * @date 2015年8月12日 下午3:12:10 
 *  
 */

public class TrainDto implements Comparable<TrainDto> {
	/**
     * 用户id
     */
    private long userId;
    /**
     * 用户头像
     */
    private String head_icon;
    /**
     * 培训室 id
     * 
     */
    private long trainId;
    /**
     * 标题
     */
    private String title;
    /**
     * 简述
     */
    private String description;
    /**
     * banner 图
     */
    private String banner_img;
    /**
     * 浏览量
     */
    private long views;
    /**
     *距离
     */
    private double range;
    /**
     * 地址
     */
    private String address;
    
    public TrainDto(){
    	
    }
    public TrainDto(TrainInfo train){
    	this.banner_img = train.getBanner_img();
    	this.title = train.getTitle();
    	this.description = train.getDescription();
    	this.views = train.getViews();
    	this.trainId = train.getId();
    	this.address = train.getAddress();
    	this.userId = train.getUserId();
    }
    
    
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getHead_icon() {
		return head_icon;
	}

	public void setHead_icon(String head_icon) {
		this.head_icon = head_icon;
	}

	public long getTrainId() {
		return trainId;
	}

	public void setTrainId(long trainId) {
		this.trainId = trainId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBanner_img() {
		return banner_img;
	}

	public void setBanner_img(String banner_img) {
		this.banner_img = banner_img;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}
    

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TrainDto o) {
		double r = this.range-o.range;
		if(r<0){
			return -1;
		}else if(r>0){
			return 1;
		}
		return 0;
	}
	
	
}
