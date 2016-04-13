package com.gravity.metal;

import java.util.*;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

public class Metal {

	private LinkedHashMap<String, Object> attributes;
	private Metal parent;
	private boolean array;
	private int length;

	public Metal() {
		attributes = new LinkedHashMap<String, Object>();
	}
	
	public Metal(String data) {		
		attributes = new LinkedHashMap<String, Object>();
		parse(data);
	}
	
	public boolean isArray() {
		return array;
	}
	
	public void makeArray() {
		array = true;
	}
	
	public int length() {
		return length;
	}

	public Object get(String property) {

		int dot = property.indexOf('.');
		if (dot > -1) {

			String path = property.substring(0, dot),
				   remainingPath = property.substring(dot + 1);

			Object pathValue = get(path);
			if (pathValue != null && pathValue instanceof Metal) {				
					return ((Metal) pathValue).get(remainingPath);				
			} else {
				if (remainingPath.trim().isEmpty()) {
					return null;
				}
			}

			if (parent != null && path.equalsIgnoreCase("$parent")) {
				if (parent.array == true) {
					return parent.parent.get(remainingPath);
				}
				return parent.get(remainingPath);
			}
		}

		if (property.equalsIgnoreCase("$length")) {
			return length;
		}

		if (parent != null && property.equalsIgnoreCase("$parent")) {
			if (parent.isArray()) {
				return parent.parent;
			}
			return parent;
		}

		if (attributes.containsKey(property)) {
			return attributes.get(property);
		}

		return null;
	}

	public void set(String property, Object value) {
		int dot = property.indexOf('.');
		if (dot > -1) {
			String path = property.substring(0, dot), remainingPath = property
					.substring(dot + 1);

			Object pathValue = get(path);
			if (pathValue == null) {
				pathValue = new Metal();
				set(path, pathValue);
			}
			((Metal) pathValue).set(remainingPath, value);
		} else {
			if (property.indexOf('@') == 0) {
				try {
					Integer.parseInt(property.substring(1));
				} catch (NumberFormatException ex) {
				}
				array = true;
			}

			if (value instanceof Metal) {
				((Metal) value).parent = this;
			}

			if (attributes.containsKey(property) == false) {
				length++;
			}
			attributes.put(property, value);
		}
	}

	public void push(Object value) {
		array = true;
		attributes.put("@" + Integer.toString(length), value);
		length++;
	}

	public List<String> properties() {
		List<String> list = new ArrayList<String>();
		for (Entry<String, Object> item : attributes.entrySet()) {
			String key = item.getKey();
			if (key.charAt(0) == '_') continue;
			list.add(key);
		}
		return list;
	}

	public void parse(String data) {
		if (data == null || data.isEmpty()) return;
		Gson json = new GsonBuilder().create();
		Object obj = json.fromJson(data, Object.class);
		parseData("", obj, this);
	}
	
	private void parseData(String key, Object value, Metal m) 
	{
		if (value instanceof LinkedTreeMap<?,?>) {
			@SuppressWarnings("unchecked")
			LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>)value;
			
			for(Entry<String, Object> e : map.entrySet()){
				Object v1 = e.getValue();
				String k1 = e.getKey();
				
				if (m.attributes.containsKey(k1) == false) {
					m.length ++;
				}
				
				if (v1 instanceof ArrayList<?> || v1 instanceof LinkedTreeMap<?,?>) {					
					Metal sm = new Metal();
					sm.parent = m;
					m.attributes.put(k1, sm);
					parseData(k1, v1, sm);	
				} else {
					m.attributes.put(k1, v1);					
				}				
			}
		}
		
		if (value instanceof ArrayList<?>) {
			m.array = true;
			@SuppressWarnings("unchecked")
			ArrayList<Object> list = (ArrayList<Object>)value;  
			for(Object item : list) {
				if (this.attributes.containsKey("@" + list.indexOf(item)) == false){
					m.length ++;
				}
				if (item instanceof ArrayList<?> || item instanceof LinkedTreeMap<?,?>) {					
					Metal sm = new Metal();
					sm.parent = m;	
					m.attributes.put("@"+list.indexOf(item), sm);
					parseData("", item, sm);
				} else {
					m.attributes.put("@"+list.indexOf(item), item);					
				}
			}
		}
	}
	
	public String json() {
		Gson json = new GsonBuilder().create();
		return json.toJson(raw());
	}
	
	public String json(boolean prettyPrint) {
		Gson json = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		return json.toJson(raw());
	}
	
	

	public Object raw() {

		if (array == false) {

			LinkedHashMap<String, Object> obj = new LinkedHashMap<String, Object>();

			for (Entry<String, Object> item : attributes.entrySet()) {
				String key = item.getKey();
				Object value = item.getValue();							
				
				if (key.charAt(0) == '_') {
					continue;
				}
				
				if (value != null && value instanceof Metal) {
					obj.put(key, ((Metal) value).raw());
				} else {
					obj.put(key, value);
				}
			}

			return obj;

		} else {

			List<Object> array = new ArrayList<Object>(length + 1);

			for (Entry<String, Object> item : attributes.entrySet()) {
				//String property = item.getKey();
				Object value = item.getValue();
				
				if (value != null && value instanceof Metal) {
					// int idx = Integer.parseInt(property.substring(1));
					array.add(((Metal) value).raw());
				} else {
					// int idx = Integer.parseInt(property.substring(1));
					array.add(value);
				}
			}

			return array;
		}
	}

}
