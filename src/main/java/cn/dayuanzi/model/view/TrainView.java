package cn.dayuanzi.model.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 学堂视图
 * 
 * @author : leihc
 * @date : 2015年8月17日
 * @version : 1.0
 */
@Entity
@Table(name = "v_train_view")
public class TrainView implements Comparable<TrainView>{

	/**
	 * 学堂ID
	 */
	private long id;
	/**
     * 所属城市
     */
    private long cityId;
	/**
	 * 经度
	 */
	private double longitude;
	/**
	 * 纬度
	 */
	private double latitude;
	
	private double disance;
	
	@Id
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	
	@Transient
	public double getDisance() {
		return disance;
	}
	public void setDisance(double disance) {
		this.disance = disance;
	}
	
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TrainView o) {
		double r = this.disance-o.disance;
		if(r>0){
			return 1;
		}else if(r<0){
			return -1;
		}
		return 0;
	}
}
