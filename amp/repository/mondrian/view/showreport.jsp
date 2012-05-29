<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib uri="http://www.tonbeller.com/wcf" prefix="wcf" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

  <link rel="stylesheet" type="text/css" href="../../../jpivot/table/mdxtable.css">
  <link rel="stylesheet" type="text/css" href="../../../navi/mdxnavi.css">
  <link rel="stylesheet" type="text/css" href="../../../wcf/form/xform.css">
  <link rel="stylesheet" type="text/css" href="../../../wcf/table/xtable.css">
  <link rel="stylesheet" type="text/css" href="../../../wcf/tree/xtree.css">
  
  <script language="JavaScript">
<!--
function go()
	{
    	 <digi:context name="changecurrency" property="context/module/moduleinstance/showreport.do"/>
		 url = "<%= changecurrency %>?pagename=query&id=<%=request.getParameter("id")%>";
		//alert(url);
	    ShowReportForm.action = url;
    	ShowReportForm.target = "_self";
    	ShowReportForm.submit();
    	return true;
	}

-->
</script>

  
<html>
 <jsp:include page="saveReport.jsp"  />
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body bgcolor=white>
<digi:form action="/showreport.do"  method="post">

<%-- include query and title, so this jsp may be used with different queries --%>
<wcf:include id="include01" httpParam="pagename" prefix="/WEB-INF/queries/" suffix=".jsp"/>
<c:if test="${query01 == null}">
  <jsp:forward page="/index.jsp"/>
</c:if>
<%-- define table, navigator and forms --%>
<jp:table id="table01" query="#{query01}"/>
<jp:navigator  id="navi01" query="#{query01}" visible="false"/>
<wcf:form id="mdxedit01" xmlUri="/WEB-INF/jpivot/table/mdxedit.xml" model="#{query01}" visible="false"/>
<wcf:form id="sortform01" xmlUri="/WEB-INF/jpivot/table/sortform.xml" model="#{table01}" visible="false"/>

<jp:print id="print01"/>
<wcf:form id="printform01" xmlUri="/WEB-INF/jpivot/print/printpropertiesform.xml" model="#{print01}" visible="false"/>

<jp:chart baseDisplayURL="/aim/DisplayChart.img" id="chart01" query="#{query01}" visible="false" controllerURL="/mondrian/showreport.do"/>


<wcf:form id="chartform01" xmlUri="/WEB-INF/jpivot/chart/chartpropertiesform.xml" model="#{chart01}" visible="false"/>
<wcf:table id="query01.drillthroughtable" visible="false" selmode="none" editable="true"/>

<h2><c:out value="${title01}"/></h2>
<%-- define a toolbar --%>
<wcf:toolbar  id="toolbar01" bundle="com.tonbeller.jpivot.toolbar.resources">
  <wcf:scriptbutton id="cubeNaviButton" tooltip="toolb.cube" img="cube" model="#{navi01.visible}"/>
   
  <wcf:scriptbutton id="mdxEditButton" tooltip="toolb.mdx.edit" img="mdx-edit" model="#{mdxedit01.visible}"/>
  
  <wcf:scriptbutton id="sortConfigButton" tooltip="toolb.table.config" img="sort-asc" model="#{sortform01.visible}"/>
  <wcf:separator/>
  <wcf:scriptbutton id="levelStyle" tooltip="toolb.level.style" img="level-style" model="#{table01.extensions.axisStyle.levelStyle}"/>
  <wcf:scriptbutton id="hideSpans" tooltip="toolb.hide.spans" img="hide-spans" model="#{table01.extensions.axisStyle.hideSpans}"/>
  <wcf:scriptbutton id="propertiesButton" visibleRef="false" tooltip="toolb.properties"  img="properties" model="#{table01.rowAxisBuilder.axisConfig.propertyConfig.showProperties}"/>
  <wcf:scriptbutton id="nonEmpty" tooltip="toolb.non.empty" img="non-empty" model="#{table01.extensions.nonEmpty.buttonPressed}"/>
  <wcf:scriptbutton id="swapAxes" tooltip="toolb.swap.axes"  img="swap-axes" model="#{table01.extensions.swapAxes.buttonPressed}"/>
  <wcf:separator/>
  <wcf:scriptbutton model="#{table01.extensions.drillMember.enabled}"	 tooltip="toolb.navi.member" radioGroup="navi" id="drillMember"   img="navi-member"/>
  <wcf:scriptbutton model="#{table01.extensions.drillPosition.enabled}" tooltip="toolb.navi.position" radioGroup="navi" id="drillPosition" img="navi-position"/>
  <wcf:scriptbutton model="#{table01.extensions.drillReplace.enabled}"	 tooltip="toolb.navi.replace" radioGroup="navi" id="drillReplace"  img="navi-replace"/>
  <wcf:scriptbutton model="#{table01.extensions.drillThrough.enabled}"  tooltip="toolb.navi.drillthru" id="drillThrough01"  img="navi-through"/>
  <wcf:separator/>
  <wcf:scriptbutton id="chartButton01" tooltip="toolb.chart" img="chart" model="#{chart01.visible}"/>
  <wcf:scriptbutton id="chartPropertiesButton01" tooltip="toolb.chart.config" img="chart-config" model="#{chartform01.visible}"/>
  <wcf:separator/>
  <wcf:scriptbutton  id="printPropertiesButton01" tooltip="toolb.print.config" img="print-config" model="#{printform01.visible}"/>
  <wcf:imgbutton id="printpdf" tooltip="toolb.print" img="print" href="../../../Print.out?cube=01&type=1"/>
  <wcf:imgbutton id="printxls" tooltip="toolb.excel" img="excel" href="../../../Print.out?cube=01&type=0"/>
  <wcf:separator/>
  <wcf:imgbutton id="save" tooltip="save report" img="save" href="javascript:mainSaveReports()"/>
  <wcf:separator/>
  
 </wcf:toolbar>


