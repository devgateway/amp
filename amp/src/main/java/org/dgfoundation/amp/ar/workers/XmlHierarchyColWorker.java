/**
 * 
 */
package org.dgfoundation.amp.ar.workers;

import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.XmlHierarchyCell;
import org.dgfoundation.amp.ar.helper.HierarchycalItem;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/**
 * @author Alex
 *
 */
public class XmlHierarchyColWorker extends ColumnWorker {

    private SimpleDateFormat xmlSimpleDateFormat;
    private SimpleDateFormat ampSimpleDateFormat;
    
    
    public XmlHierarchyColWorker(String condition,String viewName,String columnName,ReportGenerator generator) {
        super(condition,viewName, columnName,generator);
        xmlSimpleDateFormat         = new SimpleDateFormat("yyyy-MM-dd");
        String defaultFormat        = FeaturesUtil
                .getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
        this.ampSimpleDateFormat    = new SimpleDateFormat(defaultFormat);  
    }

    
    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromRow(java.sql.ResultSet)
     */
    @Override
    protected Cell getCellFromRow(ResultSet rs) throws SQLException {
        Long ownerId = new Long(rs.getLong(1));

        Document document   = null;
        
        try {
            Reader reader   = rs.getCharacterStream(2);
            InputSource src = new InputSource(reader);
            DocumentBuilder parser;
            parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = parser.parse(src);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//      SQLXML sqlXml = rs.getSQLXML(2);
//      DOMSource domSource = sqlXml.getSource(DOMSource.class);
//      document = (Document) domSource.getNode();
        if ( document != null ) {
            Element element     = document.getDocumentElement();
            ArrayList<HierarchycalItem> rootItems   = new ArrayList<HierarchycalItem>();
                    
            if (element != null && element.getChildNodes() != null) {
                for (int j = 0; j < element.getChildNodes().getLength(); j++) {
                    Node child  = element.getChildNodes().item(j);
                    if ( child.getNodeType() != Node.TEXT_NODE ) {
                        HierarchycalItem item   = new HierarchycalItem();
                        item.setSimpleDateFormat(this.ampSimpleDateFormat);
                        this.populateItem(item, child);
                        rootItems.add(item);
                    }
                }
            }
            
            XmlHierarchyCell cell   = new XmlHierarchyCell(ownerId);
            Collections.sort(rootItems, HierarchycalItem.DATE_COMPARTOR);
            cell.setRootItems(rootItems);
            return cell;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#getCellFromCell(org.dgfoundation.amp.ar.cell.Cell)
     */
    @Override
    protected Cell getCellFromCell(Cell src) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.workers.ColumnWorker#newCellInstance()
     */
    @Override
    public Cell newCellInstance() {
        XmlHierarchyCell cell   = new XmlHierarchyCell();
        cell.setRootItems(new ArrayList<HierarchycalItem>());
        return cell;
    }
    
    private void populateItem (HierarchycalItem item, Node node) {
        item.setName( node.getNodeName() );
        if ( node.getChildNodes() != null ) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                Node n  = node.getChildNodes().item(i);
                if ( "description".equals(n.getNodeName() ) ){
                    item.setDescription(n.getTextContent());
                }
                if ( "date".equals(n.getNodeName() ) ){
                    try {
                        String dateText = n.getTextContent().trim();
                        Date date = dateText.isEmpty() ? null : 
                            xmlSimpleDateFormat.parse(dateText);
                        item.setDate(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ( "list".equals(n.getNodeName()) ) {
                    for (int j = 0; j < n.getChildNodes().getLength(); j++) {
                        Node child  = n.getChildNodes().item(j);
                        if ( child.getNodeType() != Node.TEXT_NODE ) {
                            HierarchycalItem newItem    = new HierarchycalItem();
                            newItem.setSimpleDateFormat(this.ampSimpleDateFormat);
                            this.populateItem(newItem, child);
                            item.addChild(newItem);
                        }
                    }
                }
            }
        }
    }

}
