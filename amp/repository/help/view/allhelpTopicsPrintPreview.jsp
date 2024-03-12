<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/moduleVisibility.tld" prefix="module" %>
<%@page import="org.digijava.module.help.util.HelpUtil"%>

<digi:instance property="helpForm" />

<bean:define id="tree" name="helpForm" property="topicTree" type="java.util.Collection"/>
<%= HelpUtil.renderHelpPrintPreview(tree,"admin",request) %>		