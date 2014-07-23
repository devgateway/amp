var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');

var Template = fs.readFileSync(__dirname + '/../templates/node-template.html', 'utf8');

var TreeNodeModel = Backbone.Model.extend({  initialize: function() {}});

var TreeNodeView = Backbone.View.extend({

  tagName: 'li',
  className: 'parent_li',

  //TODO: debug after usability testing, currently setting in addUIListeners
  // won't work on second use of widget.
  // events: {
  //   'click .selectable': 'select',
  //   'click  .toggle-nav': 'toggle'
  // },

  template: _.template(Template),

  // TODO: tkeep ref to parent and add 'update parent' to add / remove .half-fill when needed

  initialize: function(obj){
    var self = this;

    //iterate over children
    if (Array.isArray(obj.children)) {
      this.children = [];
      _.each(obj.children, function(child){
        self.children.push(new TreeNodeView(child));
      });
    }

    // create model
    delete obj.children;  // remove children from model, already in view
    obj.selected = true;  // default is selected.
    obj.expanded = false;
    this.model = new TreeNodeModel(obj);
    
    this._addModelListeners();

  },


  render: function(){

    //render this node
    this.$el.html(this.template(this.model.toJSON()));
    this.renderChildren();

    return this.el;
  },

  renderChildren: function () {
    var self = this;
    var ul = $('<ul>');
    this.$el.append(ul);

    if(!_.isEmpty(this.children)){
      _.each(this.children, function(child){
        ul.append(child.render());
      });
    } else{
      this.$('.expanded').remove();
    }
    this.updateSelection();
    this.updateExpanded(ul);
    this._addUIListeners();
  },

  _addUIListeners: function(){
    var self = this;
    this.$('> span > .selectable').on('click', function(evnt){
      evnt.stopPropagation();
      self.select(evnt);

    });
    this.$('> span > .toggle-nav').on('click', function(evnt){
      evnt.stopPropagation();
      self.toggle(evnt);
    });
  },

  _addModelListeners: function(){
    var self = this;

    //Add model listeneres
    this.model.on('change:selected', function (model, argument, options) {
      if (options.propagation) {
        Backbone.trigger('NODE_SELECTED', self.model);
      }
      self.updateSelection();
      self.updateChildNodes();
    });

    this.model.on('change:expanded', function (model, argument, options) {
      self.updateExpanded();
    });

  },

  updateSelection: function () {
    if (this.model.get('selected')) {
      this.$('> span > .selectable').addClass('label-success');
    } else {
      this.$('> span > .selectable').removeClass('label-success');
    }
  },

  updateExpanded: function(ul){
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

  updateChildNodes: function () {
    var self = this;
    if (this.children) {
      this.children.forEach(function (childView) {
        childView.model.set('selected', self.model.get('selected'), {propagation: false});
      });
    }
  },

  select: function (evnt) {
    this.model.set('selected', !this.model.get('selected'));
    evnt.stopPropagation();
  },


  toggle: function (evnt) {
    // if we have children expand
    if(!_.isEmpty(this.children)){
      if (this.model.get('expanded')) {
        this.model.set('expanded', false);
      } else {
        this.model.set('expanded', true);
      }
    } else{
      //no children, so toggle checkmark...may be confusing and should remove this behaviour.
      this.select(evnt);
    }
    evnt.stopPropagation();
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
