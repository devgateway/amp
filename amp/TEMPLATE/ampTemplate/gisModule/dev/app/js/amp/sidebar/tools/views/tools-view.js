var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');

var BaseControlView = require('../../base-control/base-control-view');
var ShareMapToolView = require('./share-map-tool-view');
var ExportMapToolView = require('./export-map-tool-view');
var PrintUtil = require('../util/print-util');

var Template = fs.readFileSync(__dirname + '/../templates/tools-template.html', 'utf8');


module.exports = BaseControlView.extend({

  id: 'tool-tools',
  title: 'Share',
  iconClass: 'ampicon-share',
  description: 'Download the data in this map or share a link to it',

  template:  _.template(Template),

  events: {
    'click .gis-tool-share': 'share',
    'click .gis-tool-img': 'print'
  },

  initialize: function() {
    BaseControlView.prototype.initialize.apply(this, arguments);  // attaches this.app, etc.
    this.savedMaps = this.app.data.savedMaps;

  },

  render: function() {
      var self = this;
      BaseControlView.prototype.render.apply(this);
      var renderedTemplate = $(this.template({title: this.title}));

      // needed to wait until 'el' exists before creating and rendering, or events break.
      self.app.data.user.load().then(function () {
          $.when(self.app.data.generalSettings.loaded).then(function () {
              var hideEditableFormatSetting = self.app.data.generalSettings.get('hide-editable-export-formats-public-view');
              if (!(hideEditableFormatSetting == true && self.app.data.user.get("logged") == false)) {
                  self.exportMapToolView = new ExportMapToolView({
                      app: self.app,
                      el: renderedTemplate.find('.form-group')
                  });
                  self.exportMapToolView.render();
              }
              var showImageButton = self.app.data.generalSettings.get('download-map-selector');
              if (showImageButton && showImageButton == true) {
                  self.$('.gis-tool-img').show();
              } else {
                  self.$('.gis-tool-img').hide();
              }
          });
      });

      this.$('.content').html(renderedTemplate);

      return this;
  },  
  loadSerialized: function(serializedState) {
    var stateBlob = this.savedMaps.model.deserializese(serializedState);
    this.app.state.load(stateBlob);
  },


  share: function() {
    $('#map-loading').show();
    var currentStateModel = this.savedMaps.create({  // create does POST
      title: this.$('#save-title').val(),
      description: this.$('#save-desc').val(),
      stateBlob: this.app.state.freeze()
    });
    this.show(new ShareMapToolView({
      app: this.app,
      model: currentStateModel
    }), '.gis-tool-share');
  },

  show: function(subView, toolClass) {
    this.$('.gis-tool').removeClass('active');
    this.$(toolClass).addClass('active');
    this.$('.gis-tool-share-form').html(subView.render().el);
  },

  print: function() {
    var self = this;
    var options = {
        success: function(response) {
            var a = document.createElement('a');
            document.body.appendChild(a);
            a.style = 'display: none';
            a.download = 'new-gis.png';
            self.fakeClick(a, response);
            $('#map-loading').hide();
            self.toggleButton(self, true);
        },
        error: function(response) {
            var messageBox = self.$('.alert');
            messageBox.show();
            $('#map-loading').hide();
            self.$('.alert .close').on('click', function(e) {
                $(this).parent().hide();
            });
            self.toggleButton(self, true);
        }
    };
    $('#map-loading').show();
    this.toggleButton(this);
    PrintUtil.printMap(options);
  },
  toggleButton: function(self, blur) {
      var button = self.$('.gis-tool-img');
      button.toggleClass('disabled');
      button.toggleClass('btn-success');
      if(blur) {
        button.blur();
      }
  },
  createBlob: function (response) {
    var byteCharacters = atob(response);
    var charCodeFromCharacter = function (c) {
        return c.charCodeAt(0);
    };
    var byteArrays = [];
    for (var offset = 0; offset < byteCharacters.length; offset += 1000) {
        var slice = byteCharacters.slice(offset, offset + 1000);
        var byteNumbers = Array.prototype.map.call(slice, charCodeFromCharacter);
        byteArrays.push(new Uint8Array(byteNumbers));
    }
    var blob = new Blob(byteArrays, {type: "image/png"});
    return blob;
  },
  fakeClick: function(anchor, response) {
    if(anchor) {
        try {
            var blob = this.createBlob(response);
            var URLObj = window.URL || window.webkitURL;
            var url = URLObj.createObjectURL(blob);
            anchor.href = url;
            anchor.click();
        } catch (e) {
            var blob = this.createBlob(response);
            if(window.navigator.msSaveBlob) {
                window.navigator.msSaveBlob(blob, 'new-gis.png');
            } else {
                var URLObj = window.URL || window.webkitURL;
                var url = URLObj.createObjectURL(blob);
                window.open(url);
            }
        }
    } else {
        console.error("null anchor")
    }
  }
});
