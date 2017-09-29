package org.digijava.module.dataExchange.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.digijava.module.dataExchange.dbentity.IatiCodeType;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 4/2/14
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class CodeImporterForm extends ActionForm {
    private FormFile file;
    List<IatiCodeType> types;

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public List<IatiCodeType> getTypes() {
        return types;
    }

    public void setTypes(List<IatiCodeType> types) {
        this.types = types;
    }
}
