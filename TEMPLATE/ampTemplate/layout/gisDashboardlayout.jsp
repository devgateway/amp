<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<script type="text/javascript">
    <!--
    var lastTimeStamp;

    function donorChanged(){
        rechart();
    }
    function yearChanged(){
        rechart();
        getTopTenDonorTable();
    }
    function rechart(){
        lastTimeStamp = new Date().getTime();
        var sectorByDonorChartImageDiv=document.getElementById('sectorByDonorChartImageDiv');
        var sectorByDonorChartImageLoadDiv=document.getElementById('sectorByDonorChartImageDivLoad');
        sectorByDonorChartImageLoadDiv.style.display="block";
        sectorByDonorChartImageDiv.style.display="none";
        var chartImageMap=document.getElementById('sectorByDonorChartImageMap');
        removeAllChildren(chartImageMap);
        var chartImage=document.createElement("img");
        //from year
        var fy=document.getElementsByName('selectedFromYear')[0].value;
        //to year
        var ty=document.getElementsByName('selectedToYear')[0].value;
        var d=document.getElementsByName('selectedDonor')[0].value;
        var myUrl = chartURL+'~selectedFromYear='+fy+'~selectedToYear='+ty+'~selectedDonor='+d+'~timestamp='+lastTimeStamp;
        myUrl+=getLegendState();
        myUrl+=getLabelState();
        chartImage.onload=loadSectorDonorMap;
        chartImage.useMap = '#sectorByDonorChartImageMap';
        chartImage.border=0;
        chartImage.src=myUrl;
        chartImage.setAttribute("alt", " ")
        removeAllChildren(sectorByDonorChartImageDiv);
        sectorByDonorChartImageDiv.appendChild(chartImage);
        //alert(myUrl);
    }
    


function loadSectorDonorMap(){
    getSectorDonorGraphMap(lastTimeStamp);
}

function constructSectorDonorMapUrl(timestmp){
    <digi:context name="donorSectorMap" property="context/widget/getDonorSectorMap.do" />
    var url="${donorSectorMap}";
    url+='~timestamp='+timestmp;
    return url;
}

function getSectorDonorGraphMap(timestamp){
    var url=constructSectorDonorMapUrl(timestamp);
    var async=new Asynchronous();
    async.complete=updateSectorDonorMapCallBack;
    async.call(url);
}
function removeAllChildren(parent){
        var childrenAreas=parent.childNodes;
        if(childrenAreas!=null){
            while (parent.firstChild) {
                parent.removeChild(parent.firstChild);
            }
        }
    }

    function updateSectorDonorMapCallBack(status, statusText, responseText, responseXML){
        var sectorByDonorChartImageLoadDiv=document.getElementById('sectorByDonorChartImageDivLoad');
        sectorByDonorChartImageLoadDiv.style.display="none"
        var sectorByDonorChartImageDiv=document.getElementById('sectorByDonorChartImageDiv');
       sectorByDonorChartImageDiv.style.display="block";
        var chartImageMap=document.getElementById('sectorByDonorChartImageMap');
        removeAllChildren(chartImageMap);
        var areas=responseXML.getElementsByTagName('area');
        for(var i=0;i<areas.length;i++){
            var area=document.createElement('area');
            area.setAttribute("alt", " ");
            area.setAttribute("shape", "poly");
            area.setAttribute("coords", areas[i].getAttribute("coords"));
            var url= areas[i].getAttribute("href");
            area.onclick = (function(par) {
                return function(){showSectorDonorWidgetReport(par) }
            })(url);
            chartImageMap.appendChild(area);
        }
     

    }


	function showSectorDonorWidgetReport(url) {
		var popup = window.open(url, "SectorDonorWidgetReport", "height=500,width=750,status=yes,resizable=yes,toolbar=no,menubar=no,location=no");
		popup.focus();
	}

    function blinkText(){
        var textElement=document.getElementById('mapLoadingMessageSecDonChart');
        textElement.style.visibility=(textElement.style.visibility=='visible') ?'hidden':'visible';
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

    function getTopTenDonorTable(){
        var topTenDonorTable=document.getElementById('topTenDonorGroupsDiv');
        topTenDonorTable.innerHTML= '<digi:img src="images/amploading.gif"/>';
        //from year
        var fy=document.getElementsByName('selectedFromYear')[0].value;
        //to year
        var ty=document.getElementsByName('selectedToYear')[0].value;
        var url='/widget/getTopTenDonorGroups.do?'+'selectedFromYear='+fy+'&selectedToYear='+ty;
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
    //-->
</script>


<style>
    #content{
        height: 100%;
    }
    #demo{
        height: 100%;
    }
    #div1{
        height: 96%;
    }
