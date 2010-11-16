<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
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
