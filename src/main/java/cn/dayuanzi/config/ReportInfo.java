package cn.dayuanzi.config;

/**
 * 
* @ClassName: ReportInfo 
* @Description: TODO
* @author qiuyingxiang
* @date 2015年5月25日 上午12:11:46 
*
 */

public class ReportInfo {
	
	
	/**
	 * 举报标签代号
	 */
	private int id;
	/**
	 * 举报标签名称
	 */
	private String name;
	
	public ReportInfo(int id, String name){
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
