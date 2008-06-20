<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="gisDemoForm"/>

<table>
	<tr>
		<td>
			<img id="testMap" src="/gis/getFoundingDetails.do?action=paintMap&mapCode=TZA&segmentData=Tanga/Muheza%20DC/38|Tanga/Korogwe%20DC/15|Tanga/Lushoto%20DC/30|Tanga/Handeni%20DC/5|Tanga/Pangani%20DC/12">
		</td>
	</tr>
	<tr>
		<td>
		<select onChange="sectorSelected(this)">
			<logic:iterate name="gisDemoForm" property="sectorCollection" id="sec">
				<option value="<bean:write name="sec" property="sector.ampSectorId"/>"><bean:write name="sec" property="sector.name"/> (<bean:write name="sec" property="count"/>)</option>
			</logic:iterate>
		</select>
		</td>
	</tr>
</table>

<script language="JavaScript">
	var xmlhttp =  new XMLHttpRequest();
	
	function sectorSelected(sec) {
	
		var uniqueStr = (new Date()).getTime();
		//xmlhttp.open("GET", "../../gis/getFoundingDetails.do?action=getDataForSector&mapCode=TZA&sectorId=" + sec.value + "&uniqueStr=" + uniqueStr, true);
//		xmlhttp.onreadystatechange = sectorDataReady;
//		xmlhttp.send(null);		
	document.getElementById("testMap").src = "../../gis/getFoundingDetails.do?action=getDataForSector&mapCode=TZA&sectorId=" + sec.value + "&uniqueStr=" + uniqueStr;
		
	}
	
	function sectorDataReady() {
	}
</script>



