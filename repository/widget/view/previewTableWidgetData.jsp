<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance id="dform" property="gisTableWidgetDataForm"/>
<digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
<digi:ref href="css/gis.css" type="text/css" rel="stylesheet" />

	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
	

	
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
	
	
	<div id="content" class="yui-skin-sam" style="width:90%;padding: 20px;">
	 <div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
	   <digi:img src="images/tabrightcorner.gif" align="right" hspace="0"/>
	   <digi:img src="images/tableftcorner.gif" align="left" hspace="0"/>
	   <div class="longTab">
	      ${dform.tableName}
	   </div>
	   <div class="yui-content" style="font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
	    <div id="tableWidgetContainer_${dform.widgetId}" style="margin-left:20px; margin-top: 20px;" class="tableContainer">
		  
	   </div>
	  </div>
	</div>
	</div>
	<br/>
	<input type="button" value="Back" onclick="javascript:location.href='/widget/tableWidgetData.do~actType=showEdit~widgetId=${dform.widgetId}'"/>
	
         



	<script language="JavaScript">

	function applyStyle(table){
		table.className += " tableElement";
		setStripsTable(table.id, "tableEven", "tableOdd");
		setHoveredTable(table.id, true);

	}
	function setStripsTable(tableId, classOdd, classEven) {
		var tableElement = document.getElementById(tableId);
		if(tableElement)
		{
			tableElement.setAttribute("border","0");
			tableElement.setAttribute("cellPadding","2");
			tableElement.setAttribute("cellSpacing","2");
			rows = tableElement.getElementsByTagName('tr');
			for(var i = 0, n = rows.length; i < n; ++i) {
				if(i%2 == 0)
					rows[i].className = classEven;
				else
					rows[i].className = classOdd;
			}
			rows = null;
		}
	}
	function setHoveredTable(tableId, hasHeaders) {
		var tableElement = document.getElementById(tableId);
		if(tableElement){
		    var className = 'Hovered',
	        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
	        rows      = tableElement.getElementsByTagName('tr');

			var i = 0;
			if(hasHeaders){
				rows[0].className += " tableHeader";
				i = 1;
				
			}
		
			for(i, n = rows.length; i < n; ++i) {
				rows[i].onmouseover = function() {
					this.className += ' ' + className;
				};
				rows[i].onmouseout = function() {
					this.className = this.className.replace(pattern, ' ');

				};
			}
			rows = null;
		}
	
	}
		
	
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
