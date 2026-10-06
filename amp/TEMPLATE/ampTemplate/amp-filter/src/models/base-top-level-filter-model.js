var Backbone = require('backbone');

// Parent model for fitlers.
module.exports = Backbone.Model.extend({
    defaults: {
        name: 'Filter Name',
        modelType: 'TREE' /*TODO: This should come from the EP. Possible values (for now) are 'TREE', 'DATE-RANGE-VALUES', 'YEAR-SINGLE-VALUE'*/
    },

    initialize: function () {

    }
});
