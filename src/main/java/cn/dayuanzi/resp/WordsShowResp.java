package cn.dayuanzi.resp;

/**
 * 
 * @author : xiaym
 * @date : 2015年6月18日 上午3:55:11
 * @version : 1.0
 */
public class WordsShowResp  extends Resp{
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public WordsShowResp(String content) {
		this.content = content;
	}
	
	
}
