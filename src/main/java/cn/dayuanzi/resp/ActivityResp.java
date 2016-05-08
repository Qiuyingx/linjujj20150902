package cn.dayuanzi.resp;

import java.util.List;

import cn.dayuanzi.model.ActivityInfo;
import cn.dayuanzi.resp.dto.ActivityReplyDto;
import cn.dayuanzi.resp.dto.LaudListDto;
import cn.dayuanzi.util.DateTimeUtil;






/**
 * 活动信息返回
 * @author qiuyingxiang
 *
 */
public class ActivityResp extends Resp{
	
	/**
	 * 活动社区ID
	 */
	private long courtyard_id;
	/**
	 * 活动标题
	 */
	private String activity_title;
	/**
	 * 简述
	 */
	private String description;
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
	 * 该用户是否参加该活动
	 */
	private boolean join ;
	
	/**
	 * 是否点赞过
	 */
	private boolean lauded;
	/**
	 * 点赞人数
	 */
	private long laudAmount;
	/**
	 * 评论总人数
	 */
	private long replyAmount;
	/**
	 * 是否结束
	 */
	private boolean ended;
	/**
	 * 不能报名
	 */
	private boolean signDisabled;
	/**
	 * 人数限制
	 */
	private int peoplesLimit;
	/**
	 * 报名人数是否已满
	 */
	private boolean peoplesFull;
	
	private List<ActivityReplyDto> replys;
	
	private List<Object[]> ats = null;
	/**
	 * 感谢列表
	 */
	private List<LaudListDto> laudList; 
	
	public ActivityResp(){
		
	}
	
	public ActivityResp(ActivityInfo activityInfo){
		this.courtyard_id = activityInfo.getCourtyard_id();
		this.activity_title = activityInfo.getActivity_title();
		this.content = activityInfo.getContent();
		this.createTime =  DateTimeUtil.formatDateTime(activityInfo.getCreateTime());
		this.startTime =DateTimeUtil.formatDateTime(activityInfo.getStartTime());
		this.endTime = DateTimeUtil.formatDateTime(activityInfo.getEndTime());
		this.ended = System.currentTimeMillis()>activityInfo.getEndTime().getTime();
		this.signDisabled = activityInfo.isSignDisable();
		this.peoplesLimit = activityInfo.getPeoplesLimit();
		this.description = activityInfo.getDescription();
	}
	
	
	public List<LaudListDto> getLaudList() {
		return laudList;
	}

	public void setLaudList(List<LaudListDto> laudList) {
		this.laudList = laudList;
	}

	public String getDescription() {
	    return description;
	}

	public void setDescription(String description) {
	    this.description = description;
	}

	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	public List<ActivityReplyDto> getReplys() {
		return replys;
	}

	public void setReplys(List<ActivityReplyDto> replys) {
		this.replys = replys;
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

	public long getReplyAmount() {
		return replyAmount;
	}

	public void setReplyAmount(long replyAmount) {
		this.replyAmount = replyAmount;
	}

	public boolean isJoin() {
		return join;
	}

	public void setJoin(boolean join) {
		this.join = join;
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

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public boolean isSignDisabled() {
		return signDisabled;
	}

	public void setSignDisabled(boolean signDisabled) {
		this.signDisabled = signDisabled;
	}

	public int getPeoplesLimit() {
		return peoplesLimit;
	}

	public void setPeoplesLimit(int peoplesLimit) {
		this.peoplesLimit = peoplesLimit;
	}

	public boolean isPeoplesFull() {
		return peoplesFull;
	}

	public void setPeoplesFull(boolean peoplesFull) {
		this.peoplesFull = peoplesFull;
	}

	public List<Object[]> getAts() {
		return ats;
	}

	public void setAts(List<Object[]> ats) {
		this.ats = ats;
	}
	
	
}
