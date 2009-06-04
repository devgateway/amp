<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="javascript">

function selectProgram(){

  <digi:context name="selPrg" property="context/module/moduleinstance/selectProgramForIndicator.do" />
  openURLinWindow("<%= selPrg %>",700, 500);
}

function selectActivity(){
  <digi:context name="selAct" property="context/module/moduleinstance/selectActivityForIndicator.do" />
  openURLinWindow("<%= selAct %>",700, 500);
  
}
function removeActivity(id) {
	<c:set var="translation">
		<digi:trn key="admin:deleteThisActivity">Do you want to delete this Activity?</digi:trn>
	</c:set>
	var temp = confirm("${translation}");
	if(temp == false)
	{
			return false;
	}
	else
	 {
		<digi:context name="update" property="context/module/moduleinstance/selectActivityForIndicator.do?action=remove" />
		document.aimNewIndicatorForm.action = "<%=update%>&aId="+id;
	    document.aimNewIndicatorForm.target = "_self"
	    document.aimNewIndicatorForm.submit();
	    return true;		  
    }
}
	
	
</script>
<digi:instance property="aimNewIndicatorForm" />
<digi:form action="/addNewIndicator.do" method="post">
    
 <!-- <html:hidden property="type" value="3"/> --> 
  <html:hidden property="trType" value="3"/>
  <html:hidden property="category" value="-1"/> 
  <html:hidden property="selActivitySector" styleId="hdnselActivitySectors" />

  <table width="100%" align="center" class=box-border-nopadding>
    <tr bgcolor="#006699" class=r-dotted-lg>
      <td vAlign="center" width="100%" align ="center" class="textalb" height="20">
      <b><digi:trn key="aim:addnewindicator">Add New Indicator</digi:trn></b>
      </td>
    </tr>
    <tr>
         <td>
            <digi:errors/>
        </td>
    </tr>
    <tr align="center" bgcolor="#ECF3FD">
       
      <td>
        <table border="0">
        <field:display name="Admin Indicator name" feature="Admin"></field:display>
          <tr id="trName">
            <td>
            <digi:trn key="aim:indicatorname">Indicator name:</digi:trn>
            <span style="color:Red;">*</span>
            </td>
            <td>
              <html:text property="name" styleId="txtName" style="font-family:verdana;font-size:11px;width:200px;" maxlength="256"/>
            </td>
          </tr>
          
          <field:display name="Admin Description" feature="Admin">
          <tr id="trDescription">
            <td valign="top">
            <digi:trn key="admin:decription">
            Description:
            </digi:trn>
            </td>
            <td>
              <html:textarea property="description" styleId="txtDescription" style="font-family:verdana;font-size:11px;width:200px;"></html:textarea>
            </td>
          </tr>
          </field:display>
          <field:display name="Indicator code" feature="Admin"></field:display>
          <tr>
            <td>
            	<digi:trn key="admin:indicatorcode">
            	Indicator code:
            	</digi:trn>
            	<span style="color:Red;">*</span>
            </td>
            <td>
               <html:text property="code" styleId="txtCode" style="font-family:verdana;font-size:11px;width:100px;"/>
            </td>
          </tr>
          
          <field:display name="Indicator Type" feature="Admin">
          <tr>
          	<td><digi:trn key="admin:indicatorType">Indicator Type</digi:trn>: <span style="color:Red;">*</span></td>
          	<td><html:select name="aimNewIndicatorForm" property="type">          		
          		<html:option value="A"><digi:trn key="admin:indicatorType:ascending">ascending</digi:trn></html:option>
          		<html:option value="D"><digi:trn key="admin:indicatorType:descending">descending</digi:trn></html:option>
          	</html:select>
          	</td>
          </tr>
          </field:display>
          <tr id="trType">
          </tr>
          <tr id="trCategory">
          </tr>
          <field:display name="Sectors" feature="Admin"></field:display> 
          <tr>
          <td>
          <digi:trn key="admin:sectors">
          	Sectors
          </digi:trn>
          <span style="color:Red;">*</span></td>
            <td >
              <jsp:include page="addIndicatorSector.jsp"/>
             </td>
          </tr> 
           
           <tr id="trSector">
           </tr>
          <field:display name="Creation date" feature="Admin">
	      <tr id="trCreationDate">
            <td>
            <digi:trn key="admin:creationdate">
            Creation date:
            </digi:trn>
            </td>
            <td>
              <html:text property="date" disabled="true" styleId="txtCreationDate" style="font-family:verdana;font-size:11px;width:80px;"/>
            </td>
          </tr> 
          </field:display>
          <!--
          <tr>
            <td colspan="10" nowrap="nowrap">
              <input type="checkbox" name="IndicatorType" id="radioProgramIndicator" value="0" /> &nbsp;<digi:trn key="admin:programind">Program indicator</digi:trn>&nbsp;
              
               <span id="spnSelectProgram" style="">
                 [<a href="javascript:selectProgram();">Select program</a>]
                <c:if test="${!empty aimNewIndicatorForm.selectedPrograms}">
                  <c:forEach var="prog" items="${aimNewIndicatorForm.selectedPrograms}">
                    [${prog.label}]
                    <html:hidden property="selectedProgramId" value="${prog.value}"/>
                  </c:forEach>
                </c:if>
                <c:if test="${empty aimNewIndicatorForm.selectedPrograms}">
                  [<span style="color:Red;">Program is not selected</span>]<span style="color:Red;">*</span>
                </c:if>
              </span>             
              <br>
              <input type="checkbox" name="IndType" id="radioProjectIndicator" value="2" /> 
              &nbsp;<digi:trn key="admin:projectind">Project indicator</digi:trn>&nbsp;
                     [<a href="javascript:selectActivity();"><digi:trn key="admin:selectproject">Select project</digi:trn></a>]
                     
                      </td>
                </tr>
                <tr> 
                 <td colspan="10" nowrap="nowrap" align="center" bgcolor="#f4f4f2">
                   <table width="98%" cellPadding=2 cellSpacing=0 valign="top" align="center" class="box-border-nopadding">
                     <c:if test="${!empty aimNewIndicatorForm.selectedActivities}">
	            		   <c:forEach var="act" items="${aimNewIndicatorForm.selectedActivities}">
			                   <tr onmouseover="style.backgroundColor='#dddddd';" onmouseout="style.backgroundColor='white'">
				                   <td align="left" >&nbsp;
				                     ${act.label}
				                  </td>
				                  <td align="right">
				                  <a href="javascript:removeActivity(${act.value})">
																	 	<digi:img src="../ampTemplate/images/deleteIcon.gif" 
																		border="0" alt="Remove this child workspace"/></a>&nbsp;
				                  </td>
			                  </tr>
	                      </c:forEach>									
			          </c:if>
             
             
                   <c:if test="${empty aimNewIndicatorForm.selectedActivities}">
                       [<span style="color:Red;"><digi:trn key="admin:activitynoselected">Activity is not selected</digi:trn></span>]
                  </c:if>                   
            </table>
            </td>
          </tr>
           -->
        </table>
      </td>
    </tr>
    <tr  align="center" bgcolor="#ECF3FD">
      <td>
      <field:display name="Add New Indicator" feature="Admin">
      <html:button  styleClass="dr-menu" property="submitButton"  onclick="addNewIndicator()">
		<digi:trn key="btn:add">Add</digi:trn> 													
 	 </html:button>
 	 </field:display>
 	 <html:reset  styleClass="dr-menu" property="submitButton">
		<digi:trn key="btn:clear">Clear</digi:trn> 
	</html:reset>											
 	 <html:button  styleClass="dr-menu" property="submitButton"  onclick="closeWindow()">
			<digi:trn key="btn:close">Close</digi:trn> 
	 </html:button>
      </td>
    </tr>
  </table>
</digi:form>
