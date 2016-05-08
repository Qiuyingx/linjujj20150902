package cn.dayuanzi.util;

/**
 * 
 * @author : leihc
 * @date : 2015年6月2日
 * @version : 1.0
 */
public class Badge {

	private long userId;
	
	private int amount;

	public Badge(long userId){
		this.userId = userId;
		this.amount = 1;
	}
	
	public Badge(long userId, int amount){
		this.userId = userId;
		this.amount = amount;
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	
}
