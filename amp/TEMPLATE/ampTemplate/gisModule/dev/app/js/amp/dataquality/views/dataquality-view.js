define(
  [
    'underscore',
    'backbone',
    'text!amp/dataquality/templates/dataquality-template.html',
    'amp/dataquality/models/dataquality-model'
  ],
  function (_, Backbone, Template, DataQualityModel) {
    'use strict';

    var DataQuality = Backbone.View.extend({
      minGoodScore: 66,
      minWarningScore: 33,
      currentScore: -1, // Should be between 0 and 100 for 'real' values

      template: _.template(Template),

      initialize: function () {
        this.model = new DataQualityModel();
      },

      render: function () {

        this.$el.html(this.template({}));
        this.updateScore();
      },


      // TODO make sure updateScore is triggered by map filter changes.
      updateScore: function(){
        this.currentScore = this.model.getScore();
        var styleClass = 'info';


        if(this.currentScore >= this.minGoodScore && this.currentScore <= 100){
          styleClass = 'success';

        } else if(this.currentScore >= this.minWarningScore && this.currentScore < this.minGoodScore){
          styleClass ='warning';

        } else if(this.currentScore >= 0 && this.currentScore < this.minWarningScore){
          styleClass = 'danger';

        } else {
          console.warn("data quality score is outside of acceptable range.");
        }

        this.$('.label').removeClass().addClass('label label-' + styleClass);
        this.$('.glyphicon').removeClass().addClass('glyphicon glyphicon-record text-' + styleClass);
      }
    });

    return DataQuality;
  }
);
