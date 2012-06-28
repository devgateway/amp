function DataSourceBuilder(dataSourceUrl, columnDefs, responseSchema) {
	this.dataSource 				= new YAHOO.util.DataSource(dataSourceUrl);
	this.dataSource.connMethodPost 	= true;
	this.dataSource.responseType 	= YAHOO.util.DataSource.TYPE_XML;
	this.dataSource.responseSchema	= responseSchema;
	this.columnDefs					= columnDefs;
	this.dtConfig					= null;
	
};

DataSourceBuilder.prototype.getDataSource	= function () {
	return this.dataSource;
}

DataSourceBuilder.prototype.createConfig	= function (configObj) {
	if ( configObj == null ) {
		this.dtConfig	= {
				paginator: new YAHOO.widget.Paginator(
						{rowsPerPage:20}
						)
		} ;
	}
	else {
		this.dtConfig	= configObj;
	}
}

DataSourceBuilder.prototype.createDataTable	= function (divId) {
	this.dataTable	= new YAHOO.widget.DataTable(divId, this.columnDefs, this.dataSource,{formatRow:function(){}} );
	return this.dataTable;
};

DataSourceBuilder.prototype.getValueOfFirstColumn	= function () {
	var dt 			= 	this.dataTable;
	var selTrEls	= dt.getSelectedTrEls();
	if ( selTrEls != null && selTrEls.length > 0  ) {
		var trEl 	= (dt.getSelectedTrEls()[0]) ;
		var id		= trEl.getElementsByTagName("td")[0].getElementsByTagName("div")[0].innerHTML;
		return id;
	}
	return null;
}

SampleConfig = {
			columnDefs: [
                       	   {key:"Name"},
                       	{key:"Age", sortable: true, formatter: YAHOO.widget.DataTable.formatNumber},
                       	{key:"Test"},
                       	   ],

			responseSchema: {
						resultNode: "Result",
						fields: ["Name", {key:"Age" ,parser:YAHOO.util.DataSource.parseNumber}, "Test"]
					}

}



function MyPanelWrapper(header, url) {
	var panel	= MyPanelWrapper.panels[url];
	if (panel == null) {
		panel 		= new YAHOO.widget.Panel("MyPanel"+url, { width:"400px", visible:true, draggable:true, close:true } );
		panel.setHeader('<digi:trn>'+header+'</digi:trn>');
		panel.setBody("<div style='text-align: center;'><img src='/TEMPLATE/ampTemplate/images/ajax-loader.gif' /></div>");
		panel.render(document.body);
		panel.center();
		
		MyPanelWrapper.panels[url] = panel;
		YAHOO.util.Connect.asyncRequest('GET', url, this.getCallback(this) );
	}
	this.panel		= panel;
	this.url		= url;
	panel.show();
}

MyPanelWrapper.panels	= new Object();

MyPanelWrapper.prototype.hide 	= function () {
	this.panel.hide();
}

MyPanelWrapper.prototype.getCallback 	= function (panelWrapper) {
	var callbackObj	= {
			success: function (o) {
				panelWrapper.panel.setBody( o.responseText );
			},
			failure: function (o) {
				MyPanelWrapper.panels[panelWrapper.url]	= null;
				panelWrapper.panel.setBody("<div align='center'><font color='red'>We are sorry but your request cannot be processed at this time</font></div>");
			}
		}

	return callbackObj;
	
}


