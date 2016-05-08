/**
 * 
 */
package cn.dayuanzi.resp.dto;

import cn.dayuanzi.model.Invitation;

/**
 * 
 *
 * @author : leihc
 * @date : 2015年5月2日
 * 
 */
public class DiscussGroupDto {

	/**
	 * 讨论组名称
	 */
	private String groupName;
	/**
	 * 成员数量
	 */
	private int memberAmount;
	/**
	 * 头像
	 */
	private String headicon ;
	
	
	public DiscussGroupDto(Invitation invitation, int memberAmount){
		this.groupName = invitation.getGroup_name();
		this.memberAmount = memberAmount;
	}
	
	public String getHeadicon() {
		return headicon;
	}

	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}

	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getMemberAmount() {
		return memberAmount;
	}
	public void setMemberAmount(int memberAmount) {
		this.memberAmount = memberAmount;
	}
	
	
}
