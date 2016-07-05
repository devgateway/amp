var fs = require('fs');
var _ = require('underscore');
var BaseControlView = require('../../base-control/base-control-view');
var FundingLayersView = require('./funding-layers-view');
var IndicatorLayersView = require('./indicator-layers-view');
//var MyLayers = require('./admin-mylayers-view');

var Template = fs.readFileSync(__dirname + '/../templates/multisection-layers-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-layers-sd',
  title: 'Statistical Data',
  iconClass: 'ampicon-layers',
  subSections: [
    //MyLayers,
    IndicatorLayersView,
    FundingLayersView,
  ],
  radioButtonGroup: [],

  template: _.template(Template),

  initialize: function() {
    BaseControlView.prototype.initialize.apply(this, arguments);  // sets this.app and this.baseTemplate
  },

  render: function() {
    // TODO: find a better way to keep our proxy collection up to date
    // Thad do you know a good pattern for this?
    BaseControlView.prototype.render.apply(this);

    this.$('.content').html(this.template({title: this.title}));

    var self = this;

    _.each(this.subSections, function(SectionView) {
      /* Note: This object will access the radioButtonGroup
      * of this parent BaseControlView */
      var section = new SectionView({app: self.app, parent: self});

      /* For Mutual Exclusion: */
      self.radioButtonGroup.push(section.collection);

      self.$('.content', self).append(section.render().el);
    });
    return this;
  }

});
