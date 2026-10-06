<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
