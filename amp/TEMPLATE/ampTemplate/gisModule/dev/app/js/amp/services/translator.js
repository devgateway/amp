var fs = require('fs');

var Translator = require('amp-translate');

var defaultKeys = JSON.parse(fs.readFileSync(__dirname +
    '/initial-translation-request.json', 'utf8'));
module.exports = new Translator({defaultKeys: defaultKeys});
