require.config({
	'waitSeconds' : 0,
	baseUrl : '/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/tabs/js',
	paths : {
		backbone : 'lib/backbone.marionette/backbone',
		underscore : 'lib/backbone.marionette/underscore',
		jquery : 'lib/jquery_1.10.2',
		jqueryui : 'lib/jquery-ui.min_1.11.0',
		jqueryuii18n : '/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/script/common/lib/jquery-ui-i18n.min',
		marionette : 'lib/backbone.marionette/backbone.marionette.min',
		text : 'lib/text_2.0.12',
		localStorage : 'lib/Backbone.localStorage-master/backbone.localStorage-min',
		documentModel : 'lib/backbone-documentmodel-master/backbone-documentmodel',
		documentCollection : 'lib/backbone-documentmodel-master/backbone-documentmodel',
		/*
		 * jqgrid_lang : 'lib/jqgrid-4.6.0/js2/i18n/grid.locale-en', jqgrid :
		 * 'lib/jqgrid-4.6.0/js2/jquery.jqGrid.src',
		 */
		jqgrid : 'lib/one_place/jqgrid-all',		
		translationManager: '/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/script/common/TranslationManager',
		commonFilterUtils : '/src/main/webapp/WEB-INF/TEMPLATE/ampTemplate/script/common/CommonFilterUtils', 
		i18next : 'lib/i18next_1.6.3.min',
		numeral : 'lib/numeral_1.4.5.min'		
	},
	shim : {
		underscore : {
			exports : '_'
		},
		backbone : {
			deps : [ 'jquery', 'underscore' ],
			exports : 'Backbone'
		},
		marionette : {
			deps : [ 'jquery', 'underscore', 'backbone' ],
			exports : 'Marionette'
		},
		jqueryui : {
			deps : ['jquery']
		},
		jqueryuii18n : {
			deps : ['jquery','jqueryui']
		},
		documentModel : {
			deps : [ 'backbone' ],
			exports : 'documentModel'
		},
		/*
		 * jqgrid_lang : { deps : [ 'jquery', 'jqueryui' ], exports :
		 * 'jqgrid_lang' },
		 */
		jqgrid : {
			deps : [ 'jquery', /* 'jqueryui' , 'jqgrid_lang' */],
			exports : "jQuery.fn.jqGrid"
		},
		filtersWidget : {
			deps : ['commonFilterUtils'],
			exports : 'filtersWidget'
		},
		settings : {			
			exports: 'settings'
		},
		i18next : {
			deps : [ 'jquery' ]
		},
		numeral : {
			exports : 'numeral'
		} 
		,
		translationManager: {
			exports : 'TranslationManager'
		}
	}
});

require([ 'jquery', 'text!views/html/regions.html','translationManager' ], function(jQuery, regionsTemplate, TranslationManager) {
	
	var data = {};
	data["tabs.common:loadingTabs"] = "Loading...";
	TranslationManager.postJSON('/rest/translations/label-translations', data,
			function(data) {
				// Need to do this here because of some crazy FF errors.
				jQuery('#tabs-container').append(regionsTemplate);
				$.each(data, function(key, value) {
					$("*[data-i18n='" + key + "']").text(value);
				})
			});
	// We need to make sure jqueryui is loaded BEFORE bootstrap because both
	// define some functions with the same name like 'botton' and 'tooltip'
	// which will mess with the tabs since we use jquery functions.
	require([ 'jqueryui' ]);
	require([ 'app' ]);

});