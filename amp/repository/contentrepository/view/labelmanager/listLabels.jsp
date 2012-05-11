<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<logic:notEmpty scope="request" name="labelCollection">
	<ul>
		<logic:iterate scope="request" name="labelCollection" id="label" type="org.digijava.module.contentrepository.jcrentity.Label">
			
			<bean:define id="liClassString" toScope="page" value=""/>
			<logic:empty name="label" property="children">
				<bean:define id="liClassString" toScope="page" value="class='dhtmlgoodies_sheet.gif'"/>
			</logic:empty>
			<li id="lid-${label.uuid}" ${liClassString} >
				<a  class="atree" id="aid-${label.uuid}" style="-moz-border-radius:3px;border-radius: 3px;padding:3px;color: ${label.color};background-color: ${label.backgroundColor}"><c:out value="${label.name}"></c:out></a>
				 <c:if test="${label.type=='FOLDER_LABEL' }">
					<img src="/TEMPLATE/ampTemplate/images/green_plus.png" style="height: 13px; cursor: pointer;" 
						title="Add Label" 
						onclick="addLabel('${label.uuid}')" />
				</c:if>
				<img src="/TEMPLATE/ampTemplate/images/application_edit.png" style="height: 13px; cursor: pointer;" 
						onclick="editLabel('${label.uuid}')" title="Edit" />
				<img src="/TEMPLATE/ampTemplate/images/deleteIcon.gif" style="height: 10px; cursor: pointer;" 
						onclick="deleteLabel('${label.uuid}')" title="Delete"/>
				<span class="spantree" style="display: none; color: red;"><digi:trn>ERROR</digi:trn></span>
				<logic:notEmpty name="label" property="children">
					<bean:define id="templabelCollection" toScope="request" name="labelCollection" scope="request"/>
					<bean:define id="labelCollection" toScope="request" name="label" property="children" />
					<jsp:include page="listLabels.jsp" />
					<bean:define id="labelCollection" toScope="request" name="templabelCollection" scope="request"/>
				</logic:notEmpty>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>
	