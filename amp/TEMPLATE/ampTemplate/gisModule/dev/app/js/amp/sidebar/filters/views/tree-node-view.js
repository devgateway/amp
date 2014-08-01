var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');

var Template = fs.readFileSync(__dirname + '/../templates/node-template.html', 'utf8');


var TreeNodeView = Backbone.View.extend({

  tagName: 'li',
  className: 'parent_li',

  //TODO: debug after usability testing, currently setting in addUIListeners
  // won't work on second use of widget.
  // events: {
  //   'click .selectable': 'select',
  //   'click  .toggle-nav': 'clickName'
  // },

  template: _.template(Template),

  initialize: function(){
  },


  render: function(model){
    this.model = model;
    this.$el.html(this.template(model.toJSON()));
    this.renderChildren();

    return this;
  },

  renderChildren: function () {
    var self = this;
    var ul = $('<ul>');
    this.$el.append(ul);

    var children = this.model.get('children');
    if(!children.isEmpty()){
      children.each(function(child){
        var tmpView = new TreeNodeView();
        ul.append(tmpView.render(child).$el);
      });
    }
    else{
      this.$('.expanded').remove();
      this.$('> span > span > .count').text('');
    }

    this._addModelListeners();
    this._addUIListeners();

    this._updateSelection();
    this._updateExpanded(ul);
  },

  _addModelListeners: function(){
    var self = this;

    //Add model listeneres
    this.model.on('change:selected', function (model, argument, options) {
      self._updateSelection();
    });

    this.model.on('change:expanded', function (model, argument, options) {
      self._updateExpanded();
    });

    this.model.on('change:numSelected', function(){
      self._updateCountUI();
    });

  },

  _addUIListeners: function(){
    var self = this;
    this.$('> span > .selectable').on('click', function(evnt){
      self.clickBox();

    });
    this.$('> span > .toggle-nav').on('click', function(evnt){
      self.clickName();
    });
  },

  _updateSelection: function () {
    this._updateCheckboxFill();
  },

  _updateCountUI: function(){
    if(!this.model.get('children').isEmpty()){
      this.$('> span > span > .count').text(this.model.get('numSelected')+ '/' +this.model.get('numPossible'));
      this._updateCheckboxFill();
    }
  },

  // For updating non-leaf nodes
  _updateCheckboxFill: function(){
    if(!this.model.get('children').isEmpty()){
      if(this.model.get('numSelected') > 0){
        if(this.model.get('numSelected') < this.model.get('numPossible')){
          this.$('> span > .selectable').addClass('half-fill');
          this.$('> span > .selectable').removeClass('label-success');
        } else {
          this.$('> span > .selectable').removeClass('half-fill');
          this.$('> span > .selectable').addClass('label-success');
        }
      } else if(this.model.get('numSelected') === 0){
        this.$('> span > .selectable').removeClass('half-fill');
        this.$('> span > .selectable').removeClass('label-success');
      }
    } else{ // else leaf node      
      if (this.model.get('selected')) {
        this.$('> span > .selectable').addClass('label-success');
      } else {
        this.$('> span > .selectable').removeClass('label-success');
      }
    }
  },

  _updateExpanded: function(ul){
    var iElement = this.$('> span > span > .expanded');
    if (this.model.get('expanded')) {
      this.expand();
      iElement.text('-');
    } else {
      this.collapse();

      // to run on first time...need to use ul, since el is not on DOM yet
      if(ul){
        ul.find('> li').hide();
      }

      iElement.text('+');
    }
  },


  clickBox: function () {
    this.model.set('selected', !this.model.get('selected'), {propagation: true});
  },


  clickName: function () {
    // if we have children expand
    if(!this.model.get('children').isEmpty()){
      this.model.set('expanded', !this.model.get('expanded'));
    } else{
      // leaf node, so pretend the clicked on the box
      this.clickBox();
    }
  },


  collapse: function () {
    var children = this.$el.find(' > ul > li');
    children.hide('fast');

  },

  expand: function () {
    var children = this.$el.find(' > ul > li');
    children.show('fast');
  },


});

module.exports = TreeNodeView;
