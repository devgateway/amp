<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ page import="java.util.List"%>

<%@page import="org.digijava.module.categorymanager.util.CategoryManagerUtil"%>
<%@ page import="org.digijava.module.aim.helper.EasternArabicService" %>
<%@ page import="org.digijava.kernel.request.TLSUtils" %>

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

<c:set var="translation8">
	<digi:trn key="cm:warningDeleteLabelCategory">Are you sure you want to remove this label category ?</digi:trn>
</c:set>
<c:set var="translation9">
	<digi:trn>Title cannot be empty and can only contain letters, digits, _, /, (), #, -, : and space. </digi:trn>
</c:set>

<c:set var="translation10">
	<digi:trn>Please enter key for category value</digi:trn>
</c:set>

<script type="text/javascript">
	labelPanels					= new Array();
    var isRtl = <%=TLSUtils.getCurrentLocale().getLeftToRight() == false%>;
    var language = '<%=TLSUtils.getCurrentLocale().getCode()%>';
    var region = '<%=TLSUtils.getCurrentLocale().getRegion()%>';
	
	function addLabelCategory() {
		var subForm				= document.forms["cmCategoryManagerForm"];
		subForm.action			= "/categorymanager/categoryLabelsAction.do";
		subForm.useAction.value	= "addCategory";
		subForm.submit();
	}
	function addLabelValues(possibleValueIndex, labelCategoryIndex) {
		var subForm				= document.forms["cmCategoryManagerForm"];
		subForm.action			= "/categorymanager/categoryLabelsAction.do?possibleValueIndex=" + possibleValueIndex + 
										"&labelCategoryIndex=" + labelCategoryIndex;
		subForm.useAction.value	= "addLabelValue";
		subForm.submit();
	}
	function delLabelCategory(categoryId) {
		if ( confirm('${translation8}') ) {
			var subForm				= document.forms["cmCategoryManagerForm"];
			subForm.action			= "/categorymanager/categoryLabelsAction.do";
			subForm.useAction.value	= "delCategory";
			subForm.delUsedCategoryId.value	= categoryId;
			//alert(subForm.delUsedCategoryId.value);
			subForm.submit();
		}
		return false;
	}
      
    function addNewValue(position) {
    	var subForm				= document.forms["cmCategoryManagerForm"];
    	var fieldNumStr			= subForm.numOfAdditionalFields.value;
    	var fieldNum			= parseInt(TranslationManager.convertNumbersToEasternArabicIfNeeded(isRtl, language, region, "" + fieldNumStr));
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
        if(validateText()==true){
        	document.forms["cmCategoryManagerForm"].submitPressed.value	= "true";
            document.cmCategoryManagerForm.submit();
        }else{
        	return false;
        }
        
    }

    function cancel()
	{
    	var subForm				= document.forms["cmCategoryManagerForm"];
		subForm.action			= "/categorymanager/categoryManager.do";
		subForm.submit();
		return false;
	}
    
    function validateText(){
    	var catValueKeys = $('input[id^="field"]');
    	var regexp = new RegExp("[a-zA-Z0-9_ÀÁÃÄÇÈÉËÌÍÏÑÒÓÕÖÙÚÜàáãäçèéêëìíïñòóõöùúü%&'/\ ()-:#]+");
    	for (var i=0;i<catValueKeys.length;i++){    		
    		if(!catValueKeys[i].disabled && catValueKeys[i].value.trim().length === 0  &&
				regexp.exec(catValueKeys[i].value) !== catValueKeys[i].value){
    			alert("${translation9}");
    			return false;
    		}
    	}
    	return true;
    }
    
    
    
	function deleteField(id, deleteId, undoId,disabeledId) {
		field							= document.getElementById(id) ;
		if ( !field.readOnly || confirm("${translation7}") ) {
			disabled					= document.getElementById(disabeledId) ;
			disabled.value				= true;
			field.style.textDecoration	= "line-through";
			field.style.color			= "darkgray";
			if ( !field.readOnly )
				field.style.backgroundColor	= "lightgray";
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
		field.style.backgroundColor	= "white";
		field.disabled				= false;
		disabled					= document.getElementById(disabeledId) ;
		disabled.value				= false;
		
		del							= document.getElementById(deleteId) ;
		del.style.display			= "inline";
		
		undo						= document.getElementById(undoId) ;
		undo.style.display			= "none";
	} 
	function showLabelOptions(panelId) {
		
		if ( labelPanels[panelId] == null ) {
			labelPanels[panelId]	= new YAHOOAmp.widget.Panel(panelId, 
				{ width : "300px",
				  fixedcenter : true,
				  visible : true, 
				  constraintoviewport : true
				 } );
			labelPanels[panelId].render(document.forms["cmCategoryManagerForm"]);
		}
		labelPanels[panelId].show();
	}
	
</script>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="1" width="100%" border="0">
				<!-- <tr> -->
					<!-- Start Navigation -->
					<!--<td height=33><span class=crumb>
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
					</td> -->
					<!-- End navigation -->
				<!-- </tr> -->
				<tr>
					<td height=16 valign="center" bgcolor=#c7d4db align="center">
						<span class=subtitle-blue style="font-size:12px; font-weight:bold;">
							<digi:trn key="aim:categoryManagerCreator">
								Category Manager Creator
							</digi:trn>					
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571>
						<digi:errors />
					</td>
				</tr>
				<tr>
				<td>
	<digi:form action="/categoryManager.do" method="post">
		<html:hidden property="submitPressed" value="false" /> 
		<table cellpadding="5px" cellspacing="5px" valign="top" width="100%" border=1 class="inside">
			<tr>
				<td colspan="2" class="inside">
					<digi:trn>Please enter a</digi:trn> <strong><digi:trn>name</digi:trn></strong> <digi:trn>for this category:</digi:trn>
					<font color="red">*</font>
					&nbsp;&nbsp;
					<html:text property="categoryName" />
				</td>
			</tr>
			<tr valign="top">
				<td class="inside" width=50%>
				   <digi:trn>You can enter a </digi:trn> <strong><digi:trn>description</digi:trn></strong> <digi:trn>for this category:</digi:trn>
			    	<br />
					<html:textarea property="description" styleClass="adm_category_desc" />
				</td>
				<td valign="middle" class="inside" width=50%>
					<html:checkbox property="isMultiselect" />
					<digi:trn>Should</digi:trn> <strong><digi:trn>multiselect</digi:trn></strong> <digi:trn>be allowed for this category</digi:trn>
					<br />
					<html:checkbox property="isOrdered" />
					<digi:trn>Should the values be presented in</digi:trn> <strong><digi:trn>alphabetical order</digi:trn></strong> 
				</td>
				</tr>
				<tr>
					<td class="inside">
						<digi:trn key="aim:categoryManagerEnterKeyText">Please Enter the Key</digi:trn>
						<font color="#FF0000">*</font>
					</td>
					<td class="inside">
						<c:set var="keyTextReadonly" value="false" />
						<c:set var="keyTextColorStyle" value="color: black; background-color:white; border-style: none; text-decoration: none;" />
						
						<c:if test="${myForm.advancedMode}">
							<c:set var="keyTextReadonly" value="false" />
							<c:set var="keyTextColorStyle" value="color: black; background-color:white; " />
						</c:if>
						<html:text property="keyName" readonly="${keyTextReadonly}" style="${keyTextColorStyle}" />
					</td>
				</tr>
				<logic:notEmpty name="cmCategoryManagerForm" property="editedCategoryId">
				<tr>
					<td colspan="2" class="inside">
						<html:hidden property="useAction" value="none"/>
						<html:hidden property="delUsedCategoryId" value="none"/>
						<html:select name="cmCategoryManagerForm" property="usedCategoryId" style="font-size: 11px;">
							<html:optionsCollection  name="cmCategoryManagerForm" property="availableCategories" value="key" label="value" />
						</html:select>
						
						<html:checkbox name="cmCategoryManagerForm" property="usedCatIsSingleSelect" style="font-size: 11px;">Single Select</html:checkbox>
						
						<button type="button" onclick="return addLabelCategory()" class="buttonx" 
							style="vertical-align:bottom; padding: 1px;">
							<img src="/TEMPLATE/ampTemplate/images/green_plus.png" style="height: 16px; vertical-align: text-bottom;"  />
							<digi:trn key="aim:categoryManagerAddLabel">
									Add Label Category
							</digi:trn>
						</button>
						<br />
						<div style="margin-top:5px;">
						<strong><digi:trn key="cm:categoryManagerImportantNote">IMPORTANT NOTE</digi:trn></strong>: 
						<digi:trn key="cm:categoryManagerPlsUseTranslations">If you need to change the translation for a category value please use Translator View</digi:trn> !</div>
					</td>
				</tr>
				</logic:notEmpty>
				<tr>
				<td id="possibleValuesTd" colspan="2" class="inside" align=center style="padding-top:15px; padding-bottom:15px;">
					<div id="possibleValuesDiv">
					<%--<digi:trn key="aim:categoryManagerAddPossibleValueKeysText">
							Please enter possible <strong>value keys</strong> :
					</digi:trn>
					<br /> <br /> --%>
			<digi:trn key="aim:categoryManagerAddMoreFields">
				Add More Fields
			</digi:trn>:
		
			<html:text property="numOfAdditionalFields" size="4" value='<%=EasternArabicService.getInstance().convertToEasternArabicBasedOnCurrentLocale("1")%>'/>
			<table cellpadding="5px" cellspacing="5px" border="0px" style="margin-top:7px; margin-bottom:7px; font-size:12px;" class="inside">
				<tr>
					<td rowspan="2" style="font-size: small; font-weight: bold; text-align: center;" bgcolor=#F2F2F2 class="inside"><digi:trn key="aim:categoryValueKey">Category Value Key</digi:trn></td>
					<td rowspan="2" style="font-size: small; font-weight: bold; text-align: center;" bgcolor=#F2F2F2 class="inside"><digi:trn key="aim:translation">Translation</digi:trn></td>
					<logic:notEmpty name="myForm" property="usedCategories">-
						<td rowspan="1" colspan="<%=myForm.getUsedCategories().size() %>" style="font-size: small; font-weight: bold; text-align: center;" bgcolor=#F2F2F2 class="inside">
							Labels
						</td>
					</logic:notEmpty>
					<td rowspan="2" bgcolor=#F2F2F2 style="font-size: small; font-weight: bold; text-align: center;" class="inside"><digi:trn key="aim:actions">Actions</digi:trn></td>
				</tr>
					<logic:notEmpty name="myForm" property="usedCategories">
					<logic:iterate name="myForm" property="usedCategories" type="org.digijava.module.categorymanager.dbentity.AmpCategoryClass" id="usedCateg">
						<td style="font-size: x-small; font-weight: bold; text-align: center;" class="inside">
							<digi:trn><c:out value="${usedCateg.name}"></c:out></digi:trn>
							<a style="cursor:pointer; text-decoration:underline; color: blue"  onclick="return delLabelCategory(${usedCateg.id})" 
								title="<digi:trn key='cm:categoryManagerDeleteLabelCategory'>Delete Label Category</digi:trn>">
								<img src="/TEMPLATE/ampTemplate/images/deleteIcon.gif" class="toolbar" style="height: 10px;" />
							</a>
						</td>
					</logic:iterate>
					</logic:notEmpty>
				<tr>
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
				<td style="text-align: center;" class="inside"> 
                  <html:text name="possibleVals" property="value" readonly="${textReadonly}" style="${textColorStyle} ${borderStyle}" indexed="true" styleId="field${index.count}"/>
                  <html:hidden name="possibleVals" property="disable"  indexed="true" styleId="disabled${index.count}"/>
                  <html:hidden name="possibleVals" property="id" indexed="true" />
				</td>
				<td style="text-align: center;" class="inside">
					<c:choose>
						<c:when test="${pVal.id!=null && pVal.id!=0}">
							<digi:trn>
								<c:out value="${pVal.value}"></c:out> 
							</digi:trn>
						</c:when>
						<c:otherwise>&nbsp;</c:otherwise>
					</c:choose>
				</td>
				<logic:notEmpty name="myForm" property="usedCategories">
				<logic:iterate name="myForm" property="usedCategories" type="org.digijava.module.categorymanager.dbentity.AmpCategoryClass" id="usedCateg" indexId="countCateg">
					<td align="center" class="inside">
						<c:forEach var="valId" items="${possibleVals.labelCategories[countCateg].labelsId}">
							<category:getoptionvalue categoryValueId="${valId}"/> <br />
						</c:forEach>
						<div  class="invisible-item">
							<div id="labelPanel${usedCateg.keyName}${index.count-1}">
								<div class="hd">Please select labels:</div>
								<div class="bd" align="center">
									<logic:notEmpty name="usedCateg" property="possibleValues">
									<table align="center">
									<logic:iterate name="usedCateg" property="possibleValues" id="usedVal" indexId="countVal">
										<tr> 
											<td>
												<c:choose>
													<c:when test="${not empty usedCateg.usedByCategorySingleSelect && usedCateg.usedByCategorySingleSelect == true}">
														<html:radio property="possibleVals[${index.count-1}].labelCategories[${countCateg}].labelsId" value="${usedVal.id}" />
													</c:when>
													<c:otherwise>
														<html:multibox property="possibleVals[${index.count-1}].labelCategories[${countCateg}].labelsId" value="${usedVal.id}" />
													</c:otherwise>
												</c:choose>
											</td>
											<td><category:getoptionvalue categoryValueId="${usedVal.id}"/></td>
										</tr>
									</logic:iterate>
									<tr>
									<td colspan="2" align="right">
									<button type="button" onclick="return addLabelValues(${index.count-1}, ${countCateg})" class="buton">
										<digi:trn key="aim:categoryManagerSubmit">
												Submit
										</digi:trn>
									</button>
									</tr>
									</table>
									</logic:notEmpty>
								</div>
							</div>
						</div>
						<div style="width: 100%; text-align: center">
						<a style="cursor:pointer; text-decoration:underline; color: blue"  onclick="showLabelOptions('labelPanel${usedCateg.keyName}${index.count-1}')"
							title="<digi:trn key='cm:categorymanagerModifyLabels'>Modify Labels</digi:trn>">
								<img src="/TEMPLATE/ampTemplate/images/application_edit.png" style="height: 12px;" />
						</a>
						</div>
					</td>
				</logic:iterate>
				</logic:notEmpty>
				<td class="inside">  
					<span id="delete${index.count}" style="${deleteField}">
						&nbsp;
						<a style="cursor:pointer;"  onclick="return deleteField('field${index.count}', 'delete${index.count}','undo${index.count}','disabled${index.count}')"
								title="<digi:trn key='aim:categoryManagerValueDelete'>Delete</digi:trn>">
							<img src="/TEMPLATE/ampTemplate/images/deleteIcon.gif" style="height: 14px;" />
						</a>
					</span>
					<span id="undo${index.count}" style="${undeleteField}">
						&nbsp;
						<a style="cursor:pointer;"  onclick="return undeleteField('field${index.count}', 'delete${index.count}','undo${index.count}','disabled${index.count}')"
							title="<digi:trn key='aim:categoryManagerValueUndelete'>Undelete</digi:trn>">
							<img src="/TEMPLATE/ampTemplate/images/undel.png" style="height: 18px;" />
						</a>
					</span>
					<c:if test="${pVal.id!=null && pVal.id!=0}">                        
						<span>
							&nbsp;
							<a style="cursor:pointer;" onclick="addNewValue(${index.count})"
								title="<digi:trn key='cm:categoryManagerAddValuesAbove'>Add value(s) above</digi:trn>">
				  				<img src="/TEMPLATE/ampTemplate/images/green_plus.png" style="height: 16px;" />
				  			</a>
						</span>
					</c:if>
				</td>
				</tr>				
				</c:forEach>
			</table>
                                                
 
					</div>
					
					<button type="button" title="<digi:trn key='cm:categoryManagerAddValues'>Add value(s)</digi:trn>" onclick="addNewValue(-1)" class="buttonx" 
						style="vertical-align:bottom; padding: 1px;">
						<img src="/TEMPLATE/ampTemplate/images/green_plus.png" style="height: 16px; vertical-align: text-bottom;"  />
						<digi:trn key='cm:categoryManagerAddValues'>Add value(s)</digi:trn>
					</button>
				</td>
			</tr>
		</table>
		<br />
		<center>
		<table>
			<tr>
				<td>
					<button type="button" onclick="return doSubmit();" style="vertical-align:bottom; padding: 1px;" class="buttonx">
			<img src="/TEMPLATE/ampTemplate/images/green_check.png" style="height: 16px; vertical-align: text-bottom;"  />
			<digi:trn>Submit</digi:trn>
		</button>
				</td>
				<td>
					<button type="button" onclick="return cancel();" style="vertical-align:bottom; padding: 1px; height: 22px; width: 60px;" class="buttonx">
						<digi:trn>Cancel</digi:trn>
					</button>
				</td>
			</tr>
		</table>
		<%-- <button type="button" onclick="return doSubmit();" style="vertical-align:bottom; padding: 1px;" class="buttonx">
			<img src="/TEMPLATE/ampTemplate/images/green_check.png" style="height: 16px; vertical-align: text-bottom;"  />
			<digi:trn>Submit</digi:trn>
		</button> --%></center>
	</digi:form>
				</td>
				</tr>
</table>
