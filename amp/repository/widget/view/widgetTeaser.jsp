<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<digi:instance property="gisWidgetTeaserForm" />
<c:if test="${gisWidgetTeaserForm.rendertype==5}">
    <feature:display name="orgprof_chart_place${gisWidgetTeaserForm.type}" module="Org Profile">
        <c:choose>
            <c:when test="${gisWidgetTeaserForm.type==1}">
                <c:set var="organization" scope="request" value="${sessionScope.orgProfileFilter.organization}"/>
                <c:set var="orgGroup" scope="request" value="${sessionScope.orgProfileFilter.orgGroup}"/>
                <c:set var="orgsCount" scope="request" value="${fn:length(sessionScope.orgProfileFilter.orgIds)}"/>
                <jsp:include page="orgSummary.jsp" flush="true"/>
            </c:when>
            <c:when test="${gisWidgetTeaserForm.type==2||gisWidgetTeaserForm.type==4}">
                <jsp:include page="/orgProfile/showOrgProfileTables.do?type=${gisWidgetTeaserForm.type}" flush="true"/>
            </c:when>
            <c:when test="${gisWidgetTeaserForm.type==7}">
                <jsp:include page="/orgProfile/showParisIndicator.do" flush="true"/>
            </c:when>
            <c:otherwise>
                <script language="javascript" type="text/javascript">
                    function getGraphMap_${gisWidgetTeaserForm.type}(){
                        var lastTimeStamp = new Date().getTime();
                        var url="/widget/getWidgetMap.do?type="+${gisWidgetTeaserForm.type}+'&timestamp='+lastTimeStamp;
                        var async=new Asynchronous();
                        async.complete=mapCallBack_${gisWidgetTeaserForm.type};
                        async.call(url);
                    }

                    function mapCallBack_${gisWidgetTeaserForm.type}(status, statusText, responseText, responseXML){
                        var map=responseXML.getElementsByTagName('map')[0];
                        var id=map.getAttribute('id');
                        var mapSpan= document.getElementById(id);
                        mapSpan.innerHTML=responseText;
                    }
                </script>
                <img  alt="chart" src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}~chartType=${gisWidgetTeaserForm.type}~imageHeight=350~imageWidth=450" usemap="#chartMap${gisWidgetTeaserForm.type}" border="0" onload="getGraphMap_${gisWidgetTeaserForm.type}()"/>
                <span id="chartMap${gisWidgetTeaserForm.type}">

                </span>


            </c:otherwise>
        </c:choose>
    </feature:display>
     	


</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==4}">
	<img src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}">		
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==3}">

	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
	
	<div id="tableWidgetContainer_${gisWidgetTeaserForm.id}">
		
	</div>
	<table id="table" style="display: none"></table>

	
         
	<script language="JavaScript">
	
		function requestTable_${gisWidgetTeaserForm.id}(columnId,itemId){
			<digi:context name="tableRendererUrl" property="/widget/getTableWidget.do" />
			var url = '${tableRendererUrl}~tableId=${gisWidgetTeaserForm.id}';
			if (columnId!=null && itemId!=null){
				url+='~columnId='+columnId+'~itemId='+itemId;
			}
			var async=new Asynchronous();
			async.complete=tableCallBack_${gisWidgetTeaserForm.id};
			async.call(url);
		}
	
		function tableCallBack_${gisWidgetTeaserForm.id}(status, statusText, responseText, responseXML){
			
			processTableResponce_${gisWidgetTeaserForm.id}(responseText);
			applyStyle(document.getElementById("tableWidget${gisWidgetTeaserForm.id}"));
			
		}
	
		function processTableResponce_${gisWidgetTeaserForm.id}(htmlResponce){
			var myDiv = document.getElementById('tableWidgetContainer_${gisWidgetTeaserForm.id}');


if(myDiv.childNodes.length>1){
	
	var table = document.getElementById("tableWidget${gisWidgetTeaserForm.id}");
	var responce = document.getElementById("table");
	responce.innerHTML= htmlResponce;
	var responceHtmlTable = responce.firstChild;	

	
		for(k=1; k<table.rows.length; k++){
			
			var row = table.rows[k];
			var celllength = row.cells.length;
	
		     for(i=1; i<responceHtmlTable.rows.length; i++){
					
					var responceRow = responceHtmlTable.rows[i];
					//alert(responceRow.innerHTML);
					var responceCellLength = responceRow.cells.length;
					if(k == i){
						var value = responceRow.cells[responceCellLength-1].innerHTML;
						
					}
				}

                row.cells[celllength-1].innerHTML = value;
                
	  	}

	 //erase Helper hidden < Table> was select tag problems;
	responce.innerHTML = '';
	
	}else{

		myDiv.innerHTML = htmlResponce;
	}


}
		
		function tableWidgetFilterChanged_${gisWidgetTeaserForm.id}(columnId){

			
			
			var myDiv = document.getElementById('tableWidgetContainer_${gisWidgetTeaserForm.id}');
			var selItem = document.getElementsByName('selectedFilterItemId_${gisWidgetTeaserForm.id}')[0];	
			var itemId = selItem.value;
			//myDiv.innerHTML = 'loading...';
			requestTable_${gisWidgetTeaserForm.id}(columnId,itemId);
		}
		
		requestTable_${gisWidgetTeaserForm.id}();
		
