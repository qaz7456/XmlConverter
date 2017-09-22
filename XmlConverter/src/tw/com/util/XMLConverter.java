package tw.com.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLConverter {
	private static final Logger logger = LogManager.getLogger(XMLConverter.class);

	public static void main(String[] args) throws Exception {
		String xml = "<Request service=\"SALE_ORDER_STATUS_PUSH_SERVICE\" lang=\"zh-CN\"><Body><SaleOrderStatusRequest><CompanyCode>W8860571504</CompanyCode><SaleOrders><SaleOrder><WarehouseCode>886DCA</WarehouseCode><ErpOrder>20170803TW1</ErpOrder><WayBillNo>289081343391</WayBillNo><ShipmentId>OXMS201708030114140703</ShipmentId><Waves>EW886A17080300102</Waves><CartNum>1</CartNum><GridNum>0001</GridNum><Carrier>顺丰速运</Carrier><CarrierProduct>島内件(80CM0.5-1.5KG)</CarrierProduct><IsSplit>N</IsSplit><Steps><Step><EventTime>2017-08-0315:31:25</EventTime><EventAddress>WOM</EventAddress><Status>1400</Status><Note>您的订单已取消,取消原因：客户要求取消订单</Note></Step></Steps></SaleOrder></SaleOrders></SaleOrderStatusRequest></Body></Request>";
		String json = "{\"Request\":{\"_lang\":\"zh-CN\",\"_service\":\"SALE_ORDER_STATUS_PUSH_SERVICE\",\"Body\":{\"SaleOrderStatusRequest\":{\"SaleOrders\":{\"SaleOrder\":{\"ShipmentId\":\"OXMS201708030114140703\",\"Sender\":\"顺丰速运\",\"Steps\":{\"Step\":{\"Status\":1400,\"EventTime\":\"2017-08-0315:31:25\",\"Note\":\"您的订单已取消,取消原因：客户要求取消订单\",\"EventAddress\":\"WOM\"}},\"ErpNo\":\"20170803TW1\",\"CartNum\":1,\"WayBillNo\":289081343391,\"GridNum\":\"0001\",\"Product\":\"島内件(80CM0.5-1.5KG)\",\"IsSplit\":\"N\",\"WarehouseCode\":\"886DCA\",\"Waves\":\"EW886A17080300102\"}},\"Code\":\"W8860571504\"}}}}";
		logger.debug(getJson(xml));
		logger.debug(getXml(json));
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
		is = XMLConverter.class.getResourceAsStream("/resources/converter-config.xml");
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

	public static String getXml(String input) throws Exception {
		String xml = "";
		List<String> list = null;
		if (Character.isJSONValid(input)) {
			JSONObject json = new JSONObject(input);
			xml = XML.toString(json);
			list = new ArrayList<String>();
		}
		if (Character.isXMLLike(input)) {
			xml = input;

		}
		if (!"".equals(xml)) {
			List<Map<String, String>> converterConfigList = getConverterConfigList();

			Document doc = getDocument(xml);

			NodeList all_nodeList = doc.getElementsByTagName("*");

			for (int i = 0; i < all_nodeList.getLength(); i++) {

				Element element = (Element) all_nodeList.item(i);
				String nodeName = element.getNodeName();

				if (Character.isJSONValid(input)) {
					if ("_".equals(nodeName.substring(0, 1))) {

						((Element) element.getParentNode()).setAttribute(nodeName.substring(1, nodeName.length()),
								element.getTextContent());

						list.add(nodeName);
					}
				}

				for (int j = 0; j < converterConfigList.size(); j++) {

					Map<String, String> map = converterConfigList.get(j);

					if (!"true".equals(map.get("isAttribute"))) {
						if (map.get("source").equals(nodeName)) {

							NodeList nodeList = doc.getElementsByTagName(nodeName);
							for (int k = 0; k < nodeList.getLength();) {
								doc.renameNode(nodeList.item(k), "", map.get("destination"));
							}
						}
					} else {
						NamedNodeMap namedNodeMap = element.getAttributes();
						for (int l = 0; l < namedNodeMap.getLength(); ++l) {
							Node attr = namedNodeMap.item(l);
							String attrName = attr.getNodeName();
							String attrVal = attr.getNodeValue();

							if (map.get("source").equals(attrName)) {
								element.removeAttribute(attrName);
								element.setAttribute(map.get("destination"), attrVal);
							}
						}
					}
				}
			}
			if (Character.isJSONValid(input)) {
				Element root = doc.getDocumentElement();
				for (int i = 0; i < list.size(); i++) {

					root.getElementsByTagName(list.get(i)).item(0).getParentNode()
							.removeChild(root.getElementsByTagName(list.get(i)).item(0));
				}
			}
			xml = docToString(doc);

		}
		return xml;
	}

	public static String getJson(String input) throws Exception {

		String xml = "";
		if (Character.isJSONValid(input)) {
			xml = getXml(input);
		} else if (Character.isXMLLike(input)) {
			xml = input;
		}
		if (!"".equals(xml)) {

			List<Map<String, String>> converterConfigList = getConverterConfigList();

			Document doc = getDocument(xml);

			NodeList all_nodeList = doc.getElementsByTagName("*");

			for (int i = 0; i < all_nodeList.getLength(); i++) {

				Element element = (Element) all_nodeList.item(i);
				String nodeName = element.getNodeName();

				for (int j = 0; j < converterConfigList.size(); j++) {

					Map<String, String> map = converterConfigList.get(j);

					if (!"true".equals(map.get("isAttribute"))) {
						if (map.get("source").equals(nodeName)) {

							NodeList nodeList = doc.getElementsByTagName(nodeName);
							for (int k = 0; k < nodeList.getLength();) {
								doc.renameNode(nodeList.item(k), "", map.get("destination"));
							}
						}
					} else {
						NamedNodeMap namedNodeMap = element.getAttributes();
						for (int l = 0; l < namedNodeMap.getLength(); ++l) {
							Node attr = namedNodeMap.item(l);
							String attrName = attr.getNodeName();
							String attrVal = attr.getNodeValue();

							if (map.get("source").equals(attrName)) {
								element.removeAttribute(attrName);
								element.setAttribute(map.get("destination"), attrVal);
							}
						}
					}
				}
			}
			for (int i = 0; i < all_nodeList.getLength(); i++) {

				Element element = (Element) all_nodeList.item(i);

				NamedNodeMap namedNodeMap = element.getAttributes();
				for (int l = 0; l < namedNodeMap.getLength(); ++l) {
					Node attr = namedNodeMap.item(l);
					String attrName = attr.getNodeName();
					String attrVal = attr.getNodeValue();

					element.removeAttribute(attrName);
					element.setAttribute("_" + attrName, attrVal);

				}
			}
			xml = XML.toJSONObject(docToString(doc)).toString();
		}
		return xml;
	}

	public static Document getDocument(String xml) throws Exception {

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
		return doc;
	}

	public static String docToString(Document doc) {
		try {
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to String", ex);
		}
	}
}
