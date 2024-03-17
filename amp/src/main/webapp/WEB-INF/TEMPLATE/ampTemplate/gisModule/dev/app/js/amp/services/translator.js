var fs = require('fs');
var Translator = require('amp-translate');
module.exports = {		
		init : function(){
			var defaultKeys = JSON.parse(fs.readFileSync(__dirname +
			    '/initial-translation-request.json', 'utf8'));
			return new Translator({defaultKeys: defaultKeys});	
		}
};
