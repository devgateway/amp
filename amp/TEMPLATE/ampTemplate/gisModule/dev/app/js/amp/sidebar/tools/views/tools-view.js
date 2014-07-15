var fs = require('fs');
var _ = require('underscore');
var BaseToolView = require('../../base-control/base-control-view');

var SavedMapModel = require('../models/saved-map-model');
var SavedMaps = require('../models/saved-map-collection');
var Template = fs.readFileSync(__dirname + '/../templates/tools-template.html', 'utf8');
var SavedMapsTemplate = fs.readFileSync(__dirname + '/../templates/saved-maps-template.html', 'utf-8');

var state = require('../../../services/state');


module.exports = BaseToolView.extend({

  id: 'tool-tools',
  title:  'Tools',
  iconClass:  'ampicon-tools',
  description:  '',

  template:  _.template(Template),
  savedMapsTemplate: _.template(SavedMapsTemplate),

  events: {
    'click .gis-tool-save': 'showSave',
    'click .gis-tool-save-save': 'reallySave',
    'click .gis-tool-load': 'load',
    'click .gis-state-link': 'loadLink',
    'click .gis-tool-export': 'export',
    'click .gis-tool-share': 'share'
  },

  initialize: function() {
    BaseToolView.prototype.initialize.apply(this);
    this.savedMaps = new SavedMaps();
    this.listenTo(this.savedMaps, 'add', this.renderSavedMapsList);
    this.listenTo(this.savedMaps, 'remove', this.renderSavedMapsList);
  },

  render: function() {
    BaseToolView.prototype.render.apply(this);
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

  reallySave: function() {
    var saveableMap = new SavedMapModel({
      title: this.$('#save-title').val(),
      description: this.$('#save-desc').val(),
      stateBlob: state.freeze()
    });
    this.savedMaps.add(saveableMap);
  },

  load: function() {
    this.$('.gis-tool-load-form').toggleClass('hidden');
    this.savedMaps.fetch();
  },

  loadLink: function(e) {
    var serializedState = e.currentTarget.hash.slice(1);  // trim the "#"
    var stateBlob = SavedMapModel.deserialize(serializedState);
    state.load(stateBlob);
  },

  export: function() {
    this.$('.gis-tool-export-form').toggleClass('hidden');
    // ???
  },

  share: function() {
    this.$('.gis-tool-share-form').toggleClass('hidden');
    // drop-down a textbox with selected text of a share URL
    // TODO: social share buttons? embeddable widgets for news/blogs?
  }

});
