package cn.dayuanzi.resp.dto;

public class VersionCheckeDto {

	
	/**
	 * 版本号 
	 */
	private String version ;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 版本更新内容
	 */
	private String content;
	
	public VersionCheckeDto(){
		
	}
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
