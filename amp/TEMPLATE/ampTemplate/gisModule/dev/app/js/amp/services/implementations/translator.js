var fs = require('fs');
var $ = require('jquery');

var APIHelper = require('../../../libs/local/api-helper');

function Translator() {
  'use strict';

  if (! (this instanceof Translator)) {
    throw new Error('Translator needs to be created with the `new` keyword.');
  }

  // this is the object that has all  the key value pairs for the widget.
  this._defaultKeys = JSON.parse(fs.readFileSync(__dirname + 
    '/../../../../mock-api/data/label-translations/sample-en.json', 'utf8'));
  this.translations = null;
  this._promise = null;


  // Phil: I maybe naive, but why do we need to have names for these functions... (copied style from state service.)  
  // TODO: add support for local storage with timestamp, and ability to keep object for each language.
  // TODO: review use of promises.
  // TODO: LowPriority: If user toggles language manually during use will need to re-send all 
  // API requests so that filter list etc. use new language....

  this.initTranslations = function get() {
    var self = this;
    // TODO: try local storage first.

    // try web
    this._promise = this._getTranslationsFromAPI(self._defaultKeys).then(function(data){
        self.translations = data;
      })
      .fail(function(jqXHR, textStatus, errorThrown){
        console.error('failed ', jqXHR, textStatus, errorThrown);
      });

  };


  // TODO: look to local first, only use api if we don't already have.
  this.getTranslations = function(lng){
    return this._getTranslationsFromAPI(this._defaultKeys, lng);
  };


  this._getTranslationsFromAPI = function(translateables, lng){

    var url = APIHelper.getAPIBase() + '/rest/translations/label-translations';

    //lng is optional, if not provided rely on server to choose.
    if(lng){
      url += '/'+lng;
    }
    return this._postJSON(url, translateables);
  };

  // helper to wrap post call
  this._postJSON= function(url, data) {
    return $.ajax({
      headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        'type': 'POST',
        'url': url,
        'data': JSON.stringify(data),
        'dataType': 'json'
      });
  };


  this.initTranslations();
}

module.exports = Translator;
