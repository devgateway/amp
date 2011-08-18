<%@page import="org.digijava.module.categorymanager.util.CategoryConstants"%>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<digi:instance property="docFromTemplateForm"/>

<digi:form action="/docFromTemplate.do?actType=saveDocument" method="post">

<table width="98%" cellPadding="4" cellSpacing="1" valign="top" align="left" bgcolor="#ffffff" border="0">	
  <tr>
  	<td width="100%" colspan="2">
  		<html:select property="templateId" styleClass="inp-text" onchange="templateNameSelected()" styleId="selTempName" style="width:90%">
			<html:option value="-1"><digi:trn>Choose Template From The List</digi:trn></html:option>
			<logic:iterate id="template" name="docFromTemplateForm" property="templates">																																															
				<html:option value="${template.id}"><digi:trn>${template.name}</digi:trn></html:option>																		
			</logic:iterate>
		</html:select>
  	</td>
  </tr>
  <tr><td style="text-align: center;" colspan="2">
  	<div id="tempLoadingDiv" style="text-align: center;display: none;">
		<digi:trn>Please Wait...</digi:trn> <br>
		<img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' border='0' height='20px' align="middle"/>
	</div>
  </td></tr>
  <c:if test="${!empty docFromTemplateForm.fields}">
  	<tr>
		<td>
			<digi:trn>Document Format:</digi:trn>
		</td>
	  	<td>
	  		<html:select property="docType" styleClass="inp-text">
				<html:option value="pdf"><digi:trn>Pdf</digi:trn></html:option>
				<html:option value="doc"><digi:trn>Word Document</digi:trn></html:option>
			</html:select>
	  	</td>
  	</tr>
  	<tr>
		<td nowrap="nowrap">
			<digi:trn>Name:</digi:trn> <font color="red">*</font>
		</td>
	  	<td>
	  		<html:text property="documentName" size="30" styleId="docName"/>
	  	</td>
  	</tr>
  <tr>
	  	<td  style="white-space: nowrap;">
	  		<digi:trn>Document Type</digi:trn>:
	  	</td>
	  	<td>
	  		<category:showoptions  firstLine="<digi:trn>Please select from below</digi:trn>" name="docFromTemplateForm" property="documentTypeCateg"  keyName="<%= CategoryConstants.DOCUMENT_TYPE_KEY %>" styleClass="inp-text" />
	  	</td>
  </tr>
    <c:forEach var="field" items="${docFromTemplateForm.fields}">
        <tr>
          <td>&nbsp;</td>
          <td align="left" colspan="2">
          	${field.rendered}
          </td>
        </tr>
    </c:forEach>
    <tr>
	    <td bgColor="#dddddb" height="25" align="center" colspan="2">
	      <c:set var="trn"><digi:trn>Save</digi:trn></c:set>
	      <c:set var="trncancel"><digi:trn>Cancel</digi:trn></c:set>
	      <c:set var="trnclose"><digi:trn>Close</digi:trn></c:set>      
	      
	      <input class="dr-menu" type="submit" value="${trn}" onclick="return validateDocFromTemp()">    
	      <input class="dr-menu" type="reset" value="${trncancel}">
	      <input class="dr-menu" type="button" name="close" value="${trnclose}" onclick="templatesPanel.hide()">
	    </td>
  	</tr>
  </c:if>
</table>

</digi:form>