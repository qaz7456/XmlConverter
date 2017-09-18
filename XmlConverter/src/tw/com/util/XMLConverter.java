package tw.com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

import tw.com.bean.Body;
import tw.com.bean.Request;
import tw.com.bean.Response;

public class XMLConverter {
	private static final Logger logger = LogManager.getLogger(XMLConverter.class);

	public static void main(String[] args) throws Exception {
		String xml = "<Request service=\"SALE_ORDER_STATUS_PUSH_SERVICE\" lang=\"zh-CN\"><Body><SaleOrderStatusRequest><CompanyCode>W8860571504</CompanyCode><SaleOrders><SaleOrder><WarehouseCode>886DCA</WarehouseCode><ErpOrder>20170803TW1</ErpOrder><WayBillNo>289081343391</WayBillNo><ShipmentId>OXMS201708030114140703</ShipmentId><Waves>EW886A17080300102</Waves><CartNum>1</CartNum><GridNum>0001</GridNum><Carrier>顺丰速运</Carrier><CarrierProduct>島内件(80CM0.5-1.5KG)</CarrierProduct><IsSplit>N</IsSplit><Steps><Step><EventTime>2017-08-0315:31:25</EventTime><EventAddress>WOM</EventAddress><Status>1400</Status><Note>您的订单已取消,取消原因：客户要求取消订单</Note></Step></Steps></SaleOrder></SaleOrders></SaleOrderStatusRequest></Body></Request>";
		String temple = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response service=\"\" lang=\"\"><Body><sender/><product/></Body></Response>";
		getJson(xml);
	}

