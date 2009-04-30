<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<script language="javascript" type="text/javascript">
    function getDonorValues(widgetId){
        var donorSelectId="sectorTableSelect_"+widgetId;
        var donorSelect=document.getElementById(donorSelectId);
        var selectedIndex=donorSelect.selectedIndex;
        var options=donorSelect.options;
        var donorId=options[selectedIndex].value;
        var url="/widget/getSectorDonorAmounts.do?widgetId="+widgetId+"&donorId="+donorId;
        var async=new Asynchronous();
        async.complete=donorValueCallBack;
        async.call(url);
    }

    function donorValueCallBack(status, statusText, responseText, responseXML){
     var mainTag=responseXML.getElementsByTagName('DonorSectorList')[0];
     var sectorsFundings=mainTag.childNodes;
     for(var i=0;i<sectorsFundings.length;i++){
        var tdId=sectorsFundings[i].getAttribute('id');
        var amount=sectorsFundings[i].getAttribute('amount');
        var empty=sectorsFundings[i].getAttribute('empty');
        var donorTD=document.getElementById(tdId);
        if(empty=='true'){
            donorTD.innerHTML="";
        }
        else{
            donorTD.innerHTML=amount;
        }
     }
    }
</script>



<digi:instance property="showSectorTableForm"/>
<table  id="sectorTable${showSectorTableForm.widgetId}" width="100%">
    <tr>
        <td style="color:#FFFFFF;font-weight:bold"><digi:trn>Sector</digi:trn></td>
        <c:forEach items="${showSectorTableForm.years}"    var="year">
            <td style="color:#FFFFFF;font-weight:bold;text-align:center">${year}</td>
        </c:forEach>
        <c:if test="${showSectorTableForm.donorColumnAdded}">
            <td>
                <select id="sectorTableSelect_${showSectorTableForm.widgetId}" onchange="getDonorValues('${showSectorTableForm.widgetId}')" AUTOCOMPLETE="OFF">
                    <option value="-1">Select Donor</option>
                    <c:forEach items="${showSectorTableForm.donors}" var="donor">
                         <option value="${donor.ampOrgId}">${donor.name}</option>
                    </c:forEach>
                </select>
            </td>
        </c:if>
    </tr>
    <c:forEach items="${showSectorTableForm.sectorsInfo}"    var="sectorInfo">

        <tr>
            <c:choose>
                <c:when test="${sectorInfo.applyStyle}">
                    <td style="font-weight:bold">${sectorInfo.sectorName}</td>
                </c:when>
                <c:when test="${sectorInfo.emptyRow}">
                    <c:forEach items="${showSectorTableForm.years}">
                        <td>&nbsp;</td>
                    </c:forEach>
                    <td>&nbsp;</td>
                </c:when>
                <c:otherwise>
                    <td>${sectorInfo.sectorName}</td>
                </c:otherwise>
            </c:choose>
            <c:forEach items="${sectorInfo.values}"    var="value">
                <c:choose>
                    <c:when test="${sectorInfo.applyStyle}">
                        <td style="font-weight:bold;text-align:center">${value}</td>
                    </c:when>
                    <c:otherwise>
                        <td align="center">${value}</td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${showSectorTableForm.donorColumnAdded}">
                <c:choose>
                    <c:when test="${sectorInfo.applyStyle}">
                        <td id="donor_${showSectorTableForm.widgetId}_${sectorInfo.sectorId}" style="font-weight:bold;text-align:left">
                            &nbsp;
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td id="donor_${showSectorTableForm.widgetId}_${sectorInfo.sectorId}">
                            &nbsp;
                        </td>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </tr>
    </c:forEach>
</table>
<script language="javascript">
    applyStyle(document.getElementById("sectorTable${showSectorTableForm.widgetId}"));
</script>