</style>

<%
        String countryName = "";
        String ISO = null;
        java.util.Iterator itr1 = org.digijava.module.aim.util.FeaturesUtil.getDefaultCountryISO().iterator();
        while (itr1.hasNext()) {
            org.digijava.module.aim.dbentity.AmpGlobalSettings ampG = (org.digijava.module.aim.dbentity.AmpGlobalSettings) itr1.next();
            ISO = ampG.getGlobalSettingsValue();
        }

        if (ISO != null && !ISO.equals("")) {
            org.digijava.kernel.dbentity.Country cntry = org.digijava.module.aim.util.DbUtil.getDgCountry(ISO);
            countryName = " " + cntry.getCountryName();
        } else {
            countryName = "";
        }
%>


<HTML>
    <digi:base />
    <digi:context name="digiContext" property="context"/>
    <HEAD>
        <TITLE><digi:trn key="gis:resultsmatrix">Results Matrix: </digi:trn><%=countryName%></TITLE>
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="EXPIRES" CONTENT="0">
        <digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
        <script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
        <digi:ref href="css/gis.css" type="text/css" rel="stylesheet" />
    </HEAD>

    <BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
        <TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=0 valign="top" align="left">
            <TBODY>
                <TR>
                    <TD>
                        <jsp:include page="/TEMPLATE/ampTemplate/layout/gisReportToolbar.jsp" />
                    </TD>
                </TR>
                <TR>
                    <TD width="100%" vAlign="top" align="left">
                        <jsp:include page="/TEMPLATE/ampTemplate/layout/gisScripts.jsp" />
                        <h2 style="padding-left:10px;font-size:15pt;"><digi:trn key="gis:resultsmatrix">Results Matrix: </digi:trn><%=countryName%></h2>
                        <div>
                        <div style="width: 600px; float:left">
                            <digi:insert attribute="body" />
                        </div>
                        <div  style="margin-left:610px;">
                          <div style="float:left;width: 600px;">
                              <digi:insert attribute="pieChart"/>
                              <br>
                         </div>
                          <div id="content" class="yui-skin-sam" style="float:left">
                                <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                    <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
                                    <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
                                    <div class="longTab">
                                        <digi:trn key="gis:resourcesatglance">Resources at a glance</digi:trn>
                                    </div>
                                    <div class="yui-content" style="font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                        <table cellpadding="5" cellspacing="0">
                                            <tr valign="top">
                                                <td>
                                                    <digi:insert attribute="widget7">
                                                        <digi:put name="widget-teaser-param">atGlanceTable_Place1</digi:put>
                                                    </digi:insert>
                                                </td>
                                                <td rowspan="2">
                                                    <digi:insert attribute="widget8">
                                                        <digi:put name="widget-teaser-param">atGlanceTable_Place3</digi:put>
                                                    </digi:insert>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <digi:insert attribute="widget9">
                                                        <digi:put name="widget-teaser-param">atGalnceTable_Place2</digi:put>
                                                    </digi:insert>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <digi:trn key="widget:SourceOECD">Source: OECD </digi:trn>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>          
                        <div style="clear:both; font-size:10px;">&nbsp;</div>
                        <div id="content" class="yui-skin-sam" style="width:60%;float:left">
                            <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
                                <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
                                <div class="longTab">
                                    <digi:trn key="gis:millenniumdevelopmentgoals">Millennium Development Goals</digi:trn>
                                </div>

                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                    <table cellpadding="5" border="0" width="100%">
                                        <tr>
                                            <td>
                                                <digi:insert attribute="widget1">
                                                    <digi:put name="widget-teaser-param">chart_place1</digi:put>
                                                </digi:insert>
                                            </td>
                                            <td>
                                                <digi:insert attribute="widget2">
                                                    <digi:put name="widget-teaser-param">chart_place2</digi:put>
                                                </digi:insert>
                                            </td>
                                            <td>
                                                <digi:insert attribute="widget3">
                                                    <digi:put name="widget-teaser-param">chart_place3</digi:put>
                                                </digi:insert>

                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <digi:insert attribute="widget4">
                                                    <digi:put name="widget-teaser-param">chart_place4</digi:put>
                                                </digi:insert>

                                            </td>
                                            <td>
                                                <digi:insert attribute="widget5">
                                                    <digi:put name="widget-teaser-param">chart_place5</digi:put>
                                                </digi:insert>

                                            </td>
                                            <td>
                                                <digi:insert attribute="widget6">
                                                    <digi:put name="widget-teaser-param">chart_place6</digi:put>
                                                </digi:insert>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" width="100%">
                                                <digi:trn key="gis:sixChartLegend:desctiption">&nbsp;</digi:trn>
                                            </td>
                                            <td align="right">
                                                <digi:img src="images/legend1.jpg"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="3">
                                                <digi:trn key="widget:SourceOfficialgovernmentsources">Source: Official government sources </digi:trn>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div style="clear:both; font-size:10px;">&nbsp;</div>
                        <div style="display:block; clear:both; font-size:1px;"></div>
                        <div id="content" class="yui-skin-sam" style="width:80%;float:left">
                            <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
                                <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
                                <div class="longTab">
                                    <digi:trn key="gis:aideffectivenessindicators">Aid Effectiveness Process Indicators</digi:trn>
                                </div>

                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

                                    <table>
                                        <tr>
                                            <td valign="top">
                                                <digi:insert attribute="widget10">
                                                    <digi:put name="widget-teaser-param">table_place1</digi:put>
                                                </digi:insert>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <digi:insert attribute="widget11">
                                                    <digi:put name="widget-teaser-param">table_place2</digi:put>
                                                </digi:insert>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <br><br>
                                                <digi:insert attribute="widget12">
                                                    <digi:put name="widget-teaser-param">table_place3</digi:put>
                                                </digi:insert>
                                            </td>
                                            <td>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <digi:trn key="widget:SourceParisDeclaration">Source: 2006 Paris Declaration Survey</digi:trn>
                                            </td>
                                        </tr>
                                    </table>

                                </div>
                            </div>
                        </div>
                        <div style="clear:both; font-size:10px;">&nbsp;</div>
                        <div id="content" class="yui-skin-sam" style="width:80%;float:left">
                            <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
                                <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
                                <div class="longTab">
                                    <digi:trn key="gis:outputindicators">Output Indicators</digi:trn>
                                </div>

                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                    <table>
                                        <tr>
                                            <td>
                                                <digi:insert attribute="widget13">
                                                    <digi:put name="widget-teaser-param">table_place4</digi:put>
                                                </digi:insert>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <digi:trn key="widget:SourceOfficialgovernmentsources">Source: Official government sources </digi:trn>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                         <div style="clear:both; font-size:10px;">&nbsp;</div>
                        <div id="content" class="yui-skin-sam" style="width:80%;float:left">
                            <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
                                <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
                                <div class="longTab">
                                    <digi:trn key="gis:totalresources">Total resources</digi:trn>
                                </div>

                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                    <table>
                                        <tr>
                                            <td valign="top">
                                                <digi:insert attribute="widget15">
                                                    <digi:put name="widget-teaser-param">table_place6</digi:put>
                                                </digi:insert>
                                                <br>
                                                <digi:trn key="widget:sourceministryoffinance">Source: Ministry of Finance</digi:trn><br>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                         <div style="clear:both; font-size:10px;">&nbsp;</div>
                        <div id="content" class="yui-skin-sam" style="width:80%;float:left">
                            <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                                <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
                                <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
                                <div class="longTab">
                                    <digi:trn key="gis:externalaidresources">External Aid Resources</digi:trn>
                                </div>

                                <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
                                    <table>
                                        <tr>
                                            <td valign="top">
                                                <digi:insert attribute="widget14">
                                                    <digi:put name="widget-teaser-param">table_place5</digi:put>
                                                </digi:insert>
                                                <br>
                                                <digi:trn key="widget:sourceAMPdatabase">Source: AMP database</digi:trn><br>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                        </div>
                         <div style="clear:both; font-size:10px;">&nbsp;</div>
                        <digi:insert attribute="widget16">
                            <digi:put name="widget-teaser-param">additional_table</digi:put>
                        </digi:insert>
                    </TD>
                </TR>
            </TBODY>
        </TABLE>

    </BODY>
</HTML>