	private static List<Map<String, String>> getConverterConfigList() throws Exception {
		System.out.println();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;

		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = null;
		try {
			dombuilder = domfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error(e);
		}
		InputStream is = null;
		try {
			is = new FileInputStream("resources/converter-config.xml");
		} catch (FileNotFoundException e) {
			logger.error(e);
		}
		Document doc = null;
		try {
			doc = dombuilder.parse(is);
		} catch (SAXException | IOException e) {
			logger.error(e);
		}

		Element root = doc.getDocumentElement();

		NodeList fields = root.getChildNodes();

		logger.debug("getConverterConfigMap");
		for (int i = 0; i < fields.getLength(); i++) {
			Node node = (Node) fields.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				map = new HashMap<String, String>();
				NodeList settings = node.getChildNodes();
				for (int j = 0; j < settings.getLength(); j++) {
					Node setting = (Node) settings.item(j);
					if (setting.getNodeType() == Node.ELEMENT_NODE) {
						String nodeName = setting.getNodeName();
						String textContent = setting.getTextContent();
						map.put(nodeName, textContent);

						logger.debug("{}: {}", nodeName, textContent);
					}
				}
				list.add(map);
			}
		}
		return list;
	}

	private static Map<String, String> getTemplateConfigMap() throws Exception {
		System.out.println();
		Map<String, String> map = new HashMap<String, String>();

		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = null;
		try {
			dombuilder = domfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error(e);
		}

		FileInputStream is = null;

		try {
			is = new FileInputStream("resources/template-config.xml");
		} catch (Exception e) {
		}
		Document doc = null;
		try {
			doc = dombuilder.parse(is);
		} catch (SAXException | IOException e) {
			logger.error(e);
		}

		NodeList all_nodeList = doc.getElementsByTagName("*");

		for (int i = 0; i < all_nodeList.getLength(); i++) {

			Element element = (Element) all_nodeList.item(i);

			if (element.getNodeType() == Node.ELEMENT_NODE) {

				String nodeName = element.getNodeName();

				map.put(nodeName, null);

				if (i == 0)
					map.put("XmlType", element.getNodeName());

				NodeList nodeList = element.getChildNodes();

				for (int j = 0; j < nodeList.getLength(); j++) {
					Node node = (Node) nodeList.item(j);
					if (node.getNodeType() == Node.TEXT_NODE) {
						String text = node.getTextContent();
						text = Character.null2str(text);
						text = text.equals("") ? null : text;
						map.put(nodeName, text);
					}
				}
			}
		}
		logger.debug("getTemplateConfigMap");
		for (String key : map.keySet()) {
			logger.debug("{}: {}", key, map.get(key));
		}
		return map;
	}

	/*
	 * @param xml The xml document to be convertedd.
	 * 
	 * @return 主鍵為xml節點，值為節點其值的Map.
	 * 
	 */
	public static Map<String, String> XML2Map(String xml) throws Exception {
		System.out.println();
		Map<String, String> map = new HashMap<String, String>();

		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = null;
		try {
			dombuilder = domfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error(e);
		}

		InputSource is = null;

		try {
			is = new InputSource(new StringReader(xml));
		} catch (Exception e) {
		}
		Document doc = null;
		try {
			doc = dombuilder.parse(is);
		} catch (SAXException | IOException e) {
			logger.error(e);
		}

		NodeList all_nodeList = doc.getElementsByTagName("*");

		for (int i = 0; i < all_nodeList.getLength(); i++) {

			Element element = (Element) all_nodeList.item(i);

			if (element.getNodeType() == Node.ELEMENT_NODE) {

				String nodeName = element.getNodeName();

				map.put(nodeName, null);

				if (i == 0)
					map.put("XmlType", element.getNodeName());

				NodeList nodeList = element.getChildNodes();

				for (int j = 0; j < nodeList.getLength(); j++) {
					Node node = (Node) nodeList.item(j);
					if (node.getNodeType() == Node.TEXT_NODE) {
						String text = node.getTextContent();
						map.put(nodeName, text);
					}
				}
			}
		}
		logger.debug("XML2Map");
		for (String key : map.keySet()) {
			logger.debug("{}: {}", key, map.get(key));
		}
		return map;

	}

	/*
	 * @param xml The xml document to be convertedd.
	 * 
	 * @return 主鍵為xml節點，值為節點其多屬性值的List<map> .
	 * 
	 */
	public static Map<String, List<Map<String, String>>> XML2AttrMap(String xml) throws Exception {
		System.out.println();
		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();

		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = null;
		try {
			dombuilder = domfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error(e);
		}

		InputSource is = null;

		try {
			is = new InputSource(new StringReader(xml));
		} catch (Exception e) {
		}
		Document doc = null;
		try {
			doc = dombuilder.parse(is);
		} catch (SAXException | IOException e) {
			logger.error(e);
		}

		NodeList all_nodeList = doc.getElementsByTagName("*");

		for (int i = 0; i < all_nodeList.getLength(); i++) {

			Element element = (Element) all_nodeList.item(i);
			if (element.getNodeType() == Node.ELEMENT_NODE) {

				String nodeName = element.getNodeName();

				map.put(nodeName, null);

				NamedNodeMap element_attr = element.getAttributes();

				List<Map<String, String>> attrList = new ArrayList<Map<String, String>>();

				for (int j = 0; j < element_attr.getLength(); ++j) {
					Node attr = element_attr.item(j);
					String attrName = attr.getNodeName();
					String attrVal = attr.getNodeValue();

					Map<String, String> attrMap = new HashMap<String, String>();
					attrMap.put(attrName, attrVal);
					attrList.add(attrMap);
				}
				map.put(nodeName, attrList);

			}
		}

		logger.debug("XML2AttrMap");
		for (String key : map.keySet()) {
			logger.debug("{}: {}", key, map.get(key));
		}
		return map;

	}

	public static String getXml(String xml) throws Exception {
		List<Map<String, String>> converterConfigList = getConverterConfigList();
		Map<String, String> templateConfigMap = getTemplateConfigMap();
		Map<String, String> nodeMap = XML2Map(xml);
		Map<String, List<Map<String, String>>> attrMap = XML2AttrMap(xml);

		for (int i = 0; i < converterConfigList.size(); i++) {
			Map<String, String> configMap = converterConfigList.get(i);

			String source = configMap.get("source");
			String destination = configMap.get("destination");

			for (String template : templateConfigMap.keySet()) {
				if (destination.equals(template)) {
					templateConfigMap.put(template, source);
				}
			}
		}
		for (String node : nodeMap.keySet()) {
			for (String template : templateConfigMap.keySet()) {
				if (node.equals(templateConfigMap.get(template))) {
					templateConfigMap.put(template, nodeMap.get(node));
				}
			}
		}

		StringWriter sw = new StringWriter();

		String type = nodeMap.get("XmlType");

		Body body = new Body();
		body.setProduct(templateConfigMap.get("product"));
		body.setSender(templateConfigMap.get("sender"));

		Request request = new Request();
		Response response = new Response();

		for (int i = 0; i < converterConfigList.size(); i++) {
			Map<String, String> configMap = converterConfigList.get(i);
			String destination = configMap.get("destination");
			String isAttribute = configMap.get("isAttribute");

			if ("true".equals(isAttribute)) {

				String[] attrArr = destination.split("\\.");
				if (attrArr.length == 2) {
					String attrType = attrArr[0];
					String attrName = attrArr[1];

					if (type.equals(attrType)) {
						List<Map<String, String>> list = null;
						switch (type) {
						case "Request":
							list = attrMap.get(type);
							for (int j = 0; j < list.size(); j++) {

								if ("lang".equals(attrName)) {

									Map<String, String> map = list.get(j);
									if (map.get(attrName) != null)
										request.setLang(map.get(attrName));
								}
								if ("service".equals(attrName)) {

									Map<String, String> map = list.get(j);
									if (map.get(attrName) != null)
										request.setService(map.get(attrName));
								}
							}
							request.setBody(body);
							break;
						case "Response":
							list = attrMap.get(type);
							for (int j = 0; j < list.size(); j++) {
								if ("lang".equals(attrName)) {

									Map<String, String> map = list.get(j);

									response.setLang(map.get(attrName));
								}
								if ("service".equals(attrName)) {

									Map<String, String> map = list.get(j);

									response.setService(map.get(attrName));
								}
							}
							response.setBody(body);
							break;
						}
					}
				}
			}
		}
		switch (type) {
		case "Request":
			JAXB.marshal(request, sw);
			break;
		case "Response":
			JAXB.marshal(response, sw);
			break;
		}
		logger.debug(sw.toString());
		return sw.toString();
	}
	public static String getJson(String xml) throws Exception {
		List<Map<String, String>> converterConfigList = getConverterConfigList();
		Map<String, String> templateConfigMap = getTemplateConfigMap();
		Map<String, String> nodeMap = XML2Map(xml);
		Map<String, List<Map<String, String>>> attrMap = XML2AttrMap(xml);

		for (int i = 0; i < converterConfigList.size(); i++) {
			Map<String, String> configMap = converterConfigList.get(i);

			String source = configMap.get("source");
			String destination = configMap.get("destination");

			for (String template : templateConfigMap.keySet()) {
				if (destination.equals(template)) {
					templateConfigMap.put(template, source);
				}
			}
		}
		for (String node : nodeMap.keySet()) {
			for (String template : templateConfigMap.keySet()) {
				if (node.equals(templateConfigMap.get(template))) {
					templateConfigMap.put(template, nodeMap.get(node));
				}
			}
		}

		String json = null;

		String type = nodeMap.get("XmlType");

		Body body = new Body();
		body.setProduct(templateConfigMap.get("product"));
		body.setSender(templateConfigMap.get("sender"));

		Request request = new Request();
		Response response = new Response();

		for (int i = 0; i < converterConfigList.size(); i++) {
			Map<String, String> configMap = converterConfigList.get(i);
			String destination = configMap.get("destination");
			String isAttribute = configMap.get("isAttribute");

			if ("true".equals(isAttribute)) {

				String[] attrArr = destination.split("\\.");
				if (attrArr.length == 2) {
					String attrType = attrArr[0];
					String attrName = attrArr[1];

					if (type.equals(attrType)) {
						List<Map<String, String>> list = null;
						switch (type) {
						case "Request":
							list = attrMap.get(type);
							for (int j = 0; j < list.size(); j++) {

								if ("lang".equals(attrName)) {

									Map<String, String> map = list.get(j);
									if (map.get(attrName) != null)
										request.setLang(map.get(attrName));
								}
								if ("service".equals(attrName)) {

									Map<String, String> map = list.get(j);
									if (map.get(attrName) != null)
										request.setService(map.get(attrName));
								}
							}
							request.setBody(body);
							break;
						case "Response":
							list = attrMap.get(type);
							for (int j = 0; j < list.size(); j++) {
								if ("lang".equals(attrName)) {

									Map<String, String> map = list.get(j);

									response.setLang(map.get(attrName));
								}
								if ("service".equals(attrName)) {

									Map<String, String> map = list.get(j);

									response.setService(map.get(attrName));
								}
							}
							response.setBody(body);
							break;
						}
					}
				}
			}
		}
		switch (type) {
		case "Request":
			json = new Gson().toJson(request);
			json ="{\"request\":"+json+"}";
			break;
		case "Response":
			json = new Gson().toJson(response);
			json ="{\"response\":"+json+"}";
			break;
		}
		logger.debug(json);
		return json;
	}
}
