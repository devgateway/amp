var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var util = require('../../ugly/util');
var template = _.template(fs.readFileSync(__dirname
		+ '/../templates/chart-detail-info-modal.html', 'UTF-8'));

module.exports = BackboneDash.View.extend({
    _currentPage: 0,
    PAGE_SIZE: 50,
    TOP: 'top',
    FRAGMENTATION: 'fragmentation',
    AID_PREDICTABILITY: 'aidPredictability',
    FUNDING_TYPE: 'fundingType',
	initialize: function(options) {
		this.app = options.app;
		this.context = options.context;
		this.model = options.model;
		this.numberDivider = app.generalSettings.numberDivider;			
	},

    getUrl : function() {
        var self = this;
        var url = self.model.url + '/';
        if (this.model.get('chartType') == this.AID_PREDICTABILITY) {
            url += this.context.x.index + '/' + this.context.y.index;
        } else  if (this.model.get('chartType') == this.FUNDING_TYPE) {
            url += this.context.x.raw + '/' + this.context.data[this.context.series.index].keyId;
        } else  if (this.model.get('chartType') == this.FRAGMENTATION) {
            url += this.context.x.index + '/' + this.context.y.index;
        } else  if (this.model.get('chartType') == this.TOP) {
            url += this.context.data[0].values[this.context.x.index].id;
        } else {
            throw new Error('error building url, wrong chartType.');
        }
        return url;
    },

    getConfig: function () {
        var self = this;

        //TODO: move this code to a new model so the API call is made automatically.
        var config = self.app.filter.serialize();
        config.settings = self.app.settingsWidget.toAPIFormat();
        config.settings['funding-type'] = self.model.get('adjtype');
        config.offset = self._currentPage * self.PAGE_SIZE;

        if (this.model.get('chartType') == this.FRAGMENTATION) {
            // Process params from heat-map/configs, in that EP we have defined each heatmap.
            var configs = self.model.get('heatmap_config').models[0];
            var thisChart = _.find(configs.get('charts'), function (item) {
                return item.name === self.model.get('name')
            });
            var xColumn = self.model.get('xAxisColumn') !== '' ? self.model.get('xAxisColumn') : configs.get('columns')[thisChart.xColumns[0]].origName; // First column is default.
            var yColumn = configs.get('columns')[thisChart.yColumns[0]].origName; // First column is default.

            // Check if we need to switch axis.
            if (self.model.get('swapAxes') === true) {
                var auxAxis = yColumn;
                yColumn = xColumn;
                xColumn = auxAxis;
            }

            config.xCount = self.model.get('xLimit');
            config.xColumn = xColumn;
            config.yColumn = yColumn;
            config.yCount = self.model.get('yLimit');
        }
        return config;
    },

	render: function() {
		var self = this;

		this.$el.html(template({
			error: undefined,
			model: this.model,
			context: this.context,
			values: undefined,
			numberDivider: this.numberDivider
		}));

		app.translator.translateDOM($(".chart-tops-info-container"));
		
        var config = self.getConfig();
    	var url = self.getUrl();

        $.ajax({
    		method: 'POST',
    		url: url,
    		dataType: 'json',
    		contentType: 'application/json',
    		processData: false,
    		data: JSON.stringify(config)
    	}).done(function(data) {
    		//TODO: Can we avoid re-calling the template by binding the changes in the 'values' field? 
    		self.$el.html(template({
				error: undefined,
    			model: self.model,
    			context: self.context,
    			values: data.values,
    			numberDivider: self.numberDivider
    		}));

            self.$el.find('.load-more').click(function() {
                self._currentPage++;
                self.render();
            });
            //hide button if all activities were loaded.
            var startIndex = self._currentPage * self.PAGE_SIZE;
			if ((startIndex + self.PAGE_SIZE) >= data.totalRecords) {
            	self.$el.find('.load-more').hide();
            } else {
				self.$el.find('.load-more').html('<span data-i18n="amp.dashboard:chart-tops-table-loadmore">load more</span> ' +
                (startIndex + self.PAGE_SIZE) + '/' + data.totalRecords);
                self.$el.find('.load-more').show();
            }

            if (self.app.generalSettings.numberDivider != 1) {
				var units;
                if(self.app.generalSettings.numberDivider === 1000) {
                    units = '<span data-i18n="amp.dashboard:chart-tops-detail-inthousands">Amounts in Thousands (000)</span>';
                } else if(self.app.generalSettings.numberDivider === 1000000) {
                    units = '<span data-i18n="amp.dashboard:chart-tops-detail-inmillions">Amounts in Millions (000 000)</span>';
                }else if(self.app.generalSettings.numberDivider === 1000000000) {
                    units = '<span data-i18n="amp.dashboard:chart-tops-detail-inbillions">Amounts in Billions (000 000 000)</span>';
                }
                self.$el.find('.number-divider-detail').html(units)
            }

            var fundingType;
            if (self.model.get('chartType') == self.AID_PREDICTABILITY) {
                fundingType = self.context.series.key;
            } else {
                var adjSettings = self.app.settingsWidget.definitions.getFundingTypeSetting();
                fundingType = $.map(adjSettings.get('value').options, function (option) {
                    if (option.id === self.model.get('adjtype')) {
                        return option.name;
                    }
                });
            }
            self.$el.find('.funding-type-title-column').html(fundingType);

    		app.translator.translateDOM($(".modal-body"));
    	}).fail(function(xhr, err) {
			var msg = JSON.parse(xhr.responseText).error;
			console.error("Error Getting chart-detail-info-modal from EP", msg);
			self.$el.html(template({
				model: self.model,
				context: self.context,
				error: err,
				numberDivider: self.numberDivider
			}));
		});

		return this;
	},

});