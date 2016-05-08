/**
 * 
 */
package cn.dayuanzi.resp.dto;

import cn.dayuanzi.model.ContentEntity;
import cn.dayuanzi.util.DateTimeUtil;

/** 
 * @ClassName: ContentEntityDto 
 * @Description: 专题返回内容
 * @author qiuyingxiang
 * @date 2015年7月8日 下午9:12:37 
 *  
 */

public class ContentEntityDto {
    /**
     * 专题id
     */
    private long  id;
    /**
     *标题
     */
    private String title;
    /**
     * 封面图片
     */
    private String image ;
    /**
     * bannnerImg
     */
    private String bannerimg;
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
     * 浏览量
     */
    private long views;
    
    
  
    
    public ContentEntityDto(){
	
    }
    
    public ContentEntityDto(ContentEntity contentEntity){
    	this.id = contentEntity.getId();
    	this.title = contentEntity.getTitle();
    	this.create_time = DateTimeUtil.formatDateTime(contentEntity.getCreate_time(),"MMM dd");
    	this.image = contentEntity.getTitle_img();
    	this.author = "邻聚";
    	this.bannerimg = contentEntity.getBanner_img(); 
    	this.views = contentEntity.getViews();
    }


   

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getBannerimg() {
        return bannerimg;
    }

    public void setBannerimg(String bannerimg) {
        this.bannerimg = bannerimg;
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
    
    
}
