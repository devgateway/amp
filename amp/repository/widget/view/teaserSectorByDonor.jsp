<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<digi:instance property="gisSectorByDonorTeaserForm" />


<script type="text/javascript">
<!--
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

<table>
    <tr>
        <td colspan="3" align="left">
            <c:set var="tooltip">
               <digi:trn key='widget:piechart:allAmountinUSDTooltip'>
                   The amounts are calculated using the exchange rate for the date of the transaction.</digi:trn>
            </c:set>
            <digi:trn key="widget:piechart:allAmountsinUSD1">All amounts in USD.</digi:trn>
            <digi:img  src="module/widget/images/help1.gif" title="${tooltip}"/>
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
				<html:option value="1998">1998</html:option>
				<html:option value="1999">1999</html:option>
				<html:option value="2000">2000</html:option>
				<html:option value="2001">2001</html:option>
				<html:option value="2002">2002</html:option>
				<html:option value="2003">2003</html:option>
				<html:option value="2004">2004</html:option>
				<html:option value="2005">2005</html:option>
				<html:option value="2006">2006</html:option>
				<html:option value="2007">2007</html:option>
				<html:option value="2008">2008</html:option>
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
</table>