var fs = require('fs');
var _ = require('underscore');
var BaseControlView = require('../../base-control/base-control-view');

var SavedMapModel = require('../models/saved-map-model');
var SavedMaps = require('../collections/saved-map-collection');
var Template = fs.readFileSync(__dirname + '/../templates/tools-template.html', 'utf8');
var SavedMapsTemplate = fs.readFileSync(__dirname + '/../templates/saved-maps-template.html', 'utf-8');

var state = require('../../../services/state');


module.exports = BaseControlView.extend({

  id: 'tool-tools',
  title:  'Share',
  iconClass:  'ampicon-share',
  description:  '',

  template:  _.template(Template),
  savedMapsTemplate: _.template(SavedMapsTemplate),

  events: {
    'click .gis-tool-save': 'showSave',
    'click .gis-tool-save-save': 'reallySave',
    'click .gis-tool-load': 'load',
    'click .gis-tool-load-url': 'loadUrl',
    'click .gis-state-link': 'loadLink',
    'click .gis-tool-export': 'export',
    'click .gis-tool-share': 'share'
  },

  initialize: function() {
    BaseControlView.prototype.initialize.apply(this, arguments);
    this.savedMaps = new SavedMaps();
    this.listenTo(this.savedMaps, 'add', this.renderSavedMapsList);
    this.listenTo(this.savedMaps, 'remove', this.renderSavedMapsList);
  },

  render: function() {
    BaseControlView.prototype.render.apply(this);
    this.$('.content').html(this.template({title: this.title}));
    return this;
  },

  renderSavedMapsList: function() {
    var renderedList = this.savedMapsTemplate({maps: this.savedMaps});
    this.$('.available-maps').html(renderedList);
  },

  showSave: function() {
    this.$('.gis-tool-save-form').toggleClass('hidden');
  },

  getStateModel: function() {
    return new SavedMapModel({
      title: this.$('#save-title').val(),
      description: this.$('#save-desc').val(),
      stateBlob: state.freeze()
    });
  },

  reallySave: function() {
    var currentStateModel = this.getStateModel();
    this.savedMaps.add(currentStateModel);
    this.savedMaps.sync();
  },

  load: function() {
    this.$('.gis-tool-load-form').toggleClass('hidden');
    this.savedMaps.fetch();
  },

  loadUrl: function() {
    var serializedState = this.$('#load-url').val().slice(1);  // trim #
    this.loadSerialized(serializedState);
  },

  loadLink: function(e) {
    var serializedState = e.currentTarget.hash.slice(1);  // trim the "#"
    this.loadSerialized(serializedState);
  },

  loadSerialized: function(serializedState) {
    var stateBlob = SavedMapModel.deserialize(serializedState);
    state.load(stateBlob);
  },

  export: function() {
    this.$('.gis-tool-export-form').toggleClass('hidden');
    // ???
  },

  share: function() {
    var currentStateModel = this.getStateModel();
    this.$('#share-url').val('#' + currentStateModel.serialize());
    this.$('.gis-tool-share-form').toggleClass('hidden');
    // TODO: social share buttons? embeddable widgets for news/blogs?
  }

});
