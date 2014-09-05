var fs = require('fs');
var $ = require('jquery');

function Translator() {
  'use strict';

  if (! (this instanceof Translator)) {
    throw new Error('Translator needs to be created with the `new` keyword.');
  }

  // this is the object that has all  the key value pairs for the widget.
  this._defaultKeys = JSON.parse(fs.readFileSync(__dirname + 
    '/../../../../mock-api/data/label-translations/sample-en.json', 'utf8'));

  this.translations = {
    locales:{
      en:null
    }
  };
  var self = this;

  this._promise = null;


  // Phil: I maybe naive, but why do we need to have names for these functions... (copied style from state service.)  
  // TODO: add support for local storage with timestamp, and ability to keep object for each language.
  this.initTranslations = function get() {
    var self = this;

    // try web
    this._promise = this._getTranslationsFromAPI(self._defaultKeys)
      .fail(function(jqXHR, textStatus, errorThrown){
        console.error('failed ', jqXHR, textStatus, errorThrown);
      });
  };

  // important to let the api know, so all responses are translated.
  this.setLanguage = function(lng){
    this._currentLng = lng;
    return this._apiCall('/rest/translations/languages/'+lng, null, 'GET');
  };

  // Look to local first, only use api if we don't already have translations for this language.
  this.getTranslations = function(){
    var deferred = $.Deferred();
    if(self.translations[this._currentLng]){
      deferred.resolve(self.translations[this._currentLng]);
    } else{
      deferred = this._getTranslationsFromAPI(this._defaultKeys, this._currentLng);
    }

    return deferred;
  };


  this._getTranslationsFromAPI = function(translateables, lng){
    var self = this;
    var url = '/rest/translations/label-translations';

    //lng is optional, if not provided rely on server to choose.
    if(lng){
      url += '/'+lng;
    }

    return this._apiCall(url, translateables, 'POST').then(function(data){
      //cache if we know the lng. TODO: get api to always retun the lng.
      if(lng){
        self.translations.locales[lng] = data;
      } else{
        console.warn('no lng set, can\'t cache',data);
      }
    });
  };


  // helper to wrap api call
  this._apiCall= function(url, data, type) {
    return $.ajax({
      headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        'type': type,
        'url': url,
        'data': JSON.stringify(data),
        'dataType': 'json'
      });
  };

  this.initTranslations();
}

module.exports = Translator;
