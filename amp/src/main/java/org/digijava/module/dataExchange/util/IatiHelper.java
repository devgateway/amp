/**
 * 
 */
package org.digijava.module.dataExchange.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.AMPException;
import org.digijava.module.dataExchange.dbentity.IatiCodeItem;
import org.digijava.module.dataExchange.dbentity.IatiCodeType;
import org.digijava.module.dataExchange.util.DataExchangeConstants.IatiCodeTypeEnum;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Common Helper for IATI import and export 
 * @author Nadejda Mandrescu
 *
 */
public class IatiHelper {
	private static Logger logger = Logger.getLogger(IatiHelper.class);

	/**
	 * Gets IatiCode
	 * @param typeName
	 * @param code
	 * @return
	 */
	public static String getIatiCodeName(IatiCodeTypeEnum typeName, String code) {
		IatiCodeType type = null;
		IatiCodeItem item = null;
		try {
			type = DbUtil.getIatiCodeTypeByName(IatiCodeTypeEnum.Language);
			item = type==null ? null : DbUtil.getIatiCodeByCode(code, type.getId());
		} catch (AMPException e) {
			logger.error(e.getMessage());
		}
		return item==null ? null : item.getName();
	}
	
	/**
	 * Sets an attribute to a specific value for the given node
	 * @param doc - the document that the node is part of
	 * @param node - the node to set the attribute
	 * @param attrName - attribute name
	 * @param attrValue - attribute value
	 * @param propagateParentToChildren - if current node (parent) attribute value must be propagated to children nodes <br>
	 * Note: if forceOvewrite=false, then actual attr value of the node will be propagated
	 * @param forceOverwrite - if attr exists, then it will be overwriten with attrValue  
	 */
	public static void setAttr(Document doc, Node node, String attrName, String attrValue, boolean propagateParentToChildren, boolean forceOverwrite) {
		Node attrNode = null;
		//skip attr checks if this is document root obj, not document root element
		if (node!=doc) {
			if (node.getAttributes()==null) return; //this is not an element
			attrNode = node.getAttributes().getNamedItem(attrName);
			if (attrNode==null || forceOverwrite) {
				if (attrNode==null) {
					attrNode = doc.createAttribute(attrName);
					node.getAttributes().setNamedItem(attrNode);
				}
				attrNode.setTextContent(attrValue);
			}
		}
		if (propagateParentToChildren) {
			NodeList children = node.getChildNodes();
			for (int i=0; i<children.getLength(); i++) 
				setAttr(doc, children.item(i), attrName, attrNode==null ? attrValue: attrNode.getTextContent(), propagateParentToChildren, forceOverwrite);
		}
	}
	
	public static void removeAttr(Node node, String attrName) {
		if (node.getAttributes()!=null) {
			node.getAttributes().removeNamedItem(attrName);
		}
	}
	
	/**
	 * Returns node signature : fullname + all attributes <br>
	 * Uses {@link #getNodeFullName} and {@link #attrToString}
	 * @param node
	 * @return
	 */
	public static String getNodeSignature(Node node) {
		return getNodeFullName(node) + attrToString(node);
	}
	
	/**
	 * Generates fully qualified name of a Node, starting from root
	 * @param node
	 * @return
	 */
	public static String getNodeFullName(Node node) {
		return (node==null || node instanceof Document) ? "" : getNodeFullName(node.getParentNode()) + "/" + node.getNodeName();
	}
	
	/**
	 * Generates a string representation of all attributes of a Node
	 * @param node
	 * @return
	 */
	public static String attrToString(Node node) {
		if (node.getAttributes()==null) return "";
		String str = "";
		for(int i=0; i<node.getAttributes().getLength(); i++) {
			Node nodeAttr = node.getAttributes().item(i);
			str += " " + nodeAttr.getNodeName() + "=\"" + nodeAttr.getNodeValue() + "\"";
		}
		return str;
	}
	
	/**
	 * Removes nodes from their parents (=> and from DOM tree)
	 * @param nodes - nodes to be removed
	 * @param removedElementsSignature - stores removed elements signature
	 * @return true if any element was removed
	 */
	public static boolean removeNodes(NodeList nodes, List<String> removedElementsSignature) {
		if (nodes!=null && nodes.getLength()>0) {
			for (int i=0; i<nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (removedElementsSignature != null)
					removedElementsSignature.add(IatiHelper.getNodeSignature(node));
				node.getParentNode().removeChild(node);
			}
			return true;
		}
		return false;
	}
	
