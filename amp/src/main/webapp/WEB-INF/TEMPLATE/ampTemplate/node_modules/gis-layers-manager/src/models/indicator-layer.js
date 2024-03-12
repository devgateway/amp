var _ = require('underscore');
var Backbone = require('backbone');
module.exports = Backbone.Model.extend({
  urlRoot: '/rest/indicator/indicator-layer/',
  defaults: {
	    name: {},
	    unit: '',
	    description: {},
	    numberOfClasses: 0
  },
  LENGTH_TO_SHOW: 25,
  initialize: function(){
  },
  url: function(){
	  return '/rest/indicator/indicator-layer/';
  },
  parse : function(resp, xhr) {
	 var data =(resp.data) ? resp.data: resp;
	 data.name = this.parseJSON(data.name);
	 data.description = this.parseJSON(data.description);
     return data;
  },
  parseJSON: function(str){
	 try {
	        return JSON.parse(str);
	    } catch (e) {
	        return str;
	    }
   }, 
   deleteLayer: function(options){
	 //TODO change to use backbone destroy. destroy fails because of incorrect url
	 $.ajax({
         url : this.url() + this.id,
         type : 'DELETE',
         success :options.success
  });
 },
 update: function(success) {
     return this.save(null, {
         type: 'POST',
         success: success
     });

 },
 getField: function (field, settings) {
	 var currentLanguage = settings.get('language');
	 var defaultLanguage = settings.get('default-language');
	 var result = '';
	 if(!_.isUndefined(this.get(field))){
		 result = this.get(field)[currentLanguage];
		 if(_.isUndefined(result) || _.isNull(result)){
			 result = this.get(field)[defaultLanguage] || '';
		 }
	 }
	 return result
 },
 getName: function (settings) {
	 return this.getField('name',settings);
 },
 getDescription: function (settings) {
     return this.getField('description',settings);
 }, 
 shorten: function(input){
    return this.isLong(input) ? input.substring(0,this.LENGTH_TO_SHOW - 1) : input;
 },
 isLong: function(input){
	 return (input && input.length > this.LENGTH_TO_SHOW) 
 }
 
});
