<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fn.tld" prefix="fn" %>
<bean:define name="currentTemplate" id="currentTemplate" type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility" scope="request" toScope="page"/>
<bean:define id="moduleAux" name="moduleAux" type="org.dgfoundation.amp.visibility.AmpTreeVisibility" scope="request" toScope="page"/>
<bean:define id="moduleAux2" name="moduleAux" property="root" type="org.digijava.module.aim.dbentity.AmpModulesVisibility" scope="page"/>
<bean:define id="counter" name="counter" type="Integer" scope="request" toScope="page"/>

<digi:instance property="aimVisibilityManagerForm" />
	<li>	
    <a href="#divmodule<bean:write name="moduleAux" property="root.id"/>" id="treeId<bean:write name="moduleAux" property="root.id"/>" indexTab="${counter}">
    <c:set var="nodeName">
	    <digi:trn key='<%="fm:"+moduleAux.getRoot().getNameTrimmed() %>'><bean:write name="moduleAux" property="root.properName"/></digi:trn>
    </c:set>
    <div style="text-transform:capitalize;">
    ${fn:toLowerCase(nodeName)}
    </div>
    </a>
</li>
