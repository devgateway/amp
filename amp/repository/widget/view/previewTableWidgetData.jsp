<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance id="dform" property="gisTableWidgetDataForm"/>


	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
	<style>
		.tableContainer table tr {
		  background-color: #DBE5F1;
		  color: #OOO;
		}
		.tableContainer table {
		 border-color: #FFF;
		}
		.tableContainer table td {
		 border: 1px solid #FFF;
		}
		.tableContainer table tr:hover {
		background-color: #222E5D;
		color: #FFF;
		}
	
	</style>
	<br/>
			<span class=crumb>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
              <digi:link href="/adminTableWidgets.do" styleClass="comment">
                <digi:trn key="admin:Navigation:WidgetList">Table Widgets</digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:link href="/tableWidgetData.do" styleClass="comment">
              <digi:trn key="admin:Navigation:editWidgetData">table widget data</digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <digi:trn >Preview table widget data</digi:trn>
			</span>
			<br/>
			<br/>
			<br/>
			<span class="subtitle-blue">Preview table widget data</span>
	<div id="tableWidgetContainer_${dform.widgetId}" style="margin-left:20px; margin-top: 20px;" class="tableContainer">
		
	</div>
	
	
	<br/>
	<input type="button" value="Back" onclick="javascript:location.href='/widget/tableWidgetData.do~actType=showEdit~widgetId=${dform.widgetId}'"/>
	
         



	<script language="JavaScript">
	
		function requestTable_${dform.widgetId}(columnId,itemId){
			<digi:context name="tableRendererUrl" property="/widget/getTableWidget.do" />
			var url = '${tableRendererUrl}~tableId=${dform.widgetId}~preview=true';
			if (columnId!=null && itemId!=null){
				url+='~columnId='+columnId+'~itemId='+itemId;
			}
			var async=new Asynchronous();
			async.complete=tableCallBack_${dform.widgetId};
			async.call(url);
		}
	
		function tableCallBack_${dform.widgetId}(status, statusText, responseText, responseXML){
			processTableResponce_${dform.widgetId}(responseText);
			applyStyle(document.getElementById("tableWidget${dform.widgetId}"));
		}
	
		function processTableResponce_${dform.widgetId}(htmlResponce){
			var myDiv = document.getElementById('tableWidgetContainer_${dform.widgetId}');
			myDiv.innerHTML = htmlResponce;		
		}
	
		function tableWidgetFilterChanged_${dform.widgetId}(columnId){
			var myDiv = document.getElementById('tableWidgetContainer_${dform.widgetId}');
			var selItem = document.getElementsByName('selectedFilterItemId_${dform.widgetId}')[0];
			var itemId = selItem.value;
			myDiv.innerHTML = 'loading...';
			requestTable_${dform.widgetId}(columnId,itemId);
		}
		
		requestTable_${dform.widgetId}();
	
	</script>
