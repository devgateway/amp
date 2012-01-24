<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<%@page import="org.digijava.module.aim.action.reportwizard.ReportWizardAction"%>
<strong>
	<digi:trn key="rep:pop:SelectedFilters">Selected Filters:</digi:trn>
</strong>
	<logic:present name="<%=ReportWizardAction.SESSION_FILTER%>" scope="session">
			<bean:define id="listable" name="<%=ReportWizardAction.SESSION_FILTER%>" toScope="request"/>
	</logic:present>
	<logic:notPresent name="<%=ReportWizardAction.SESSION_FILTER%>" scope="session">
		<logic:present name="<%=ReportWizardAction.EXISTING_SESSION_FILTER%>" scope="session">
				<bean:define id="listable" name="<%=ReportWizardAction.EXISTING_SESSION_FILTER%>" toScope="request"/>
		</logic:present>
	</logic:notPresent>
	
	<logic:present name="listable" scope="request">
		<bean:define id="listableStyle" value="settingsList" toScope="request"/>
		<bean:define id="persistenceProperties" value="true" toScope="request"/>
	 			<bean:define id="listableTrnPrefix" value="filterProperty" toScope="request"/>
				<jsp:include page="${listable.jspFile}" />
	</logic:present>