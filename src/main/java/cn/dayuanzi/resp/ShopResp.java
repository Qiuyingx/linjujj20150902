/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.List;

import cn.dayuanzi.model.Shop;

/** 
 * @ClassName: ShowResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月11日 下午1:40:57 
 *  
 */

public class ShopResp extends Resp{
	
	/**
	 * 商品id
	 */
	private long  goodsId;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品价格
	 */
	private int price;
	/**
	 * 库存限制，0表示无限制
	 */
	private int stockLimit;
	/**
	 * 图片
	 */
	private List<String>  image;
	/**
	 * 商品描述
	 */
	private String remark;
	
	/**
	 * 商品状态，0正常 1下架
	 */
	private int status;
	/**
	 * 多少人购买
	 */
	private long count;
	
	public ShopResp(){
		
	}
	
	public ShopResp(Shop shop){
		this.goodsId = shop.getId();
		this.goodsName = shop.getGoodsName();
		this.image = shop.getListImage();
		this.price = shop.getPrice();
		this.remark = shop.getRemark();
		this.status = shop.getStatus();
		this.stockLimit = shop.getStockLimit();
	}

	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
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

	public int getStockLimit() {
		return stockLimit;
	}

	public void setStockLimit(int stockLimit) {
		this.stockLimit = stockLimit;
	}

	
	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
