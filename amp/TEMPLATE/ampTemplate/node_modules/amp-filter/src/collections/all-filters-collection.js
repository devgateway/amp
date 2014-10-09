var Backbone = require('backbone');
var _ = require('underscore');
var $ = require('jquery');

var GenericFilterModel = require('../models/generic-filter-model');
var YearsFilterModel = require('../models/years-filter-model');


//TODO: move most code from filters-view here.
module.exports = Backbone.Collection.extend({
  url: '/rest/filters',
  _loaded: null,
  _allDeferreds: [],

  initialize: function() {
    this._loaded = new $.Deferred();
    this.on('add', this._cleanUpAfterAdd);
  },

  load: function() {
    var self = this;
    this.fetch().then(function() {
      // when all child calls are done resolve.
      $.when.apply($, self._allDeferreds).then(function() {
        self._loaded.resolve();
      });
    });

    return this._loaded;
  },

  _cleanUpAfterAdd: function(model) {
    var self = this;
    // remove if ui false also catches empty dummy filters we add in 'model' function below.
    if (!model.get('ui')) {
      self.remove(model);
    }
  },


  model: function(attrs, options) {
    var tmpModel = null;

    // slightly unconventional, but model is special case since it's called with
    // new so 'this' wont be the collection.
    var self = options.collection;

    // switch for model polymorphism.
    switch (attrs.name) {
      case 'Programs':
      case 'Sectors':
        self._allDeferreds.push(self._goOneDeeper(self, attrs));
        //tmp hack because we need to return something.
        tmpModel = new Backbone.Model({ui:false});
        break;
      case 'organizationsRoles':
        self._allDeferreds.push(self._goOneDeeperSpecialOrgRoles(self, attrs));
        //tmp hack because we need to return something.
        tmpModel = new Backbone.Model({ui:false});
        break;
      case 'Dates':
        tmpModel = new YearsFilterModel(attrs);
        break;
      // case 'ActivityBudgetList':
      // case 'TypeOfAssistanceList':
      // case 'FinancingInstrumentsList':
      // case 'ActivityStatusList':
      // case 'ActivityApprovalStatus':
      // case 'Organizations':
      // case 'OrganizationGroupList':
      // case 'OrgTypesList':
      default:
        tmpModel = new GenericFilterModel(attrs);
    }

    return tmpModel;
  },

  // get endpoint's children and load them into targetCollection...
  _goOneDeeper: function(targetCollection, attrs) {
    var url = attrs.endpoint;
    var deferred = $.Deferred();

    var tmpCollection = new Backbone.Collection();
    tmpCollection.url = url;
    tmpCollection.fetch().then(function(data) {
      _.each(data, function(APIFilter) {
        var tmpModel = new GenericFilterModel({
          url: url + '/' + APIFilter.id,
          title: APIFilter.name,
          ui: true,
          group: attrs.name
        });
        targetCollection.add(tmpModel);
      });

      deferred.resolve();

      if (_.isEmpty(data)) {
        console.warn('Filters API returned empty', data);
      }
    });

    return deferred;
  },

  // get endpoint's children and load them into targetCollection...
  _goOneDeeperSpecialOrgRoles: function(targetCollection, attrs) {
    var url = attrs.endpoint;
    var deferred = $.Deferred();
    var allOrgDeferreds = [];
    var tmpCollection = new Backbone.Collection();

    tmpCollection.url = url;
    tmpCollection.fetch({type:'POST', data: '{}'})
    .done(function(data) {
      _.each(data, function(APIFilter) {
        var tmpModel = new GenericFilterModel({
          url:'/rest/filters/organizations/',
          title: APIFilter.name,
          ui: true,
          group: attrs.name
        });

        allOrgDeferreds.push(tmpModel.fetch({data: {ampRoleId: APIFilter.id}}).then(function(data) {
          if (data && data.length > 0) {
            targetCollection.add(tmpModel);
          }
        }));

      });

      $.when.apply($, allOrgDeferreds).then(function() {
        deferred.resolve();
      });

      if (_.isEmpty(data)) {
        console.warn('Filters API returned empty', data);
      }
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
      var errorMessage = 'Getting filters failed';
      console.error('Getting filters failed', jqXHR, textStatus, errorThrown);
      deferred.reject(errorMessage);
    });

    return deferred;
  },



  //TODO: in progress...
  setupOrgListener:function() {
    // var orgFilter = this.findWhere({title: 'Organizations'});
    var orgGroupFilter = this.findWhere({title: 'OrganizationGroupList'});
    var orgTypeFilter = this.findWhere({title: 'OrgTypesList'});

    orgTypeFilter.getTree().then(function(tree) {
      // only listens to children...won't do nested types...
      tree.get('children').on('change:numSelected', function(type) {
        orgGroupFilter.getTree().then(function(groupTree) {
          groupTree.get('children').each(function(group) {
            //console.log('group', group);
            if (group.TypeID === type.id) {
              group.set('selected', true);
            }
          });
        });

      });

      //TODO: setup 'Organizations' to listen to 'OrganizationGroups'
    });
  }
});
