package org.digijava.module.dataExchange.util;

import org.digijava.module.dataExchange.dbentity.DESourceSetting;

public class WrapperSourceSetting implements XmlWrapper{
	private DESourceSetting ss;


	public WrapperSourceSetting(DESourceSetting ss) {
		super();
		this.ss = ss;
	}



	@Override
	public String toXMLString () {
		StringBuffer sb	= new StringBuffer();
		sb.append("<SourceSetting>");
		
		
		sb.append("<Name>" + ss.getName() + "</Name>");
		
		sb.append("<DbId>" + ss.getId() + "</DbId>");
		
		sb.append("<Source>" + ss.getSource() + "</Source>");
		
		sb.append("<Workspace>" + ss.getImportWorkspace().getName() + "</Workspace>");
		
		sb.append("<ImportStrategy>" + ss.getImportStrategy() + "</ImportStrategy>");
		
		sb.append("<ApprovalStatus>" + ss.getApprovalStatus() + "</ApprovalStatus>");
		
		
		sb.append("</SourceSetting>");
		
		return sb.toString();
	}
}
