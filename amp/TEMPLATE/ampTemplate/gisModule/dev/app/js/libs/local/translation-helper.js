var $ = require('jquery');
var _ = require('underscore');
var APIHelper = require('../local/api-helper');

// var i18next = require('i18next');
// TODO: talk to Julian about i18next and if we plan on supporting that style in our api
// sending language in param etc...and how we will deal with calls where the server auto chooses langauge.
// may need to pretend i18 is in en in those cases.
// jquery i18n docs: 
//   http://i18next.com/pages/doc_jquery.html
// .i18n(); would be useful instead of manual scrape and replace for data-18n...

// modifying from Julian's sample.
module.exports = {
  searchTranslations: function(el, lng){
    // get translateable dom elements
    var translateables = this._getTranslateableFromDom(el);

    if(translateables){
      this._getTranslations(translateables, lng)
        .then(this._updateDom)
        .fail(function(jqXHR, textStatus, errorThrown){
          console.error('failed ', jqXHR, textStatus, errorThrown);
        });
    } else{
      console.warn('tried to translate something with no translateable components.');
    }
  },

  // get all translateable elements from dom
  _getTranslateableFromDom: function(el){
    var translateables = {};
    _.each(el.find("*[data-i18n^='amp.']"), function(i, val) {
      var key = $(i).attr('data-i18n');
      var value = $(i).text();
      translateables[key] = value;
    });
    return translateables;
  },

  // get all translateable elements from dom
  _updateDom: function(data) {
    $.each(data, function(key, value) {
        $("*[data-i18n='" + key + "']").text(value);
    });
  },

  _getTranslations: function(translateables, lng){
    var url = APIHelper.getAPIBase() + '/rest/translations/label-translations';

    //lng is optional, if not provided rely on server to choose.
    if(lng){
      url += '/'+lng;
    }
    return this._postJSON(url, translateables);
  },

  // helper to wrap post call
  _postJSON: function(url, data) {
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
  }
};
