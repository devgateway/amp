define([ 'numeral', 'jquery', 'jqueryui', 'jqgrid' ], function(Numeral, jQuery) {

	"use strict";

	function TabUtils() {
		if (!(this instanceof TabUtils)) {
			throw new TypeError("TabUtils constructor cannot be called as a function.");
		}
	}

	TabUtils.hideInvisibleTabs = function(tabsCollection, animate) {
		var duration = animate !== undefined ? animate : 0;
		var allTabs = jQuery("#tabs-section ul li");
		_.each(tabsCollection, function(val, i) {
			if (val.get('visible') === false) {
				if (app.TabsApp.tabsCollection.models[i].get('isOtherTabNowVisible') === false) {
					// This step is necessary when we first load the page because we are not triggering the change event 
					// since 'isOtherTabNowVisible' is already false.
					jQuery(allTabs[i]).hide(duration);
				} else {
					app.TabsApp.tabsCollection.models[i].set('isOtherTabNowVisible', false);
				}
			}
		});
	};

	TabUtils.showInvisibleTab = function(id, animate) {
		var duration = animate !== undefined ? animate : 0;
		this.hideInvisibleTabs(app.TabsApp.tabsCollection.models, 250);
		_.each(app.TabsApp.tabsCollection.models, function(item, i) {
			if (item.get('id') === id) {
				//jQuery(jQuery("#tabs-section ul li")[i]).show(duration);
				jQuery(app.TabsApp.tabContainer).tabs("option", "active", i);
				app.TabsApp.tabsCollection.models[i].set('isOtherTabNowVisible', true);
			}
		});
		// AMP-20998: Hide 'More tabs' tab for good if there are no more hidden tabs.
		var invisibleTabsCount = _.filter(app.TabsApp.tabsCollection.models, function(item) {return (item.get('isOtherTabNowVisible') === false);}).length;
		if (invisibleTabsCount === 0) {
			var moreTabsTab = _.find(app.TabsApp.tabsCollection.models, function(item) {return item.get('id') === -1});
			jQuery("#tab-link--1").parent().hide(duration);
		}
	}

	/**
	 * This ugly method will solve the known problem of latest jqueryui tabs +
	 * <base> tag by changing the <a href> when it points to a local "#"
	 * address.
	 * 
	 * @param selector
	 * @param options
	 */
	TabUtils.createTabs = function(selector, options) {
		jQuery(selector).find("ul a").each(
				function() {
					var href = jQuery(this).attr("href");
					var newHref = window.location.protocol + '//' + window.location.hostname + (location.port ? ':' + location.port : '')
							+ window.location.pathname + window.location.search + href;
					if (href.indexOf("#") == 0) {
						jQuery(this).attr("href", newHref);
					}
				});
		jQuery(selector).tabs(options);
	};

	TabUtils.shortenTabNames = function(tabs) {
		var maxChars = 20;
		_.each(tabs, function(item, i) {
			var name = item.get('name');
			// Ignore "more tabs" tab.
			if (name.length > maxChars && item.get('id') !== -1) {
				item.set('shortName', name.substring(0, maxChars) + '...');
			} else {
				item.set('shortName', name);
			}
		});
	};

	/**
	 * Resize the grid when the container grows/shrinks but maintaining the
	 * desktop limit.
	 */
	TabUtils.resizePanel = function(id, maxWidth) {
		try {
			var newWidth = 0;
			var container = jQuery("#" + app.TabsApp.mainTableContainer);
			var separatorWidth = jQuery("#center-column").width();
			var rightWidth = jQuery("#right-column").width();

			newWidth = maxWidth - separatorWidth - rightWidth;
			container.css("width", maxWidth);
			jQuery("#tabs-container").css("width", newWidth);
			jQuery("#tabs-container .dynamic-content-table").css("width", newWidth - 20);
			jQuery("#tab_grid_" + id).jqGrid().setGridWidth(newWidth - 20, true);
		} catch (err) {
			console.error(err);
		}
	};

	/**
	 * Convert a float number to its string representation with the format from
	 * settings (notice the definition of our current format is in the
	 * 'language')
	 */
	TabUtils.numberToString = function(number, settings) {
		var auxSettings = extractSettings(settings);
		var format = "";
		var stringNumber = null;

		// Create the formatting string to be applied.
		if (auxSettings.useGrouping) {
			format = "0,0";
		}
		if (auxSettings.maxDecimalDigits > 0) {
			format = format + "." + new Array(auxSettings.maxDecimalDigits + 1).join("0");
		}
		// Define a new "language" for Numeral where we can change the default
		// delimiters.
		Numeral.language('amp', createLanguage(auxSettings));
		// Apply new language.
		Numeral.language('amp');
		// Apply the format.
		stringNumber = new Numeral(number).format(format);
		return stringNumber;
	};

	TabUtils.stringToNumber = function(stringNumber, settings) {
		var auxSettings = extractSettings(settings);
		var format = "";
		var number = null;

		// Create the formatting string to be applied.
		if (auxSettings.useGrouping) {
			format = "0" + auxSettings.currentThousandSeparator + "0";
		}
		if (auxSettings.maxDecimalDigits > 0) {
			format = format + auxSettings.currentDecimalSeparator + new Array(auxSettings.maxDecimalDigits + 1).join("0");
		}
		// Define a new "language" for Numeral where we can change the default
		// delimiters.
		Numeral.language('amp', createLanguage(auxSettings));
		// Apply new language.
		Numeral.language('amp');
		// Apply the format.
		Numeral.defaultFormat = format;
		number = new Numeral().unformat(stringNumber);
		return number;
	};

	/**
	 * Called on initialization of tabs only if we want to activate a tab other
	 * than the first one.
	 * 
	 * @param id
	 */
	TabUtils.activateTabById = function(id) {
		_.each(app.TabsApp.tabsCollection.models, function(item, i) {
			if (item.get('id') === id) {
				if (item.get('visible') === false) {
					// TODO: Bind an event to changes on "visible" field so this
					// process is triggered automatically.
					item.set('visible', true);
					TabUtils.showInvisibleTab(id);
				}
				// Set this tab active with JQuery.
				jQuery("#tabs-container").tabs("option", "active", i);
			}
		});
	};

	function extractSettings(settings) {
		var options = {
			currentThousandSeparator : null,
			currentDecimalSeparator : null,
			useGrouping : false,
			maxDecimalDigits : 0,
			groupSize : 0
		};
		
		var amountFormat = settings['amount-format'];
		if (amountFormat) {
			if (amountFormat.get('decimal-symbol')) {
				options.currentDecimalSeparator = amountFormat.get('decimal-symbol');
			}
			if (amountFormat.get('max-frac-digits')) {
				options.maxDecimalDigits = amountFormat.get('max-frac-digits');
			}
			if (amountFormat.get('use-grouping')) {
				options.useGrouping = amountFormat.get('use-grouping');
			}
			if (amountFormat.get('group-size')) {
				options.groupSize = amountFormat.get('group-size');
			}
			if (amountFormat.get('group-separator')) {
				options.currentThousandSeparator = amountFormat.get('group-separator');
			}
		}

		return options;
	}

	function createLanguage(auxSettings) {
		var ret = {
			delimiters : {
				thousands : auxSettings.currentThousandSeparator,
				decimal : auxSettings.currentDecimalSeparator
			},
			abbreviations : {
				thousand : 'k',
				million : 'm',
				billion : 'b',
				trillion : 't'
			},
			ordinal : function(number) {
				return number === 1 ? 'st' : 'rds';
			},
			currency : {
				symbol : '$'
			}
		};
		return ret;
	}

	TabUtils.prototype = {
		constructor : TabUtils
	};

	return TabUtils;
});