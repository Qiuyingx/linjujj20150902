package cn.dayuanzi.util;

/**
 * 地理范围
 * 
 * @author : leihc
 * @date : 2015年8月11日
 * @version : 1.0
 */
public class GeoRange {

	/**
	 * 最小经度
	 */
	private double minLgt;
	/**
	 * 最大经度
	 */
	private double maxLgt;
	/**
	 * 最小纬度
	 */
	private double minLat;
	/**
	 * 最大纬度
	 */
	private double maxLat;
	
	
	public GeoRange(double minLgt, double maxLgt, double minLat, double maxLat){
		this.minLgt = minLgt;
		this.maxLgt = maxLgt;
		this.minLat = minLat;
		this.maxLat = maxLat;
	}
	
	public double getMinLgt() {
		return minLgt;
	}
	public void setMinLgt(double minLgt) {
		this.minLgt = minLgt;
	}
	public double getMaxLgt() {
		return maxLgt;
	}
	public void setMaxLgt(double maxLgt) {
		this.maxLgt = maxLgt;
	}
	public double getMinLat() {
		return minLat;
	}
	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}
	public double getMaxLat() {
		return maxLat;
	}
	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}
	
	
}
