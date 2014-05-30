package org.digijava.module.dataExchange.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.xerces.dom.DeferredElementImpl;
import org.digijava.module.dataExchange.dbentity.IatiCodeItem;
import org.digijava.module.dataExchange.dbentity.IatiCodeType;
import org.digijava.module.dataExchange.form.CodeImporterForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.dataExchange.util.DataExchangeConstants;
import org.digijava.module.dataExchange.util.DbUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 4/2/14
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CodeImporter extends DispatchAction {
    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        CodeImporterForm myform = (CodeImporterForm) form;
        myform.setTypes(DbUtil.getAllCodetypes());
        return mapping.findForward("forward");
    }

    public ActionForward getCodeItems(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        CodeImporterForm myform = (CodeImporterForm) form;
        String typeIdStr = request.getParameter("typeId");
        Long typeId = new Long(typeIdStr);

        IatiCodeType selType = (IatiCodeType) DbUtil.getObject(IatiCodeType.class, typeId);
        JSONObject retObj = new JSONObject();
        retObj.accumulate("id", selType.getId());
        retObj.accumulate("name", selType.getName());
        retObj.accumulate("description", selType.getDescription());
        retObj.accumulate("date", selType.getImportDateFormated());
        retObj.accumulate("ampName", selType.getAmpName());

        JSONArray objects = new JSONArray();
        for (IatiCodeItem item : selType.getItems()) {
            JSONObject codeItem = new JSONObject();
            codeItem.accumulate("name", item.getName());
            codeItem.accumulate("code", item.getCode());
            codeItem.accumulate("updateDate", item.getUpdateDate());
            objects.add(codeItem);
        }
        retObj.accumulate("objects", objects);

        PrintWriter pw =  response.getWriter();
        retObj.write(pw);
        pw.close();
        return null;
    }

    public ActionForward updateAmpName(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        CodeImporterForm myform = (CodeImporterForm) form;
        String typeIdStr = request.getParameter("typeId");
        Long typeId = new Long(typeIdStr);
        String newVal = request.getParameter("newVal");

        IatiCodeType selCodeType = (IatiCodeType) DbUtil.getObject(IatiCodeType.class, typeId);
        selCodeType.setAmpName(newVal);
        DbUtil.saveObject(selCodeType);

        return null;
    }

    public ActionForward upload(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        CodeImporterForm myform = (CodeImporterForm) form;
        FormFile upFile = myform.getFile();
        if (upFile != null) {
            Set <IatiCodeType> newTypes = new HashSet<IatiCodeType>();
            String contentType = upFile.getContentType();
            if (contentType.equals("text/xml")) {
                String xmlSrc = new String(upFile.getFileData(), "UTF-8");
                try {
                    IatiCodeType codeType = processXml(xmlSrc.trim().getBytes("UTF-8"));
                    codeType.setFileName(upFile.getFileName());
                    codeType.setImportDate(new Date());
                    newTypes.add(codeType);
                } catch (Exception ex) {

                }
            } else if (contentType.equals("application/zip") || contentType.equals("application/x-zip-compressed")) {
                ZipInputStream zip = new ZipInputStream(upFile.getInputStream());
                ZipEntry entry = null;
                while ((entry = zip.getNextEntry()) != null) {
                    byte[] buffer = new byte[2048];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int len = 0;
                    while ((len = zip.read(buffer)) > 0)
                    {
                        baos.write(buffer, 0, len);
                    }
                    zip.read(baos.toByteArray());
                    String xmlSrc = new String(baos.toByteArray(), "UTF-8");
                    try {
                        IatiCodeType codeType = processXml(xmlSrc.trim().getBytes("UTF-8"));
                        codeType.setFileName(entry.getName());
                        codeType.setImportDate(new Date());
                        newTypes.add(codeType);

                    } catch (Exception ex) {

                    }
                }
                zip.close();



            } else { //unsupported type
                //System.out.println("xz");
            }

            List <IatiCodeType> updated = new ArrayList<IatiCodeType>(checkExistingAndSave(newTypes));
            //myform.setTypes(updated);



        }
        myform.setFile(null);
        return mapping.findForward("afterUpload");
    }

    private IatiCodeType processXml(byte[] xml) throws ParserConfigurationException, IOException, SAXException {
        IatiCodeType retVal = new IatiCodeType();
        retVal.setItems(new HashSet<IatiCodeItem>());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new ByteArrayInputStream(xml));
        Document document = builder.parse(is);
        Element root = document.getDocumentElement();
        String name = root.getAttribute("name");
        String ampName = null;
        Map <String, String> ampToIatiStaticMap = DataExchangeConstants.AmpToIATI;

        for (String key : ampToIatiStaticMap.keySet()) {
            String iatiName = ampToIatiStaticMap.get(key);
            if (name.equals(iatiName)) {
                ampName = key;
                break;
            }
        }
        if (ampName == null) ampName = name;

        retVal.setName(name);
        retVal.setAmpName(ampName);

        NodeList nodeList = root.getChildNodes();

        for (int nodeIdx = 0; nodeIdx < nodeList.getLength(); nodeIdx ++) {
            Node node = nodeList.item(nodeIdx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();
                if (nodeName.equals("metadata")) {
                    String desc = getFirstChildNodeValueByName(node.getChildNodes(),"description");
                    retVal.setDescription(desc);
                } else if (nodeName.equals("codelist-items")) {
                    Node n = node.getFirstChild();
                    while (n.getNextSibling() != null) {
                        if (n.getNodeName().equals("codelist-item")) {
                            IatiCodeItem ci = new IatiCodeItem();
                            ci.setName(getFirstChildNodeValueByName(n.getChildNodes(), "name"));
                            ci.setCode(getFirstChildNodeValueByName(n.getChildNodes(), "code"));
                            ci.setUpdateDate(new Date());
                            ci.setType(retVal);
                            retVal.getItems().add(ci);
                        }
                        n = n.getNextSibling();
                    }

                }
            }
        }
        return retVal;
    }

    private String getFirstChildNodeValueByName(NodeList nodeList, String nodeName) {
        String retVal = null;
        for (int idx = 0; idx < nodeList.getLength(); idx ++) {
            Node node = nodeList.item(idx);
            if (node.getNodeName().equals(nodeName) && node.getFirstChild() != null) {
                retVal = node.getFirstChild().getNodeValue();
                break;
            }
        }
        return retVal;
    }

    //Updates if exists and creates if new
    private Set <IatiCodeType> checkExistingAndSave (Set <IatiCodeType> types) {
        Set <IatiCodeType> retVal = null;
        if (types != null && !types.isEmpty()) {
            Set <String>names = new HashSet<String>();

            for (IatiCodeType type : types) {
                names.add(type.getName());
            }
    //        DbUtil.saveObjectSet(types);
            List<IatiCodeType> dbCodeTypes = DbUtil.getCodetypeListByNames(names);
            Map <String, IatiCodeType> dbNamesCodetypeMap = new HashMap<String, IatiCodeType>();

            for (IatiCodeType type : dbCodeTypes) {
                dbNamesCodetypeMap.put(type.getName(), type);
            }

            Set <IatiCodeType> createSet = new HashSet<IatiCodeType>();
            //Set <IatiCodeType> updateSet = new HashSet<IatiCodeType>();
            for (IatiCodeType type : types) {
                IatiCodeType dbObj = dbNamesCodetypeMap.get(type.getName());
                if (dbObj == null) {
                    createSet.add(type);
                } else {
                    Set <IatiCodeItem>itemCreateSet = new HashSet<IatiCodeItem>(type.getItems());
                    for (Iterator<IatiCodeItem> it = dbObj.getItems().iterator(); it.hasNext();) {
                        IatiCodeItem dbItem = it.next();
                        boolean exists = false;
                        for (IatiCodeItem memItem : type.getItems()) {
                            if (dbItem.getName().equals(memItem.getName())) {
                                    if (!dbItem.getCode().equals(memItem.getCode())) {
                                    dbItem.setCode(memItem.getCode());
                                    dbItem.setUpdateDate(memItem.getUpdateDate());
                                }
                                itemCreateSet.remove(memItem);
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) it.remove();
                    }

                    for (IatiCodeItem item : itemCreateSet) {
                        item.setType(dbObj);
                    }
                    dbObj.getItems().addAll(itemCreateSet);
                }
            }

            if (!createSet.isEmpty()) {
                dbCodeTypes.addAll(createSet);
            }

            Set <IatiCodeType> forDb = new HashSet();
            forDb.addAll(dbCodeTypes);
            DbUtil.saveObjectSet(forDb);
            retVal = forDb;
        }
        return retVal;
    }
}
