var fs = require('fs');
var $ = require('jquery');
var Backbone = require('backbone');
var MapView = require('../../map/views/main-view');
//var DataQualityView = require('../../dataquality/views/dataquality-view');
var SidebarView = require('../../sidebar/sidebar-view');

var boilerplate = require('amp-boilerplate');
var ModuleTemplate = fs.readFileSync(__dirname + '/../templates/module-template.html', 'utf8');

var UserModel = require('../../data/models/amp-user-model.js');

module.exports = Backbone.View.extend({

  data: null,  // data attaches here

  events: {
    'click .sidebar-icons-only-button': 'toggleIconOnlySidebar',
    'click .accordion-group': 'disableIconOnlySidebar'
  },

  initialize: function(options) {
    this.url = options.url;
    this.data = options.data;
    this.state = options.state;

    this.mapView = new MapView({app: this});
    /* this.dataQualityView = new DataQualityView({app: this}); */
    this.sidebarView = new SidebarView({app: this});
  },

  // Render entire geocoding view.
  render: function() {
    this.$el.html(ModuleTemplate);

    this.mapView.setElement(this.$('#map-container')).render();
    /* this.dataQualityView.setElement(this.$('#quality-indicator')).render();*/
    this.sidebarView.setElement(this.$('#sidebar-tools')).render();


    // just for testing...
    this.renderStaticAmpTemplate();

    // update translations
    this.translator.translateDOM(this.el);
    this.translationToggle();
  },

  translationToggle: function() {
    var self = this;
    this.$('.lang-change').click(function(evt) {
      evt.preventDefault();
      var lng = $(this).data('lng');
      self.translator.setLanguage(lng).then(function() {
        self.translator.translateDOM(self.el);
      });
    });
  },

  // not a view, because it's static and just for testing.
  renderStaticAmpTemplate: function() {

    /* Prepare the user data for the appropriate header */
    var userModel = new UserModel();

    var $header = $('#amp-header');
    var headerWidget = new boilerplate.headerObj(
      {
        caller: 'GIS'
      });
    window.boilerh = $header;

    $header.html(headerWidget.view.render().el);

    userModel.load().then(function(user) {
      if (user.get('email')) {
        //$('.container-fluid', $header).toggleClass('ampUserLoggedIn');
        $('#header-workspace', $header).text(user.get('workspace'));
        $('#header-name #header-first-name', $header).text(user.get('firstName'));
        $('#header-name #header-last-name', $header).text(user.get('lastName'));
      }
    });


    // TODO: If it's our responsibility...
    // render translation selector using: this.translator.getAvailableLanguages
  },

  toggleIconOnlySidebar: function(ev) {
    if ($('#sidebar', this.el).hasClass('sidebar-icons-only') !== true) {
      //Manually close all the uncollapsed (bootstrap accordion)
      //First mark the header as unhighlighted
      $('.accordion-group .panel .in', this.el).parent().find('.accordion-toggle').addClass('collapsed');
      //then hide the children contents using Bootstrap js
      $('.accordion-group .panel .in', this.el).collapse('hide');
      //$(this.el).find('.accordion-box .in').collapse('hide');

    }

    $(this.el).find('#sidebar').toggleClass('sidebar-icons-only');
    $(this.el).find('#map-container').toggleClass('sidebar-icons-only');

    $('i', ev.currentTarget).toggleClass('glyphicon-chevron-right');
    $('i', ev.currentTarget).toggleClass('glyphicon-chevron-left');

  },

  disableIconOnlySidebar: function() {
    $(this.el).find('#sidebar').removeClass('sidebar-icons-only');
    $(this.el).find('#map-container').removeClass('sidebar-icons-only');
    $(this.el).find('#sidebar > button > i').removeClass('glyphicon-chevron-right');
    $(this.el).find('#sidebar > button > i').addClass('glyphicon-chevron-left');
  }

});
