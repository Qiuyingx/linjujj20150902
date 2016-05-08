/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.List;

import cn.dayuanzi.model.TrainInfo;
import cn.dayuanzi.resp.dto.LaudListDto;
import cn.dayuanzi.resp.dto.TrainDto;


/** 
 * @ClassName: TrainDetailResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年8月13日 下午1:38:12 
 *  
 */

public class TrainDetailResp extends Resp {
	
	/**
     * 用户id
     */
    private long userId;
    /**
     * 用户头像
     */
    private String head_icon;
    /**
     * 昵称
     */
    private String name;
	 /**
     * 标题
     */
    private String title;
    /**
     * 简述
     */
    private String description;
	/**
     * 内容
     */
    private String content;
    /**
     * banner 图
     */
    private String banner_img;
    /**
     * 营业时间
     */
    private String businessHours;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 浏览量
     */
    private long views;
    /**
     * 学堂地址
     */
    private String address;
    /**
	 * 是否点赞过
	 */
	private boolean lauded;
	/**
	 * 点赞人数
	 */
	private long laudAmount;
	/**
	 * 感谢列表
	 */
	private List<LaudListDto> laudList; 
	/**
	 * 推荐
	 */
	private  List<TrainDto> trainlist ;
	
	public TrainDetailResp(){
		
	}
	public TrainDetailResp(TrainInfo train){
		this.address = train.getAddress();
		this.banner_img = train.getBanner_img();
		this.title = train.getTitle();
		this.businessHours = train.getBusinessHours();
		this.content = train.getContent();
		this.description = train.getDescription();
		this.userId = train.getUserId();
		this.tel = train.getTel();
		
	}

	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getHead_icon() {
		return head_icon;
	}
	public void setHead_icon(String head_icon) {
		this.head_icon = head_icon;
	}
	public List<TrainDto> getTrainlist() {
		return trainlist;
	}
	public void setTrainlist(List<TrainDto> trainlist) {
		this.trainlist = trainlist;
	}
	public List<LaudListDto> getLaudList() {
		return laudList;
	}
	public void setLaudList(List<LaudListDto> laudList) {
		this.laudList = laudList;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBanner_img() {
		return banner_img;
	}

	public void setBanner_img(String banner_img) {
		this.banner_img = banner_img;
	}

	public String getBusinessHours() {
		return businessHours;
	}

	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isLauded() {
		return lauded;
	}

	public void setLauded(boolean lauded) {
		this.lauded = lauded;
	}

	public long getLaudAmount() {
		return laudAmount;
	}

	public void setLaudAmount(long laudAmount) {
		this.laudAmount = laudAmount;
	}
	
}
