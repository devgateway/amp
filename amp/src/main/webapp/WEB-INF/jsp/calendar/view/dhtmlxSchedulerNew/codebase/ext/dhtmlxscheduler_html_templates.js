/*
dhtmlxScheduler v.4.3.0 Stardard

This software is covered by GPL license. You also can obtain Commercial or Enterprise license to use it in non-GPL project - please contact sales@dhtmlx.com. Usage without proper license is prohibited.

(c) Dinamenta, UAB.
*/
scheduler.attachEvent("onTemplatesReady",function(){for(var e=document.body.getElementsByTagName("DIV"),t=0;t<e.length;t++){var s=e[t].className||"";if(s=s.split(":"),2==s.length&&"template"==s[0]){var r='return "'+(e[t].innerHTML||"").replace(/\"/g,'\\"').replace(/[\n\r]+/g,"")+'";';r=unescape(r).replace(/\{event\.([a-z]+)\}/g,function(e,t){return'"+ev.'+t+'+"'}),scheduler.templates[s[1]]=Function("start","end","ev",r),e[t].style.display="none"}}});
//# sourceMappingURL=../sources/ext/dhtmlxscheduler_html_templates.js.map