/**
 * 
 */
package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.VersionPersistentSupport;

/** 
 * @ClassName: ShopInfo 
 * @Description: 学堂
 * @date 2015年8月11日 下午3:59:53 
 *  
 */
@Entity
@Table(name = "t_train_info")
public class TrainInfo extends VersionPersistentSupport{
    
    /**
     * 用户id
     */
    private long userId;
    /**
     * 所属院子
     */
    private long courtyardId;
    /**
     * 所属城市
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
     * 内容
     */
    private String content;
    /**
     * banner 图
     */
    private String banner_img;
    /**
     * 营业时间
     */
    private String businessHours;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 浏览量
     */
    private long views;
    /**
     * 学堂地址
     */
    private String address;
    /**
	 * 经度
	 */
	private double longitude;
	/**
	 * 纬度
	 */
	private double latitude;
	/**
	 *类别
	 */
	private int category;
	/**
	 *类别
	 */
	private String categoryName;
	/**
	 * 状态'0未完善资料 1开通  2下线（禁用）
	 */
	private int status ;
 
    
    public TrainInfo(){
	
    }
    
    
    
    public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}



	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getBusinessHours() {
		return businessHours;
	}

	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCourtyardId() {
        return courtyardId;
    }

    public void setCourtyardId(long courtyardId) {
        this.courtyardId = courtyardId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    
}
