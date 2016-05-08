package cn.dayuanzi.common.huanxin;

/**
 * 
 * @author : leihc
 * @date : 2015年4月26日 下午4:19:12
 * @version : 1.0
 */
public class ResponseAndStatus {

	/**
	 * 返回的状态码
	 */
	private int statusCode;
	
	/**
	 * 返回的数据
	 */
	private String content;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
