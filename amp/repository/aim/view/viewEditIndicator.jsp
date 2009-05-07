<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />
<digi:instance property="aimNewIndicatorForm" />
<script language="javascript">
	

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
		document.aimNewIndicatorForm.action = "<%=update%>&forward=edit&aId="+id;
	    document.aimNewIndicatorForm.target = "_self"
	    document.aimNewIndicatorForm.submit();
	    return true;		  
    }
}

function selectProgram(){
  <digi:context name="selPrg" property="context/module/moduleinstance/selectProgramForIndicator.do?action=edit" />
  openURLinWindow("<%= selPrg %>",700, 500);
}

function selectActivity(){

  <digi:context name="selAct" property="context/module/moduleinstance/selectActivityForIndicator.do?action=edit" />
  openURLinWindow("<%= selAct %>",700, 500);
}
</script>
<digi:form action="/viewEditIndicator.do" method="post"> 
  <html:hidden property="prjStatus" styleId="projectStatus" />
  <html:hidden property="prgStatus" styleId="programStatus" /> 
  <html:hidden name="aimNewIndicatorForm" property="themeId" styleId="hdnThemeId" />
  <html:hidden property="selActivitySector" styleId="hdnselActivitySectors" />

  <table width="100%" align="center" border="0" class=box-border-nopadding>
    <tr bgcolor="#006699" class=r-dotted-lg >
      <td colspan="1" align="center" class="textalb">
      <b><digi:trn key="aim:vieweditindicator">View/Edit Indicator</digi:trn></b>
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
          <tr id="trName">
            <td>
            <digi:trn key="aim:indicatorname">Indicator name:</digi:trn>
            </td>
            <td>
              <html:text property="name" styleId="txtName" style="font-family:verdana;font-size:11px;width:200px;"/>
            </td>
          </tr>
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
          <tr>
            <td>
            <digi:trn key="admin:indicatorcode">
            	Indicator code:
            	</digi:trn>
            </td>
            <td>
               <html:text property="code" styleId="txtCode" style="font-family:verdana;font-size:11px;width:100px;"/>
            </td>
          </tr>
          <tr>
          	<td><digi:trn key="admin:indicatorType">Indicator Type</digi:trn>:</td>
          	<td><html:select name="aimNewIndicatorForm" property="type">          		
          		<html:option value="A"><digi:trn key="admin:indicatorType:ascending">ascending</digi:trn></html:option>
          		<html:option value="D"><digi:trn key="admin:indicatorType:descending">descending</digi:trn></html:option>
          	</html:select>
          	</td>
          </tr>
          <tr id="trType">
          </tr>
          <tr id="trCategory">
          </tr>
          <tr>
            <td><digi:trn key="admin:sectors">
          	Sectors
          </digi:trn></td>
             <td>
              <jsp:include page="addIndicatorSector.jsp"/>
            </td>
          </tr>  
          <tr id="trCreationDate">
            <td>
            <digi:trn key="admin:creationdate">
            Creation date:
            </digi:trn>
            </td>
            <td>
               <html:text property="date" styleId="date" readonly="true" style="font-family:verdana;font-size:11px;width:80px;"/>
			<a id="date0" href='javascript:pickDateById("date0","date")'>
				<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0> 
			</a> 


            </td>
          </tr>
          <!-- 
          <tr>
            <td colspan="10" nowrap="nowrap">
              <input type="checkbox" name="indTypeRadio" id="radioProgramIndicator"  checked="checked"/> 
              &nbsp;Program indicator&nbsp;
             <br />
              <input type="checkbox" name="tradio" id="radioProjectSpecific"  checked="checked" />
                   &nbsp;Project specific&nbsp;
                      [<a href="javascript:selectActivity();">Select project</a>]
                      <br>
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
                         [<span style="color:Red;">Activity is not selected</span>]
                      </c:if>
				 </table>									
            </td>
          </tr>
           -->
        </table>
      </td>
    </tr>
    <tr align="center" bgcolor="#ECF3FD">
      <td>
      <c:forEach var="act" items="${aimNewIndicatorForm.selectedActivities}">
      <html:hidden property="selectedActivityId"  value="${act.value}"/>
      </c:forEach>
      <html:button  styleClass="dr-menu" property="submitButton"  onclick="saveIndicator();">
			<digi:trn key="btn:Save">Save</digi:trn> 
	     </html:button>
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

<script language="javascript">
//radiosStatus(document.getElementById("Intype").value);
</script>
