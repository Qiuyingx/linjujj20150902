package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.MemberDto;

/**
 * 我的房屋
 * 
 * @author : leihc
 * @date : 2015年5月5日
 * @version : 1.0
 */
public class MyHouseResp extends Resp {

	/**
	 * 是否做过验证
	 */
	private boolean validated;

	/**
	 * 验证方式
	 */
	private int validateType;
	
	/**
	 * 验证是否通过
	 */
	private int validateStatus;
	/**
	 * 验证图片
	 */
	private String image ;
	/**
	 * 验证时填写的邀请码
	 */
	private Long inviteCode;
	/**
	 * 可以切换社区
	 */
	private boolean changeCourtyard = false;

	private String houseAddress;
	
	private List<MemberDto> members = new ArrayList<MemberDto>();

	public String getHouseAddress() {
		return houseAddress;
	}

	public boolean isChangeCourtyard() {
		return changeCourtyard;
	}

	public void setChangeCourtyard(boolean changeCourtyard) {
		this.changeCourtyard = changeCourtyard;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public List<MemberDto> getMembers() {
		return members;
	}

	public void setMembers(List<MemberDto> members) {
		this.members = members;
	}

	public int getValidateType() {
		return validateType;
	}

	public void setValidateType(int validateType) {
		this.validateType = validateType;
	}

	public int getValidateStatus() {
		return validateStatus;
	}

	public void setValidateStatus(int validateStatus) {
		this.validateStatus = validateStatus;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(Long inviteCode) {
		this.inviteCode = inviteCode;
	}
	
}
