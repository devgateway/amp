package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.admin.helper.FieldInfo;

import java.util.ArrayList;
import java.util.List;

public class DataImporterForm extends ActionForm {
    List<FieldInfo> fieldInfos =new ArrayList<>();

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }
}
