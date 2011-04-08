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
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<digi:instance property="gisWidgetTeaserForm" />
<c:if test="${gisWidgetTeaserForm.rendertype==5}">
    <c:choose>
        <c:when test="${gisWidgetTeaserForm.type==1}">
            <jsp:include page="/orgProfile/showOrgSummary.do" flush="true"/>
        </c:when>
        <c:when test="${gisWidgetTeaserForm.type==7}">
            <jsp:include page="/orgProfile/showParisIndicator.do" flush="true"/>
        </c:when>
        <c:otherwise>
            <DIV id="tabs">
                <UL>
                    <div  class="tab_graph_${gisWidgetTeaserForm.type}_selected">
                        <LI>
                            <a name="node">
                                <div>
                                    <digi:trn>Graph</digi:trn>
                                </div>
                            </a>
                        </LI>
                    </div>
                    <div  class="tab_graph_${gisWidgetTeaserForm.type}_unselected" style="display: none">
                        <LI>
                            <span>
                                <a href="javascript:hideDataSource_${gisWidgetTeaserForm.type}()">
                                    <div title='<digi:trn>Hide data source table and show chart</digi:trn>'>
                                        <digi:trn>Graph</digi:trn>
                                    </div>
                                </a>
                            </span>
                        </LI>
                    </div>

                    <div  class="tab_graph_${gisWidgetTeaserForm.type}_unselected" style="display: none">
                        <LI>
                            <a name="node">
                                <div>
                                    <digi:trn>Data Source</digi:trn>
                                </div>
                            </a>
                        </LI>
                    </div>

                    <div  class="tab_graph_${gisWidgetTeaserForm.type}_selected">
                        <LI>
                            <span>
                                <a  href="javascript:loadDataSource_${gisWidgetTeaserForm.type}()">
                                    <div title='<digi:trn>Hide Chart and Show data source table</digi:trn>'>
                                        <digi:trn>Data Source</digi:trn>
                                    </div>
                                </a>
                            </span>
                        </LI>
                    </div>
                </UL>
            </DIV>
            <c:set var="imageClass">
                nonSectorChart
            </c:set>
            <script language="javascript" type="text/javascript">
                function getGraphMap_${gisWidgetTeaserForm.type}(transactionType){
                    var lastTimeStamp = new Date().getTime();
                    var url="/widget/getWidgetMap.do?type="+${gisWidgetTeaserForm.type}+'&timestamp='+lastTimeStamp;
                    if (typeof transactionType != 'undefined') {
                        url+='&transactionType='+transactionType;
                    }
                    var async=new Asynchronous();
                    async.complete=mapCallBack_${gisWidgetTeaserForm.type};
                    async.call(url);
                }

                function mapCallBack_${gisWidgetTeaserForm.type}(status, statusText, responseText, responseXML){
                    var map=responseXML.getElementsByTagName('map')[0];
                    var id=map.getAttribute('id');
                    var mapSpanId="span_"+id;
                    var mapSpan= document.getElementById(mapSpanId);
                    mapSpan.innerHTML=responseText;
                   }
                function  loadDataSource_${gisWidgetTeaserForm.type}(){
                    $(".tab_graph_${gisWidgetTeaserForm.type}_unselected").each(function(index) {
                        this.style.display="block";
                    });

                    $(".tab_graph_${gisWidgetTeaserForm.type}_selected").each(function(index) {
                        this.style.display="none";
                    });
                    var postString		="type=${gisWidgetTeaserForm.type}&sectorClassConfigId="+configId;
                <digi:context name="url" property="context/orgProfile/showOrgProfileTables.do"/>
                        var url = "${url}";
                        YAHOO.util.Connect.asyncRequest("POST", url, callback_${gisWidgetTeaserForm.type}, postString);
                    }
                    function  hideDataSource_${gisWidgetTeaserForm.type}(){
                        if(${gisWidgetTeaserForm.type}==5){
                        $(".sectorChart").each(function(index) {
                            var lastTimeStamp = new Date().getTime();
                                    var newsrc=this.src;
                                    var index=newsrc.indexOf("&sectorClassConfigId=");
                                    if(index!=-1){
                                        newsrc=newsrc.substring(0, index)
                                    }

                                    var indexTimeStamp=newsrc.indexOf("&lastTimeStamp=");
                                    if(indexTimeStamp!=-1){
                                        newsrc=newsrc.substring(0, indexTimeStamp)
                                    }
                                    var newImage = new Image();
                                    newsrc+="&sectorClassConfigId="+configId+"&lastTimeStamp="+lastTimeStamp;
                                    newImage.src=newsrc;
                                    newImage.alt="<digi:trn>chart</digi:trn>";
                                    newImage.className="sectorChart";
                                    var map=this.useMap;

                                    newImage.border="0";
                                    newImage.onload=this.onload;
                                    var mapId=map.substring(1);
                                    var oldMap= document.getElementById(mapId);
                                    if(oldMap!=null){
                                        oldMap.parentNode.removeChild(oldMap);
                                    }
                                    newImage.useMap=map;
                                    var parent=this.parentNode;
                                    parent.appendChild(newImage);
                                    parent.removeChild(this);
                        });
                        }
                        $(".tab_graph_${gisWidgetTeaserForm.type}_selected").each(function(index) {
                            this.style.display="block";
                        });

                        $(".tab_graph_${gisWidgetTeaserForm.type}_unselected").each(function(index) {
                            this.style.display="none";
                        });

                        var div=document.getElementById("table_${gisWidgetTeaserForm.type}");
                        div.style.display="none";
                    }

                    var responseSuccess_${gisWidgetTeaserForm.type} = function(o){
                        var div=document.getElementById("table_${gisWidgetTeaserForm.type}");
                        var loadImg=document.getElementById("loadDataSource_${gisWidgetTeaserForm.type}_image");
                        var response = o.responseText;
                        div.innerHTML=response;
                        loadImg.style.display="none";
                        div.style.display="block";
                    }

                    var responseFailure_${gisWidgetTeaserForm.type}= function(o){
                        // Access the response object's properties in the
                        // same manner as listed in responseSuccess( ).
                        // Please see the Failure Case section and
                        // Communication Error sub-section for more details on the
                        // response object's properties.
                        //alert("Connection Failure!");
                    }
                    var callback_${gisWidgetTeaserForm.type} =
                        {
                        success:responseSuccess_${gisWidgetTeaserForm.type},
                        failure:responseFailure_${gisWidgetTeaserForm.type}
                    };
            </script>
            <c:if test="${gisWidgetTeaserForm.type!=5}">
                    <DIV class="contentboxEmpty">
                       &nbsp;
                    </DIV>
            </c:if>
            <c:if test="${gisWidgetTeaserForm.type==5}">
                <c:set var="imageClass">
                    sectorChart
                </c:set>
                <script language="javascript" type="text/javascript">
                    var configId;
                    function loadClassification(id){
                        configId=id;
                         $("LI[class$='_config_deselected']").css('display', 'block');
                         $("LI[class$='_config_selected']").css('display', 'none');
                         $("LI[class='"+id+"_config_selected']").css('display', 'block');
                         $("LI[class='"+id+"_config_deselected']").css('display',  'none');
                           
                        var graphVisible=$(".tab_graph_${gisWidgetTeaserForm.type}_selected:first").css('display');
                        if(graphVisible=="none"){
                    <digi:context name="url" property="context/orgProfile/showOrgProfileTables.do"/>
                                var url="${url}"
                                var postString="type=${gisWidgetTeaserForm.type}&sectorClassConfigId="+id;
                                YAHOO.util.Connect.asyncRequest("POST", url, callback_${gisWidgetTeaserForm.type}, postString);
                            }
                            else{
                            $(".sectorChart").each(function(index) {
                                    var lastTimeStamp = new Date().getTime();
                                    var newsrc=this.src;
                                    var index=newsrc.indexOf("&sectorClassConfigId=");
                                    if(index!=-1){
                                        newsrc=newsrc.substring(0, index)
                                    }
                                        
                                    var indexTimeStamp=newsrc.indexOf("&lastTimeStamp=");
                                    if(indexTimeStamp!=-1){
                                        newsrc=newsrc.substring(0, indexTimeStamp)
                                    }
                                    var newImage = new Image();
                                    newsrc+="&sectorClassConfigId="+configId+"&lastTimeStamp="+lastTimeStamp;
                                    newImage.src=newsrc;
                                    newImage.alt="<digi:trn>chart</digi:trn>";
                                    newImage.className="sectorChart";
                                    var map=this.useMap;
                                    
                                    newImage.border="0";
                                    newImage.onload=this.onload;
                                    var mapId=map.substring(1);
                                    var oldMap= document.getElementById(mapId);
                                    if(oldMap!=null){
                                        oldMap.parentNode.removeChild(oldMap);
                                    }
                                    newImage.useMap=map;
                                    var parent=this.parentNode;
                                    parent.appendChild(newImage);
                                    parent.removeChild(this);
                                                                   
                                });
                            }


                        }
                </script>
                <div id="main" style="width:610px">
                    <DIV id="subtabs">
                        <div id="pa">
                            <UL>
                                <c:forEach var="config" items="${gisWidgetTeaserForm.sectorClassificationConfigs}" varStatus="status">
                                    <c:if test="${config.name=='Primary'}">
                                        <field:display name="Primary Sector" feature="Sectors">
                                            <LI class="${config.id}_config_selected">
                                                <span>
                                                    ${config.classification.secSchemeName}
                                                </span>
                                                &nbsp;&nbsp;|
                                            </LI>
                                            <LI class="${config.id}_config_deselected" style="display: none">
                                                <div>
                                                    <span>
                                                        <a href="javascript:loadClassification('${config.id}')" >
                                                            ${config.classification.secSchemeName}
                                                        </a>

                                                    </span>&nbsp;&nbsp;|
                                                </div>
                                            </LI>
                                        </field:display>
                                    </c:if>
                                    <c:if test="${config.name=='Secondary'}">
                                        <field:display name="Secondary Sector" feature="Sectors">
                                            <LI class="${config.id}_config_selected" style="display: none">
                                                <span>
                                                    ${config.classification.secSchemeName}
                                                </span>
                                                 &nbsp;&nbsp;|
                                            </LI>
                                            <LI class="${config.id}_config_deselected">
                                                <div>
                                                    <span>
                                                        <a href="javascript:loadClassification('${config.id}')" >
                                                            ${config.classification.secSchemeName}
                                                        </a>
                                                    </span>
                                                    &nbsp;&nbsp;|
                                                </div>
                                            </LI>
                                        </field:display>

                                    </c:if>
                                       <c:if test="${config.name=='Tertiary'}">
                                        <field:display name="Tertiary Sector" feature="Sectors">
                                            <LI class="${config.id}_config_selected" style="display: none">
                                                <span>
                                                    ${config.classification.secSchemeName}
                                                </span>
                                            </LI>
                                            <LI class="${config.id}_config_deselected">
                                                <div>
                                                    <span>
                                                        <a href="javascript:loadClassification('${config.id}')" >
                                                            ${config.classification.secSchemeName}
                                                        </a>
                                                    </span>
                                                </div>
                                            </LI>
                                        </field:display>

                                    </c:if>
                                </c:forEach>
                            </UL>
                            &nbsp;
                        </div>
                    </DIV>
                </div>


            </c:if>
            <div id="table_${gisWidgetTeaserForm.type}"  class="noTopBorder contentbox_border chartPlaceCss" style="display: none;"></div>
            <div id="loadDataSource_${gisWidgetTeaserForm.type}_image" class="noTopBorder contentbox_border chartPlaceCss tab_graph_${gisWidgetTeaserForm.type}_unselected" style="display: none" >
                <img src="images/amploading.gif" alt=""  />
            </div>
            <div  class="noTopBorder contentbox_border chartPlaceCss tab_graph_${gisWidgetTeaserForm.type}_selected ">
                <c:choose>
                    <c:when test="${sessionScope.orgProfileFilter.transactionType==2&&gisWidgetTeaserForm.type!=3}">
                        <img class="${imageClass}" alt="chart" src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}~chartType=${gisWidgetTeaserForm.type}~imageHeight=350~imageWidth=580~transactionType=0~lastTimeStamp=${gisWidgetTeaserForm.time}" usemap="#chartMap${gisWidgetTeaserForm.type}_0" border="0" onload="getGraphMap_${gisWidgetTeaserForm.type}(0)"/>
                        <span id="span_chartMap${gisWidgetTeaserForm.type}_0">
                        </span>
                        <br/>
                        <img class="${imageClass}" alt="chart" src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}~chartType=${gisWidgetTeaserForm.type}~imageHeight=350~imageWidth=580~transactionType=1~lastTimeStamp=${gisWidgetTeaserForm.time}" usemap="#chartMap${gisWidgetTeaserForm.type}_1" border="0" onload="getGraphMap_${gisWidgetTeaserForm.type}(1)"/>
                        <span id="span_chartMap${gisWidgetTeaserForm.type}_1">
                        </span>
                    </c:when>
                    <c:otherwise>
                        <img  class="${imageClass}" alt="<digi:trn>chart</digi:trn>" src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}~chartType=${gisWidgetTeaserForm.type}~imageHeight=350~imageWidth=580~lastTimeStamp=${gisWidgetTeaserForm.time}" usemap="#chartMap${gisWidgetTeaserForm.type}" border="0" onload="getGraphMap_${gisWidgetTeaserForm.type}()"/>
                        <span id="span_chartMap${gisWidgetTeaserForm.type}">

                        </span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:otherwise>
    </c:choose>

