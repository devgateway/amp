<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<!-- pour avh formulas -->

<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>


<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<!-- END pour avh formulas -->


<style type="text/css">
		.jcol{												
		padding-left:10px;												 
		}
		.jlien{
			text-decoration:none;
		}
		.tableEven {
			background-color:#dbe5f1;
			font-size:8pt;
			padding:2px;
		}

		.tableOdd {
			background-color:#FFFFFF;
			font-size:8pt;
			padding:2px;
		}
		 
		.Hovered {
			background-color:#a5bcf2;
		}
		
		.notHovered {
			background-color:#FFFFFF;
		}
		
		
</style>
<script language="JavaScript">


function banUser(txt) {
  var ban=confirm("${translationBan}");
  return ban;
  }
  
 function unbanUser(txt) {
  var ban=confirm("${translationUnban}");
  return ban;
  }
  
  
  function searchAlpha(val) {
		     document.umViewAllUsersForm.action = "/um/viewAllUsers.do?currentAlpha="+val;
		     document.umViewAllUsersForm.submit();
			 return true;		
	}

  function setStripsTable(tableId, classOdd, classEven) {
		var tableElement = document.getElementById(tableId);
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
	function setHoveredTable(tableId, hasHeaders) {

		var tableElement = document.getElementById(tableId);
		if(tableElement){
	    	var className = 'Hovered',
	        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
	        rows      = tableElement.getElementsByTagName('tr');

			for(var i = 0, n = rows.length; i < n; ++i) {
				rows[i].onmouseover = function() {
					this.className += ' ' + className;
				};
				rows[i].onmouseout = function() {
					this.className = this.className.replace(pattern, ' ');

				};
			}
			rows = null;
		}
	}

	
	function setHoveredRow(rowId) {

		var rowElement = document.getElementById(rowId);
		if(rowElement){
	    	var className = 'Hovered',
	        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
	        cells      = rowElement.getElementsByTagName('td');

			for(var i = 0, n = cells.length; i < n; ++i) {
				cells[i].onmouseover = function() {
					this.className += ' ' + className;
				};
				cells[i].onmouseout = function() {
					this.className = this.className.replace(pattern, ' ');

				};
			}
			cells = null;
		}
	}
	
	

</script>

<!--  ============================== -->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
function saveFormula(){
  var a=document.getElementById("act");
  if(a!=null){
    a.value="save"
  }
  var ie=document.getElementById("chkEnabled");
  var he=document.getElementById("hdnEna");
  if(ie!=null && he!=null){
    he.value=ie.checked;
  }
  document.aimViewAhSurveyFormulasForm.submit();
}

function changeFormula(){
  document.aimViewAhSurveyFormulasForm.submit();
}

function resetFormula(){
  var textBox=document.getElementById("txtBaseLineValue");
  textBox.value="";

  var textBox=document.getElementById("txtTargetValue");
  textBox.value="";
  
  textBox=document.getElementById("txtConstant");
  textBox.value="";

  textBox=document.getElementById("txtFormula");
  textBox.value="";
}

</script>
<!-- =============================== -->



<digi:instance property="aimViewAhSurveyFormulasForm" />

<jsp:include page="teamPagesHeader.jsp" flush="true" />



<div style="float:left; width:500px;">




<table bgColor=#ffffff cellPadding=0 cellSpacing=0  >
  <tr>
    <td width=14>&nbsp;</td>
    <td align=left   vAlign=top >
      <table cellPadding=5 cellSpacing=0 >
        <tr>
          <td valign="bottom" class="crumb" >
            <c:set var="translation">
              <digi:trn key="aim:clickToViewMyDesktop">Click here to view admin home page</digi:trn>
            </c:set>
            <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
              <digi:trn key="aim:adminHomeLink">Admin home</digi:trn>
            </digi:link>
            &gt;
            <digi:trn key="aim:allParisReportsList">Paris Indicator Reports List</digi:trn>
          </td>
        </tr>
        <tr>
          <td>  <span class=subtitle-blue>
              <digi:trn key="aim:parisIndcReports">Paris Indicator Target Manager</digi:trn>
            </span></td>
        </tr>
        <tr>
          <td height=16 >
         <digi:trn key="aim:listofparisIndcReports">List of Paris indicator reports</digi:trn>
          </td>
        </tr>
        <tr>
          <td noWrap vAlign="top">
          

            
                  <table align=center  cellPadding=0 cellSpacing=0 id="dataTable">
                    <tr>
                      <td bgColor=#ffffff class="box-border" height="170px" >
                        <table border=0 cellPadding=3 cellSpacing=5>
                          <tr >
                          <!--  <td  align="center" height="20" style="background-color:#999999;">
                               <b>
                                <digi:trn key="aim:parisIndcReportsList">
                                List of Paris Indicator reports
                                </digi:trn>
                              </b>
                              
                            </td> --> 
                          </tr>
                          <c:if test="${empty aimViewAhSurveyFormulasForm.surveis}">
                            <tr>
                              <td>
                                <b>
                                  <font color="red">
                                    <digi:trn key="aim:noParisIndcRecordFound">No Aid Effectiveness Indicator found</digi:trn>
                                  </font>
                                </b>
                              </td>
                            </tr>
                          </c:if>
                          <c:if test="${!empty aimViewAhSurveyFormulasForm.surveis}">
                            <c:forEach var="report" items="${aimViewAhSurveyFormulasForm.surveis}">
                              <c:if test="${report.indicatorCode != '4' && report.indicatorCode != '5aii' && report.indicatorCode != '5bii' && report.indicatorCode != '9'&& report.indicatorCode != '10a' && report.indicatorCode != '10b'&&report.indicatorCode != '8'}">
                                <tr>
                                  <td>
                                    <IMG alt=Link height=10 src="../ampTemplate/images/arrow-gr.gif" width=10 />
                                    <strong>[${report.indicatorCode}]</strong>
                                    <c:set var="translation">
                                      <digi:trn key="aim:clickToViewReport">Click here view Report</digi:trn>
                                    </c:set>
                                    <a href="/aim/viewAhSurveyFormulas.do?indId=${report.ampIndicatorId}">
                                      <digi:trn key="aim:${report.nameTrn}">
                                           ${report.name}
                                      </digi:trn>
                                    </a>
                                  </td>
                                </tr>
                              </c:if>
                            </c:forEach>
                          </c:if>
                        </table>
                      </td>
                    </tr>
                  </table>
          
              
         
</td>
        </tr>
      </table>
</td>
  </tr>
</table>
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
	setHoveredRow("rowHighlight");
</script>
<!-- ==================================================================== -->
<!-- ===========   viewAhSurveis.jsp code paste under ========================= -->

<!-- ==================================================================== -->
</div>
<div style="float:left; padding-top:75px;">

<digi:errors/>
<!--<digi:instance property="aimViewAhSurveyFormulasForm" />-->

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<digi:form action="/viewAhSurveyFormulas.do" >
  <html:hidden property="action" styleId="act" />
  

   <table class = "box-border">


    <tr>
      <td colspan="2" height=16 align="center" vAlign="middle" style=" background-color:#C2C2C2; color:#000;">
        <span >
          <digi:trn key="aim:AhSurveyFormula">AhSurvey Formula</digi:trn>
        </span>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>
        <html:hidden property="formulaEnabled" styleId="hdnEna" />
        <c:if test="${aimViewAhSurveyFormulasForm.formulaEnabled}">
          <input type="checkbox" id="chkEnabled" checked="checked" />
        </c:if>
        <c:if test="${!aimViewAhSurveyFormulasForm.formulaEnabled}">
          <input type="checkbox" id="chkEnabled" />
        </c:if>
        <%--Not Working correctly ->  <html:checkbox property="formulaEnabled" styleId="chkEnabled" /> --%>
        &nbsp;<digi:trn key="aim:enable">Enable</digi:trn>
      </td>
      <td>&nbsp;</td>
    </tr>

    <tr>
      <td>
      <digi:trn key="aim:baselinevalue">Baseline value</digi:trn>
      </td>
      <td>
        <html:text property="baseLineValue" styleId="txtBaseLineValue" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>
      <digi:trn key="aim:targetvalue">Target value</digi:trn>      
      </td>
      <td>
        <html:text property="targetValue" styleId="txtTargetValue" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>
      <digi:trn key="aim:constant">Constant</digi:trn>      
      </td>
      <td>
        <html:text property="constantName" styleId="txtConstant" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>
      <digi:trn key="aim:formula">Formula</digi:trn>
      </td>
      <td>
        <html:text property="formulaText"  styleId="txtFormula" style="font-family:verdana;font-size:8pt;" />
      </td>
    </tr>

    <tr>
      <td>

      </td>
      <td>
        <input type="button" value="<digi:trn key="aim:btnsave">Save</digi:trn>" onclick="saveFormula();" style="font-family:verdana;font-size:8pt;">
        <input type="button" value="<digi:trn key="aim:btnreset">Reset</digi:trn>" onclick="resetFormula();" style="font-family:verdana;font-size:8pt;">
      </td>
    </tr>
  </table>
</digi:form>

</div>


