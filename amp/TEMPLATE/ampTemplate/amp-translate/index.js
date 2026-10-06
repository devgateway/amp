// TODO: move this up a dir, and instantiate and attach to the app

var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');

function Translator(options) {
  'use strict';

  if (!(this instanceof Translator)) {
    throw new Error('Translator needs to be created with the `new` keyword.');
  }

  // this is the object that has all  the key value pairs for the widget.
  var translatorDefaults = {
    defaultKeys: {},
    availableLanguages: null,// backbone collection
    translations: {
      locales: {
        en:null
      }
    },
    ajax: $.ajax
  };

  /* Gather options */
  if (options) {
    /* Especially useful for overwriting defaultKeys, availableLanguages and translations */
    _.defaults(this, options, translatorDefaults);
  }

  this._promise = null;
  this._currentLng = 'tmp';
  this._firstGet = null;

  // TODO: add support for local storage with timestamp
  this.initTranslations = function get() {
    var self = this;


    // try web
    this._promise = this.getTranslations(self.defaultKeys)
      .fail(function(jqXHR, textStatus, errorThrown) {
        console.error('failed ', jqXHR, textStatus, errorThrown);
      });
    this.promise = this._promise.promise();
  };

  /* Use this for adding more defaultKeys or translations by module
   * if translator has already been instantiated
   */
  this.addTranslatorOptions = function(options) {
    /* force a request from API next time */
    _.defaults(this.defaultKeys, options.defaultKeys);
    this._firstGet = null;
  };


  this.getAvailableLanguages = function() {
    var deferred = $.Deferred();

    if (this.availableLanguages) {
      deferred.resolve(this.availableLanguages);
    } else {
      this._initAvailableLanguages().then(function() {
        deferred.resolve(this.availableLanguages);
      });
    }

    return deferred;
  };

  this._initAvailableLanguages = function() {
    this.availableLanguages = new Backbone.Collection([]);
    this.availableLanguages.url = '/rest/translations/languages';
    return this.availableLanguages.fetch();
  };


  // important to let the api know, so all responses are translated.
  this.setLanguage = function(lng) {
    this._currentLng = lng;
    return this._apiCall('/rest/translations/languages/' + lng, null, 'GET');
  };


  this.translateDOM = function(el) {
    var $newEl = $(el);
    /* TODO(tdk): We identified a major bug here where root immediate child
     *  template elements with translations are not being caught by this
     *  selector.
     *
     *  To workaround, we should clone, wrap, run the selector and unwrap the DOM.
     *
     *  Remember this has to work for el's around the app that are bound
     *  and not bound.
     */

    return this.getTranslations().then(function(data) {
      $.each(data, function(key, value) {
        /*if ($('[data-i18n="' + key + '"]', $newEl).length > 0) {
         console.log(key, '->', value,' $ found->', $newEl.find('[data-i18n="' + key + '"]').text());
         }*/

        // We need a way to identify controls where the placeholder needs to be translated instead of the text.
        if (key.indexOf('[placeholder]') > -1) {
          $('[data-i18n="' + key + '"]', $newEl).attr('placeholder', value);
        } else if (key.indexOf('[title]') > -1) {
          $('[data-i18n="' + key + '"]', $newEl).attr('title', value);
        } else {
          $('[data-i18n="' + key + '"]', $newEl).text(value);
        }
      });
      return $newEl;
    });
  };


  /*
   * Pass in a {"amp.gis:data-i18n-code": "base lang words", ...} object for
   * translation e.g. page title
   * */
  // TODO: don't call it a list if it's an object ?!
  this.translateList = function(list) {

    // update translateable elements in this key-value set
    var _updateList = function(list, i18nData) {
      _.each(list, function(value, key, list) {
        if (i18nData[key]) {
          list[key] = i18nData[key];
        } else {
          list[key] = key[value];
        }
      });
      return list;
    };

    return this.getTranslations().then(function(i18nData) {
      var outList = _updateList(list, i18nData);
      return outList;
    });
  };


  /*
   * Synchronously get a (already-loaded) translation
   */
  this.translateSync = function(key, alt) {
    if (this.getTranslations().state() !== 'resolved') {
      console.error('translateSync was called when getTranslations is not successfully resolved');
    }
    var translated = this.translations.locales[this._currentLng][key];
    if (translated === undefined) {
      console.warn('No translation has been loaded for', key);
      translated = alt || key;
    }
    return translated;
  };


  // Only do single request on launch.
  this.getTranslations = function() {
    // this way won't work with change languages mid way though.
    if (!this._firstGet) {
      this._firstGet = this._getTranslationsFromAPI(this.defaultKeys, this._currentLng);
    }
    return this._firstGet;
  };


  this._getTranslationsFromAPI = function(translateables, lng) {
    var self = this;
    var url = '/rest/translations/label-translations';

    return this._apiCall(url, translateables, 'POST').then(function(data) {
      //cache if we know the lng. TODO: get api to always return the lng.
      if (lng) {
        self.translations.locales[lng] = data;
      } else {
        // temp hack to do caching if API doesn't return current lng
        lng = this._currentLng;
        self.translations.locales[lng] = data;
      }

      return data;
    });
  };


  // helper to wrap api call
  this._apiCall = function(url, data, type) {
    var ajaxOptions = {
      headers: {
        // jscs:disable disallowQuotedKeysInObjects
        'Accept': 'application/json',
        'Content-Type': 'application/json'
        // jscs:enable disallowQuotedKeysInObjects
      },
      type: type,
      url: url,
      dataType: 'json'
    };
    if (data) {
      ajaxOptions.data = JSON.stringify(data);
    }

    return this.ajax(ajaxOptions);
  };

  this.initTranslations();
}

module.exports = Translator;