</script>

</c:if>

    <c:if test="${gisWidgetTeaserForm.rendertype==6}">
        <script language="JavaScript" type="text/javascript">
            function renderSectorTable_${gisWidgetTeaserForm.id}(){
                var lastTimeStamp = new Date().getTime();
            <digi:context name="sectorTableRendererUrl" property="/widget/showSectorTable.do" />
                    var url = '${sectorTableRendererUrl}~widgetId=${gisWidgetTeaserForm.id}'+'&timestamp='+lastTimeStamp;
                    var async=new Asynchronous();
                    async.complete=sectorTableCallBack_${gisWidgetTeaserForm.id};
                    async.call(url);
                }

                function sectorTableCallBack_${gisWidgetTeaserForm.id}(status, statusText, responseText, responseXML){
                    processSectorTableResponse_${gisWidgetTeaserForm.id}(responseXML);
                    applyStyle(document.getElementById("sectorTableWidget${gisWidgetTeaserForm.id}"));

                }
                function processSectorTableResponse_${gisWidgetTeaserForm.id}(responseXML){
                    var sectorTableDiv=document.getElementById('sectorTableWidgetDiv_${gisWidgetTeaserForm.id}');
                    var sectorTableLoadingDiv=document.getElementById('sectorTableWidgetDivLoad_${gisWidgetTeaserForm.id}');
                    while (sectorTableDiv.firstChild) {
                        sectorTableDiv.removeChild(sectorTableDiv.firstChild);
                    }
                    var table=document.createElement('TABLE');
                    table.id="sectorTableWidget${gisWidgetTeaserForm.id}"
                    var tableBody=document.createElement('TBODY');
                    var headerRow=document.createElement('TR');
                    var mainTag=responseXML.getElementsByTagName('SectorTableWidget')[0];
                    var headers=mainTag.getElementsByTagName('Header');
                    var sectors=mainTag.getElementsByTagName('Sector');
                    var donors=mainTag.getElementsByTagName('Select');
                    var sectorTitle=document.createElement('TD');
                    sectorTitle.innerHTML='<digi:trn jsFriendly="true">Sector</digi:trn>';
                    headerRow.appendChild(sectorTitle);
                    var size=headers.length;
                    var isDonorColumn=false;
                    for(var i=0;i<size;i++){
                        var headerTD=document.createElement('TD');
                        headerTD.innerHTML=headers[i].getAttribute('name');
                        headerRow.appendChild(headerTD);
                    }
                    size++;
                    if(donors!=null&&donors.length>0){
                        var donorHeaderTD=document.createElement('TD');
                        var donorHeaderSelect=document.createElement('select');
                        donorHeaderSelect.style.width="200px";
                        donorHeaderSelect.autocomplete="off";
                        donorHeaderSelect.onchange=getDonorValues_${gisWidgetTeaserForm.id};
                        donorHeaderSelect.id="sectorTableSelect_${gisWidgetTeaserForm.id}";
                        var options=donors[0].childNodes;
                        var donorAllOption=document.createElement('option');
                        donorAllOption.value="-1";
                        var text='<digi:trn jsFriendly="true">Select Donor</digi:trn>';
                        donorAllOption.appendChild(document.createTextNode(text));
                        donorHeaderSelect.appendChild(donorAllOption);
                        for(var i=0;i<options.length;i++){
                            var donorOption=document.createElement('option');
                            donorOption.value=options[i].getAttribute('id');
                            text=options[i].getAttribute('name');
                            donorOption.appendChild(document.createTextNode(text));
                            donorHeaderSelect.appendChild(donorOption);
                        }
                        donorHeaderTD.appendChild(donorHeaderSelect);
                        headerRow.appendChild(donorHeaderTD);
                        isDonorColumn=true;
                    }
                    tableBody.appendChild(headerRow);
                    for(var i=0;i<sectors.length;i++){
                        var sectorTR=document.createElement('TR');
                        var nameTD=document.createElement('TD');
                        var id=sectors[i].getAttribute('id')
                        nameTD.innerHTML=sectors[i].getAttribute('name');
                        sectorTR.appendChild(nameTD);
                        var values=sectors[i].getElementsByTagName('Year');
                        if(values!=null){
                            for(var j=0;j<values.length;j++){
                                var sectorValueTD=document.createElement('TD');
                                sectorValueTD.innerHTML=values[j].getAttribute('value');
                                sectorTR.appendChild(sectorValueTD);
                            }

                        }

                        if(isDonorColumn){
                            var donorColumn=document.createElement('TD');
                            donorColumn.id="donor_${gisWidgetTeaserForm.id}_"+id;
                            sectorTR.appendChild(donorColumn);
                            size++;
                        }
                        tableBody.appendChild(sectorTR);
                    }

                    var totalRow = tableBody.lastChild;
                    var totalCells=totalRow.childNodes;
                    for(var i=0;i<totalCells.length;i++){
                        totalCells[i].style.fontWeight="bold";
                    }

                    var emptyRow=document.createElement('TR');
                    var emptyCell=document.createElement('TD');
                    var colspansize=size;
                    emptyCell.colSpan=colspansize;
                    emptyCell.innerHTML='&nbsp';
                    emptyRow.appendChild(emptyCell);
                    tableBody.insertBefore(emptyRow,totalRow);
                    table.appendChild(tableBody);
                    sectorTableDiv.appendChild(table);
                    sectorTableDiv.style.display="block";
                    sectorTableLoadingDiv.style.display="none";
                }
                function getDonorValues_${gisWidgetTeaserForm.id}(){
                    var donorSelectId="sectorTableSelect_"+${gisWidgetTeaserForm.id};
                    var donorSelect=document.getElementById(donorSelectId);
                    var selectedIndex=donorSelect.selectedIndex;
                    var options=donorSelect.options;
                    var donorId=options[selectedIndex].value;
                    var url="/widget/getSectorDonorAmounts.do?widgetId="+${gisWidgetTeaserForm.id}+"&donorId="+donorId;
                    var async=new Asynchronous();
                    async.complete=donorValueCallBack_${gisWidgetTeaserForm.id};
                    async.call(url);
                }

                function donorValueCallBack_${gisWidgetTeaserForm.id}(status, statusText, responseText, responseXML){
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
                renderSectorTable_${gisWidgetTeaserForm.id}();

        </script>

        <div id="sectorTableWidgetDiv_${gisWidgetTeaserForm.id}" style="display:none">

        </div>
        <div id="sectorTableWidgetDivLoad_${gisWidgetTeaserForm.id}">
            <img src='<digi:file src="images/amploading.gif"/>' alt="">
        </div>

</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==7}">
     <jsp:include page="/widget/showParisIndicatorTableWidget.do?widgetId=${gisWidgetTeaserForm.id}" flush="true"/>
