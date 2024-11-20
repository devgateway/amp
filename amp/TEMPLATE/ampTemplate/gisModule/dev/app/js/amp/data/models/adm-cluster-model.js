var when = require('jquery').when;
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
const AMP_WOCAT_API= 'https://ggw-dashboard.dgstg.org/api/amp-wocat/search?country=BFA';


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({
  url: '/rest/gis/cluster',

  initialize: function() {
    this.on('sync', this.handleSync);

  },

          handleSync: async function (model, response, options) {
            console.log('Original response from /cluster:', response);

            // Check if the response has features
            if (!response || !response.features) {
              console.error('No features found in response!');
              return;
            }

            const AMP_WOCAT_API = 'https://ggw-dashboard.dgstg.org/api/amp-wocat/search?country=BFA';

            try {
              // Step 1: Fetch totalElements
              const totalElementsResponse = await fetch(AMP_WOCAT_API);
              const totalElementsData = await totalElementsResponse.json();

              const totalElements = totalElementsData.totalElements;
              if (!totalElements) {
                console.error('Failed to fetch totalElements.');
                return;
              }

              console.log(`Total elements for AMP WOCAT API: ${totalElements}`);

              // Step 2: Fetch full content data
              const contentResponse = await fetch(`${AMP_WOCAT_API}&page=1&size=${totalElements}`);
              const contentData = await contentResponse.json();

              if (!contentData.content) {
                console.error('Failed to fetch content data.');
                return;
              }

              console.log('Fetched content data:', contentData);

              // Step 3: Extract IDs from content and replace activityIds
              const newActivityIds = contentData.content.map(item => item.id);

              response.features.forEach(feature => {
                if (feature.properties.admName === 'Burkina Faso') {
                  console.log('Replacing activityIds for Burkina Faso...');
                  feature.properties.activityid = newActivityIds;
                }
              });

              console.log('Modified response:', response);

              // Optionally, update the model
              this.set(response);
            } catch (error) {
              console.error('Error in handleSync:', error);
            }
          },


      attachListeners: function() {
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });

    this.listenTo(this.collection.filter, 'apply', this.refreshModel);
    this.listenTo(this.collection.settingsWidget, 'applySettings', this.refreshModel);
    this.listenTo(this.collection.performanceToggleModel, 'change:isPerformanceToggleSelected', this.refreshModel);
  },

  // if filters change and layer is selected update it.
  refreshModel: function() {
    // this forces next 'load' call to do a fresh fetch.
    delete this._loaded;

    if (this.get('selected')) {
      this.load();
    }
  },

  fetch: function(options) {
    var filter = {};

    // TODO: move lastFetchXhr code into a mixin or something...
    //Stop last fetch before doing new one.
    //request is between initialized(0) and complete(4).
    if (this.lastFetchXhr && this.lastFetchXhr.readyState > 0 && this.lastFetchXhr.readyState < 4) {
      this.lastFetchXhr.abort();
    }

    // get filters
    if (this.collection.filter) {
      _.extend(filter, this.collection.filter.serialize());
    }

    // TODO: verify settings works..
    filter.settings = this.collection.settingsWidget.toAPIFormat();

    filter.filters = filter.filters || {};
    filter.filters.wocat=true;
    filter.filters.adminLevel = this._translateADMToMagicWord(this.get('value'));

    if (this.collection.performanceToggleModel.get('isPerformanceToggleSelected') != null) {
      filter['performanceIssues'] = !this.collection.performanceToggleModel.get('isPerformanceToggleSelected');
    }

    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(filter)
    });

    this.lastFetchXhr = Backbone.Model.prototype.fetch.call(this, options);
    return this.lastFetchXhr;
  },

  loadBoundary: function() {
    var boundaries = this.collection.boundaries.where({admLevel: this.get('value')});
    var promises = [ this.load() ];
    boundaries.forEach(function (b) { promises.push(b.load()); });
    return $.when.apply($, promises)
      .done(function() {
        var self = arguments[0];
        var boundaryModels = Array.prototype.slice.call(arguments, 1);
        self.set('boundaries', boundaryModels.map(function(model) { return model.toJSON(); }));
      });
  },

  loadAll: function() {
    return when(this.load(), this.loadBoundary()).promise();
  },

  _translateADMToMagicWord: function(admString) {
    var magicWords = {
      'adm-0': 'Administrative Level 0',
      'adm-1': 'Administrative Level 1',
      'adm-2': 'Administrative Level 2',
      'adm-3': 'Administrative Level 3'
    };

    return magicWords[admString];
  }
});
