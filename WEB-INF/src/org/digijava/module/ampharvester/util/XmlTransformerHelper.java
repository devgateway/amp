package org.digijava.module.ampharvester.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.digijava.module.ampharvester.exception.AmpHarvesterException;
import org.digijava.module.ampharvester.jaxb10.Activities;
import org.digijava.module.ampharvester.jaxb10.ActivityType;
import org.digijava.module.ampharvester.jaxb10.impl.ActivitiesImpl;
import org.w3c.dom.Document;

public class XmlTransformerHelper {

  private static String INSTANCE = "org.digijava.module.ampharvester.jaxb10";

  /***********************************************
   ****** XML transforming and validationg  ******
   ***********************************************/


  public static Object geterateJAXB(String str) throws AmpHarvesterException {
    return geterateJAXB(str.getBytes());
  }

  public static Object geterateJAXB(byte[] bytes) throws AmpHarvesterException {
    Object retValue = null;
    try {
      JAXBContext context = JAXBContext.newInstance(INSTANCE);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      retValue = unmarshaller.unmarshal(new ByteArrayInputStream(bytes));
    } catch (Exception ex) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_XML_VALIDATION, ex);
    }

    return retValue;
  }

  public static Object geterateJAXB(File xml) throws AmpHarvesterException {
    Object retValue = null;
    try {
      JAXBContext context = JAXBContext.newInstance(INSTANCE);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      retValue = unmarshaller.unmarshal(xml);
    } catch (Exception ex) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_XML_VALIDATION, ex);
    }
    return retValue;
  }

  public static String marshal(ActivityType activity) throws AmpHarvesterException {
    Activities activities = new ActivitiesImpl();
    activities.getActivity().add(activity);
    return marshal(activities);
  }

  public static String marshal(List<ActivityType> activity) throws AmpHarvesterException {
    Activities activities = new ActivitiesImpl();
    activities.getActivity().addAll(activity);
    return marshal(activities);
  }

  public static String marshal(Activities activities) throws AmpHarvesterException {
      String retValue = new String(marshalToByte(activities));
    return retValue;    
  }
  
  public static byte[] marshalToByte(Activities activities) throws AmpHarvesterException {
    byte [] retValue=null;
    try {
      JAXBContext context = JAXBContext.newInstance(INSTANCE);
      Marshaller marshaller = context.createMarshaller();

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      marshaller.marshal(activities, outputStream);
      
      retValue = outputStream.toByteArray();

    } catch (Exception ex) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_XML_VALIDATION, ex);
    }
    return retValue;
  }

  public static byte[] transform(String utiXSLT, String isXML) throws AmpHarvesterException {
    try {
      return transform((new URL(utiXSLT)).openStream(), (new URL(isXML)).openStream());
    } catch (Exception ex) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_XML_VALIDATION, ex);
    }
  }

  public static byte[] transform(File utiXSLT, String isXML) throws AmpHarvesterException {
    try {
      return transform(new FileInputStream(utiXSLT), (new URL(isXML)).openStream());
    } catch (Exception ex) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_XML_VALIDATION, ex);
    }
  }

  public static byte[] transform(File utiXSLT, File isXML) throws AmpHarvesterException {
    try {
      return transform(new FileInputStream(utiXSLT), new FileInputStream(isXML));
    } catch (Exception ex) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_XML_VALIDATION, ex);
    }
  }

  public static byte[] transform(byte[] isXSLT, byte[] isXML) throws AmpHarvesterException {
    return transform(new ByteArrayInputStream(isXSLT), new ByteArrayInputStream(isXML));
  }

  public static byte[] transform(InputStream isXSLT, InputStream isXML) throws AmpHarvesterException {
    byte[] retValue = null;

    StreamSource xslt = null;
    StreamSource sourceXML = null;
    StreamResult result = null;

    try {
      xslt = new StreamSource(isXSLT);
      sourceXML = new StreamSource(isXML);
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      result = new StreamResult(bos);

      TransformerFactory tFactory = TransformerFactory.newInstance();

      Transformer transformer = tFactory.newTransformer(xslt);

      transformer.transform(sourceXML, result);

      retValue = bos.toByteArray();

    } catch (Exception ex) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_XML_VALIDATION, ex);
    }
    return retValue;
  }

  public static byte[] getBytes(String uri) throws AmpHarvesterException {
    byte[] retValue = null;
    try {
      URL url = new URL(uri);
      URLConnection uConn = url.openConnection();
      uConn.setConnectTimeout(30000);
      uConn.setReadTimeout(30000);
      InputStream is = uConn.getInputStream();
//            InputStream is = new URL(uri).openStream();

      byte data[] = new byte[2048];
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int count;
      while ((count = is.read(data, 0, 2048)) != -1) {
        out.write(data, 0, count);
      }

      retValue = out.toByteArray();
      out.close();

    } catch (Exception ex) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_XML_VALIDATION, ex);
    }
    return retValue;
  }

  public static void validation(byte[] isXSD, byte[] isXML) throws AmpHarvesterException {
    try {

      // parse an XML document into a DOM tree
      DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = parser.parse(new ByteArrayInputStream(isXML));

      // create a SchemaFactory capable of understanding WXS schemas
      SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

      // load a WXS schema, represented by a Schema instance
      Source schemaFile = new StreamSource(new ByteArrayInputStream(isXSD));
      Schema schema = factory.newSchema(schemaFile);

      // create a Validator instance, which can be used to validate an instance document
      Validator validator = schema.newValidator();

      // validate the DOM tree
      validator.validate(new DOMSource(document));
    } catch (Exception ex) {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_XML_VALIDATION, ex);
    }
  }

}
