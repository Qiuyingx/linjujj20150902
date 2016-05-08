package cn.dayuanzi.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.dayuanzi.exception.ConfigException;
import cn.dayuanzi.util.YardUtils;
/**
 * 
* @ClassName: ReportConfig 
* @Description: 举报标签的config
* @author qiuyingxiang
* @date 2015年5月25日 上午12:11:04 
*
 */
public class ReportConfig implements IConfig {

	private static Map<Integer,  ReportInfo>  reports = new HashMap<Integer, ReportInfo>();
	
	
	@Override
	public void init() {
		Document docXml;
		try {
			SAXReader reader = new SAXReader();
			docXml = reader.read(YardUtils.getResourcesFile("reports.xml"));
			Element root = docXml.getRootElement();
			for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				Integer id = Integer.parseInt(element.attributeValue("id"));
				String name = element.attributeValue("name");
				reports.put(id, new ReportInfo(id,name));
			}
		} catch (DocumentException ex) {
			ex.printStackTrace();
			throw new ConfigException("reports.xml信息错误！");
		}
	}

	/**
	 * @see cn.dayuanzi.config.IConfig#reload()
	 */
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		this.init();
	}

	public static Map<Integer, ReportInfo> getReports() {
		return reports;
	}
	
	
}
