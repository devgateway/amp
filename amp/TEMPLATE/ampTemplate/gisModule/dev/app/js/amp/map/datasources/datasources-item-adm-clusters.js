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

      self.app.data.settings.load().then(function() {
        //TODO: inefficient to constantly redraw (if already on page), put in temp obj first.
        // then only append once.
        self.collection.each(function(project) {
          // it joins on activity init, but for some reason it was being overridden...
          // temp dirty force rejoin for now, otherwise use: getJoinedVersion
          var ampFormatter = new chartUtils.DecimalFormat(self.app.data.settings.get('number-format').get('name'));
          project.tempDirtyForceJoin().then(function() {

            var formattedCommitments = ampFormatter.format(project.toJSON()['Actual Commitments']);
            var formattedDisbursements = ampFormatter.format(project.toJSON()['Actual Disbursements']);
            var currencyCode = self.app.data.settings.get('1').get('selected');

            self.$el.append(self.template({
              activity: project.toJSON(),
              formattedCommitments: [formattedCommitments, ' ', currencyCode].join(''),
              formattedDisbursements: [formattedDisbursements, ' ', currencyCode].join('')
            }));
          });
        });

      });

    });
    return this;
  }

});
