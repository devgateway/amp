"use strict";(self.webpackChunkreampv2=self.webpackChunkreampv2||[]).push([[9116,8960,5770,5753],{89126:function(n,t,e){e.r(t),e.d(t,{default:function(){return F}});var r=e(9249),o=e(87371),i=e(45754),a=e(86906),u=e(50383),c=e.n(u),s=e(83112),f=e(10941),l=e.n(f),d=e(94652),p=e(80753),h=e(98329),v=e.n(h),y=e(33028),m="FETCH_RELEASES_PENDING",b="FETCH_RELEASES_SUCCESS",g="FETCH_RELEASES_ERROR";var w={pending:!1,releases:[],error:null};var x=function(n){return n.releases},j=function(n){return n.pending},O=function(n){return n.error},Z="/rest/amp/amp-offline-release",A="windows",S="debian",P="redhat";var C=function(){return function(n){n({type:m}),fetch(Z).then((function(n){return n.json()})).then((function(t){if(t.error)throw t.error;return n({type:b,payload:t}),t})).catch((function(t){n(function(n){return{type:g,error:n}}(t))}))}},E=e(35209),_=JSON.parse('{"amp.offline:download":"Download AMP Offline","amp.offline:all-versions":"All installer versions","amp.offline:best-version-message":"We have automatically detected which version of the application meets your operating system requirements. Other versions are available below.","amp.offline:bits":"bits","amp.offline:page-title":"Aid Management Platform - Download Offline Client","amp.offline:download-title":"Download the Offline Client","amp.offline:text":"The AMP Offline application allows you to edit and add activity information to the AMP without having an active internet connection. In order to use the application, you must download and install the compatible version of AMP Offline application from the list of the latest AMP Offline installers. When you run the application for the first time, you must have an active internet connection in order to sync your user data, activity data, and other critical data to the application. After that, you may work offline and sync your data periodically.","amp.offline:unknown":"Unknown"}'),R=e(47371),T=e(59653),k=c().createContext({translations:_}),N=function(n){(0,i.Z)(e,n);var t=(0,a.Z)(e);function e(){return(0,r.Z)(this,e),t.apply(this,arguments)}return(0,o.Z)(e,[{key:"componentDidMount",value:function(){this.props.fetchTranslations(_)}},{key:"render",value:function(){var n=this.props,t=n.translationPending,e=n.children,r=n.translations;return t?(0,T.jsx)(R.g,{}):(0,T.jsx)(k.Provider,{value:{translations:r},children:e})}}]),e}(u.Component),M=(0,d.connect)((function(n){return{translationPending:n.translationsReducer.pending,translations:n.translationsReducer.translations}}),(function(n){return(0,s.bindActionCreators)({fetchTranslations:E.Z},n)}))(N),I=function(n){(0,i.Z)(e,n);var t=(0,a.Z)(e);function e(n){var o;return(0,r.Z)(this,e),(o=t.call(this,n)).shouldComponentRender=o.shouldComponentRender.bind((0,p.Z)(o)),o}return(0,o.Z)(e,[{key:"componentDidMount",value:function(){(0,this.props.fetchReleases)()}},{key:"shouldComponentRender",value:function(){return!this.props.pending}},{key:"_buildLinksTable",value:function(){var n=this,t=[];return this.props.releases?(this.props.releases.sort((function(n,t){return n.os>t.os})).map((function(e){return t.push((0,T.jsx)("a",{href:"".concat(Z,"/").concat(e.id),children:n._getInstallerName(e.os,e.arch)}))})),(0,T.jsx)("ul",{children:t.map((function(n,t){return(0,T.jsx)("li",{children:n},t)}))})):null}},{key:"_getInstallerName",value:function(n,t){var e="";switch(n){case A:e="Windows Vista/7/8/10 - ".concat(t," ").concat(this.context.translations["amp.offline:bits"]);break;case S:e="Ubuntu Linux (.deb) - ".concat(t," ").concat(this.context.translations["amp.offline:bits"]);break;case P:e="RedHat Linux (.rpm) - ".concat(t," ").concat(this.context.translations["amp.offline:bits"]);break;case"osx":e="Mac OS - ".concat(t," ").concat(this.context.translations["amp.offline:bits"]);break;default:e="".concat(this.context.translations["amp.offline:unknown"])}return e}},{key:"_detectBestInstaller",value:function(){var n=this,t=v().os,e=t.architecture,r=t.family.toLowerCase(),o=null;if(r.indexOf(A)>-1)o=[A];else if(r.indexOf("macintosh")>-1||r.indexOf("os x")>-1)o=["osx"];else{if(!(r.indexOf("linux")>-1))return[];o=[S,P]}var i=this.props.releases.filter((function(n){return o.filter((function(t){return t===n.os})).length>0&&n.arch===e.toString()})),a=i.map((function(t){var e=n._getInstallerName(t.os,t.arch);return(0,T.jsx)("div",{className:"link",children:(0,T.jsxs)("a",{href:"".concat(Z,"/").concat(t.id),children:[n.context.translations["amp.offline:download"]," ",t.version," ","-"," ",e]})},t.id)})),u=this.context.translations["amp.offline:best-version-message"];return a.length>0&&(0,T.jsxs)("div",{className:"alert alert-info",role:"alert",children:[(0,T.jsx)("span",{className:"info-text",children:u}),a]})}},{key:"render",value:function(){return this.shouldComponentRender()?(0,T.jsxs)("div",{children:[(0,T.jsx)("div",{children:this._detectBestInstaller()}),(0,T.jsx)("h4",{children:this.context.translations["amp.offline:all-versions"]}),(0,T.jsx)("div",{children:this._buildLinksTable()})]}):(0,T.jsx)("div",{children:"loading"})}}]),e}(u.Component);I.contextType=k;var D=(0,d.connect)((function(n){return{error:O(n.startupReducer),releases:x(n.startupReducer),pending:j(n.startupReducer),translations:n.translationsReducer.translations}}),(function(n){return(0,s.bindActionCreators)({fetchReleases:C},n)}))(I),L=e.p+"static/media/monitor.0d3bcd4b5f57a7495585.png",U=function(n){(0,i.Z)(e,n);var t=(0,a.Z)(e);function e(){return(0,r.Z)(this,e),t.apply(this,arguments)}return(0,o.Z)(e,[{key:"render",value:function(){var n=this.context.translations;return(0,T.jsxs)("div",{children:[(0,T.jsx)("div",{className:"col-md-5",children:(0,T.jsx)("img",{src:L,alt:n["amp.offline:download-title"]})}),(0,T.jsxs)("div",{className:"col-md-7",children:[(0,T.jsxs)("div",{className:"main_text",children:[(0,T.jsx)("h2",{children:n["amp.offline:download-title"]}),(0,T.jsx)("span",{children:n["amp.offline:text"]})]}),(0,T.jsx)("div",{children:(0,T.jsx)(D,{})})]})]})}}]),e}(u.Component);U.contextType=k;var W=e(50840),B=(0,s.combineReducers)({startupReducer:function(){var n=arguments.length>0&&void 0!==arguments[0]?arguments[0]:w,t=arguments.length>1?arguments[1]:void 0;switch(t.type){case m:return(0,y.Z)((0,y.Z)({},n),{},{pending:!0});case b:return(0,y.Z)((0,y.Z)({},n),{},{pending:!1,releases:t.payload});case g:return(0,y.Z)((0,y.Z)({},n),{},{pending:!1,error:t.error});default:return n}},translationsReducer:W.Z}),G=window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__||s.compose,F=function(n){(0,i.Z)(e,n);var t=(0,a.Z)(e);function e(n){var o;return(0,r.Z)(this,e),(o=t.call(this,n)).store=(0,s.createStore)(B,G((0,s.applyMiddleware)(l()))),o}return(0,o.Z)(e,[{key:"render",value:function(){return(0,T.jsx)(d.Provider,{store:this.store,children:(0,T.jsx)(M,{defaultTrnPack:_,children:(0,T.jsx)(U,{})})})}}]),e}(u.Component)},1342:function(n,t,e){e.d(t,{BB:function(){return h},ET:function(){return v},Ez:function(){return l},IN:function(){return G},Ic:function(){return c},JP:function(){return F},LM:function(){return S},O:function(){return p},PU:function(){return y},S9:function(){return b},SJ:function(){return w},Sw:function(){return C},TM:function(){return L},Vs:function(){return s},WP:function(){return I},_G:function(){return x},aD:function(){return k},ah:function(){return M},bm:function(){return q},cE:function(){return d},eM:function(){return A},e_:function(){return f},fz:function(){return g},g1:function(){return E},gW:function(){return Z},ij:function(){return D},io:function(){return R},pj:function(){return _},qp:function(){return u},rF:function(){return a},rP:function(){return P},ry:function(){return o},sC:function(){return B},sK:function(){return U},tU:function(){return T},um:function(){return m},vW:function(){return O},wU:function(){return W},wt:function(){return j},xE:function(){return r},xg:function(){return X},yr:function(){return N},z7:function(){return i}});var r=0,o=1,i=-1,a="/assets/img/flags/",u="dashboard-default-min-year-range",c="dashboard-default-max-year-range",s="/rest/filters/sectors",f="/rest/filters/locations?firstLevelOnly=true&showAllCountries=true",l="/rest/amp/settings",d="/rest/filters/modalities?sscWorkspace=true",p="/rest/gis/sscdashboard",h="/rest/gis/sscdashboard/xsl",v="/rest/gis/activities",y=12,m="#fd8f00",b="#fd8f00",g="#007236",w="#007236",x="png",j=50,O=-999999999,Z=9,A=6,S=1766,P=5,C="-1",E=1920,_=145,R=2,T="0",k="SSC_",N="home",M="sector",I="modality",D="download",L="country-column",U="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAbCAQAAACkGQXlAAAAI0lEQVR42mP8L8lAVcA4auCogaMGjho4auCogaMGjhpINAAAOBcdpLw/CDsAAAAASUVORK5CYII=",W="000",B="primary-sector",G="ssc-modalities",F="donor-agency-country",H=new Map([["110","#d46453"],["120","#f5a15d"],["130","#ffcf8e"],["140","#ff7a7d"],["150","#ff417d"],["160","#d61a88"],["210","#fff540"],["220","#28c074"],["230","#429058"],["240","#bd4035"],["250","#b0fff1"],["311","#144491"],["312","#9b0e3e"],["313","#10908e"],["321","#8f5765"],["322","#c7d4e1"],["323","#77b02a"],["331","#488bd4"],["332","#928fb8"],["400","#c6d831"],["500","#d41e3c"],["600","#cf968c"],["700","#78d7ff"],["910","#94007a"],["920","#ffb84a"],["930","#ed7b39"],["998","#ed4c40"],["000","#8f5765"]]),V=new Map([[W,"#8f5765"]]),X=new Map([[I,["#00429d","#3761ab","#5681b9","#73a2c6","#93c4d2","#b9e5dd","#ffffe0","#ffd3bf","#ffa59e","#f4777f","#dd4c65","#be214d","#93003a"]]]),q=new Map([[M,H],[I,V]])},35209:function(n,t,e){var r=e(64454),o=e(58234);t.Z=function(n){return function(t){return t((0,r.M0)(n)),(0,o.X)(n).then((function(n){return t((0,r.sq)(n))})).catch((function(n){return t((0,r.nW)(n))}))}}},64454:function(n,t,e){e.d(t,{M0:function(){return a},b5:function(){return r},k_:function(){return o},nW:function(){return c},pv:function(){return i},sq:function(){return u}});var r="FETCH_TRANSLATIONS_PENDING",o="FETCH_TRANSLATIONS_SUCCESS",i="FETCH_TRANSLATIONS_ERROR";function a(n){return{type:r,payload:n}}function u(n){return{type:o,payload:n}}function c(n){return{type:i,error:n}}},47371:function(n,t,e){e.d(t,{g:function(){return o}});e(50383);var r=e(59653),o=function(){return(0,r.jsx)("div",{className:"jumbotron",children:(0,r.jsx)("div",{className:"progress",children:(0,r.jsx)("div",{className:"progress-bar progress-bar-striped bg-info",role:"progressbar","aria-valuenow":"100","aria-valuemin":"0","aria-valuemax":"100",style:{width:"100%"},children:"..."})})})}},58234:function(n,t,e){e.d(t,{X:function(){return c},a:function(){return u}});var r=e(1342),o="POST",i="GET";function a(n,t){var e={method:n?o:i,headers:t||{"Content-Type":"application/json",Accept:"application/json","ws-prefix":r.aD}};return n&&(e.body=JSON.stringify(n)),e}var u=function(n){var t=n.body,e=n.url,r=n.headers;return new Promise((function(n,o){return fetch(e,a(t,r)).then((function(n){return r&&"text/html"===r.Accept?n.text():n.headers&&n.headers.get("Content-Type")&&n.headers.get("Content-Type").indexOf("application/json")>-1?n.json():n})).then((function(t){return t.error?o(t.error):n(t)}))}))};function c(n){return new Promise((function(t){return fetch("/rest/translations/label-translations",a(n)).then((function(n){var e=n.headers.get("content-type");if(e&&-1!==e.indexOf("application/json"))return n.json().then((function(n){if(n.error)throw new Error(n.error);return t(n)}))}))}))}},50840:function(n,t,e){e.d(t,{Z:function(){return a}});var r=e(33028),o=e(64454),i={pending:!0,translations:{},error:null};function a(){var n=arguments.length>0&&void 0!==arguments[0]?arguments[0]:i,t=arguments.length>1?arguments[1]:void 0;switch(t.type){case o.b5:return(0,r.Z)((0,r.Z)({},n),{},{pending:!0,translations:t.payload});case o.k_:return(0,r.Z)((0,r.Z)({},n),{},{pending:!1,translations:t.payload});case o.pv:return(0,r.Z)((0,r.Z)({},n),{},{pending:!1,error:t.error});default:return n}}},80753:function(n,t,e){function r(n){if(void 0===n)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return n}e.d(t,{Z:function(){return r}})},9249:function(n,t,e){function r(n,t){if(!(n instanceof t))throw new TypeError("Cannot call a class as a function")}e.d(t,{Z:function(){return r}})},87371:function(n,t,e){e.d(t,{Z:function(){return i}});var r=e(45850);function o(n,t){for(var e=0;e<t.length;e++){var o=t[e];o.enumerable=o.enumerable||!1,o.configurable=!0,"value"in o&&(o.writable=!0),Object.defineProperty(n,(0,r.Z)(o.key),o)}}function i(n,t,e){return t&&o(n.prototype,t),e&&o(n,e),Object.defineProperty(n,"prototype",{writable:!1}),n}},86906:function(n,t,e){e.d(t,{Z:function(){return a}});var r=e(95058),o=e(70352),i=e(11987);function a(n){var t=(0,o.Z)();return function(){var e,o=(0,r.Z)(n);if(t){var a=(0,r.Z)(this).constructor;e=Reflect.construct(o,arguments,a)}else e=o.apply(this,arguments);return(0,i.Z)(this,e)}}},56666:function(n,t,e){e.d(t,{Z:function(){return o}});var r=e(45850);function o(n,t,e){return(t=(0,r.Z)(t))in n?Object.defineProperty(n,t,{value:e,enumerable:!0,configurable:!0,writable:!0}):n[t]=e,n}},95058:function(n,t,e){function r(n){return r=Object.setPrototypeOf?Object.getPrototypeOf.bind():function(n){return n.__proto__||Object.getPrototypeOf(n)},r(n)}e.d(t,{Z:function(){return r}})},45754:function(n,t,e){e.d(t,{Z:function(){return o}});var r=e(88960);function o(n,t){if("function"!==typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function");n.prototype=Object.create(t&&t.prototype,{constructor:{value:n,writable:!0,configurable:!0}}),Object.defineProperty(n,"prototype",{writable:!1}),t&&(0,r.Z)(n,t)}},70352:function(n,t,e){function r(){if("undefined"===typeof Reflect||!Reflect.construct)return!1;if(Reflect.construct.sham)return!1;if("function"===typeof Proxy)return!0;try{return Boolean.prototype.valueOf.call(Reflect.construct(Boolean,[],(function(){}))),!0}catch(n){return!1}}e.d(t,{Z:function(){return r}})},33028:function(n,t,e){e.d(t,{Z:function(){return i}});var r=e(56666);function o(n,t){var e=Object.keys(n);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(n);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(n,t).enumerable}))),e.push.apply(e,r)}return e}function i(n){for(var t=1;t<arguments.length;t++){var e=null!=arguments[t]?arguments[t]:{};t%2?o(Object(e),!0).forEach((function(t){(0,r.Z)(n,t,e[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(n,Object.getOwnPropertyDescriptors(e)):o(Object(e)).forEach((function(t){Object.defineProperty(n,t,Object.getOwnPropertyDescriptor(e,t))}))}return n}},11987:function(n,t,e){e.d(t,{Z:function(){return i}});var r=e(86522),o=e(80753);function i(n,t){if(t&&("object"===(0,r.Z)(t)||"function"===typeof t))return t;if(void 0!==t)throw new TypeError("Derived constructors may only return object or undefined");return(0,o.Z)(n)}},88960:function(n,t,e){function r(n,t){return r=Object.setPrototypeOf?Object.setPrototypeOf.bind():function(n,t){return n.__proto__=t,n},r(n,t)}e.d(t,{Z:function(){return r}})},45850:function(n,t,e){e.d(t,{Z:function(){return o}});var r=e(86522);function o(n){var t=function(n,t){if("object"!=(0,r.Z)(n)||!n)return n;var e=n[Symbol.toPrimitive];if(void 0!==e){var o=e.call(n,t||"default");if("object"!=(0,r.Z)(o))return o;throw new TypeError("@@toPrimitive must return a primitive value.")}return("string"===t?String:Number)(n)}(n,"string");return"symbol"==(0,r.Z)(t)?t:String(t)}},86522:function(n,t,e){function r(n){return r="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(n){return typeof n}:function(n){return n&&"function"==typeof Symbol&&n.constructor===Symbol&&n!==Symbol.prototype?"symbol":typeof n},r(n)}e.d(t,{Z:function(){return r}})}}]);
//# sourceMappingURL=9116.bundle.js.map