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
                    var mapSpan= document.getElementById(id);
                    mapSpan.innerHTML=responseText;
                }
                function  loadDataSource_${gisWidgetTeaserForm.type}(){
                    $(".tab_graph_${gisWidgetTeaserForm.type}_unselected").each(function(index) {
                        this.style.display="block";
                    });

                    $(".tab_graph_${gisWidgetTeaserForm.type}_selected").each(function(index) {
                        this.style.display="none";
                    });               
                    var postString		="type=${gisWidgetTeaserForm.type}";
                <digi:context name="url" property="context/orgProfile/showOrgProfileTables.do"/>
                        var url = "${url}";
                        YAHOO.util.Connect.asyncRequest("POST", url, callback_${gisWidgetTeaserForm.type}, postString);
                    }
                    function  hideDataSource_${gisWidgetTeaserForm.type}(){
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
            <div id="table_${gisWidgetTeaserForm.type}"  class="contentbox_border chartPlaceCss" style="display: none;"></div>
            <div id="loadDataSource_${gisWidgetTeaserForm.type}_image" class="contentbox_border chartPlaceCss tab_graph_${gisWidgetTeaserForm.type}_unselected" style="display: none" >
                    <img src="images/amploading.gif" alt=""  />
            </div>
            <div  class="contentbox_border chartPlaceCss tab_graph_${gisWidgetTeaserForm.type}_selected ">
               
                    <c:choose>
                        <c:when test="${sessionScope.orgProfileFilter.transactionType==2&&gisWidgetTeaserForm.type!=3}">
                            <img  alt="chart" src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}~chartType=${gisWidgetTeaserForm.type}~imageHeight=350~imageWidth=450~transactionType=0" usemap="#chartMap${gisWidgetTeaserForm.type}_0" border="0" onload="getGraphMap_${gisWidgetTeaserForm.type}(0)"/>
                            <span id="chartMap${gisWidgetTeaserForm.type}_0">
                            </span>
                            <br/>
                            <img  alt="chart" src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}~chartType=${gisWidgetTeaserForm.type}~imageHeight=350~imageWidth=450~transactionType=1" usemap="#chartMap${gisWidgetTeaserForm.type}_1" border="0" onload="getGraphMap_${gisWidgetTeaserForm.type}(1)"/>
                            <span id="chartMap${gisWidgetTeaserForm.type}_1">
                            </span>
                        </c:when>
                        <c:otherwise>
                            <img  alt="<digi:trn>chart</digi:trn>" src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}~chartType=${gisWidgetTeaserForm.type}~imageHeight=350~imageWidth=450" usemap="#chartMap${gisWidgetTeaserForm.type}" border="0" onload="getGraphMap_${gisWidgetTeaserForm.type}()"/>
                            <span id="chartMap${gisWidgetTeaserForm.type}">

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

<c:if test="${gisWidgetTeaserForm.rendertype==1}">
    <gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.SHOW_WIDGET_PLACE_NAMES%>" compareWith="true" onTrueEvalBody="true">
        <digi:trn key="gis:widgetTeaser:emptyPlace">empty teaser: </digi:trn>&nbsp;${gisWidgetTeaserForm.placeName}
    </gs:test>
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==0}">
    <digi:trn key="gis:widgetTeaser:noParamSpecified">ERROR : no place param specified in layout definition for this teaser.</digi:trn>
</c:if>
