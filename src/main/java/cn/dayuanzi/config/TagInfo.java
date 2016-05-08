package cn.dayuanzi.config;

/**
 * 邻居帮帮标签
 * 
 * @author : leihc
 * @date : 2015年5月12日
 * @version : 1.0
 */
public class TagInfo {

	/**
	 * 标签代号
	 */
	private int id;
	/**
	 * 标签名称
	 */
	private String name;
	
	public TagInfo(int id, String name){
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
