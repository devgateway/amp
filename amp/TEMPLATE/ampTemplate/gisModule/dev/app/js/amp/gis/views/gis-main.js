var fs = require('fs');
var $ = require('jquery');
var Backbone = require('backbone');
var MapView = require('../../map/views/main-view');
//var DataQualityView = require('../../dataquality/views/dataquality-view');
var SidebarView = require('../../sidebar/sidebar-view');
var ModuleTemplate = fs.readFileSync(__dirname + '/../templates/module-template.html', 'utf8');
var ResizeSensor = require('css-element-queries/src/ResizeSensor');
var boilerplate = require('amp-boilerplate');

function updateMapContainerSidebarPosition() {
    var top = $('#amp-header-menu').height() - 4;
    $('#sidebar').css('top', top + 'px');
    $('#map-container').css('top', top + 'px');
}

function loadResizeSensor() {
	new ResizeSensor($('#amp-header-menu'), updateMapContainerSidebarPosition);
	updateMapContainerSidebarPosition();
}
function fetchDataAndCheckLoginRequired() {
  return new Promise((resolve, reject) => {
    // Fetch settings data
    fetch('/rest/amp/settings')
        .then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then(data => {
          // Extract the login-required field
          const loginRequired = data['login-required'];

          // Perform actions based on the loginRequired value
          console.log('Login required:', loginRequired);

          if (loginRequired) {
            // If login is required, fetch user login status
            return fetch('/rest/amp/user-logged-in');
          } else {
            // If login is not required, resolve with a message
            resolve('Login is not required.');
          }
        })
        .then(response => {
          // Check user login status
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          return response.json();
        })
        .then(userData => {
          // Perform actions based on user login status
          const isLoggedIn = userData.isLoggedIn;

          if (isLoggedIn) {
            // If user is logged in, resolve with the user data
            resolve({ isLoggedIn, userData });
          } else {
            // If user is not logged in, resolve with a message
            resolve('User is not logged in.');
          }
        })
        .catch(error => {
          // Handle errors, such as network issues or errors returned by the API
          console.error('Error fetching data:', error);
          reject(error);
        });
  });
}


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

    

  },
  createViews: function(){
	  this.mapView = new MapView({app: this});
	    /* this.dataQualityView = new DataQualityView({app: this}); */
	  this.sidebarView = new SidebarView({app: this}); 
  },
  // Render entire geocoding view.
  render: function() {
    fetchDataAndCheckLoginRequired()
        .then(result => {
          // Handle success
          if (result.isLoggedIn===false) {
            // User is logged in, do something
            alert('User is not logged in');
            window.location.href="/";
          }
          else
          {
            this.$el.html(ModuleTemplate);

            this.mapView.setElement(this.$('#map-container')).render();
            /* this.dataQualityView.setElement(this.$('#quality-indicator')).render();*/
            this.sidebarView.setElement(this.$('#sidebar-tools')).render();

            //auto-render the layout
            var headerWidget = new boilerplate.layout(
                {
                  callingModule: 'GIS',
                  showDGFooter: false,
                  useSingleRowHeader: true
                });
            $.when(headerWidget.layoutFetched).then(function() {
              $('.dropdown-toggle').dropdown();

              $.when(headerWidget.header.menuRendered).then(function() {
                loadResizeSensor();
              });
            });

            // update translations
            this.translator.translateDOM(this.el);
            this.translationToggle();

            // Translate parts of leaflet UI.
            var leafletZoomIn = $('.leaflet-control-zoom-in');
            $(leafletZoomIn).attr('data-i18n', 'amp.gis:leaflet-button-zoom-in[title]');
            var leafletZoomOut = $('.leaflet-control-zoom-out');
            $(leafletZoomOut).attr('data-i18n', 'amp.gis:leaflet-button-zoom-out[title]');

            /* TODO(thadk): test without app here? this?*/
            app.translator.translateDOM('.leaflet-control-zoom');
          }
        });

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
