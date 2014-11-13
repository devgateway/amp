var fs = require('fs');
var _ = require('underscore');
var chartUtils = require('../../../libs/local/chart-util');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/datasources-item-adm-clusters.html', 'utf8');


module.exports = Backbone.View.extend({
  tagName: 'tbody',

  template: _.template(Template),
  initialize: function(options) {
    this.app = options.app;

    _.bindAll(this, 'render');
  },


  render: function() {
    var self = this;
    this.collection.load().then(function() {

      //TODO: inefficient to constantly redraw (if already on page), put in temp obj first.
      // then only append once.
      self.collection.each(function(project) {
        // it joins on activity init, but for some reason it was being overridden...
        // temp dirty force rejoin for now.
        project.tempDirtyForceJoin().then(function() {
          self.$el.append(self.template({
            activity: project.toJSON(),
            formattedCommitments: chartUtils.formatKMB()(project.toJSON()['Actual Commitments']),
            formattedDisbursements: chartUtils.formatKMB()(project.toJSON()['Actual Disbursements'])
          }));
        });
      });

    });
    return this;
  }

});
