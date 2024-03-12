var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');

var MessageView = Backbone.View.extend({

    template: _.template('<div class="alert alert-<%=message.type||\'danger\'%>" role="alert"><strong><%=message.title||\'Error\'%></strong> <%=message.description||\'\'%></div>'),

    initialize: function (options) {
        var self = this;
        _.bindAll(this, 'render', 'renderMessage');
        this.on("MESSAGE", function(message) {
            self.renderMessage(message);
            self.$el.show().delay(5000).fadeOut()
        });
    },

    render: function() {
        this.renderMessage();
        return this;
    },
    renderMessage: function(message) {
        message = message || {};
        this.$el.html(this.template({message: message}));
        this.$el.css({
            position: 'absolute',
            left: '300px',
            bottom: '-20px',
            zIndex: '1000',
            display: 'none'
        });
    }
}, {
    INSTANCE: undefined,
    getInstance: function () {
        MessageView.INSTANCE = MessageView.INSTANCE || new MessageView();
        return MessageView.INSTANCE
    }
});
module.exports = MessageView;