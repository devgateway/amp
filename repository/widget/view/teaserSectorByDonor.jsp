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
	function donorChanged(){
		rechart();
	}
	function yearChanged(){
		rechart()
	}
	function rechart(){
		var chartImage=document.getElementById('sectorByDonorChartImage');
		chartImage.src = '<digi:file src="images/amploading.gif"/>';
		var y=document.getElementsByName('selectedYear')[0].value;
		var d=document.getElementsByName('selectedDonor')[0].value;
		var myUrl = chartURL+'~selectedYear='+y+'~selectedDonor='+d;
		myUrl+=getLegendState();
		myUrl+=getLabelState();
		chartImage.src=myUrl;
		//alert(myUrl);
	}
	function getLegendState(){
		var chkLegend = document.getElementsByName('showLegends')[0];
		if (chkLegend.checked){
			return '~showLegend=true'
		}else{
			return '~showLegend=false'
		}
	}
	function getLabelState(){
		var chkLabel = document.getElementsByName('showLabels')[0];
		if (chkLabel.checked){
			return '~showLabel=true'
		}else{
			return '~showLabel=false'
		}
	}
	
//-->
</script>


<div id="content" class="yui-skin-sam" style="width:100%;z-index: 1;">
  <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
    <ul class="yui-nav">
      <li class="selected">
        <div class="nohover">
        <a style="cursor:default">
        <div>
          <digi:trn key="gis:breakdownbysector">Breakdown by sector</digi:trn>
        </div>
        </a>
        </div>
      </li>
    </ul>
    <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

<table>
    <tr>
        <td colspan="3" align="left">
            <div id="piehelptooltip" style="display:none; width:200; position: absolute; left:50px; top: 50px; background-color: #ffffff; border: 1px solid silver;">
               <digi:trn key='widget:piechart:allAmountinUSDTooltip'>
                   The amounts are calculated using the exchange rate for the date of the transaction.</digi:trn>
            </div>  
                <digi:trn key="widget:piechart:allAmountsin000USD">All amounts in 000s of USD</digi:trn>
            <digi:img  src="module/widget/images/help1.gif" onmouseover="showHelpTooltip()" onmouseout="hideHelpTooltip()"/>
        </td>
    </tr>
	<tr>
		<td>
			<html:select name="gisSectorByDonorTeaserForm" property="selectedDonor"  style="width : 230px" onchange="donorChanged()">
				<html:option value="-1"><digi:trn key="widget:piechart:allDonorsItem">All Donors</digi:trn></html:option>
				<html:optionsCollection name="gisSectorByDonorTeaserForm" property="donors" label="name" value="ampOrgId"/>
			</html:select>
		</td>
		<td>
			<html:select name="gisSectorByDonorTeaserForm" property="selectedYear" onchange="yearChanged()">
				<html:optionsCollection name="gisSectorByDonorTeaserForm" property="years" label="label" value="value"/>
			</html:select>
		</td>
		<td nowrap="nowrap">
			<input type="checkbox" title="Show Labels" name="showLabels" onchange="rechart()" checked="checked">
			&nbsp;
			<input type="checkbox" title="Show Legends" name="showLegends" onchange="rechart()" checked="checked">
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<img id="sectorByDonorChartImage" src="/widget/showSectorByDonorChart.do">
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
