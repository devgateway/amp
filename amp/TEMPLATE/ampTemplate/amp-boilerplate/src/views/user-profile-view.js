var fs = require('fs');
var Backbone = require('backbone');
var _ = require('underscore');
require('bootstrap/dist/js/bootstrap');
var Template = fs.readFileSync(__dirname + '/../templates/user-profile-template.html', 'utf8');

module.exports = Backbone.View.extend({
  model: null,
  template: _.template(Template),
  id: 'userProfileModal',
  initialize: function (options) {
    this.model = options.model;
    this.render();
    _.bindAll(this, 'render');
  },
  render: function () {
    if (this.model) {
      var self = this;
      this.$el.html(this.template({
        properties: self.model
      }));
    }
    return this;
  },
  showUserProfile: function () {
    if (!this.profileLoaded) {
      this.profileLoaded = true;
    }
    location.replace('/TEMPLATE/reampv2/packages/container/build/index.html#/user-manager/edit-profile');
  }
});
