var fs = require('fs');
var $ = require('jquery');
var Backbone = require('backbone');
var MapView = require('../../map/views/main-view');
var DataQualityView = require('../../dataquality/views/dataquality-view');
var SidebarView = require('../../sidebar/sidebar-view');

var AmpNavTemplate = fs.readFileSync(__dirname + '/../templates/amp-nav-template.html', 'utf8');
var ModuleTemplate = fs.readFileSync(__dirname + '/../templates/module-template.html', 'utf8');


module.exports = Backbone.View.extend({

  data: null,  // data attaches here

  initialize: function(options) {
    this.data = options.data;
    this.display = options.display;
    this.mapView = new MapView({app: this});
    this.dataQualityView = new DataQualityView({app: this});
    this.sidebarView = new SidebarView({app: this});
  },

  // Render entire geocoding view.
  render: function () {
    this.$el.html(ModuleTemplate);

    this.mapView.setElement(this.$('#map-container')).render();
    this.dataQualityView.setElement(this.$('#quality-indicator')).render();
    this.sidebarView.setElement(this.$('#sidebar-tools')).render();

    // just for testing...
    this.renderStaticAmpTemplate();

    // update translations
    this.translator.translateDOM(this.el);
    this.translationToggle();
  },

  translationToggle: function(){
    var self = this;
    this.$('.lang-change').click(function(evt){
      evt.preventDefault();
      var lng = $(this).data('lng');
      self.translator.setLanguage(lng).then(function(){
        self.translator.translateDOM(self.el);
      });
    });
  },

  // not a view, because it's static and just for testing.
  renderStaticAmpTemplate: function(){
    $('#amp-menu').html(AmpNavTemplate);
    // TODO: If it's our responsibility...
    // render translation selector using: this.translator.getAvailableLanguages
  }
});
