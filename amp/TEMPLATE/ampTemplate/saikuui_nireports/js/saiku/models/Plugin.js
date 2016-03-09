var Plugin = Backbone.Model.extend({
// urlRoot: Settings.REST_URL+'info'
});

var PluginCollection = Backbone.Collection.extend({
	model : Plugin,
	url : function() {
		return "/TEMPLATE/ampTemplate/saikuui_nireports/mockData/plugins.json";
	}
});