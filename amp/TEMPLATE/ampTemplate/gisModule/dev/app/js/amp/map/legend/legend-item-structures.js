/* global app */
var fs = require('fs');
const { getGisSettings } = require('../../app');

var _ = require('underscore');
const Backbone = require('backbone');
const Template = fs.readFileSync(__dirname + '/legend-item-structures.html', 'utf8');
const SettingsUtils = require('../../../libs/local/settings-utils.js');
var Constants = require('../../../libs/local/constants.js');


module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-structure',

  initialize: function(options) {
    this.app = options.app;
  },


  render:  function() {
	  var self = this;
	  //getStructuresWithActivities was null...

			   self.model.structuresCollection.getStructuresWithActivities().then(function() {
				   var geoJSON = self.model.structuresCollection.toGeoJSON();
				   var customStructureColors = []
				   geoJSON.features.forEach(function (feature) {
					   if (feature.properties.color && feature.properties.color.indexOf(Constants.STRUCTURE_COLORS_DELIMITER) !== -1) {
						   var splits = feature.properties.color.split(Constants.STRUCTURE_COLORS_DELIMITER);
						   if (customStructureColors.find(function (c) {
							   return c.color === splits[0]
						   }) == null) {
							   customStructureColors.push({
								   color: splits[0],
								   label: splits[1]
							   });
						   }
					   }
				   });

				   getGisSettings()
				      .then(gisSettings=> {

				   var renderObject = {
					   status: 'loaded',
					   colourBuckets: self.model.structuresCollection.palette.colours,
					   selectedVertical: self.model.get('filterVertical'),
					   gisSettings: gisSettings,
					   customStructureColors: customStructureColors
				   };

				   //TODO: Move this code to a config class.
				   //IT IS REPEATED IN map/views/structures-view.js
				   var MAX_NUM_FOR_ICONS = 0;
				   var useIconsForSectors = app.data.generalSettings.get('use-icons-for-sectors-in-project-list');
				   var maxLocationIcons = app.data.generalSettings.get('max-locations-icons');
				   if (useIconsForSectors !== undefined && useIconsForSectors === true) {
					   if (maxLocationIcons !== undefined && maxLocationIcons !== '') {
						   if (maxLocationIcons === 0) {
							   MAX_NUM_FOR_ICONS = -1; //always show
						   } else {
							   MAX_NUM_FOR_ICONS = maxLocationIcons;
						   }
					   } else {
						   MAX_NUM_FOR_ICONS = 0;
					   }
				   } else {
					   MAX_NUM_FOR_ICONS = 0;
				   }

				   // render icons if available
				   if ((MAX_NUM_FOR_ICONS === -1 || self.model.structuresCollection.length < MAX_NUM_FOR_ICONS) &&
					   self.model.get('filterVertical') === 'Primary Sector') {
					   renderObject.imageBuckets = self.model.iconMappings;
					   renderObject.DEFAULT_ICON_CODE = self.model.DEFAULT_ICON_CODE;
					   renderObject.palletteElements = self.model.structuresCollection.palette.get('elements');
				   }

				   self.app.translator.promise.then(function () {
					   self.app.translator.translateDOM(
						   self.template(_.extend({}, self.model.toJSON(), renderObject))
					   ).then(function (legend) {
						   self.$el.html(legend);
					   });

				   });

				   if (MAX_NUM_FOR_ICONS !== -1) {
					   self.app.translator.translateList({
						   'amp.gis:legend-popover': 'If there are less than',
						   'amp.gis:legend-popover-2': 'points map will show icons otherwise: show coloured circles.',
						   'amp.gis:title-AdministrativeLevel1': 'Administrative Level 1'
					   }).then(function (legendPopoverList) {
						   var legendPopover = [legendPopoverList['amp.gis:legend-popover'],
							   ' ',
							   MAX_NUM_FOR_ICONS,
							   ' ',
							   legendPopoverList['amp.gis:legend-popover-2']
						   ].join('');
						   self.$('[data-toggle="popover"]').popover();
						   self.$('[data-toggle="popover"]').attr('data-content', legendPopover);
						   self.$('[data-toggle="popover"]').show();
					   });
				   } else {
					   self.$('[data-toggle="popover"]').hide();
				   }


				   // add listener to select. Didn't work when i used 'events'
				   // probably because happens after view populated...or translate strips events..
				   self.$('select').change(function () {
					   var verticalID = self.$('option:selected').val();
					   self.model.set('filterVertical', verticalID);
				   });
			   });
			   });


	  return this;
  }

});