</c:if>

    <c:if test="${gisWidgetTeaserForm.rendertype==8}">
        <div id="topTenDonorGroupsDiv">

        </div>
        <script language="JavaScript" type="text/javascript">
            donorWidget=true;
            function getTopTenDonorTable(){
                var topTenDonorTable=document.getElementById('topTenDonorGroupsDiv');
            <digi:context name="topDonorsRendererUrl" property="/widget/getTopTenDonorGroups.do" />
                         topTenDonorTable.innerHTML= '<digi:img src="images/amploading.gif"/>';
                         //from year
                         var fy=document.getElementsByName('selectedFromYear')[0].value;
                         //to year
                     var ty=document.getElementsByName('selectedToYear'    )[0].value;
                         var url='${topDonorsRendererUrl}?'+'selectedFromYear='+fy+'&selectedToYear='+ty;
                         var async=new Asynchronous();
                         async.complete=topTenDonorTableCallBack;
                         async.call(url);
                     }

                     function topTenDonorTableCallBack(status, statusText, responseText, responseXML){
                         var root=responseXML.getElementsByTagName('DonorGroups')[0];
                         var donorGroups=root.childNodes;
                         //get div that should hold top donor groups
                         var divTopTen=document.getElementById('topTenDonorGroupsDiv');
                         //create top ten table
                         var table=document.createElement('TABLE');
                         var tbody=document.createElement('TBODY');
                         var trHead=document.createElement('TR');
                         var tdHeadEmpty=document.createElement('TD');
                         tdHeadEmpty.innerHTML="&nbsp;"
                         var tdHeadDonorGroup=document.createElement('TD');
                         tdHeadDonorGroup.innerHTML="<strong><digi:trn>Top 10 Donors</digi:trn></strong>";
                         var tdHeadCom=document.createElement('TD');
                         tdHeadCom.innerHTML="<strong><digi:trn>Commitments in USD Millions for</digi:trn> "+root.getAttribute("years")+"</strong>";
                         trHead.appendChild(tdHeadEmpty);
                         trHead.appendChild(tdHeadDonorGroup);
                         trHead.appendChild(tdHeadCom);
                         tbody.appendChild(trHead);
                         for(var i=0;i<donorGroups.length;i++){
                             var tr=document.createElement('TR');
                             var tdOrder=document.createElement('TD');
                             tdOrder.innerHTML=donorGroups[i].getAttribute("order");
                             var tdName=document.createElement('TD');
                             tdName.innerHTML=donorGroups[i].getAttribute("name");
                             var tdAmount=document.createElement('TD');
                             tdAmount.innerHTML=donorGroups[i].getAttribute("amount");
                             tr.appendChild(tdOrder);
                             tr.appendChild(tdName);
                             tr.appendChild(tdAmount);
                             tbody.appendChild(tr);
                         }
                         table.appendChild(tbody);
                         table.id="topTenDonorGroupsWidgetTable";
                         divTopTen.innerHTML='';
                         divTopTen.appendChild(table);
                         applyStyle(table);
                     }
                     getTopTenDonorTable();
        </script>

    </c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==1}">
	<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.SHOW_WIDGET_PLACE_NAMES %>" compareWith="true" onTrueEvalBody="true">
		<digi:trn key="gis:widgetTeaser:emptyPlace">empty teaser: </digi:trn>&nbsp;${gisWidgetTeaserForm.placeName}
	</gs:test>
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==0}">
	<digi:trn key="gis:widgetTeaser:noParamSpecified">ERROR : no place param specified in layout definition for this teaser.</digi:trn>
</c:if>

