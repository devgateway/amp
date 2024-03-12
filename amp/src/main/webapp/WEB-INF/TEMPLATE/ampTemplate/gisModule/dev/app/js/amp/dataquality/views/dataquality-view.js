var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var DataQualityModel = require('../models/dataquality-model');
var Template = fs.readFileSync(__dirname + '/../templates/dataquality-template.html', 'utf8');


module.exports = Backbone.View.extend({
  minGoodScore: 66,     // Break point for good score
  minWarningScore: 33,  // Break point for warning score
  currentScore: -1,     // Should be between 0 and 100 for 'real' values

  template: _.template(Template),

  initialize: function() {
    this.model = new DataQualityModel();
  },

  render: function() {

    this.$el.html(this.template({}));
    this.updateScore();

    return this;
  },


  // TODO make sure updateScore is triggered on map changes.
  // Colours the icon according the current score.
  updateScore: function() {
    this.currentScore = this.model.getScore();
    var styleClass = 'info';


    if (this.currentScore >= this.minGoodScore && this.currentScore <= 100) {
      styleClass = 'success';

    } else if (this.currentScore >= this.minWarningScore && this.currentScore < this.minGoodScore) {
      styleClass = 'warning';

    } else if (this.currentScore >= 0 && this.currentScore < this.minWarningScore) {
      styleClass = 'danger';

    } else {
      console.warn('data quality score is outside of acceptable range.');
    }

    this.$('.label').removeClass().addClass('label label-' + styleClass);
    this.$('.glyphicon').removeClass().addClass('glyphicon glyphicon-record text-' + styleClass);
  }
});
