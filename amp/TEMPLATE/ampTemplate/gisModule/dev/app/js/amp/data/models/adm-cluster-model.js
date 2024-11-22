var when = require('jquery').when;
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var countries = require('i18n-iso-countries');
countries.registerLocale(require('i18n-iso-countries/langs/en.json'));
const AMP_WOCAT_API= 'https://ggw-dashboard.dgstg.org/api/amp-wocat/search?country=';


module.exports = Backbone.Model
    .extend(LoadOnceMixin).extend({

      url: '/rest/gis/cluster',

      initialize: function(attributes, options) {
        console.log("Options in model",options)

        // this.on('sync', this.modifySync);
      },

        modifySync: function (response) {

            // Check if the response has features
            if (!response || !response.features) {
                console.error('No features found in response!');
                return Promise.resolve(response); // Return a resolved Promise with the original response
            }

            // Create an array of promises to handle all fetchWocat calls
            var promises = response.features.map(function (feature) {
                var country = countries.getAlpha3Code(feature.properties.admName, 'en');

                // Fetch new activity IDs for the given country
                return this.fetchWocat(country).then(function (responseObject) {
                    var newActivityIds = responseObject.ids;
                    if (newActivityIds===undefined)
                    {
                        newActivityIds =[]
                    }
                    var data = responseObject.data;
                    if (data===undefined)
                    {
                        data =[]
                    }
                    if (this.get('id') === 'wocat') {
                        feature.properties.wocat = true;
                        feature.properties.wocatActivities = newActivityIds;
                        feature.properties.wocatCountryData=data;
                    }
                }.bind(this)).catch(function (error) {
                    console.error("Failed to fetch activityIds for", feature.properties.admName, error);
                });
            }.bind(this));

            // Return a Promise that resolves when all fetchWocat calls are complete
            return Promise.all(promises).then(function () {
                return response; // Return the modified response
            });
        },

      fetchWocat: function (country) {
        const api = AMP_WOCAT_API+ country;

        // Step 1: Fetch totalElements
        return fetch(`${api}`)
            .then(function(contentResponse) {
              return contentResponse.json();
            })
            .then(function(contentData) {
              if (!contentData.content) {
                console.error('Failed to fetch content data.');
                return [];
              }


              // Step 3: Extract IDs from content and return them
              const newActivityIds = contentData.content.map(function(item) {
                return item.id;
              });

              return {ids:contentData.content.length>0?newActivityIds:[],data:contentData.content};
            })
            .catch(function(error) {
              console.error('Error in fetchWocat:', error);
              return []; // Return an empty array in case of error
            });
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
        console.log("Refreshing model. Model is:", this);

        delete this._loaded;

        if (this.get('selected')) {
          this.load();
        }
      },

        fetch: function (options) {
            var filter = {};

            if (this.lastFetchXhr && this.lastFetchXhr.readyState > 0 && this.lastFetchXhr.readyState < 4) {
                console.log("Aborting previous fetch...");
                this.lastFetchXhr.abort();
            }

            if (this.collection.filter) {
                _.extend(filter, this.collection.filter.serialize());
            }

            filter.settings = this.collection.settingsWidget.toAPIFormat();
            filter.filters = filter.filters || {};
            filter.filters.adminLevel = this._translateADMToMagicWord(this.get('value'));

            if (this.collection.performanceToggleModel.get('isPerformanceToggleSelected') !== null) {
                filter['performanceIssues'] = !this.collection.performanceToggleModel.get('isPerformanceToggleSelected');
            }

            options = _.defaults(options || {}, {
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(filter),
            });

            console.log("Fetching data with filter:", filter);

            // Perform the fetch
            this.lastFetchXhr = Backbone.Model.prototype.fetch.call(this, options);

            // Intercept and modify the response
            var modifiedPromise = this.lastFetchXhr.then(
                function (response) {
                    console.log("Original response:", response);

                    // Modify the response
                    return this.modifySync(response).then(function (modifiedResponse) {
                        console.log("Modified response:", modifiedResponse);

                        // Trigger 'sync' event with the modified response
                        this.trigger('sync', this, modifiedResponse, options);

                        return modifiedResponse; // Return the modified response
                    }.bind(this));
                }.bind(this),
                function (error) {
                    console.error("Fetch failed:", error);
                    return Promise.reject(error); // Forward the error
                }
            );

            // Return a Promise-compatible jqXHR that resolves to the modified response
            return Object.assign(this.lastFetchXhr, modifiedPromise);
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
