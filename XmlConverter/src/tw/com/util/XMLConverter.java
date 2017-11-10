package tw.com.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

import org.apache.commons.codec.binary.Base64;
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

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class XMLConverter {
	private static final Logger logger = LogManager.getLogger(XMLConverter.class);
	public static String FILE_PATH = null;

	public static void main(String[] args) throws Exception {
		String xml = "<Request service=\"SALE_ORDER_STATUS_PUSH_SERVICE\" lang=\"zh-CN\"><Body><SaleOrderStatusRequest><CompanyCode>W8860571504</CompanyCode><SaleOrders><SaleOrder><WarehouseCode>886DCA</WarehouseCode><ErpOrder>20170803TW1</ErpOrder><WayBillNo>289081343391</WayBillNo><ShipmentId>OXMS201708030114140703</ShipmentId><Waves>EW886A17080300102</Waves><CartNum>1</CartNum><GridNum>0001</GridNum><Carrier>顺丰速运</Carrier><CarrierProduct>島内件(80CM0.5-1.5KG)</CarrierProduct><IsSplit>N</IsSplit><Steps><Step><EventTime>2017-08-0315:31:25</EventTime><EventAddress>WOM</EventAddress><Status>1400</Status><Note>您的订单已取消,取消原因：客户要求取消订单</Note></Step></Steps></SaleOrder></SaleOrders></SaleOrderStatusRequest></Body></Request>";
		String json = "{\"Request\":{\"_lang\":\"zh-CN\",\"_service\":\"SALE_ORDER_STATUS_PUSH_SERVICE\",\"Body\":{\"SaleOrderStatusRequest\":{\"SaleOrders\":{\"SaleOrder\":{\"ShipmentId\":\"OXMS201708030114140703\",\"Sender\":\"顺丰速运\",\"Steps\":{\"Step\":{\"Status\":1400,\"EventTime\":\"2017-08-0315:31:25\",\"Note\":\"您的订单已取消,取消原因：客户要求取消订单\",\"EventAddress\":\"WOM\"}},\"ErpNo\":\"20170803TW1\",\"CartNum\":1,\"WayBillNo\":289081343391,\"GridNum\":\"0001\",\"Product\":\"島内件(80CM0.5-1.5KG)\",\"IsSplit\":\"N\",\"WarehouseCode\":\"886DCA\",\"Waves\":\"EW886A17080300102\"}},\"Code\":\"W8860571504\"}}}}";
		// xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response
		// Status=\"ok\"><Product><ProductId>p09498618</ProductId><ProductStatus>Online</ProductStatus><ProductName>清涼櫻花剉冰111</ProductName><ShortDescription>白玉櫻花剉冰~清涼退火又消暑</ShortDescription><spec
		// Id=\"1\"><SpecDescription>-</SpecDescription><CustomizedProductId/><CurrentStock>300</CurrentStock><Stock>1</Stock></spec><ImageMain>https://b-iu101.ac.tw1.yahoo.com/pimg1/c0/05/p09498618-itema-1991xf1x0300x0183-s.jpg</ImageMain><MarketPrice>0</MarketPrice><SalePrice>200</SalePrice><CostPrice>100</CostPrice></Product></Response>";
		String xmlConverterPath = "C:\\Users\\Ian\\Desktop\\Development\\xmlconverter-config.xml";
		xml="<?xml version=\"1.0\" encoding=\"gbk\" standalone=\"no\"?><Request lang=\"zh-CN\" service=\"SALE_ORDER_STATUS_PUSH_SERVICE\"><Body><SaleOrderStatusRequest><CompanyCode>W8860571504</CompanyCode><SaleOrders><SaleOrder><WarehouseCode>886DCA</WarehouseCode><ErpOrder>20170803TW1</ErpOrder><WayBillNo>289081343391</WayBillNo><ShipmentId>OXMS201708030114140703</ShipmentId><Waves>EW886A17080300102</Waves><CartNum>1</CartNum><GridNum>0001</GridNum><Carrier>顺丰速运</Carrier><CarrierProduct>島内件(80CM 0.5-1.5KG)</CarrierProduct><IsSplit>N</IsSplit><Steps><Step><EventTime>2017-08-03 15:31:25</EventTime><EventAddress>WOM</EventAddress><Status>1400</Status><Note>您的订单已取消,取消原因：客户要求取消订单</Note></Step></Steps></SaleOrder></SaleOrders></SaleOrderStatusRequest></Body></Request>";
		// logger.debug(getJson(xml));
		// logger.debug(getXml(xml,xmlConverterPath));
		// logger.debug(getRest(xml, xmlConverterPath));
		System.out.println();
		logger.debug("得到: " + getRest("Get", xml, xmlConverterPath));
		System.out.println();

		// String xmlUrl = args[0];
	}

	private static List<Map<String, String>> getConverterConfigList(String path) throws Exception {
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
		// InputStream is = null;

		// is = new FileInputStream(path);
		logger.debug("XMLConverter path: {}", path);
		File file = new File(path);
		Document doc = null;
		try {
			doc = dombuilder.parse(file);
		} catch (SAXException | IOException e) {
			logger.error(e);
		}

		Element root = doc.getDocumentElement();

		NodeList fields = root.getChildNodes();

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
					}
				}
				list.add(map);
			}
		}
		return list;
	}

	public static String trim(String input) {
		BufferedReader reader = new BufferedReader(new StringReader(input));
		StringBuffer result = new StringBuffer();
		try {
			String line;
			while ((line = reader.readLine()) != null)
				result.append(line.trim());
			return result.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getRest(String type, String input, String path, String encode) throws Exception {

		String xml = getXml(input, path);
		logger.debug("轉換後的XML: " + xml);
		logger.debug("去除空白: " + trim(xml));
		xml = trim(xml);
		Map<String, String> map = getXmlMap(xml);
		StringBuffer sf = new StringBuffer();
		sf.append("Format=").append(type);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sf.append(key).append("=").append(value).append("&");
		}
		sf.setLength(sf.length() - 1);

		String result = null;
		if ("utf-8".equalsIgnoreCase(encode)) {
			result = new String(sf.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
		}
		if ("Base64".equalsIgnoreCase(encode)) {
			result = new String(Base64.encodeBase64String(sf.toString().getBytes()));
		}
		return result;

	}

	public static String getRest(String type, String input, String path) throws Exception {

		String xml = getXml(input, path);
		logger.debug("轉換後的XML: " + xml);
		logger.debug("去除空白: " + trim(xml));
		Map<String, String> map = getXmlMap(xml);
		StringBuffer sf = new StringBuffer();
		sf.append("Format=").append(type).append("&");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sf.append(key).append("=").append(value).append("&");
		}
		sf.setLength(sf.length() - 1);
		return sf.toString();

	}

	public static Map<String, String> getXmlMap(String xml) throws Exception {

		InputStream is = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(is);

		return createMap(document);

	}

	public static Map<String, String> createMap(Node node) {
		Map<String, String> map = new HashMap<String, String>();
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.hasAttributes()) {
				for (int j = 0; j < currentNode.getAttributes().getLength(); j++) {
					Node item = currentNode.getAttributes().item(j);
					map.put(item.getNodeName(), item.getTextContent());
				}
			}
			if (node.getFirstChild() != null && node.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
				map.putAll(createMap(currentNode));
			} else if (node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
				map.put(node.getLocalName(), node.getTextContent());
			}
		}
		return map;
	}

	public static String getXml(String input, String path) throws Exception {
		String xml = "";
		List<String> list = null;
		if (Character.isJSONValid(input)) {
			JSONObject json = new JSONObject(input);
			xml = XML.toString(json);
			list = new ArrayList<String>();
			logger.debug("判斷是json型態");
		} else {
			logger.debug("判斷不是json型態");
		}
		if (Character.isXMLLike(input)) {
			xml = input;
			logger.debug("判斷是XML型態");
		} else {
			logger.debug("判斷不是XML型態");
		}
		if (!"".equals(xml)) {
			List<Map<String, String>> converterConfigList = getConverterConfigList(path);

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

	public static String getJson(String input, String path) throws Exception {

		String xml = "";
		if (Character.isJSONValid(input)) {
			xml = getXml(input, xml);
		} else if (Character.isXMLLike(input)) {
			xml = input;
		}
		if (!"".equals(xml)) {

			List<Map<String, String>> converterConfigList = getConverterConfigList(path);

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
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to String", ex);
		}
	}
}
