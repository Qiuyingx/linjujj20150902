/**
 * 
 */
package cn.dayuanzi.config;

/** 
 * @ClassName: TopicTagInfo 
 * @Description: 话题标签
 * @author qiuyingxiang
 * @date 2015年7月27日 下午8:41:56 
 *  
 */

public class TopicTagInfo {

    	/**
	 * 标签代号
	 */
	private int id;
	/**
	 * 标签名称
	 */
	private String name;
	
	public TopicTagInfo(int id, String name){
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
