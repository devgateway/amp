var fs = require('fs');
var Backbone = require('backbone');
var _ = require('underscore');
var Template = fs.readFileSync(__dirname + '/../templates/submenu-template.html', 'utf8');


module.exports = Backbone.View.extend({
  tagName: 'li',
  className: 'dropdown',
  events: {
    'click .language': 'switchLanguage',
    'click .popup': 'openPopup',
    'click .about-amp': 'about'
  },

  template: _.template(Template),

  initialize:function(options) {
    _.bindAll(this, 'render','switchLanguage','about');
  },

  render: function() {
      var self = this;
      this.$el.html(this.template(self.model.attributes));

      return this;
    },
   switchLanguage:function (event) {
       event.preventDefault();
	   var index = event.currentTarget.href.lastIndexOf ('/');
	   var lng = event.currentTarget.href.substr (index +1);
	   //if the SwithLanguageMenu exists, let the jsp handle how to change language.
	   //TODO trigger an event and handle on the jsp
	   if (typeof SwitchLanguageMenu !== 'undefined' && $.isFunction(SwitchLanguageMenu)) {
		   var url = "/translation/switchLanguage.do?code="+lng+"&rfr=";
		   SwitchLanguageMenu (url);
	   }
	   else {
		   this.trigger ('switchLanguage',{language:lng});
	   }
	   /*var self = this;
	   this.Translator.setLanguage(lng).then(function() {
		   self.Translator.translateDOM(document); 
       });*/
	  
   },
   openPopup:function (event) {
	   window.name = "opener" + new Date().getTime();
	   var wndWidth = 768; 
	   var wndHeight = 1024;
	   var t = ((screen.width)-768)/2;
	   var l = ((screen.height)-1024)/2;
	   var wparams = "height=" + wndHeight + ",width=" + wndWidth + ",top=" + l + ",left=" + t +",menubar=no,scrollbars=yes";
	   popupPointer = window.open(event.currentTarget.href, "forumPopup", wparams);
	   event.preventDefault();
	   return popupPointer;
   },
   about:function (event) {
	   this.trigger ('showAbout',{});
	   return false;
   }

});

