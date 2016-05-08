/**
 * 
 */
package cn.dayuanzi.config;

/** 
 * @ClassName: TrainTagInfo 
 * @Description: 学堂类别
 * @author qiuyingxiang
 * @date 2015年8月18日 上午10:09:50 
 *  
 */

public class TrainTagInfo {

 	/**
	 * 标签代号
	 */
	private int id;
	/**
	 * 标签名称
	 */
	private String name;
	
	public TrainTagInfo(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
