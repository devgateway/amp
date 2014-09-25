var fs = require('fs');
var _ = require('underscore');

var BaseControlView = require('../../base-control/base-control-view');
var ShareMapToolView = require('./share-map-tool-view');
var ExportMapToolView = require('./export-map-tool-view');

var Template = fs.readFileSync(__dirname + '/../templates/tools-template.html', 'utf8');


module.exports = BaseControlView.extend({

  id: 'tool-tools',
  title: 'Share',
  iconClass: 'ampicon-share',
  description: 'Download the data in this map or share a link to it',

  template:  _.template(Template),

  events: {
    'click .gis-tool-export': 'exportOption',
    'click .gis-tool-share': 'share'
  },

  initialize: function() {
    BaseControlView.prototype.initialize.apply(this, arguments);  // attaches this.app, etc.
    this.savedMaps = this.app.data.savedMaps;
  },

  render: function() {
    BaseControlView.prototype.render.apply(this);
    this.$('.content').html(this.template({title: this.title}));
    return this;
  },

  loadSerialized: function(serializedState) {
    var stateBlob = this.savedMaps.model.deserializese(serializedState);
    this.app.state.load(stateBlob);
  },

  // can't call it export because that's a reserved word.
  exportOption: function() {
    this.show(new ExportMapToolView(), '.gis-tool-export');
  },

  share: function() {
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
  }

});
