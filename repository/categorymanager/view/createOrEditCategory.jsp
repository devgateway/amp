<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.List"%>

<%@page import="org.digijava.module.categorymanager.util.CategoryManagerUtil"%>

<digi:instance property="cmCategoryManagerForm" />
<bean:define id="myForm" name="cmCategoryManagerForm" toScope="session" type="org.digijava.module.categorymanager.form.CategoryManagerForm" />

<!--  AMP Admin Logo -->
<%-- <jsp:include page="teamPagesHeader.jsp" flush="true" /> --%> 

<c:set var="translation1">
		<digi:trn key="aim:categoryManagerPlsEnterName">You need to enter a name for the category</digi:trn>
</c:set>

<c:set var="translation2">
	<digi:trn key="aim:categoryManagerPlsEnterKey">You need to enter a key for the category</digi:trn>
</c:set>

<c:set var="translation3">
	<digi:trn key="aim:categoryManagerMoreThan2Values">Category must have at least two possible values</digi:trn>
</c:set>

<c:set var="translation4">
	<digi:trn key="aim:CategoryOnlyOneCountryValue">Category must have one country value</digi:trn>
</c:set>

<c:set var="translation5">
	<digi:trn key="aim:CategoryOnlyOneRegionValue">Category must have one region value</digi:trn>
</c:set>
<c:set var="translation6">
	<digi:trn key="cm:correctNumberOfFields">Please specify a number of fields to add that is above zero and below 30</digi:trn>
</c:set>
<c:set var="translation7">
	<digi:trn key="cm:warningDeleteCategoryValue">Please proceed only if you are sure that the value you are trying to delete is not used by the system anymore.  Are you sure you want to delete it ?</digi:trn>
</c:set>

<script type="text/javascript">
	
      
    function addNewValue(position) {
    	var subForm				= document.forms["cmCategoryManagerForm"];
    	var fieldNumStr			= subForm.numOfAdditionalFields.value;
    	var fieldNum			= parseInt(fieldNumStr);
    	if ( isNaN(fieldNumStr) || fieldNum < 1 || fieldNum > 30 ) {
    		alert("${translation6}");
    		return false;
    	}
 		subForm.action			= "/categorymanager/categoryManager.do?addValue=true";
 		if ( position > 0 )
 			subForm.action		= "/categorymanager/categoryManager.do?addValue=true&position="+position;
 		subForm.submit();
	}
	
    function doSubmit() {
        if (document.cmCategoryManagerForm.categoryName.value.length == 0) {
            alert ("${translation1}");
            return false;
        }
        if (document.cmCategoryManagerForm.keyName.value.length == 0) {
            alert ("${translation2}");
            return false;
        } 
        document.forms["cmCategoryManagerForm"].submitPressed.value	= "true";
        document.cmCategoryManagerForm.submit();
    }
	function deleteField(id, deleteId, undoId,disabeledId) {
		if ( confirm("${translation7}") ) {
			field						= document.getElementById(id) ;
			disabled					= document.getElementById(disabeledId) ;
			disabled.value				= true;
			field.style.textDecoration	= "line-through";
			field.style.color			= "darkgray";
			field.disabled				= true;
			
			del							= document.getElementById(deleteId) ;
			del.style.display			= "none";
			
			undo						= document.getElementById(undoId) ;
			undo.style.display			= "inline";
		}
	}
	function undeleteField(id, deleteId, undoId,disabeledId) {
		field						= document.getElementById(id) ;
		field.style.textDecoration	= "none";
		field.style.color			= "black";
		field.disabled				= false;
		disabled					= document.getElementById(disabeledId) ;
		disabled.value				= false;
		
		del							= document.getElementById(deleteId) ;
		del.style.display			= "inline";
		
		undo						= document.getElementById(undoId) ;
		undo.style.display			= "none";
	} 
	
