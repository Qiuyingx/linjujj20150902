package cn.dayuanzi.resp.dto;

import org.apache.commons.lang.StringUtils;

import cn.dayuanzi.model.Notice;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.util.DateTimeUtil;


/**
 * 
 * @author : leihc
 * @date : 2015年5月5日
 * @version : 1.0
 */
public class NewNoticeDto {
	
	
	/**
	 * 通知id
	 */
	private long id ;
	/**
	 * 通知类型
	 */
	private int notice_type;
	/**
	 * 通知标题
	 */
	private String title;
	/**
	 * 通知内容
	 */
	private String content;
	/**
	 * 发通知时间
	 */
	private String create_time;
	/**
	 * 附加信息
	 */
	private long append;
	
	public NewNoticeDto(Notice notice){
		this.notice_type = notice.getNotice_type();
		this.id = notice.getId();
		this.title = notice.getTitle();
		if(StringUtils.isBlank(this.title)){
			this.title = NoticeType.values()[this.notice_type].getTitle();
		}
		this.append = notice.getAppend();
		this.content = notice.getContent();
		this.create_time = DateTimeUtil.getDisplay(notice.getCreate_time());
	}
	
	public int getNotice_type() {
		return notice_type;
	}

	public void setNotice_type(int notice_type) {
		this.notice_type = notice_type;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getAppend() {
		return append;
	}

	public void setAppend(long append) {
		this.append = append;
	}
}
