<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.List"%>

<digi:instance property="aimCategoryManagerForm" />
<bean:define id="myForm" name="aimCategoryManagerForm" toScope="session" type="org.digijava.module.aim.form.CategoryManagerForm" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" /> 

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
<script type="text/javascript">
	
	
        
      
        function addNewValue() {
          
 	document.forms[0].action="/categoryManager.do?addValue";
 	document.forms[0].submit();
		
		
	}
	
    function doSubmit() {
        if (document.aimCategoryManagerForm.categoryName.value.length == 0) {
            alert ("${translation1}");
            return false;
        }
        if (document.aimCategoryManagerForm.keyName.value.length == 0) {
            alert ("${translation2}");
            return false;
        } 
       var fieldTypes=document.aimCategoryManagerForm.getElementsByTagName("select");
        var countryNum=0;
        var regionNum=0;
	if(fieldTypes!=null&&fieldTypes.length>0){
		for (var i=0;i<fieldTypes.length;i++){
			
			if (fieldTypes[i].selectedIndex==1){
				countryNum++;
                                if(countryNum>1){
                                    alert("${translation4}");
                                    return false;
                                }
			}
                        if (fieldTypes[i].selectedIndex==2){
				regionNum++;
                                if(regionNum>1){
                                    alert("${translation5}");
                                    return false;
                                }
			}
		}
                if(countryNum==0){
                    alert("${translation4}");
                    return false;
                }
	}
	
	
        
        document.aimCategoryManagerForm.submit();
    }
	function deleteField(id, deleteId, undoId,disabeledId) {
		field						= document.getElementById(id) ;
                disabled=document.getElementById(disabeledId) ;
                disabled.value=true;
		field.style.textDecoration	= "line-through";
		field.disabled				= true;
		
		del							= document.getElementById(deleteId) ;
		del.style.display			= "none";
		
		undo						= document.getElementById(undoId) ;
		undo.style.display			= "inline";
	}
	function undeleteField(id, deleteId, undoId,disabeledId) {
		field						= document.getElementById(id) ;
		field.style.textDecoration	= "none";
		field.disabled				= false;
                disabled=document.getElementById(disabeledId) ;
                disabled.value=false;
		
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
                                         
                                              <table>
                                              <c:forEach var="possibleVals" items="${myForm.possibleVals}" varStatus="index">
                                                  <tr>
                                                      <td>
                                                          <html:text name="possibleVals" property="value"  style="text-decoration:none" indexed="true" styleId="field${index.count}"/>
                                                          <html:hidden name="possibleVals" property="disable"  indexed="true" styleId="disabled${index.count}"/>
                                              </td>
                                             
                                               <c:if test="${myForm.keyName=='implementation_location'}">
                                               <td>
                                                   &nbsp;
                                              </td>
                                              <td>
                                                  <digi:trn key="aim:CategoryManagerValueType">
                                                       Value Type:
                                                  </digi:trn>
				
                                                        <html:select name="possibleVals" property="fieldType" indexed="true">
                                                            <html:option value="0"><digi:trn key="aim:CategoryManagerNone">None</digi:trn></html:option>
                                                            <html:option value="1"><digi:trn key="aim:CategoryManagerCountry">Country</digi:trn></html:option>
                                                            <html:option value="2"><digi:trn key="aim:CategoryManagerRegion">Region</digi:trn></html:option>
                                                        </html:select>
                                                </td> 
                                                </c:if>
                                                <td>
                                                    <span id="delete${index.count}" style="display:inline">
						[<a style="cursor:pointer; text-decoration:underline; color: blue"  onclick="return deleteField('field${index.count}', 'delete${index.count}','undo${index.count}','disabled${index.count}')">
							<digi:trn key="aim:categoryManagerValueDelete">
								Delete
							</digi:trn>
						</a>]
						</span>
						<span id="undo${index.count}" style="display:none">
						[<a style="cursor:pointer; text-decoration:underline; color:blue"  onclick="return undeleteField('field${index.count}', 'delete${index.count}','undo${index.count}','disabled${index.count}')">
							<digi:trn key="aim:categoryManagerValueUndelete">
								Undelete
							</digi:trn>
						</a>]
						</span>
						
                                                </td>
                                                </tr>
						</c:forEach>
                                            </table>
						
                                                
 
					</div>
					<a style="cursor:pointer; text-decoration:underline; color: blue"  onclick="return addNewValue()">
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




