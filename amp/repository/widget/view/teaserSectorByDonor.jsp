<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<script type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<digi:instance property="gisSectorByDonorTeaserForm" />
<script type="text/javascript">
<!--

       function showHelpTooltip() {
          document.getElementById("piehelptooltip").style.display = "block";
         }
                
        function hideHelpTooltip() {
             document.getElementById("piehelptooltip").style.display = "none";
        }
	<digi:context name="chartImageUrl" property="context/module/moduleinstance/showSectorByDonorChart.do" />
	var chartURL = "<%=chartImageUrl%>";
//-->
</script>


<div id="content" class="yui-skin-sam" style="width:100%;z-index: 1;">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
      <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
      <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
      <div class="longTab">
          <digi:trn key="gis:breakdownbysector">Breakdown by sector</digi:trn>
        </div>
    <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

<table width="100%">
    <tr>
        <td colspan="3" align="left">
            <div id="piehelptooltip" style="display:none; width:200; position: absolute; left:50px; top: 50px; background-color: #ffffff; border: 1px solid silver;">               
               	<digi:trn key='widget:piechart:allAmountinUSDTooltip'>The amounts are calculated using the exchange rate for the date of the transaction.</digi:trn>               
            </div>  
            	<c:if test="${gisSectorByDonorTeaserForm.amountsInThousands==true}">
            		<digi:trn key="widget:piechart:allAmountsin000USD">All amounts in 000s of</digi:trn> ${gisSectorByDonorTeaserForm.selectedCurrency}
            	</c:if>
                <c:if test="${gisSectorByDonorTeaserForm.amountsInThousands==false}">
               		<digi:trn>All amounts in</digi:trn> ${gisSectorByDonorTeaserForm.selectedCurrency}
               </c:if>
            <digi:img  src="module/widget/images/help1.gif" onmouseover="showHelpTooltip()" onmouseout="hideHelpTooltip()"/>
        </td>
    </tr>
	<tr>
		<td width="90%">
			<html:select name="gisSectorByDonorTeaserForm" property="selectedDonor" onchange="donorChanged()">
				<html:option value="-1"><digi:trn key="widget:piechart:allDonorsItem">All Donors</digi:trn></html:option>
				<html:optionsCollection name="gisSectorByDonorTeaserForm" property="donors" label="name" value="ampOrgId"/>
			</html:select>
		</td>
		<%-- 
			<td>
			<html:select name="gisSectorByDonorTeaserForm" property="selectedYear" onchange="yearChanged()">
				<html:optionsCollection name="gisSectorByDonorTeaserForm" property="years" label="label" value="value"/>
			</html:select>
		</td>
		--%>		
		<td nowrap="nowrap" align="right">
			<input type="checkbox" title="<digi:trn>Show Labels</digi:trn>" name="showLabels" onclick="rechart()" checked="checked">
			&nbsp;
			<input type="checkbox" title="<digi:trn>Show Legends</digi:trn>" name="showLegends" onclick="rechart()" checked="checked">
		</td>
	</tr>
	<tr>
        <td colspan="3">
            <div id="sectorByDonorChartImageDiv" style="display:none">
                <img alt="" id="sectorByDonorChartImage" src="/widget/showSectorByDonorChart.do" onload="loadSectorDonorMap()" usemap="#sectorByDonorChartImageMap" border="0" >
            </div>
            <div id="sectorByDonorChartImageDivLoad">
                <img src='<digi:file src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif"/>' alt="">
            </div>
            <MAP name="sectorByDonorChartImageMap" id="sectorByDonorChartImageMap">
            </MAP>
        </td>
	</tr>
        <tr>
		<td colspan="3" align="left">
                    <digi:trn key="widget:SourceAmpdatabase">Source: AMP database</digi:trn>
		</td>
	</tr>
</table>
    </div>
  </div>
</div>   
