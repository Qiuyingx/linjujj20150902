package cn.dayuanzi.model.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 感谢视图
 * 
 * @author : leihc
 * @date : 2015年8月19日
 * @version : 1.0
 */
@Entity
@Table(name = "v_laud")
public class LaudView {

	/**
	 * 感谢ID
	 */
	private long id;
	/**
	 * 感谢者
	 */
	private long lauder_id;
	/**
	 * 被感谢者
	 */
	private long beLauder_id;
	/**
	 * 内容ID，帖子，回复的ID
	 */
	private long content_id;
	/**
	 * 感谢时间
	 */
	private long create_time;
	/**
	 * 1 t_post_laud 2 t_invitation_laud 3 t_thank_reply
	 */
	private int laud_type;
	
	@Id
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getLauder_id() {
		return lauder_id;
	}
	public void setLauder_id(long lauder_id) {
		this.lauder_id = lauder_id;
	}
	public long getBeLauder_id() {
		return beLauder_id;
	}
	public void setBeLauder_id(long beLauder_id) {
		this.beLauder_id = beLauder_id;
	}
	public long getContent_id() {
		return content_id;
	}
	public void setContent_id(long content_id) {
		this.content_id = content_id;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public int getLaud_type() {
		return laud_type;
	}
	public void setLaud_type(int laud_type) {
		this.laud_type = laud_type;
	}
	
}
