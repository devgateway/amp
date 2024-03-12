/**
 * Basic custom logger, can be enabled/disabled through localStorage. To enable
 * open browser console and type:
 * 
 * Saiku.logger.enableGeneral() 
 * Saiku.logger.enableLevel('log')
 * Saiku.logger.enableLevel('error')
 */
var AMPCustomLogging = Backbone.Model.extend({
	enabled : false,
	storage : 'saiku-custom-logging',

	initialize : function() {

	},

	enableGeneral : function() {
		localStorage.setItem(this.storage, "true");
		localStorage.setItem(this.storage + '-log', "true");
		localStorage.setItem(this.storage + '-info', "true");
		localStorage.setItem(this.storage + '-warn', "true");
		localStorage.setItem(this.storage + '-error', "true");
	},

	disableGeneral : function() {
		localStorage.setItem(this.storage, "false");
	},

	enableLevel : function(level) {
		if (level === "log") {
			localStorage.setItem(this.storage + '-log', "true");
		} else if (level === "warn") {
			localStorage.setItem(this.storage + '-warn', "true");
		} else if (level === "warn") {
			localStorage.setItem(this.storage + '-log', "true");
		}
	},

	disableLevel : function(level) {
		if (level === "log") {
			localStorage.setItem(this.storage + '-log', "false");
		} else if (level === "warn") {
			localStorage.setItem(this.storage + '-warn', "false");
		} else if (level === "warn") {
			localStorage.setItem(this.storage + '-log', "false");
		}
	},

	log : function(message) {
		if (localStorage.getItem(this.storage) === "true") {
			if (localStorage.getItem(this.storage + "-log") === "true") {
				console.log(message);
			}
		}
	},

	info : function(message) {
		if (localStorage.getItem(this.storage) === "true") {
			if (localStorage.getItem(this.storage + "-info") === "true") {
				console.log(message);
			}
		}
	},

	warn : function(message) {
		if (localStorage.getItem(this.storage) === "true") {
			if (localStorage.getItem(this.storage + "-warn") === "true") {
				console.warn(message);
			}
		}
	},

	error : function(message) {
		if (localStorage.getItem(this.storage) === "true") {
			if (localStorage.getItem(this.storage + "-error") === "true") {
				console.error(message);
			}
		}
	}
});