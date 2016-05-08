package cn.dayuanzi.config;

/**
 * 
 * @author : leihc
 * @date : 2015年6月9日
 * @version : 1.0
 */
public class GoodsInfo {

	/**
	 * 商品代号
	 */
	private int id;
	/**
	 * 商品名
	 */
	private String goodsName;
	/**
	 * 商品单价
	 */
	private int price;
	/**
	 * 商品描述
	 */
	private String remark;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