</script>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						
						<c:set var="translation">
							<digi:trn key="aim:clickToViewCategoryManager">Click here to goto Category Manager</digi:trn>
						</c:set>
						<digi:link href="/categoryManager.do" styleClass="comment" title="${translation}" >
							<digi:trn key="aim:categoryManager">
								Category Manager
							</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:categoryManagerCreator">
							Category Manager Creator
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
							<digi:trn key="aim:categoryManagerCreator">
								Category Manager Creator
							</digi:trn>					
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>
				<tr>
				<td>
	<digi:form action="/categoryManager.do" method="post">
		<html:hidden property="submitPressed" value="false" /> 
		<table cellpadding="5px" cellspacing="5px" valign="top" width="80%">
			<tr>
				<td colspan="2">
					<digi:trn key="aim:categoryManagerAddNameText">
						Please enter a <strong>name</strong> for this category:
					</digi:trn>
					<font color="red">*</font>
					&nbsp;&nbsp;
					<html:text property="categoryName" />
				</td>
			</tr>
			<tr valign="top">
				<td>
					<digi:trn key="aim:categoryManagerAddDescriptionText">
						You can enter a <strong>description</strong> for this category: 
					</digi:trn>
					<br />
					<html:textarea property="description" cols="20" rows="4" />
				</td>
				<td valign="middle">
					<%-- <html:checkbox property="isMultiselect" />
					<digi:trn key="aim:categoryManagerAllowMultiselect">
						Should <strong>multiselect</strong> be allowed for this category
					</digi:trn> 
					<br /> --%>
					<html:checkbox property="isOrdered" />
					<digi:trn key="aim:categoryManagerIsOrdered">
						Should the values be presented in <strong>alphabetical order</strong> 
					</digi:trn>
				</td>
				</tr>
				<tr>
					<td>
						<digi:trn key="aim:categoryManagerEnterKeyText"> Please Enter the Key</digi:trn>
						<font color="#FF0000">*</font>
					</td>
					<td>
						<c:set var="keyTextReadonly" value="false" />
						<c:set var="keyTextColorStyle" value="color: black; background-color:white; border-style: none; text-decoration: none;" />
						
						<c:if test="${myForm.advancedMode}">
							<c:set var="keyTextReadonly" value="false" />
							<c:set var="keyTextColorStyle" value="color: black; background-color:white; " />
						</c:if>
						<html:text property="keyName" readonly="${keyTextReadonly}" style="${keyTextColorStyle}" />
					</td>
				</tr>
				<tr>
					<td colspan="2"><strong><digi:trn key="cm:categoryManagerImportantNote">IMPORTANT NOTE</digi:trn></strong>: 
						<digi:trn key="cm:categoryManagerPlsUseTranslations">If you need to change the translation for a category value please use Translator View</digi:trn> !
					</td>
				</tr>
				<tr>
				<td id="possibleValuesTd" colspan="2">
					<div id="possibleValuesDiv">
					<%--<digi:trn key="aim:categoryManagerAddPossibleValueKeysText">
							Please enter possible <strong>value keys</strong> :
					</digi:trn>
					<br /> <br /> --%>
			<digi:trn key="aim:categoryManagerAddMoreFields">
				Add More Fields
			</digi:trn>:
		
			<html:text property="numOfAdditionalFields" size="4" value="1"/>
			<table cellpadding="5px" cellspacing="5px">
				<tr>
					<td width="35%"><strong>Category Value Key</strong></td>
					<td width="35%"><strong>Translation</strong></td>
					<td width="30%"><strong>Actions</strong></td>
				</tr>
			<c:forEach var="possibleVals" items="${myForm.possibleVals}" varStatus="index">
				<bean:define id="pVal" toScope="page" name="possibleVals" type="org.digijava.module.categorymanager.util.PossibleValue" />
               <%-- <c:choose>
               	<c:when test="${possibleVals.disable}">
               		<c:set var="textDecorationStyle" scope="page">text-decoration:line-through;</c:set>
               		<c:set var="textDisabled" scope="page" value="true" />
               		<c:set var="deleteField" scope="page">display: none</c:set>
               		<c:set var="undeleteField" scope="page">display: inline</c:set>
               	</c:when>
               	<c:otherwise>
               		<c:set var="textDecorationStyle" scope="page"> </c:set>
               		<c:set var="textDisabled" scope="page" value="false" />
               		<c:set var="deleteField" scope="page">display: inline</c:set>
               		<c:set var="undeleteField" scope="page">display: none</c:set>
               	</c:otherwise>
               	
               </c:choose> --%>
               
				<c:if test="${pVal.id != null && pVal.id != 0 && !pVal.disable}">
					<c:set var="textReadonly" value="true" />
					<c:set var="borderStyle" value="border-style: none;" />
					<c:set var="textColorStyle" value="color: black; background-color:white; text-decoration: none;" />
					<c:set var="deleteField" scope="page">display: inline</c:set>
               		<c:set var="undeleteField" scope="page">display: none</c:set>
				</c:if>
				<c:if test="${pVal.id == null || pVal.id == 0}">
					<c:set var="textReadonly" value="false" />
					<c:set var="borderStyle" value=" " />
					<c:set var="textColorStyle" value="color: black; background-color:white; text-decoration: none;" />
				</c:if>
				<c:if test="${pVal.disable}">
					<c:set var="textReadonly" value="true" />
					<c:set var="borderStyle" value="border-style: none;" />
					<c:set var="textColorStyle" value="color:darkgray; background-color:white; text-decoration: line-through;" />
					<c:set var="deleteField" scope="page">display: none</c:set>
               		<c:set var="undeleteField" scope="page">display: inline</c:set>
				</c:if>
				
				<c:if test="${myForm.advancedMode}">
					<c:set var="textReadonly" value="false" />
					<c:set var="borderStyle" value=" " />
				</c:if>
               
				<tr>
				<td>
                  <html:text name="possibleVals" property="value" readonly="${textReadonly}" style="${textColorStyle} ${borderStyle}" indexed="true" styleId="field${index.count}"/>
                  <html:hidden name="possibleVals" property="disable"  indexed="true" styleId="disabled${index.count}"/>
                  <html:hidden name="possibleVals" property="id" indexed="true" />
				</td>
				<td>
					<c:choose>
						<c:when test="${pVal.id!=null && pVal.id!=0}">
							<digi:trn key="<%=CategoryManagerUtil.getTranslationKeyForCategoryValue(pVal.getValue(), myForm.getKeyName() ) %>">
								${pVal.value}
							</digi:trn>
						</c:when>
						<c:otherwise>&nbsp;</c:otherwise>
					</c:choose>
				</td>
				<td>  
					<c:if test="${pVal.id!=null && pVal.id!=0}">                        
						<span id="delete${index.count}" style="${deleteField}">
							[<a style="cursor:pointer; text-decoration:underline; color: blue"  onclick="return deleteField('field${index.count}', 'delete${index.count}','undo${index.count}','disabled${index.count}')">
								<digi:trn key="aim:categoryManagerValueDelete">
									Delete
								</digi:trn>
							</a>]
						</span>
						<span id="undo${index.count}" style="${undeleteField}">
							[<a style="cursor:pointer; text-decoration:underline; color:blue;"  onclick="return undeleteField('field${index.count}', 'delete${index.count}','undo${index.count}','disabled${index.count}')">
								<digi:trn key="aim:categoryManagerValueUndelete">
									Undelete
								</digi:trn>
							</a>]
						</span>
						<span>
							[<a style="cursor:pointer; text-decoration:underline; color: blue" onclick="addNewValue(${index.count})">
				  				Add value(s) above
				  			</a>]
						</span>
					</c:if>
				</td>
				</tr>				
				</c:forEach>
			</table>
                                                
 
					</div>
					
					[ <a style="cursor:pointer; text-decoration:underline; color: blue" onclick="addNewValue(-1)">Add value(s)</a> ]
				</td>
			</tr>
		</table>
		<br />
		<html:button property="xx" onclick="return doSubmit()">
			<digi:trn key="aim:categoryManagerSubmit">
					Submit
			</digi:trn>
		</html:button>
	</digi:form>
				</td>
				</tr>
</table>
