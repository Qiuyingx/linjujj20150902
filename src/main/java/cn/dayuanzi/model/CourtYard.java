package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.dayuanzi.util.YardUtils;
import cn.framework.persist.db.VersionPersistentSupport;

/**
 * 院子信息表，社区
 * 
 * @author : leihc
 * @date : 2015年4月15日 上午11:02:19
 * @version : 1.0
 */
@Entity
@Table(name = "t_courtyard")
public class CourtYard extends VersionPersistentSupport {

	/**
	 * 院子名称
	 */
	private String courtyard_name;
	
	/**
	 * 城市ID
	 */
	private int city_id;
	
	/**
	 * 物业公司ID
	 */
	private long company_id;
	
	/**
	 * 院子地址
	 */
	private String address;
	/**
	 * 是否合作
	 */
	private  int ally;
	/**
	 * 经度
	 */
	private double longitude;
	/**
	 * 纬度
	 */
	private double latitude;
	
	/**
	 * 判断目标点是否在指定范围内
	 * 
	 * @param distance 距离，单位米
	 * @param targetLgt 目标点经度
	 * @param targetLat 目标点纬度
	 * @return
	 */
	public boolean atRange(double distance, double targetLgt, double targetLat){
		return YardUtils.getDistance(this.longitude, this.latitude, targetLgt, targetLat) < distance;
	}

	public String getCourtyard_name() {
		return courtyard_name;
	}
	/**
	 * 
	 * @param courtyard_name
	 */
	public void setCourtyard_name(String courtyard_name) {
		this.courtyard_name = courtyard_name;
	}

	/**
	 * @return the city_id
	 */
	public int getCity_id() {
		return city_id;
	}

	/**
	 * @param city_id the city_id to set
	 */
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	/**
	 * @return the company_id
	 */
	public long getCompany_id() {
		return company_id;
	}

	/**
	 * @param company_id the company_id to set
	 */
	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	public void setAlly(int ally) {
		this.ally = ally;
	}

	public int getAlly() {
		return ally;
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
	
	
}
