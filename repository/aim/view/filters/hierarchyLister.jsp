<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<bean:define id="entityList" toScope="page" scope="request" name="reqEntityList" />
<bean:define id="selectedEntityIds" toScope="page" scope="request" name="reqSelectedEntityIds" />



<logic:notEmpty name="entityList">
	<ul style="list-style-type: none">
		<logic:iterate id="entity" name="entityList" scope="page">
			<li>
				<html:checkbox onchange="toggleCheckChildren(this)" value="${entity.uniqueId}" property="${selectedEntityIds}">
					<span style="font-family: Arial; font-size: 12px;">${entity.label}</span> 
				</html:checkbox>
				<logic:notEmpty name="entity" property="children">
					<bean:define id="reqEntityList" toScope="request" name="entity" property="children" ></bean:define>
					<jsp:include page="hierarchyLister.jsp" />
				</logic:notEmpty>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>