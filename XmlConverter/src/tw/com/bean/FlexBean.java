package tw.com.bean;
//package com.bean;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.collections4.IterableGet;
//
//
//public class FlexBean {
//	private List<String> propertyNames = new ArrayList<>();
//	private Map<String, Object> propertyValueMap = new LinkedHashMap<>();
//	private List<Type> propertyTypes = new ArrayList<>();
//
//	public FlexBean() {
//
//	}
//
//	public void setProperties(Map<String, Object> props) {
//		for (String propName : props.keySet()) {
//			addProperty(propName, props.get(propName));
//		}
//	}
//
//	public void addPropertyNames(List<String> propertyNames) {
//		for (String name : propertyNames) {
//			addProperty(name);
//		}
//	}
//
//	public Collection<Object> getPropertyValues() {
//		return propertyValueMap.values();
//	}
//
//	public List<String> getPropertyNames() {
//		return propertyNames;
//	}
//
//	public Map<String, Object> getPropertyValueMap() {
//		return propertyValueMap;
//	}
//
//	public List<Type> getPropertyTypes() {
//		return propertyTypes;
//	}
//
//	public void addProperty(String propName, Type propType) {
//		propertyNames.add(propName);
//		propertyTypes.add(propType);
//	}
//
//	public void addProperty(String propName) {
//		// default property type is String
//		addProperty(propName, String.class);
//	}
//
//	public void addProperty(String propName, Object value) {
//		addProperty(propName);
//		setProperty(propName, value);
//	}
//
//	public void addProperty(String propName, Type propType, Object value) {
//		addProperty(propName, propType);
//		setProperty(propName, value);
//	}
//
//	public int getPropertyIndex(String propName) {
//		return propertyNames.indexOf(propName);
//	}
//
//	public Type getPropertyType(String propName) {
//		int index = getPropertyIndex(propName);
//		return Iterator.get(propertyTypes, index);
//	}
//
//	public void setProperty(String propName, Object propValue) {
//		propertyValueMap.put(propName, propValue);
//	}
//
//	public Object getPropertyValue(String propName) {
//		return propertyValueMap.get(propName);
//	}
//
//	public <Any> Any getTypedPropertyValue(String propName) {
//		return (Any) ((Any) propertyValueMap.get(propName));
//	}
//
//	public Object getProperty(int propIndex) {
//		return IterableGet.get(propertyValueMap.entrySet(), propIndex).getValue();
//	}
//}