package cn.dayuanzi.resp.dto;

/**
 * 
 * @author : leihc
 * @date : 2015年5月6日
 * @version : 1.0
 */
public class InterestDto {

	/**
	 * 兴趣代号
	 */
	private int id;
	/**
	 * 兴趣
	 */
	private String interest;
	
	
	public InterestDto(int id, String interest){
		this.id = id;
		this.interest = interest;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	
	
}
