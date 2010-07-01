function DataSourceBuilder(dataSourceUrl, columnDefs, responseSchema) {
	this.dataSource 				= new YAHOOAmp.util.DataSource(dataSourceUrl);
	this.dataSource.connMethodPost 	= true;
	this.dataSource.responseType 	= YAHOOAmp.util.DataSource.TYPE_XML;
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
				paginator: new YAHOOAmp.widget.Paginator(
						{rowsPerPage:20}
						)
		} ;
	}
	else {
		this.dtConfig	= configObj;
	}
}

DataSourceBuilder.prototype.createDataTable	= function (divId) {
	this.dataTable	= new YAHOOAmp.widget.DataTable(divId, this.columnDefs, this.dataSource );
	return this.dataTable;
};

SampleConfig = {
			columnDefs: [
                       	   {key:"Name"},
                       	{key:"Age", sortable: true, formatter: YAHOOAmp.widget.DataTable.formatNumber},
                       	{key:"Test"},
                       	   ],

			responseSchema: {
						resultNode: "Result",
						fields: ["Name", {key:"Age" ,parser:YAHOOAmp.util.DataSource.parseNumber}, "Test"]
					}

}