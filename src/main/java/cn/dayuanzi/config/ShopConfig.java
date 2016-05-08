package cn.dayuanzi.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.dayuanzi.util.YardUtils;

/**
 * 商城配置
 * @author : leihc
 * @date : 2015年6月9日
 * @version : 1.0
 */
public class ShopConfig implements IConfig {

	private static final Map<Integer, GoodsInfo> datas = new HashMap<Integer, GoodsInfo>();
	
	/**
	 * @see cn.dayuanzi.config.IConfig#init()
	 */
	@Override
	public void init() {
		Document docXml;
		try {
			SAXReader reader = new SAXReader();
			docXml = reader.read(YardUtils.getResourcesFile("shop.xml"));
			Element root = docXml.getRootElement();
			for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				Integer id = Integer.parseInt(element.attributeValue("id"));
				String name = element.attributeValue("name");
				int price  = Integer.parseInt(element.attributeValue("price"));
				String remark = element.attributeValue("remark");
				GoodsInfo info = new GoodsInfo();
				info.setId(id);
				info.setGoodsName(name);
				info.setPrice(price);
				info.setRemark(remark);
				datas.put(id, info);
			}
		} catch (DocumentException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * @see cn.dayuanzi.config.IConfig#reload()
	 */
	@Override
	public void reload() {
		datas.clear();
		this.init();
	}

	public static Map<Integer, GoodsInfo> getDatas() {
		return datas;
	}

	
}
