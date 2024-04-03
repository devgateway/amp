package org.digijava.module.admin.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.admin.helper.FieldInfo;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.form.DataImporterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataImporter extends Action {
    Logger logger = LoggerFactory.getLogger(DataImporter.class);
    private static final long serialVersionUID = 1L;
    private List<FieldInfo> fieldsInfo; // List of field information

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fieldsInfo = getEntityFieldsInfo(AmpActivityFields.class);
//        logger.info("Fields: "+fieldsInfo);
//        DataImporterForm dataImporterForm = (DataImporterForm) form;
//        dataImporterForm.setFieldInfos(fieldsInfo);
        request.setAttribute("fieldsInfo",fieldsInfo);
//        Map<String, String> fieldMapping = new HashMap<>();
//        request.setAttribute("fieldMapping", fieldMapping);

//        request.getRequestDispatcher("/WEB-INF/country-selection.jsp").forward(request, response);

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
        return fieldsInfos;
    }
}
