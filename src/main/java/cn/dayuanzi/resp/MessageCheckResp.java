package cn.dayuanzi.resp;

import org.apache.commons.lang.StringUtils;

import cn.dayuanzi.model.Notice;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.resp.dto.LevelExpDto;

/**
 * 
 * @author : leihc
 * @date : 2015年4月22日 上午9:38:58
 * @version : 1.0
 */
public class MessageCheckResp extends Resp {

	private LevelExpDto levelExpDto;
	/**
	 * 未读评论数量
	 */
	private long notReadReplyAmount;
	/**
	 * 未读点赞数量
	 */
	private long notReadLaudAmount;
	/**
	 * 未读通知数量
	 */
	private long notReadNotice;
	/**
	 * 未读邀约数量
	 */
	private long notReadInvitation;
	/**
	 * 未读好友请求数量
	 */
	private long notReadFriendRequest;
	/**
	 * 新邻居数量
	 */
	private long newNeighborAmount;
	/**
	 * 通知内容
	 */
	private String content;
	/**
	 * 通知时间
	 */
	private String create_time ;
	/**
	 * 通知类型
	 * 
	 */
	private String noticeType;
	/**
	 * 邻豆数
	 */
	private int lindouAmount;
	
	/**
	 * 是否通过验证
	 */
	private boolean validated;
	/**
	 * 是否有未完成的任务
	 */
	private boolean haveNotCompletedTask;
	/**
	 * 新的关注自己的人数
	 */
	private long newFollowMeAmount;
	/**
	 * 是否有新的的专题
	 */
	private boolean newSpecialAmout;
	/**
	 * 是否有新的邀约
	 */
	private boolean newInvitationAmout;
	/**
	 * 未读艾特数
	 */
	private long notReadAtAmount;
	
    public boolean isNewInvitationAmout() {
	    return newInvitationAmout;
	}
	public void setNewInvitationAmout(boolean newInvitationAmout) {
	    this.newInvitationAmout = newInvitationAmout;
	}
    public boolean isNewSpecialAmout() {
      return newSpecialAmout;
    }
    public void setNewSpecialAmout(boolean newSpecialAmout) {
      this.newSpecialAmout = newSpecialAmout;
    }
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public long getNotReadReplyAmount() {
		return notReadReplyAmount;
	}
	public void setNotReadReplyAmount(long notReadReplyAmount) {
		this.notReadReplyAmount = notReadReplyAmount;
	}
	public long getNotReadLaudAmount() {
		return notReadLaudAmount;
	}
	public void setNotReadLaudAmount(long notReadLaudAmount) {
		this.notReadLaudAmount = notReadLaudAmount;
	}
	public long getNotReadNotice() {
		return notReadNotice;
	}
	public void setNotReadNotice(long notReadNotice) {
		this.notReadNotice = notReadNotice;
	}
	public long getNotReadInvitation() {
		return notReadInvitation;
	}
	public void setNotReadInvitation(long notReadInvitation) {
		this.notReadInvitation = notReadInvitation;
	}
	public long getNotReadFriendRequest() {
		return notReadFriendRequest;
	}
	public void setNotReadFriendRequest(long notReadFriendRequest) {
		this.notReadFriendRequest = notReadFriendRequest;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(Notice notices) {
//		if(notices.getNotice_type()==0){
//			this.noticeType = "系统通知";
//		}else if(notices.getNotice_type()==1){
//			this.noticeType = "社区公告";
//		}else if(notices.getNotice_type()==2){
//			this.noticeType = "紧急通知";
//		}
		noticeType = notices.getTitle();
		if(StringUtils.isBlank(noticeType)){
			noticeType = NoticeType.values()[notices.getNotice_type()].getTitle();
		}
	}
	public LevelExpDto getLevelExpDto() {
		return levelExpDto;
	}
	public void setLevelExpDto(LevelExpDto levelExpDto) {
		this.levelExpDto = levelExpDto;
	}
	public int getLindouAmount() {
		return lindouAmount;
	}
	public void setLindouAmount(int lindouAmount) {
		this.lindouAmount = lindouAmount;
	}
	public boolean isValidated() {
		return validated;
	}
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	public long getNewNeighborAmount() {
		return newNeighborAmount;
	}
	public void setNewNeighborAmount(long newNeighborAmount) {
		this.newNeighborAmount = newNeighborAmount;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public boolean isHaveNotCompletedTask() {
		return haveNotCompletedTask;
	}
	public void setHaveNotCompletedTask(boolean haveNotCompletedTask) {
		this.haveNotCompletedTask = haveNotCompletedTask;
	}
	public long getNewFollowMeAmount() {
		return newFollowMeAmount;
	}
	public void setNewFollowMeAmount(long newFollowMeAmount) {
		this.newFollowMeAmount = newFollowMeAmount;
	}
	public long getNotReadAtAmount() {
		return notReadAtAmount;
	}
	public void setNotReadAtAmount(long notReadAtAmount) {
		this.notReadAtAmount = notReadAtAmount;
	}
	
}
