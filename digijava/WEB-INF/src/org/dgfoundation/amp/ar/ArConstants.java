/**
 * ArConstants.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 15, 2006
 *
 */
public final class ArConstants {
	public final static MetaInfo []prefixes=new MetaInfo[] {
		//PLEASE KEEP THE SAME ORDER IN prefixes AND suffixes !!
		
		new MetaInfo(GenericViews.HTML,"/repository/aim/view/ar/html/"),
		new MetaInfo(GenericViews.XLS,"org.dgfoundation.amp.ar.view.xls."),
		new MetaInfo(GenericViews.PDF,"org.dgfoundation.amp.ar.view.pdf."),
		new MetaInfo(GenericViews.PRINT,"/repository/aim/view/ar/print/"),
		};

	public final static MetaInfo []suffixes=new MetaInfo[] {
		new MetaInfo(GenericViews.HTML,".jsp"),
		new MetaInfo(GenericViews.XLS,"XLS"),
		new MetaInfo(GenericViews.PDF,"PDF"),
		new MetaInfo(GenericViews.PRINT,".jsp"),
		};
}