</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==4}">
    <img src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}">
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==3}">

    <script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

    <div id="tableWidgetContainer_${gisWidgetTeaserForm.id}">

    </div>


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
                myDiv.innerHTML = htmlResponce;
            }
	
            function tableWidgetFilterChanged_${gisWidgetTeaserForm.id}(columnId){
                var myDiv = document.getElementById('tableWidgetContainer_${gisWidgetTeaserForm.id}');
                var selItem = document.getElementsByName('selectedFilterItemId_${gisWidgetTeaserForm.id}')[0];
                var itemId = selItem.value;
                myDiv.innerHTML = 'loading...';
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
                    table.id="sectorTableWidget${gisWidgetTeaserForm.id}";
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
            <img src='<digi:file src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif"/>' alt="">
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
                         topTenDonorTable.innerHTML= '<digi:img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif"/>';
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
                         tdHeadCom.innerHTML="<strong><digi:trn>Commitments in ${gisWidgetTeaserForm.baseCurr} Millions for</digi:trn> "+root.getAttribute("years")+"</strong>";
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
    <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.SHOW_WIDGET_PLACE_NAMES%>" compareWith="true" onTrueEvalBody="true">
        <digi:trn key="gis:widgetTeaser:emptyPlace">empty teaser: </digi:trn>&nbsp;${gisWidgetTeaserForm.placeName}
    </gs:test>
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==0}">
    <digi:trn key="gis:widgetTeaser:noParamSpecified">ERROR : no place param specified in layout definition for this teaser.</digi:trn>
</c:if>
