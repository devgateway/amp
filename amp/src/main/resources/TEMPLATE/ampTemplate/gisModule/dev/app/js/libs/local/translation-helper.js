var $ = require('jquery');
var _ = require('underscore');

var translator = require('../../amp/services/translator');

// var i18next = require('i18next');
// TODO: talk to Julian about i18next and if we plan on supporting that style in our api
// sending language in param etc...and how we will deal with calls where the server auto chooses langauge.
// may need to pretend i18 is in en in those cases.
// jquery i18n docs:
//   http://i18next.com/pages/doc_jquery.html
// .i18n(); would be useful instead of manual scrape and replace for data-18n...

// modifying from Julian's sample.
module.exports = {

  setLanguage: function(lng) {
    return translator.setLanguage(lng);
  },

  // Phil: the 'el' object is a jquery element.
  // what is the preferred naming convention for jQuery object?
  // something with a $ in it right?
  setTranslationsOnDOM: function(el) {
    var self = this;

    return translator.getTranslations().then(function(data) {
      self._updateDom(el, data);
    });
  },

  // update translateable elements in the dom
  _updateDom: function(el, data) {
    $.each(data, function(key, value) {
      el.find('*[data-i18n="' + key + '"]').text(value);
    });
  },

  // get all translateable elements from dom
  // (not needed since we trust our initial json)
  _getTranslateableFromDom: function(el) {
    var translateables = {};
    _.each(el.find('*[data-i18n^="amp."]'), function(i) {
      var key = $(i).attr('data-i18n');
      var value = $(i).text();
      translateables[key] = value;
    });
    return translateables;
  }

};
