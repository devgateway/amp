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
<script language="javascript" type="text/javascript">
 
   
    function getGraphMap(type){
        var url="/widget/getWidgetMap.do?type="+type;
        var async=new Asynchronous();
        async.complete=mapCallBack;
        async.call(url);
    }
    
    function mapCallBack(status, statusText, responseText, responseXML){
        var map=responseXML.getElementsByTagName('map')[0];
	var id=map.getAttribute('id');
        var mapSpan= document.getElementById(id);
        mapSpan.innerHTML=responseText;  
    }


</script>

<c:if test="${gisWidgetTeaserForm.rendertype==5}">
<feature:display name="orgprof_chart_place${gisWidgetTeaserForm.type}" module="Org Profile">
    <c:choose>
        <c:when test="${gisWidgetTeaserForm.type==1}">
            <c:set var="organization" scope="request" value="${sessionScope.orgProfileFilter.organization}"/>
            <c:set var="orgGroup" scope="request" value="${sessionScope.orgProfileFilter.orgGroup}"/>
            <jsp:include page="orgSummary.jsp" flush="true"/>
        </c:when>
           <c:when test="${gisWidgetTeaserForm.type==7}">
            <jsp:include page="/orgProfile/showParisIndicator.do" flush="true"/>
        </c:when>
        <c:otherwise>
            <img  alt="chart" src="/widget/widgetchart.do~widgetId=${gisWidgetTeaserForm.id}~chartType=${gisWidgetTeaserForm.type}~imageHeight=420~imageWidth=400" usemap="#chartMap${gisWidgetTeaserForm.type}" border="0" onload="getGraphMap(${gisWidgetTeaserForm.type})"/>
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
     <jsp:include page="/widget/showSectorTable.do?widgetId=${gisWidgetTeaserForm.id}" flush="true"/>
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==7}">
     <jsp:include page="/widget/showParisIndicatorTableWidget.do?widgetId=${gisWidgetTeaserForm.id}" flush="true"/>
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==1}">
	<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.SHOW_WIDGET_PLACE_NAMES %>" compareWith="true" onTrueEvalBody="true">
		<digi:trn key="gis:widgetTeaser:emptyPlace">empty teaser: </digi:trn>&nbsp;${gisWidgetTeaserForm.placeName}
	</gs:test>
</c:if>

<c:if test="${gisWidgetTeaserForm.rendertype==0}">
	<digi:trn key="gis:widgetTeaser:noParamSpecified">ERROR : no place param specified in layout definition for this teaser.</digi:trn>
</c:if>

