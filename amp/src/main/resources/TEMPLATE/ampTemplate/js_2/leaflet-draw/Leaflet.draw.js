/**
 * Leaflet.draw assumes that you have already included the Leaflet library.
 */
L.drawVersion = '0.4.2';
/**
 * @class L.Draw
 * @aka Draw
 *
 *
 * To add the draw toolbar set the option drawControl: true in the map options.
 *
 * @example
 * ```js
 *      var map = L.map('map', {drawControl: true}).setView([51.505, -0.09], 13);
 *
 *      L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
 *          attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
 *      }).addTo(map);
 * ```
 *
 * ### Adding the edit toolbar
 * To use the edit toolbar you must initialise the Leaflet.draw control and manually add it to the map.
 *
 * ```js
 *      var map = L.map('map').setView([51.505, -0.09], 13);
 *
 *      L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
 *          attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
 *      }).addTo(map);
 *
 *      // FeatureGroup is to store editable layers
 *      var drawnItems = new L.FeatureGroup();
 *      map.addLayer(drawnItems);
 *
 *      var drawControl = new L.Control.Draw({
 *          edit: {
 *              featureGroup: drawnItems
 *          }
 *      });
 *      map.addControl(drawControl);
 * ```
 *
 * The key here is the featureGroup option. This tells the plugin which FeatureGroup contains the layers that
 * should be editable. The featureGroup can contain 0 or more features with geometry types Point, LineString, and Polygon.
 * Leaflet.draw does not work with multigeometry features such as MultiPoint, MultiLineString, MultiPolygon,
 * or GeometryCollection. If you need to add multigeometry features to the draw plugin, convert them to a
 * FeatureCollection of non-multigeometries (Points, LineStrings, or Polygons).
 */
L.Draw = {};

/**
 * @class L.drawLocal
 * @aka L.drawLocal
 *
 * The core toolbar class of the API — it is used to create the toolbar ui
 *
 * @example
 * ```js
 *      var modifiedDraw = L.drawLocal.extend({
 *          draw: {
 *              toolbar: {
 *                  buttons: {
 *                      polygon: 'Draw an awesome polygon'
 *                  }
 *              }
 *          }
 *      });
 * ```
 *
 * The default state for the control is the draw toolbar just below the zoom control.
 *  This will allow map users to draw vectors and markers.
 *  **Please note the edit toolbar is not enabled by default.**
 */
L.drawLocal = {
	// format: {
	// 	numeric: {
	// 		delimiters: {
	// 			thousands: ',',
	// 			decimal: '.'
	// 		}
	// 	}
	// },
	draw: {
		toolbar: {
			// #TODO: this should be reorganized where actions are nested in actions
			// ex: actions.undo  or actions.cancel
			actions: {
				title: TranslationManager.getTranslated('Cancel drawing'),
				text: TranslationManager.getTranslated('Cancel')
			},
			finish: {
				title: TranslationManager.getTranslated('Finish drawing'),
				text: TranslationManager.getTranslated('Finish')
			},
			undo: {
				title: TranslationManager.getTranslated('Delete last point drawn'),
				text: TranslationManager.getTranslated('Delete last point')
			},
			buttons: {
				polyline: TranslationManager.getTranslated('Draw a polyline'),
				polygon: TranslationManager.getTranslated('Draw a polygon'),
				rectangle: TranslationManager.getTranslated('Draw a rectangle'),
				circle: TranslationManager.getTranslated('Draw a circle'),
				marker: TranslationManager.getTranslated('Draw a marker')
			}
		},
		handlers: {
			circle: {
				tooltip: {
					start: TranslationManager.getTranslated('Click and drag to draw circle.')
				},
				radius: TranslationManager.getTranslated('Radius')
			},
			marker: {
				tooltip: {
					start: TranslationManager.getTranslated('Click map to place marker.')
				}
			},
			polygon: {
				tooltip: {
					start: TranslationManager.getTranslated('Click to start drawing shape.'),
					cont: TranslationManager.getTranslated('Click to continue drawing shape.'),
					end: TranslationManager.getTranslated('Click first point to close this shape.')
				}
			},
			polyline: {
				error: '<strong>Error:</strong>' + TranslationManager.getTranslated('shape edges cannot cross!'),
				tooltip: {
					start: TranslationManager.getTranslated('Click to start drawing line.'),
					cont: TranslationManager.getTranslated('Click to continue drawing line.'),
					end: TranslationManager.getTranslated('Click last point to finish line.')
				}
			},
			rectangle: {
				tooltip: {
					start: TranslationManager.getTranslated('Click and drag to draw rectangle.')
				}
			},
			simpleshape: {
				tooltip: {
					end: TranslationManager.getTranslated('Release mouse to finish drawing.')
				}
			}
		}
	},
	edit: {
		toolbar: {
			actions: {
				save: {
					title: TranslationManager.getTranslated('Save changes.'),
					text: TranslationManager.getTranslated('Save')
				},
				cancel: {
					title: TranslationManager.getTranslated('Cancel editing, discards all changes.'),
					text: TranslationManager.getTranslated('Cancel')
				},
				clearAll:{
					title: TranslationManager.getTranslated('clear all layers.'),
					text: TranslationManager.getTranslated('Clear All')
				}
			},
			buttons: {
				edit: TranslationManager.getTranslated('Edit layers.'),
				editDisabled: TranslationManager.getTranslated('No layers to edit.'),
				remove: TranslationManager.getTranslated('Delete layers.'),
				removeDisabled: TranslationManager.getTranslated('No layers to delete.')
			}
		},
		handlers: {
			edit: {
				tooltip: {
					text: TranslationManager.getTranslated('Drag handles, or marker to edit feature.'),
					subtext: TranslationManager.getTranslated('Click cancel to undo changes.')
				}
			},
			remove: {
				tooltip: {
					text: TranslationManager.getTranslated('Click on a feature to remove')
				}
			}
		}
	}
};