<%-- render toolbar --%>
<wcf:render ref="toolbar01" xslUri="/WEB-INF/jpivot/toolbar/htoolbar.xsl" xslCache="false"/>
<p>

<%-- if there was an overflow, show error message --%>
<c:if test="${query01.result.overflowOccured}">
  <p>
  <strong style="color:red">Resultset overflow occured</strong>
  <p>
</c:if>

<%-- render navigator --%>
<wcf:render ref="navi01" xslUri="/WEB-INF/jpivot/navi/navigator.xsl" xslCache="false"/>
<%-- edit mdx
  	<h3>MDX Query Editor</h3>
  	<wcf:render ref="mdxedit01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="false"/>
 --%>
<%-- sort properties --%>
<wcf:render ref="sortform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="false"/>

<%-- chart properties --%>
<wcf:render ref="chartform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="false"/>

<%-- print properties --%>
<wcf:render ref="printform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="false"/>

<!-- Errors-->
<span style="text-align: center;color: red;"> 
<logic:iterate id="element" name="ShowReportForm" property="errors">
	<digi:trn key="${element.key}"><bean:write name="element" property="value"/></digi:trn>
</logic:iterate>

<logic:iterate id="element" name="ShowReportForm" property="messages">
	<digi:trn key="${element.key}"><bean:write name="element" property="value"/> </digi:trn>
</logic:iterate>
</span>

<table>
	<tr>
		<td>
			<digi:trn>Select Currency</digi:trn> 
		</td>
		<td>
			<html:select property="currency" styleClass="dr-menu" onchange="go();">
    			<html:optionsCollection name="ShowReportForm" property="currencies" value="currencyCode" label="currencyName"/>
			</html:select>
		</td>
	</tr>
</table>
<p>
<wcf:render ref="table01" xslUri="/WEB-INF/jpivot/table/mdxtable.xsl" xslCache="true"/>
<p>
	<wcf:render ref="table01" xslUri="/WEB-INF/jpivot/table/mdxslicer.xsl" xslCache="true"/>
<p>
<!-- drill through table -->
<wcf:render ref="query01.drillthroughtable" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

<!-- Chart -->
<p>
	<wcf:render  ref="chart01" xslUri="/WEB-INF/jpivot/chart/chart.xsl" xslCache="true"/>
<p>	
<table>
	<tr>
    	<td style="white-space:nowrap;background-color:#CCCCCC;padding: 5px 5px 5px 5px;width:120px;border-left:solid 1px #000000;">
    		<b><digi:trn key="aim:mondrian:datalastrefresedon">Data last refreshed on</digi:trn> <c:out value="${ShowReportForm.lastdate}"/></b>
        </td>
     </tr>
</table>
</digi:form>
</body>
</html>
