/*  
 *   Copyright 2012 OSBI Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
 
/**
 * Object which handles authentication and stores connections and cubes
 * @param username
 * @param password
 * @returns {Session}
 */
var Session = Backbone.Model.extend({
    username: null,
    password: null,
    sessionid: null,
    upgradeTimeout: null,
    isadmin: false,
        
    //IMPORTANT: Care with this file, some changes can cause stop loading Saiku.
    initialize: function(args, options) {
    	Saiku.logger.log("Session.initialize");
        // Attach a custom event bus to this model
        _.extend(this, Backbone.Events);
        _.bindAll(this, "check_session", "process_session", "load_session","login");
        // Check if credentials are being injected into session
        this.check_session();
    },

    check_session: function() {
    	Saiku.logger.log("Session.check_session");
    	this.clear();
        this.fetch({ success: this.process_session });
    },

    load_session: function() {
    	Saiku.logger.log("Session.load_session");
        this.sessionworkspace = new SessionWorkspace();
    },

    process_session: function(model, response) {
    	Saiku.logger.log("Session.process_session");
    	this.load_session();
        return this;
    },
    
    error: function() {
    	Saiku.logger.log("Session.error");
        $(this.form.el).dialog('open');
    },
    
    login: function(username, password) {        
    	Saiku.logger.log("Session.login");
    },
    login_failed: function(response){
    	Saiku.logger.log("Session.login_failed");
    },
    logout: function() {
    	Saiku.logger.log("Session.logout");
    },

    url: function() {
    	Saiku.logger.log("Session.url");
    	//TODO: Instead of loading this file add the content here.
    	return "/TEMPLATE/ampTemplate/saikuui_reports/mockData/session.json";
    }
});
