package org.digijava.module.aim.helper;

import java.io.Serializable;
import java.util.Comparator;

public class KeyValue implements Serializable {
	String key;
	String value;
	
	public KeyValue(String key, String value) {
		this.key	= key;
		this.value	= value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public final static Comparator<KeyValue> keyComparator		= new Comparator<KeyValue>() {
									public int compare(KeyValue o1, KeyValue o2) {
										return o1.key.compareTo(o2.key);
									}
							};
	public final static Comparator<KeyValue> valueComparator		= new Comparator<KeyValue>() {
								public int compare(KeyValue o1, KeyValue o2) {
									return o1.value.compareTo(o2.value);
								}
						};
						
	@Override
	public String toString()
	{
		return String.format("KeyValue: (%s, %s)", this.key, this.value);
	}
}
