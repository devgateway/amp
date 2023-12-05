var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var Backbone = require('backbone');
var Setting = require('../models/indicator-layer');

module.exports  = Backbone.Collection.extend({
	model : Setting,
	url: function() {
		return '/rest/indicator/indicator-layer/?'
			+ 'offset='	+ ((this.page.get('currentPageNumber') - 1) * this.page.get('recordsPerPage'))
			+ '&count=' + this.page.get('recordsPerPage')
			+ '&orderby=' + (this.page.get('orderBy') || '')
			+ '&sort=' + (this.page.get('sort') || '');
	},
	parse: function (response) {
		if(response.page) {
			response.page.orderBy = this.page.get('orderBy');
			response.page.sort = this.page.get('sort');
		}
		this.page = new Backbone.Model(response.page);
		return response.data;
	},
	page: new Backbone.Model({
		pageArea: null,
		recordsPerPage: 10,
		currentPageNumber: 1,
		totalPageCount: 1,
		totalRecords: 10,
		orderBy: 'createdOn',
		sort: 'desc'
	}),
 });