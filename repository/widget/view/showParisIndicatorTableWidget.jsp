<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<script language="javascript" type="text/javascript">
    function getDonorGroupValues(widgetId){
        var donorGroupSelectId="parisIndicatorTableSelect_"+widgetId;
        //used to insert ajax loading image
        var partialId="donorGroup_"+widgetId+"_";  
        $("td[@id^='"+partialId+"']").html('<img src="images/amploading.gif" height="10px"/>');
        var donorGroupSelect=document.getElementById(donorGroupSelectId);
        var selectedIndex=donorGroupSelect.selectedIndex;
        var options=donorGroupSelect.options;
        var donorGroupId=options[selectedIndex].value;
        var url="/widget/getParisIndicatorDonorGroupValues.do?widgetId="+widgetId+"&donorGroupId="+donorGroupId;
        var async=new Asynchronous();
        async.complete=donorGroupValueCallBack;
        async.call(url);
    }

    function donorGroupValueCallBack(status, statusText, responseText, responseXML){
     var mainTag=responseXML.getElementsByTagName('ParisIndicatorValuesList')[0];
     var parisIndicatorsValues=mainTag.childNodes;
     for(var i=0;i<parisIndicatorsValues.length;i++){
        var tdId=parisIndicatorsValues[i].getAttribute('id');
        var value=parisIndicatorsValues[i].getAttribute('value');
        var donorGroupTD=document.getElementById(tdId);
        var empty=parisIndicatorsValues[i].getAttribute('empty');
        if(empty=='true'){
            donorGroupTD.innerHTML="";
        }
        else{
           donorGroupTD.innerHTML=value;
        }
        
     }
    }
</script>



<digi:instance property="showParisIndicatorTableForm"/>
<table  id="parisIndicatorTable${showParisIndicatorTableForm.widgetId}" width="100%">
    <tr>
        <td>&nbsp;</td>
        <td style="font-weight:bold"><digi:trn>Paris Declaration Indicators: Donors</digi:trn></td>
        <td style="font-weight:bold"><digi:trn>2005 Baseline</digi:trn></td>
        <td style="font-weight:bold"><digi:trn>2010 Target</digi:trn></td>
            <td>
                <select id="parisIndicatorTableSelect_${showParisIndicatorTableForm.widgetId}" onchange="getDonorGroupValues('${showParisIndicatorTableForm.widgetId}')" AUTOCOMPLETE="OFF">
                    <option value="-1"><digi:trn>Select Donor Group</digi:trn></option>
                    <c:forEach items="${showParisIndicatorTableForm.donorGroups}" var="donorGroup">
                         <option value="${donorGroup.ampOrgGrpId}">${donorGroup.orgGrpName}</option>
                    </c:forEach>
                </select>
            </td>
    </tr>
    <c:forEach items="${showParisIndicatorTableForm.parisIndicators}"    var="parisIndicatorInfo">
        <tr>      
          <td><digi:trn>Indicator</digi:trn>&nbsp;${parisIndicatorInfo.parisIndicator.indicatorCode}</td>
           <td>${parisIndicatorInfo.parisIndicator.name}</td>
           <c:choose>
                   <td>${parisIndicatorInfo.baseValue}</td>
                   <td>${parisIndicatorInfo.targetValue}</td>
               <c:when test="${parisIndicatorInfo.parisIndicator.indicatorCode=='10b'}">
                   <td><digi:trn>N/A</digi:trn></td>
               </c:when>
               <c:otherwise>
                   <td id="donorGroup_${showParisIndicatorTableForm.widgetId}_${parisIndicatorInfo.parisIndicator.ampIndicatorId}">&nbsp</td>
               </c:otherwise>
           </c:choose>
        </tr>
    </c:forEach>
</table>
<script language="javascript" type="text/javascript">
    applyStyle(document.getElementById("parisIndicatorTable${showParisIndicatorTableForm.widgetId}"));
</script>





