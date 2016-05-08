package cn.dayuanzi.resp.dto;


import cn.dayuanzi.model.ActivityInfo;
import cn.dayuanzi.util.DateTimeUtil;


/**
 * 
 * @author qiuyingxiang
 *
 */
public class ActivityDto {
	/**
	 * 活动ID
	 */
	private long activityId;
	/**
	 * 活动社区ID
	 */
	private long courtyard_id;
	/**
	 * 活动标题
	 */
	private String activity_title;
	/**
	 * 活动有效期的起始时间
	 */
	private String startTime;
	/**
	 * 活动有效期的截止时间
	 */
	private String endTime;
	/**
	 * 活动内容
	 */
	private String content;
	/**
	 * 活动发布时间
	 */
	private String createTime;
	/**
	 * 活动图文标题 
	 */
	private String image;
	
	/**
	 * 该用户时候参加该活动
	 */
	private boolean join ;
	
	/**
	 * 是否点赞过
	 */
	private boolean lauded;
	/**
	 * 是否结束
	 */
	private boolean ended;
	
	
	public ActivityDto(){
		
	}
	
	public ActivityDto( ActivityInfo activityInfo){
		this.activityId = activityInfo.getId();
		this.activity_title = activityInfo.getActivity_title();
		this.createTime = DateTimeUtil.formatDateTime(activityInfo.getCreateTime());
		this.startTime = DateTimeUtil.formatDateTime(activityInfo.getStartTime());
		this.endTime =DateTimeUtil.formatDateTime(activityInfo.getEndTime());
		this.courtyard_id =activityInfo.getCourtyard_id();
		this.image = activityInfo.getImage();
		this.ended = System.currentTimeMillis()>activityInfo.getEndTime().getTime();
	}
	
	
	
	
	
	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public boolean isJoin() {
		return join;
	}

	public void setJoin(boolean join) {
		this.join = join;
	}

	public boolean isLauded() {
		return lauded;
	}

	public void setLauded(boolean lauded) {
		this.lauded = lauded;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public long getCourtyard_id() {
		return courtyard_id;
	}
	public void setCourtyard_id(long courtyard_id) {
		this.courtyard_id = courtyard_id;
	}
	public String getActivity_title() {
		return activity_title;
	}
	public void setActivity_title(String activity_title) {
		this.activity_title = activity_title;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	
	
}
