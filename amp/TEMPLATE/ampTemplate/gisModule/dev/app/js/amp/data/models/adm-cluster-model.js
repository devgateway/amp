var when = require('jquery').when;
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var countries = require('i18n-iso-countries');
countries.registerLocale(require('i18n-iso-countries/langs/en.json'));
const AMP_WOCAT_API= 'https://ggw-dashboard.dgstg.org/api/amp-wocat/search?country=BFA';


module.exports = Backbone.Model
    .extend(LoadOnceMixin).extend({

      url: '/rest/gis/cluster',

      initialize: function(attributes, options) {
        console.log("Options in model",options)

        // this.on('sync', this.modifySync);
      },

        modifySync: async function (response) {
            console.log('Original response from /cluster:', response);

            // Check if the response has features
            if (!response || !response.features) {
                console.error('No features found in response!');
                return response; // Return early if there's no valid response
            }

            // Create an array of promises to handle all fetchWocat calls
            let promises = response.features.map(async (feature) => {
                const country = countries.getAlpha3Code(feature.properties.admName, 'en');
                console.log('Fetching activityIds for', feature.properties.admName);

                try {
                    // Fetch new activity IDs for the given country
                    const newActivityIds = await this.fetchWocat(country);
                    console.log("New activityIds:", newActivityIds, "for", feature.properties.admName);

                    if (newActivityIds.length > 0) {
                        if (this.get('id') === 'wocat') {
                            feature.properties.wocat = true;
                            feature.properties.wocatActivities = newActivityIds;
                            feature.properties.activityid = newActivityIds;
                        }
                    }
                } catch (error) {
                    console.error("Failed to fetch activityIds for", feature.properties.admName, error);
                }
            });

            // Wait for all promises to resolve
            await Promise.all(promises);

            // Return the modified response
            return response;
        },

      fetchWocat: function (country) {
        const AMP_WOCAT_API = 'https://ggw-dashboard.dgstg.org/api/amp-wocat/search?country=' + country;

        // Step 1: Fetch totalElements
        return fetch(AMP_WOCAT_API)
            .then(function(response) {
              return response.json();
            })
            .then(function(totalElementsData) {
              const totalElements = totalElementsData.totalElements;
              if (!totalElements) {
                console.error('Failed to fetch totalElements.');
                return [];
              }

              console.log('Total elements for AMP WOCAT API:', totalElements);

              // Step 2: Fetch full content data
              return fetch(`${AMP_WOCAT_API}&page=1&size=${totalElements}`);
            })
            .then(function(contentResponse) {
              return contentResponse.json();
            })
            .then(function(contentData) {
              if (!contentData.content) {
                console.error('Failed to fetch content data.');
                return [];
              }

              console.log('Fetched content data:', contentData);

              // Step 3: Extract IDs from content and return them
              const newActivityIds = contentData.content.map(function(item) {
                return item.id;
              });

              return newActivityIds;
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
                const modifiedPromise = this.lastFetchXhr.then(
                    async function (response) {
                        console.log("Original response:", response);

                        // Modify the response (await the async modifySync)
                        const modifiedResponse = await this.modifySync(response);
                        console.log("Modified response:", modifiedResponse);

                        // Trigger 'sync' event with the modified response
                        this.trigger('sync', this, modifiedResponse, options);

                        return modifiedResponse; // Return the modified response
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
