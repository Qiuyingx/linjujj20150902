package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.PersistentSupport;

/**
 * 申请达人时用的标签
 * @author : leihc
 * @date : 2015年7月28日
 * @version : 1.0
 */
@Entity
@Table(name = "t_star_tag")
public class StarTag extends PersistentSupport {

	/**
	 * 标签名称
	 */
	private String tagName;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
}
