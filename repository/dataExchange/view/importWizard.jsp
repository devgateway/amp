<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="deImportForm" />
<digi:form action="/import.do" method="post" enctype="multipart/form-data">
<table>

	<tr>
		<td>
			Import Wizard
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td><input id="ploadedFile" name="uploadedFile" type="file" class="file"></td>
		<td> 
		  <c:set var="translation">
             <digi:trn key="btn:Import">Import</digi:trn>
          </c:set>
          <html:submit style="dr-menu" value="${translation}" property="import"/></td>
		</td>
	</tr>
</table>
<table>

	<tr>
		<th>
			Log Work
		</th>
	</tr>
	<tr>
		<td>
			<bean:define id="errorLog" value="<%= session.getAttribute("errorLogForDE").toString() %>"/>
			<logic:equal  name="errorLog" value="">
			    <%= session.getAttribute("messageLogForDe") %>
			</logic:equal>
			<logic:notEqual  name="errorLog" value="">
			   <%= session.getAttribute("errorLogForDE") %>
			</logic:notEqual>
		</td>
	</tr>
	
</digi:form>


