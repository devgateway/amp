var _ = require('underscore');
var SettingsUtils = {};

SettingsUtils.getMaxNumberOfIcons = function (settings) {	
	var maxNumberOfIcons = 0;
	var useIconsForSectors = _.find(settings.models, function(item) {
		return (item.id === 'use-icons-for-sectors-in-project-list');
	});
	var maxLocationIcons = _.find(settings.models, function(item) {
		return (item.id === 'max-locations-icons');
	});

	if (useIconsForSectors !== undefined && useIconsForSectors.get('defaultId') === 'true') {
		if (maxLocationIcons !== undefined && maxLocationIcons.get('defaultId') !== '') {
			if (maxLocationIcons.get('defaultId') === '0') {
				maxNumberOfIcons = 99999; //always show
			} else {
				maxNumberOfIcons = parseInt(maxLocationIcons.get('defaultId'), 10);
			}
		} else {
			maxNumberOfIcons = 0;
		}
	} else {
		maxNumberOfIcons = 0;
	}
	return maxNumberOfIcons;
};

module.exports = SettingsUtils;