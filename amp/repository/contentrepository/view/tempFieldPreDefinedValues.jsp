<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="manageFieldForm"/>

<digi:form action="/manageField.do?action=saveValues" method="post">
<table width="420px" cellPadding="4" cellSpacing="1" valign="top" align="left" bgcolor="#ffffff" border="0">
  <tr>
    <td bgColor="#c7d4db" class="box-title" height="10" align="center" colspan="7">
    	<digi:trn><b style="font-size:12px;">Add/Edit Values</b></digi:trn>
    </td>
  </tr>  
  <c:if test="${!empty manageFieldForm.possibleValuesForField}">
    <c:forEach var="posVal" varStatus="index" items="${manageFieldForm.possibleValuesForField}">
        <tr>
          <td height="10" align="center" width="10%"> 
          	<c:if test="${posVal.dbId!=null}">
          		<c:set var="textReadonly" value="true" />
          	</c:if>
          	<c:if test="${posVal.dbId==null}">
          		<c:set var="textReadonly" value="false" />
          	</c:if>
          	<c:if test="${manageFieldForm.selectedFieldType=='org.digijava.module.contentrepository.dbentity.template.StaticTextField'}">
          		<c:set var="isTextArea">true</c:set>
          		<html:textarea name="posVal" property="preDefinedValue" styleId="val_${index}" readonly="${textReadonly}" rows="5" cols="45" />
          	</c:if>
            <c:if test="${manageFieldForm.selectedFieldType!='org.digijava.module.contentrepository.dbentity.template.StaticTextField'}">
            	<c:set var="isTextArea">false</c:set>
          		<html:text name="posVal" property="preDefinedValue" styleId="val_${index}" readonly="${textReadonly}" size="45"/>
          	</c:if>            
          </td>
          <td>
            <a href="javascript:deleteData('${posVal.tempId}','${isTextArea}')">
              <img src="../ampTemplate/images/trash_16.gif" border="0" alt="Delete indicator value" />
            </a>
          </td>
        </tr>        
    </c:forEach>   
  </c:if>
  <c:if test="${empty manageFieldForm.possibleValuesForField}">
    <tr align="center" bgcolor="#ffffff"><td><b>
      <digi:trn>No data present</digi:trn></b></td>
    </tr>
  </c:if>
  <tr>
    <td height="25" align="center" colspan="6">
      <c:set var="trnadd"><digi:trn>Add data</digi:trn></c:set>
      <c:if test="${manageFieldForm.hasAddModeValuesRight}">
      		<c:set var="disabledbutton"></c:set>
      		<c:set var="buttonStyle">font-family:verdana;font-size:11px;</c:set>
      </c:if>
     <c:if test="${!manageFieldForm.hasAddModeValuesRight}">
      		<c:set var="disabledbutton">disabled="disabled"</c:set>
      		<c:set var="buttonStyle">font-family:verdana;font-size:11px;color: gray</c:set>
      </c:if>
      
      <input style="${buttonStyle}" type="button" class="buttonx" name="addValBtn" value="${trnadd}" onclick="addNewPreDefinedValue()" ${disabledbutton}>&nbsp;&nbsp;
    </td>
  </tr>  
  <tr>
    <td height="25" align="center" colspan="6">
      <c:set var="trn"><digi:trn>Save</digi:trn></c:set>
      <c:set var="trncancel"><digi:trn>Cancel</digi:trn></c:set>
      <c:set var="trnclose"><digi:trn>Close</digi:trn></c:set>      
      
      <input type="reset" class="buttonx" value="${trn}" onclick="submitPreDefinedValues('${isTextArea}');">    
      <input type="reset" class="buttonx" value="${trncancel}">
      <input type="button" class="buttonx" name="close" value="${trnclose}" onclick="myPanel.hide()">
    </td>
  </tr>  
</table>

</digi:form>