	public static boolean removeUselessTranslations(NodeList nodes, String defaultLang, Set<Node> processed, List<String> removedElementsSignature) {
		boolean removed = false;
		for (int i=0; i<nodes.getLength()-1; i++) {
			Node node1 = nodes.item(i);
			if (processed.contains(node1)) continue;
			for (int j=i+1; j<nodes.getLength(); j++) {
				Node node2 = nodes.item(j);
				//detect if this is the same node type
				if (!processed.contains(node2) && node1.getParentNode().isSameNode(node2.getParentNode())
						&& node1.getNodeName().equals(node2.getNodeName())) {
					String lang1 = node1.getAttributes().getNamedItem("xml:lang").getTextContent();
					String lang2 = node2.getAttributes().getNamedItem("xml:lang").getTextContent();
					//only translations should be filtered out. Multiple entries for same language are kept (e.g. multiple Contact Info)
					if (!lang1.equals(lang2)) {
						//temporary alter lang to be able to compare attributes equality
						node2.getAttributes().getNamedItem("xml:lang").setTextContent(lang1);
						//same name, but check if also same attributes, because other differences than lang makes nodes different
						boolean same = true;
						for(int a=0; same && a<node1.getAttributes().getLength(); a++) {
							same = false;
							for(int b=0; !same && b<node2.getAttributes().getLength();b++)
								same = node1.getAttributes().item(a).isEqualNode(node2.getAttributes().item(b));
						}
						//restore lang attr
						node2.getAttributes().getNamedItem("xml:lang").setTextContent(lang2);
						if (same) {
							Node remove  = node2;
							//default language is preferable to be kept
							if (lang2.equals(defaultLang)) { 
								remove =  node1;
								node1 = node2;
							}
							removedElementsSignature.add(IatiHelper.getNodeSignature(remove));
							remove.getParentNode().removeChild(remove);
							processed.add(node2);
							removed = true;
						}
					}
				}
			}
		}
		return removed;
	}
	
	/**
	 * Configures xml:lang attribute to all elements. If no xml:lang is specified for an element, then its ancestor xml:lang is used.
	 * The main ancestors (activities) are configured with default language if no xml:lang is explicitly set. 
	 * @param activities - list of activities to configure in bulk
	 * @param defaultLanguage - default language for roots
	 */
	public static <T> void setLangToAll(List<T> activities, String defaultLanguage) {
		if (activities==null || activities.size()==0) return;
		Class clazz = activities.get(0).getClass();

		//preconfigure default langs for roots
		Map<T, String> nodes = new HashMap<T, String>();
		for (T activity : activities) {
			nodes.put(activity, defaultLanguage);
		}
		setLangToAll(nodes, clazz);
	}
	
	/**
	 * Supporting method used by its public version {@link #setLangToAll(List, String)} <br>
	 * JAXB tree is traversed once, but at each JAXB node (represented by clazz) 
	 * it works with all actual nodes of the initial list of activities.   
	 * @param nodes - a map of current Nodes and their default language 
	 * @param clazz - nodes class 
	 */
	private static <T> void setLangToAll(Map<T, String> nodes, Class clazz) {
		String childrenField = null; //normally should be 1 XmlElementRefs 
		
		try {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				XmlAttribute xmlAttr  = field.getAnnotation(XmlAttribute.class); //looking for xml:lang
				XmlAnyElement xmlAny = field.getAnnotation(XmlAnyElement.class); //looking for children lists
				
				if (xmlAny!=null)
					childrenField = StringUtils.capitalize(field.getName());
				else if (xmlAttr!=null && "lang".equals(xmlAttr.name())) {
					Method getLang = clazz.getMethod("getLang");
					Method setLang = clazz.getMethod("setLang", String.class);
					for (Map.Entry<T, String> langElem : nodes.entrySet()) {
						String val = (String) getLang.invoke(langElem.getKey());
						if (StringUtils.isBlank(val)) {
							setLang.invoke(langElem.getKey(), langElem.getValue()); //set parent lang
						} else {
							langElem.setValue(val);//remember language used by this node and that should be applied to children
						}
					}
				} 
			}
	
			if (childrenField!=null) {
				Method getChildren = clazz.getMethod("get" + childrenField);
				//group children of all current parents by their class type
				Map<Class<?>, Map<Object, String>> childrenMap = new HashMap<Class<?>, Map<Object, String>>();
				for (Map.Entry<T, String> langElem : nodes.entrySet()) {
					List<Object> children = (List<Object>)getChildren.invoke(langElem.getKey());
					for (Object child: children) {
						if (child.getClass().isAssignableFrom(JAXBElement.class)) {
							child = ((JAXBElement)child).getValue();
						}
						Map<Object, String> childLang = childrenMap.get(child.getClass());
						if (childLang==null) {
							childLang = new HashMap<Object, String>();
							childrenMap.put(child.getClass(), childLang);
						}
						childLang.put(child, langElem.getValue());
					}
				}
				//for each child type, recursive call for language config for all activities from the original list
				for(Map.Entry<Class<?>, Map<Object, String>> entry : childrenMap.entrySet()) {
					setLangToAll(entry.getValue(), entry.getKey());
				}
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage());
		}
	}
	
}
