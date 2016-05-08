/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.List;

import cn.dayuanzi.model.ContentEntity;
import cn.dayuanzi.resp.dto.ContentEntityReplyDto;
import cn.dayuanzi.resp.dto.LaudListDto;
import cn.dayuanzi.util.DateTimeUtil;

/** 
 * @ClassName: ContentEntityResp 
 * @Description: 专题详情
 * @author qiuyingxiang
 * @date 2015年7月9日 下午12:10:45 
 *  
 */

public class ContentEntityResp extends Resp {

   
    /**
     *标题
     */
    private String title;
    /**
     * 简述
     */
    private String description;
    /**
     * 封面图片
     */
    private String image ;
    /**
     * 创建时间 
     * 
     */
    private String create_time;
    /**
     * 作者
     */
    private String author;
    /**
     * 内容
     */
    private String content;
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
    
    private List<ContentEntityReplyDto> replys;
    /**
     * 艾特对象
     */
    private List<Object[]> ats = null;
	/**
	 * 感谢列表
	 */
	private List<LaudListDto> laudList; 
    
    public  ContentEntityResp(){
	
    }
    
    public ContentEntityResp(ContentEntity contentEntity){
    	this.title = contentEntity.getTitle();
    	this.create_time = DateTimeUtil.formatDateTime(contentEntity.getCreate_time());
    	this.image = contentEntity.getTitle_img();
    	this.author = "邻聚";
    	this.content = contentEntity.getContent();
    	this.description = contentEntity.getDescription();
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

    public List<ContentEntityReplyDto> getReplys() {
        return replys;
    }

    public void setReplys(List<ContentEntityReplyDto> replys) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

	public List<Object[]> getAts() {
		return ats;
	}

	public void setAts(List<Object[]> ats) {
		this.ats = ats;
	}
    
    
}
