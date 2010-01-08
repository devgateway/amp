package org.digijava.module.mondrian.query;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mondrian.olap.Util.PropertyList;
import mondrian.rolap.CacheControlImpl;
import mondrian.spi.DynamicSchemaProcessor;
import mondrian.spi.impl.FilterDynamicSchemaProcessor;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



/**
 * @author Diego Dimunzio
 * @since January 09,2009
 * 
 */

public class SchemaManager extends FilterDynamicSchemaProcessor implements
		DynamicSchemaProcessor {
	protected static Logger logger = Logger.getLogger(SchemaManager.class);

	@Override
	public String processSchema(String arg0, PropertyList arg1)
			throws Exception {
		FileSystemManager fsManager = VFS.getManager();
		File userDir = new File("").getAbsoluteFile();
		FileObject file = fsManager.resolveFile(userDir, arg0);
		FileContent fileContent = ((org.apache.commons.vfs.FileObject) file)
				.getContent();
		String schema = super.filter(arg0, arg1, fileContent.getInputStream());
		CacheControlImpl cache = new mondrian.rolap.CacheControlImpl();
		cache.flushSchemaCache();
		return setTeamQuery(schema);
	}

	public String setTeamQuery(String schema) {
		String newschema = "";
		newschema = schema.replaceAll("(?:@donorquery)+", getQueryText());
		logger.info("Query = " + getQueryText());
		return Translate(ApllyVisibility(newschema));
	}

	public String getQueryText() {
		String result = QueryThread.getQuery();
		Pattern p = Pattern.compile(MoConstants.AMP_ACTIVITY_TABLE);
		Matcher m = p.matcher(result);
		result = m.replaceAll(MoConstants.CACHED_ACTIVITY_TABLE);
		return result;
	}

	private String ApllyVisibility(String schema) {
		Document schemaxml = Schema2Xml(schema);
		NodeList dimensions = schemaxml.getElementsByTagName("Dimension");
		
		ServletContext ampContext;
		ampContext=QueryThread.getContext();
		AmpTreeVisibility ampTreeVisibility=(AmpTreeVisibility) ampContext.getAttribute("ampTreeVisibility");
		for (int i = 0; i < dimensions.getLength(); i++) {
			AmpFieldsVisibility field = ampTreeVisibility.getFieldByNameFromRoot(dimensions.item(i).getAttributes().item(0).getNodeValue());
			if(field !=null){
				logger.info("Field Name: " + field.getName());
			}
			if(field !=null && !field.isFieldActive(ampTreeVisibility)){
				 dimensions.item(i).getParentNode().removeChild(dimensions.item(i));
				 i--;
			 }
		}
		try {
			return Xml2String(schemaxml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return schema;
		}
	}
	
	public String Xml2String(Document doc)throws Exception{
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StringWriter sw=new StringWriter();
        StreamResult result = new StreamResult(sw);
        transformer.transform(source, result);
        String xmlString=sw.toString();
        return xmlString;
        
    }  
	
	private Document Schema2Xml(String xml){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Document doc = null;
        
        try {
			doc = builder.parse(new InputSource(new StringReader(xml)));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return doc;
	}
	
	private String Translate(String shema) {
		Long siteId = QueryThread.getSite().getId();
		String locale = QueryThread.getLocale().getCode();
		try {
			// Dimensions
			shema = shema.replaceAll("#Activity#", TranslatorWorker .translateText(MoConstants.ACTIVITY, locale, siteId));
			shema = shema.replaceAll("#Primary_Program#",TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAMS, locale, siteId));
			shema = shema.replaceAll("#Primary_Program_level_1#",TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAM_LEVEL_1,locale, siteId));
			shema = shema.replaceAll("#Primary_Program_level_2#",TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAM_LEVEL_2,locale, siteId));
			shema = shema.replaceAll("#Primary_Program_level_3#",TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAM_LEVEL_3,locale, siteId));
			shema = shema.replaceAll("#Primary_Program_level_4#",TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAM_LEVEL_4,locale, siteId));
			shema = shema.replaceAll("#Primary_Program_level_5#",TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAM_LEVEL_5,locale, siteId));
			shema = shema.replaceAll("#Primary_Program_level_6#",TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAM_LEVEL_6,locale, siteId));
			shema = shema.replaceAll("#Primary_Program_level_7#",TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAM_LEVEL_7,locale, siteId));
			shema = shema.replaceAll("#Primary_Program_level_8#",TranslatorWorker.translateText(MoConstants.PRIMARY_PROGRAM_LEVEL_8,locale, siteId));

			shema = shema.replaceAll("#Secondary_Program#", TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS, locale,siteId));
			shema = shema.replaceAll("#Secondary_Program_level_1#",TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS_LEVEL_1, locale,siteId));
			shema = shema.replaceAll("#Secondary_Program_level_2#",TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS_LEVEL_2, locale,siteId));
			shema = shema.replaceAll("#Secondary_Program_level_3#",TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS_LEVEL_3, locale,siteId));
			shema = shema.replaceAll("#Secondary_Program_level_4#",TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS_LEVEL_4, locale,siteId));
			shema = shema.replaceAll("#Secondary_Program_level_5#",TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS_LEVEL_5, locale,siteId));
			shema = shema.replaceAll("#Secondary_Program_level_6#",TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS_LEVEL_6, locale,siteId));
			shema = shema.replaceAll("#Secondary_Program_level_7#",TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS_LEVEL_7, locale,siteId));
			shema = shema.replaceAll("#Secondary_Program_level_8#",TranslatorWorker.translateText(MoConstants.SECONDARY_PROGRAMS_LEVEL_8, locale,siteId));

			shema = shema.replaceAll("#National_Program#",TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM, locale, siteId));
			shema = shema.replaceAll("#National_Program_level_1#",TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM_LEVEL_1, locale,siteId));
			shema = shema.replaceAll("#National_Program_level_2#",TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM_LEVEL_2, locale,siteId));
			shema = shema.replaceAll("#National_Program_level_3#",TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM_LEVEL_3, locale,siteId));
			shema = shema.replaceAll("#National_Program_level_4#",TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM_LEVEL_4, locale,siteId));
			shema = shema.replaceAll("#National_Program_level_5#",TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM_LEVEL_5, locale,siteId));
			shema = shema.replaceAll("#National_Program_level_6#",TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM_LEVEL_6, locale,siteId));
			shema = shema.replaceAll("#National_Program_level_7#",TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM_LEVEL_7, locale,siteId));
			shema = shema.replaceAll("#National_Program_level_8#",TranslatorWorker.translateText(MoConstants.NATIONAL_PROGRAM_LEVEL_8, locale,siteId));

			shema = shema.replaceAll("#Primary_Sector#", TranslatorWorker.translateText(MoConstants.PRIMARY_SECTOR, locale, siteId));
			shema = shema.replaceAll("#Donor_Dates#", TranslatorWorker.translateText(MoConstants.DONOR_DATES, locale, siteId));
			shema = shema.replaceAll("#Regions#", TranslatorWorker.translateText(MoConstants.REGIONS, locale, siteId));
			shema = shema.replaceAll("#Status#", TranslatorWorker.translateText(MoConstants.STATUS, locale, siteId));
			shema = shema.replaceAll("#Donor#", TranslatorWorker.translateText(MoConstants.DONOR, locale, siteId));
			shema = shema.replaceAll("#Donor_Group#", TranslatorWorker.translateText(MoConstants.DONOR_GROUP, locale, siteId));
			shema = shema.replaceAll("#Donors_types#", TranslatorWorker.translateText(MoConstants.DONOR_TYPES, locale, siteId));
			shema = shema.replaceAll("#Financing_Instrument#", TranslatorWorker.translateText(MoConstants.FINANCING_INTRUMENT, locale,siteId));
			shema = shema.replaceAll("#Terms_of_Assistance#", TranslatorWorker.translateText(MoConstants.TERMS_OF_ASSISTANCE, locale,siteId));
			shema = shema.replaceAll("#Sub-Sectors#", TranslatorWorker.translateText(MoConstants.SUB_SECTORS, locale, siteId));
			shema = shema.replaceAll("#Sec-Sub-Sectors#", TranslatorWorker.translateText(MoConstants.SEC_SUB_SECTORS, locale,siteId));
			shema = shema.replaceAll("#Currency#", TranslatorWorker.translateText(MoConstants.CURRENCY, locale, siteId));

			// All label
			shema = shema.replaceAll("#All_Activities#", TranslatorWorker.translateText(MoConstants.ALL_ACTIVITIES, locale, siteId));
			shema = shema.replaceAll("#All_Programs#", TranslatorWorker.translateText(MoConstants.ALL_PROGRAMS, locale, siteId));
			shema = shema.replaceAll("#All_Programs#", TranslatorWorker.translateText(MoConstants.ALL_PROGRAMS, locale, siteId));
			shema = shema.replaceAll("#All_Primary_Sectors#", TranslatorWorker.translateText(MoConstants.ALL_PRIMARY_SECTOR, locale,siteId));
			shema = shema.replaceAll("#All_Secondary_Sectors#",TranslatorWorker.translateText(MoConstants.ALL_SECONDARY_SECTOR, locale, siteId));
			shema = shema.replaceAll("#All_Donor_Dates#", TranslatorWorker.translateText(MoConstants.ALL_PERIODS, locale, siteId));
			shema = shema.replaceAll("#All_Regions#", TranslatorWorker.translateText(MoConstants.ALL_REGIONS, locale, siteId));
			shema = shema.replaceAll("#All_Status#", TranslatorWorker.translateText(MoConstants.ALL_STATUS, locale, siteId));
			shema = shema.replaceAll("#All_Donor#", TranslatorWorker.translateText(MoConstants.ALL_DONOR, locale, siteId));
			shema = shema.replaceAll("#All_Donors_types#", TranslatorWorker.translateText(MoConstants.ALL_DONOR_TYPES, locale,siteId));
			shema = shema.replaceAll("#All_Donor_Group#", TranslatorWorker.translateText(MoConstants.ALL_DONOR_GROUP, locale,siteId));
			shema = shema.replaceAll("#All_Financing_Instrument#",TranslatorWorker.translateText(MoConstants.ALL_FINANCING_INTRUMENT,locale, siteId));
			shema = shema.replaceAll("#All_Terms_of_Assistance#",TranslatorWorker.translateText(MoConstants.ALL_TERMS_OF_ASSISTANCE,locale, siteId));
			shema = shema.replaceAll("#All Sub-Sectors#", TranslatorWorker.translateText(MoConstants.ALL_SUB_SECTORS, locale,siteId));
			shema = shema.replaceAll("#All Currencies#", TranslatorWorker.translateText(MoConstants.ALL_CURRENCIES, locale, siteId));

			// Measures
			shema = shema.replaceAll("#Activity_Count#", TranslatorWorker.translateText(MoConstants.ACTIVITY_COUNT, locale, siteId));
			shema = shema.replaceAll("#Actual_Commitments#", TranslatorWorker.translateText(MoConstants.ACTUAL_COMMITMENTS, locale,siteId));
			shema = shema.replaceAll("#Actual_Disbursements#", TranslatorWorker.translateText(MoConstants.ACTUAL_DISBURSEMENTS, locale,siteId));
			shema = shema.replaceAll("#Actual_Expenditures#", TranslatorWorker.translateText(MoConstants.ACTUAL_EXPENDITURES, locale,siteId));
			shema = shema.replaceAll("#Planned_Commitments#", TranslatorWorker.translateText(MoConstants.PLANNED_COMMITMENTS, locale,siteId));
			shema = shema.replaceAll("#Planned_Disbursements#",TranslatorWorker.translateText(MoConstants.PLANNED_DISBURSEMENTS, locale, siteId));
			shema = shema.replaceAll("#Planned_Expenditures#", TranslatorWorker.translateText(MoConstants.PLANNED_EXPENDITURES, locale,siteId));

			shema = shema.replaceAll("#No_Data#", TranslatorWorker.translateText(ArConstants.UNALLOCATED, locale, siteId));

		} catch (WorkerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return shema;
	}
}
