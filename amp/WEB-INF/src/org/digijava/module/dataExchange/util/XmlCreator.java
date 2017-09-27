package org.digijava.module.dataExchange.util;

import java.util.List;

public class XmlCreator {
	private List<? extends XmlWrappable> items;

	public XmlCreator(List<? extends XmlWrappable> item) {
		super();
		this.items = item;
	}
	
//	public LPEXmlCreator(Long deSourceSettingId) throws HibernateException, SQLException, DgException {
//		ImportLogDAO dao	= new SessionImportLogDAO();
//		this.items			= dao.getAllAmpLogPerExecutionObjects();
//	}
	
	public String createXml(){
		StringBuffer sb	= new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-16\"?>");
		//sb.append("<?xml version=\"1.0\" ?>");
		sb.append("<ResultSet>");
		if ( this.items != null ) {
			for (XmlWrappable item: this.items) {
				sb.append( item.getXmlWrapperInstance().toXMLString() );
			}
		}
		sb.append("</ResultSet>");
		
		return sb.toString();
	}
}
