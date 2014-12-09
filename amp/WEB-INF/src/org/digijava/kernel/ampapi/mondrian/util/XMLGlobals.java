package org.digijava.kernel.ampapi.mondrian.util;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.*;

import java.io.*;
import java.util.*;

/**
 * XML-related stuff.
 * copy-pasted from a file I wrote in 2005
 * @author Constantin Dolghier
 *
 */
public final class XMLGlobals 
{
	private static DocumentBuilder documentBuilder = getDocumentBuilder();
	
	private static DocumentBuilder getDocumentBuilder()
	{
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * parses the given String as an XML; returns null if invalid
	 * @param value
	 * @return
	 */
	public static Document createNewXML(String value) {
		try {
			Document doc = documentBuilder.parse(new ByteArrayInputStream(value.getBytes("utf-8")));
			return doc;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Element createNewNode(String value) {
		return createNewXML(value).getDocumentElement();
	}
	
	/**
	 * select the nodes which correspond to the raw XPath expression. Returns null if smth goes wrong (e.g. incorrect expression)
	 * @param document
	 * @param expression
	 * @return
	 */
	public static NodeList selectNodes(Object document, String expression) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
			return nodes;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * return the nodes which correspond to the expression and have the corresponding attributes
	 * @param document - the XML
	 * @param expression - the XPath expression
	 * @param attrName - attribute name, e.g. 'Name'
	 * @param attrMask - attribute mask, e.g. 'Ion*';
	 * @return
	 */
	public static List<Element> selectNodesWhichFit(Object document, String expression, String attrName, String attrExpected) {
		NodeList nodeList = selectNodes(document, expression);
		if (nodeList == null) return null;
		ArrayList<Element> res = new ArrayList<Element>();
		for(int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Element elem = (Element) nodeList.item(i);
				String attrVal = elem.getAttribute(attrName);
				if (fitsInMask(attrName, attrVal, attrExpected))
					res.add(elem);
			}
		}
		return res;
	}

	public static boolean fitsInMask(String attrName, String attrValue, String attrExpected) {
		if (attrName == null) return true;
		return attrExpected.equals(attrValue);
	}

	private static void saveToStreamResult(Node node, StreamResult streamResult) throws Exception {
		DOMSource domSource = new DOMSource(node);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING,"utf-8");
			//serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.transform(domSource, streamResult);
	}
	
	/**
	 * writes the XML to a String
	 * @param document
	 * @return the String, if successful. Null if an error occured
	 */
	public static String saveToString(Node node) {
		try {
			StringWriter writer = new StringWriter();
			StreamResult streamResult = new StreamResult(writer);
			saveToStreamResult(node, streamResult);
			return writer.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * writes the XML to a file
	 * @param document
	 * @param fName
	 * @return true, if successful and false, otherwise
	 */
	public static boolean saveToFile(Document document, String fName) {
		try {
			OutputStream out = new FileOutputStream(fName);
			StreamResult streamResult = new StreamResult(out);
			saveToStreamResult(document, streamResult);
			out.close();
			return true;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * appends an XML fragment to an another document
	 * @param where - the destination document
	 * @param what - the source XML tree
	 * @return the copy of the node 'what' in the destination XML
	 */
	public static Node copyXMLFragment(Document where, Node what) {
		Node copiedNode = where.importNode(what, true);
		where.getDocumentElement().appendChild(copiedNode);
		return copiedNode;
	}
	
	/**
	 * selects one node which fits the XPath expression. Throws a TaskException if there is no such node
	 * @param document
	 * @param expression
	 * @return
	 */
	public static Node selectNode(Object document, String expression) {
		NodeList nl = selectNodes(document, expression);
		if (nl.getLength() < 1) {
			throw new RuntimeException("cannot find the node " + expression + "in the XML document!");
		}
		return nl.item(0);
	}
	
	/**
	 * selects one node which fits the XPath expression and which matches the attribute thing. Throws a TaskException if there is no such node
	 * @param document
	 * @param expression
	 * @param attrName
	 * @param attrMask
	 * @return
	 */
	public static Node selectNode(Document document, String expression, String attrName, String attrMask) {
		List<Element> v = selectNodesWhichFit(document, expression, attrName, attrMask);
		if (v.size() < 1)
			throw new RuntimeException("the node " + expression + " does not exist in the XML document!");
		return v.get(0);
	}
	
	/**
	 * tests whether there is EXACTLY one match in the XML for the expression/attribute
	 * @param document - the input XML
	 * @param expression - the expression
	 * @param attrName - the attribute
	 * @param attrMask - the attrmask
	 * @return false, if there is no match. True, if there is exactly one match
	 * @throws TaskException, if there is more than one match
	 */
	public static boolean oneElementMatch(Document document, String expression, String attrName, String attrMask) {
		List<Element> t = selectNodesWhichFit(document, expression, attrName, attrMask);
		if (t.isEmpty()) return false;
		if (t.size() > 1) throw new RuntimeException("BUG: more than element match for search: " + expression + "; attrName = " + attrName + "; attrMask = " + attrMask);
		return true;
	}
	
}

