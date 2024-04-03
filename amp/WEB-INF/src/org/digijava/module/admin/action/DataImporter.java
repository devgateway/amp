package org.digijava.module.admin.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.admin.helper.FieldInfo;
import org.digijava.module.aim.dbentity.AmpActivityFields;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DataImporter extends Action {
    private static final long serialVersionUID = 1L;
    private List<FieldInfo> fieldsInfo; // List of field information

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fieldsInfo = getEntityFieldsInfo(AmpActivityFields.class);
        return mapping.findForward("importData");
    }
    private List<FieldInfo> getEntityFieldsInfo(Class<?> entityClass) {
        List<FieldInfo> fieldsInfos = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            // Extract field information and group them by subclass
            Class<?> declaringClass = field.getDeclaringClass();
            String subclass = declaringClass.getSimpleName();
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            fieldsInfos.add(new FieldInfo(subclass, fieldName, fieldType));
        }
        return fieldsInfo;
    }
}
