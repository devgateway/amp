<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.List"%>

<digi:instance property="aimCategoryManagerForm" />
<bean:define id="myForm" name="aimCategoryManagerForm" toScope="page" type="org.digijava.module.aim.form.CategoryManagerForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" /> 

<script type="text/javascript">
	var numOfAdditionalFields 	= 0;
	var numOfPossibleValues		= <bean:write name="myForm" property="numOfPossibleValues"/>;
	if (numOfPossibleValues == 0)
		numOfPossibleValues = 3;
	
	function addNewValueField() {
		var divValues		= document.getElementById('possibleValuesDiv');
		var newInput		= document.createElement('input');
		newInput.setAttribute('type','text');
		newInput.setAttribute('value','');
		newInput.setAttribute('name','possibleValues');
		newInput.setAttribute('id','additionalField' + numOfAdditionalFields);
		divValues.appendChild(newInput);
		divValues.appendChild(document.createElement('br'));
		divValues.appendChild(document.createElement('br'));
		
		numOfAdditionalFields ++;
	}
	function doSubmit() {
		if (document.forms[0].categoryName.value.length == 0) {
			alert ("You have to enter a name for the category");
			return;
		} 
		document.forms[1].categoryName.value	= document.forms[0].categoryName.value;
		document.forms[1].keyName.value			= document.forms[0].keyName.value;
		document.forms[1].description.value		= document.forms[0].description.value;
		if (document.forms[0].isMultiselect.checked)
			document.forms[1].isMultiselect.value	= "on";
		else
			document.forms[1].isMultiselect.value	= "off";
		if (document.forms[0].isOrdered.checked)
			document.forms[1].isOrdered.value	= "on";
		else
			document.forms[1].isOrdered.value	= "off";
		numOfItemsCopied	= 0;
		var numOfNotEmptyFields	= 0;
		for (i=0; i<numOfPossibleValues; i++) {
			var value	= "";
			var field	= document.forms[0].possibleValues[i];
			if (!field.disabled) {
				value	= field.value;
			}
			document.forms[1].possibleValues[i].value	= value;
			if (document.forms[1].possibleValues[i].value.length > 0)
				 numOfNotEmptyFields++;
			numOfItemsCopied ++;
		}
		for (i=0; i<numOfAdditionalFields; i++){
			document.forms[1].possibleValues[i+numOfItemsCopied].value	= document.getElementById('additionalField'+i).value;
			if (document.forms[1].possibleValues[i+numOfItemsCopied].value.length > 0)
				 numOfNotEmptyFields++;
		}
		if (numOfNotEmptyFields < 2) {
			alert("Category must have at least two possible values");
			return;
		}
		document.forms[1].submit();
	}
	function deleteField(id, deleteId, undoId) {
		field						= document.getElementById(id) ;
		field.style.textDecoration	= "line-through";
		field.disabled				= true;
		
		del							= document.getElementById(deleteId) ;
		del.style.display			= "none";
		
		undo						= document.getElementById(undoId) ;
		undo.style.display			= "inline";
	}
	function undeleteField(id, deleteId, undoId) {
		field						= document.getElementById(id) ;
		field.style.textDecoration	= "none";
		field.disabled				= false;
		
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
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
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
						<span class=subtitle-blue>Category Manager Creator</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<html:errors />
					</td>
				</tr>
				<tr>
				<td>
	<digi:form action="/categoryManager.do" method="post">
		<html:hidden property="addNewCategory" value="yes" /> 
		<table cellpadding="5px" cellspacing="5px" valign="top">
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
					<html:checkbox property="isMultiselect" />
					<digi:trn key="aim:categoryManagerAllowMultiselect">
						Should <strong>multiselect</strong> be allowed for this category
					</digi:trn> 
					<br />
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
						<html:text property="keyName"/>
					</td>
				</tr>
				<tr>
				<td id="possibleValuesTd" colspan="2">
					<div id="possibleValuesDiv">
					<digi:trn key="aim:categoryManagerAddPossibleValuesText">
							Please enter possible <strong>values</strong>:
					</digi:trn>
					<br />
					<% 
						int max	= myForm.getNumOfPossibleValues().intValue();
						if (max == 0)
							max	= 3;
						for (int i=0; i<max; i++) { 
							String value	= "";
							String deleteId	= "delete" + i;
							String undoId	= "undo" + i;
							String fieldId	= "field" + i;
							if (myForm.getNumOfPossibleValues().intValue() > 0) 
								value	= myForm.getPossibleValues()[i];
					%>
						<input type="text" name="possibleValues" value="<%=value %>" style="text-decoration:none" id="<%= fieldId %>" />
						&nbsp; 
						<span id="<%= deleteId %>" style="display:inline">
						[<a style="cursor:pointer; text-decoration:underline; color: blue"  onclick="return deleteField('<%= fieldId %>', '<%= deleteId %>','<%= undoId %>')">
							<digi:trn key="aim:categoryManagerValueDelete">
								Delete
							</digi:trn>
						</a>]
						</span>
						<span id="<%= undoId %>" style="display:none">
						[<a style="cursor:pointer; text-decoration:underline; color:blue"  onclick="return undeleteField('<%= fieldId %>', '<%= deleteId %>', '<%= undoId %>')">
							<digi:trn key="aim:categoryManagerValueUndelete">
								Undelete
							</digi:trn>
						</a>]
						</span>
						 <br /><br />
					<%	} %>
 
					</div>
					<a style="cursor:pointer; text-decoration:underline; color: blue"  onclick="return addNewValueField()">
						<digi:trn key="aim:categoryManagerAddAnotherValueField">
							Add Another Field
						</digi:trn>
					</a>
				</td>
			</tr>
		</table>
		<html:button property="xx" onclick="return doSubmit()">
			<digi:trn key="aim:categoryManagerSubmit">
					Submit
			</digi:trn>
		</html:button>
	</digi:form>
				</td>
				</tr>
</table>

<div id="hiddenDiv" style="visibility:hidden"> 
	<form action="/aim/categoryManager.do" method="post" id="hiddenForm">
		<logic:notEmpty name="myForm" property="editedCategoryId">
			<html:hidden property="editedCategoryId" />
		</logic:notEmpty>
		<input type="hidden" name="addNewCategory" value="yes" /> 
		<input type="hidden" name="categoryName" />
		<input type="hidden" name="keyName" />
		<input type="hidden" name="description" />
		<input type="hidden" name="isMultiselect" />
		<input type="hidden" name="isOrdered" />
		<% 
		for (int j=0; j<100; j++) {
				String hiddenInput	= "hiddenInput"+j;
		%>
		<input type="hidden" name="possibleValues" id="<%= hiddenInput %>">
		<%
		}
		%>
	</form>
</div>



