package tw.com.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "head", "body" })
public class Request {
	@XmlElement(name = "Head")
	private String head;
	@XmlAttribute(name = "service")
	private String service;
	@XmlAttribute(name = "lang")
	private String lang;
	@XmlElement(name = "Body")
    private Body body;
	

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}
