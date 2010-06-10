/**
 * 
 */
package org.digijava.module.dataExchange.engine;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.dataExchange.action.ImportValidationEventHandler;
import org.digijava.module.dataExchange.jaxb.Activities;
import org.digijava.module.dataExchange.pojo.DEImportItem;
import org.xml.sax.SAXException;
import org.digijava.module.dataExchange.util.DataExchangeConstants;

/**
 * @author dan
 *
 */
public class DEImportBuilder {
	
	private static Logger logger = Logger.getLogger(DEImportBuilder.class);
	private DEImportItem ampImportItem;

	public DEImportBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	public DEImportBuilder(DEImportItem ampImportItem) {
		super();
		this.ampImportItem = ampImportItem;
	}

	public DEImportItem getAmpImportItem() {
		return ampImportItem;
	}

	public void setAmpImportItem(DEImportItem ampImportItem) {
		this.ampImportItem = ampImportItem;
	}
	
	public void checkInputString(){
		//Activities acts;
		try {
			JAXBContext jc = JAXBContext.newInstance(DataExchangeConstants.JAXB_INSTANCE);
	        Unmarshaller m = jc.createUnmarshaller();
	        boolean xsdValidate = true;
	        	
	        	if(xsdValidate){
	                // create a SchemaFactory that conforms to W3C XML Schema
	                 SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

	                 // parse the purchase order schema
	                 Schema schema = sf.newSchema(new File(DataExchangeConstants.IDML_SCHEMA_LOCATION));

	                 m.setSchema(schema);
	                 // set your error handler to catch errors during schema construction
	                 // we can use custom validation event handler
	                 m.setEventHandler(new ImportValidationEventHandler());
	           }
	        	this.getAmpImportItem().setActivities( (org.digijava.module.dataExchange.jaxb.Activities) m.unmarshal(this.ampImportItem.getInputStream()) );
	        	
	        } 
			catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
	        catch (javax.xml.bind.JAXBException jex) {
	        	jex.printStackTrace();
	        	
	        }

	}
	
	public void createAmpLogPerExecution(){
		
	}
	
	public void createAmpLogPerItem(){
		
	}
}
