<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="docFromTemplateForm"/>

<digi:form action="/docFromTemplate.do?actType=saveDocument" method="post">
<table width="420px" cellPadding="4" cellSpacing="1" valign="top" align="left" bgcolor="#ffffff" border="0">
  <tr>
  	<td width="100%">
  		<html:select property="templateId" styleClass="inp-text" onchange="templateNameSelected()" styleId="selTempName" style="width:90%">
			<html:option value="-1"><digi:trn>Choose Template From The List</digi:trn></html:option>
			<logic:iterate id="template" name="docFromTemplateForm" property="templates">																																															
				<html:option value="${template.id}"><digi:trn>${template.name}</digi:trn></html:option>																		
			</logic:iterate>
		</html:select>
  	</td>
  </tr> 
  <c:if test="${!empty docFromTemplateForm.fields}">
    <c:forEach var="field" items="${docFromTemplateForm.fields}">
        <tr>
          <td align="center">
          	${field.rendered}
          </td>
        </tr>        
    </c:forEach>
    <tr>
	    <td bgColor="#dddddb" height="25" align="center">
	      <c:set var="trn"><digi:trn>Save</digi:trn></c:set>
	      <c:set var="trncancel"><digi:trn>Cancel</digi:trn></c:set>
	      <c:set var="trnclose"><digi:trn>Close</digi:trn></c:set>      
	      
	      <input class="dr-menu" type="submit" value="${trn}" >    
	      <input class="dr-menu" type="reset" value="${trncancel}">
	      <input class="dr-menu" type="button" name="close" value="${trnclose}" onclick="templatesPanel.hide()">
	    </td>
  	</tr>   
  </c:if>  
</table>

</digi:form>