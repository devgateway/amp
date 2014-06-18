define(
  [
    'underscore',
    'backbone',
    'text!' + APP_ROOT + '/amp/sidebar/base-control/base-control-template.html',
  ],
  function (_, Backbone, BaseTemplate, Template) {
    'use strict';

    /* jshint ignore:start */
    var unset = new Object();  // singleton so we no if it has been set
    /* jshint ignore:end */

    var BaseToolView = Backbone.View.extend({
      /*
       * Base view for all tool views
       *
       * Child views should access parent methods via the prototype:
       *    `BaseToolView.prototype.method.apply(this);`
       */


      // Required properties to be overridden by child classes
      title: unset,
      iconClass: unset,
      description: unset,

      initialize: function () {
        this.baseTemplate = _.template(BaseTemplate);
      },

      render: function() {
        this.$el.html(this.baseTemplate({
          title: this._getFromChild('title'),
          titleID: this._getFromChild('title').replace(/ /g, '').toLowerCase(),
          iconClass: this._getFromChild('iconClass'),
          description: this._getFromChild('description')
        }));
      },

      _getFromChild: function(property) {
        var value = this[property];
        if (value === unset) {
          console.error('No value set for property "' + property + '" on ', this);
          return undefined;
        }
        return value;
      }

    });

    return BaseToolView;
  }
);
