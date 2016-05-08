package cn.dayuanzi.resp.dto;

import java.util.List;

/**
 * 
 * @author : leihc
 * @date : 2015年4月23日 下午2:25:24
 * @version : 1.0
 */
public class NewLaudDto implements Comparable<NewLaudDto>{
	
	/**
	 * 点赞在数据表所在id
	 */
	private long id;
	/**
	 * 点赞者ID
	 */
	private long lauder_id;
	/**
	 * 点赞者昵称
	 */
	private String lauder_nickname;
	/**
	 * 点赞者头像
	 */
	private String lauder_head;
	/**
	 * 点赞时间
	 */
	private String laud_time;
	/**
	 * 数字表示的时间，用户排序
	 */
	private long laud_time_number;
	/**
	 * 被点赞的帖子ID
	 */
	private long postId;
	/**
	 * 被点赞的帖子内容
	 */
	private String postContent;
	
	/**
	 * 被点赞的帖子图片地址
	 */
	private List<String> images;
	/**
	 * 点赞的帖子类型
	 */
	private int postType;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPostType() {
		return postType;
	}

	public void setPostType(int postType) {
		this.postType = postType;
	}

	public long getLauder_id() {
		return lauder_id;
	}

	public void setLauder_id(long lauder_id) {
		this.lauder_id = lauder_id;
	}

	public String getLauder_nickname() {
		return lauder_nickname;
	}

	public void setLauder_nickname(String lauder_nickname) {
		this.lauder_nickname = lauder_nickname;
	}

	public String getLaud_time() {
		return laud_time;
	}

	public void setLaud_time(String laud_time) {
		this.laud_time = laud_time;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public String getPostContent() {
		return postContent;
	}

	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getLauder_head() {
		return lauder_head;
	}

	public void setLauder_head(String lauder_head) {
		this.lauder_head = lauder_head;
	}

	public long getLaud_time_number() {
		return laud_time_number;
	}

	public void setLaud_time_number(long laud_time_number) {
		this.laud_time_number = laud_time_number;
	}
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(NewLaudDto o) {
		if(this.laud_time_number>o.laud_time_number){
			return -1;
		}else if(this.laud_time_number<o.laud_time_number){
			return 1;
		}
		return 0;
	}
	
}
