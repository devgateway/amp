var Plugin = Backbone.Model.extend({
// urlRoot: Settings.REST_URL+'info'
});

var PluginCollection = Backbone.Collection.extend({
	model : Plugin,
	url : function() {
		Saiku.logger.log("PluginCollection.url");
		return "/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/saikuui_reports/mockData/plugins.json";
	}
});