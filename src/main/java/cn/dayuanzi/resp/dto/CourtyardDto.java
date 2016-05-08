package cn.dayuanzi.resp.dto;

import cn.dayuanzi.model.CourtYard;

/**
 * 
 * @author : leihc
 * @date : 2015年4月29日
 * @version : 1.0
 */
public class CourtyardDto implements Comparable<CourtyardDto>{

	/**
	 * 院子的ID
	 */
	private long courtyard_id;
	/**
	 * 院子的名称
	 */
	private String courtyard_name;
	/**
	 * 
	 * 该院子是否与物业合作
	 */
	private int courtyard_ally; 
	/**
	 * 该院子与用户当前位置的距离
	 */
	private double distance;
	/**
	 * 经度
	 */
	private double longitude;
	/**
	 * 纬度
	 */
	private double latitude;
	
	public CourtyardDto(CourtYard courtyard){
		this.courtyard_id = courtyard.getId();
		this.courtyard_name = courtyard.getCourtyard_name();
		this.courtyard_ally = courtyard.getAlly();
		this.latitude = courtyard.getLatitude();
		this.longitude = courtyard.getLongitude();
	}
	
	
	public long getCourtyard_id() {
		return courtyard_id;
	}
	public void setCourtyard_id(long courtyard_id) {
		this.courtyard_id = courtyard_id;
	}
	public String getCourtyard_name() {
		return courtyard_name;
	}
	public void setCourtyard_name(String courtyard_name) {
		this.courtyard_name = courtyard_name;
	}
	public int getCourtyard_ally() {
		return courtyard_ally;
	}
	public void setCourtyard_ally(int courtyard_ally) {
		this.courtyard_ally = courtyard_ally;
	}

	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
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

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(CourtyardDto o) {
		double r = this.distance-o.distance;
		if(r<0){
			return -1;
		}else if(r>0){
			return 1;
		}
		return 0;
	}
}
