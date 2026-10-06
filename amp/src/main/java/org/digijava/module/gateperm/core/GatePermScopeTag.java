/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 *
 */
package org.digijava.module.gateperm.core;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.MetaInfo;
import org.digijava.module.gateperm.util.PermissionUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * This tag sets an object in the permission scope. If the tag has a body then the object is
 * removed from the scope when the body ends ( you can read the object from inside a {@link Gate}
 * only within the tag body. If the tag has a null body, then the scope is never removed. 
 * You can only remove it from the servlet using {@ PermissionUtil#removeFromScope(javax.servlet.http.HttpSession, MetaInfo)}
 * or {@ PermissionUtil#resetScope(javax.servlet.http.HttpSession)}
 * @author mpostelnicu@dgateway.org
 * @since Apr 28, 2011
 */
public class GatePermScopeTag extends BodyTagSupport {

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(GatePermScopeTag.class);
    private String key;
    private String name;
    private String property;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String value) {
        this.property = value;
    }

    @Override
    public int doStartTag() throws JspException {
        if (value != null) {
            PermissionUtil.putInScope(pageContext.getSession(), getMetaKey(),
                    value);
            return EVAL_BODY_BUFFERED;
        }
        Object nameBean = pageContext.findAttribute(name);
        Object simpleProperty = null;
        if (property != null)
            try {
                simpleProperty = PropertyUtils.getSimpleProperty(nameBean,
                        property);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                logger.error(e.getMessage(), e);
            }

        if (property == null)
            PermissionUtil.putInScope(pageContext.getSession(), getMetaKey(),
                    nameBean);
        else
            PermissionUtil.putInScope(pageContext.getSession(), getMetaKey(),
                    simpleProperty);

        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        if (bodyContent != null) {
            PermissionUtil.removeFromScope(pageContext.getSession(),
                    getMetaKey());
             try {
                pageContext.getOut().print(bodyContent.getString());
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        
        return EVAL_PAGE;
    }

    private MetaInfo getMetaKey() {
        MetaInfo metaKey = GatePermConst.scopeKeysMap.get(key);
        if (metaKey == null)
            throw new RuntimeException("No scope key with name " + key
                    + " could be found");
        return metaKey;
    }
}
