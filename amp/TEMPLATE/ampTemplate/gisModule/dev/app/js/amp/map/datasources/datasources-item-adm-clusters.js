var fs = require('fs');
var _ = require('underscore');
var d3 = require('d3-browserify');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/datasources-item-adm-clusters.html', 'utf8');


module.exports = Backbone.View.extend({
  tagName: 'tbody',

  template: _.template(Template),
  initialize: function(options) {
    this.app = options.app;

    _.bindAll(this, 'render');
  },

  /* format for Kilo/Million/Billion quantities */
  // drs: this is a duplicate of a function already in util
  formatKMB: function(precision) {
    var formatSI = d3.format('.' + (precision || 3) + 's');
    return function(value) {
      return formatSI(value)
        .replace('G', 'B');  // now just need to convert G Gigia -> B Billion
    };
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
            formattedCommitments: self.formatKMB()(project.toJSON()['Actual Commitments']),
            formattedDisbursements: self.formatKMB()(project.toJSON()['Actual Disbursements'])
          }));
        });
      });

    });
    return this;
  }

});
