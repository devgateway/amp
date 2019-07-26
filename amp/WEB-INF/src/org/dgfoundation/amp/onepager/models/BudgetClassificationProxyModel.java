/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

/**
 * @author aartimon@dginternational.org since Feb 4, 2011
 */
public class BudgetClassificationProxyModel extends Model {
    private static Logger logger = Logger.getLogger(BudgetClassificationProxyModel.class);

    private IModel sourceModel;
    private String fieldName;
    private IModel<List<?>> optionList;
    
    public BudgetClassificationProxyModel(IModel sourceModel, String fieldName, IModel optionList) {
        this.sourceModel = sourceModel;
        this.fieldName = fieldName;
        this.optionList = optionList;
    }
    
    @Override
    public void setObject(Serializable object) {
        if (object == null){
            sourceModel.setObject(null);
            return;
        }
        sourceModel.setObject(getField(object));
    }

    private Object getField(Object object){
        Class c = object.getClass();
        try {
            String methodName = "get" + Strings.capitalize(fieldName);
            Method method = c.getMethod(methodName);
            
            Object value = method.invoke(object);
            return value;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    @Override
    public Serializable getObject() {
        Long id = (Long) sourceModel.getObject();
        if (id == null)
            return null;
        Serializable result = null;
        if (optionList != null && optionList.getObject() != null){
            Iterator it = optionList.getObject().iterator();
            while (it.hasNext()) {
                Object opt = (Object) it.next();
                if (id.compareTo((Long) getField(opt)) == 0){
                    result = (Serializable) opt;
                    break;
                }
            }
        }
        return result;
    }

}
