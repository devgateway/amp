var ampoffline;(()=>{"use strict";var e={101:(e,r,t)=>{var n={"./AmpOfflineApp":()=>Promise.all([t.e(351),t.e(900),t.e(830),t.e(325)]).then((()=>()=>t(305)))},o=(e,r)=>(t.R=r,r=t.o(n,e)?n[e]():Promise.resolve().then((()=>{throw new Error('Module "'+e+'" does not exist in container.')})),t.R=void 0,r),a=(e,r)=>{if(t.S){var n="default",o=t.S[n];if(o&&o!==e)throw new Error("Container initialization failed as it has already been initialized with a different share scope");return t.S[n]=e,t.I(n,r)}};t.d(r,{get:()=>o,init:()=>a})}},r={};function t(n){var o=r[n];if(void 0!==o)return o.exports;var a=r[n]={id:n,loaded:!1,exports:{}};return e[n].call(a.exports,a,a.exports,t),a.loaded=!0,a.exports}t.m=e,t.c=r,t.n=e=>{var r=e&&e.__esModule?()=>e.default:()=>e;return t.d(r,{a:r}),r},(()=>{var e,r=Object.getPrototypeOf?e=>Object.getPrototypeOf(e):e=>e.__proto__;t.t=function(n,o){if(1&o&&(n=this(n)),8&o)return n;if("object"===typeof n&&n){if(4&o&&n.__esModule)return n;if(16&o&&"function"===typeof n.then)return n}var a=Object.create(null);t.r(a);var i={};e=e||[null,r({}),r([]),r(r)];for(var l=2&o&&n;"object"==typeof l&&!~e.indexOf(l);l=r(l))Object.getOwnPropertyNames(l).forEach((e=>i[e]=()=>n[e]));return i.default=()=>n,t.d(a,i),a}})(),t.d=(e,r)=>{for(var n in r)t.o(r,n)&&!t.o(e,n)&&Object.defineProperty(e,n,{enumerable:!0,get:r[n]})},t.f={},t.e=e=>Promise.all(Object.keys(t.f).reduce(((r,n)=>(t.f[n](e,r),r)),[])),t.u=e=>e+".bundle.js",t.miniCssF=e=>"static/css/"+e+".ec58946f.chunk.css",t.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"===typeof window)return window}}(),t.o=(e,r)=>Object.prototype.hasOwnProperty.call(e,r),(()=>{var e={},r="ampoffiline:";t.l=(n,o,a,i)=>{if(e[n])e[n].push(o);else{var l,u;if(void 0!==a)for(var f=document.getElementsByTagName("script"),d=0;d<f.length;d++){var s=f[d];if(s.getAttribute("src")==n||s.getAttribute("data-webpack")==r+a){l=s;break}}l||(u=!0,(l=document.createElement("script")).charset="utf-8",l.timeout=120,t.nc&&l.setAttribute("nonce",t.nc),l.setAttribute("data-webpack",r+a),l.src=n),e[n]=[o];var p=(r,t)=>{l.onerror=l.onload=null,clearTimeout(h);var o=e[n];if(delete e[n],l.parentNode&&l.parentNode.removeChild(l),o&&o.forEach((e=>e(t))),r)return r(t)},h=setTimeout(p.bind(null,void 0,{type:"timeout",target:l}),12e4);l.onerror=p.bind(null,l.onerror),l.onload=p.bind(null,l.onload),u&&document.head.appendChild(l)}}})(),t.r=e=>{"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},t.nmd=e=>(e.paths=[],e.children||(e.children=[]),e),(()=>{t.S={};var e={},r={};t.I=(n,o)=>{o||(o=[]);var a=r[n];if(a||(a=r[n]={}),!(o.indexOf(a)>=0)){if(o.push(a),e[n])return e[n];t.o(t.S,n)||(t.S[n]={});var i=t.S[n],l="ampoffiline",u=(e,r,t,n)=>{var o=i[e]=i[e]||{},a=o[r];(!a||!a.loaded&&(!n!=!a.eager?n:l>a.from))&&(o[r]={get:t,from:l,eager:!!n})},f=[];if("default"===n)u("platform","1.3.6",(()=>t.e(556).then((()=>()=>t(556))))),u("react-dom","18.2.0",(()=>Promise.all([t.e(416),t.e(900)]).then((()=>()=>t(416))))),u("react-redux","8.1.3",(()=>Promise.all([t.e(435),t.e(900),t.e(830)]).then((()=>()=>t(435))))),u("react","18.2.0",(()=>t.e(831).then((()=>()=>t(831))))),u("redux-thunk","2.4.2",(()=>t.e(912).then((()=>()=>t(912))))),u("redux","4.2.1",(()=>t.e(128).then((()=>()=>t(128))))),u("web-vitals","2.1.4",(()=>t.e(269).then((()=>()=>t(269)))));return f.length?e[n]=Promise.all(f).then((()=>e[n]=1)):e[n]=1}}})(),t.p="/TEMPLATE/reampv2/packages/ampoffline/build/",(()=>{var e=e=>{var r=e=>e.split(".").map((e=>+e==e?+e:e)),t=/^([^-+]+)?(?:-([^+]+))?(?:\+(.+))?$/.exec(e),n=t[1]?r(t[1]):[];return t[2]&&(n.length++,n.push.apply(n,r(t[2]))),t[3]&&(n.push([]),n.push.apply(n,r(t[3]))),n},r=(r,t)=>{r=e(r),t=e(t);for(var n=0;;){if(n>=r.length)return n<t.length&&"u"!=(typeof t[n])[0];var o=r[n],a=(typeof o)[0];if(n>=t.length)return"u"==a;var i=t[n],l=(typeof i)[0];if(a!=l)return"o"==a&&"n"==l||"s"==l||"u"==a;if("o"!=a&&"u"!=a&&o!=i)return o<i;n++}},n=e=>{var r=e[0],t="";if(1===e.length)return"*";if(r+.5){t+=0==r?">=":-1==r?"<":1==r?"^":2==r?"~":r>0?"=":"!=";for(var o=1,a=1;a<e.length;a++)o--,t+="u"==(typeof(l=e[a]))[0]?"-":(o>0?".":"")+(o=2,l);return t}var i=[];for(a=1;a<e.length;a++){var l=e[a];i.push(0===l?"not("+u()+")":1===l?"("+u()+" || "+u()+")":2===l?i.pop()+" "+i.pop():n(l))}return u();function u(){return i.pop().replace(/^\((.+)\)$/,"$1")}},o=(r,t)=>{if(0 in r){t=e(t);var n=r[0],a=n<0;a&&(n=-n-1);for(var i=0,l=1,u=!0;;l++,i++){var f,d,s=l<r.length?(typeof r[l])[0]:"";if(i>=t.length||"o"==(d=(typeof(f=t[i]))[0]))return!u||("u"==s?l>n&&!a:""==s!=a);if("u"==d){if(!u||"u"!=s)return!1}else if(u)if(s==d)if(l<=n){if(f!=r[l])return!1}else{if(a?f>r[l]:f<r[l])return!1;f!=r[l]&&(u=!1)}else if("s"!=s&&"n"!=s){if(a||l<=n)return!1;u=!1,l--}else{if(l<=n||d<s!=a)return!1;u=!1}else"s"!=s&&"n"!=s&&(u=!1,l--)}}var p=[],h=p.pop.bind(p);for(i=1;i<r.length;i++){var c=r[i];p.push(1==c?h()|h():2==c?h()&h():c?o(c,t):!h())}return!!h()},a=(e,t)=>{var n=e[t];return Object.keys(n).reduce(((e,t)=>!e||!n[e].loaded&&r(e,t)?t:e),0)},i=(e,r,t,o)=>"Unsatisfied version "+t+" from "+(t&&e[r][t].from)+" of shared singleton module "+r+" (required "+n(o)+")",l=(e,r,t,n)=>{var l=a(e,t);return o(n,l)||f(i(e,t,l,n)),d(e[t][l])},u=(e,t,n)=>{var a=e[t];return(t=Object.keys(a).reduce(((e,t)=>o(n,t)&&(!e||r(e,t))?t:e),0))&&a[t]},f=e=>{"undefined"!==typeof console&&console.warn&&console.warn(e)},d=e=>(e.loaded=1,e.get()),s=e=>function(r,n,o,a){var i=t.I(r);return i&&i.then?i.then(e.bind(e,r,t.S[r],n,o,a)):e(r,t.S[r],n,o,a)},p=s(((e,r,n,o,a)=>r&&t.o(r,n)?l(r,0,n,o):a())),h=s(((e,r,n,o,a)=>{var i=r&&t.o(r,n)&&u(r,n,o);return i?d(i):a()})),c={},v={900:()=>p("default","react",[1,18,2,0],(()=>t.e(831).then((()=>()=>t(831))))),830:()=>p("default","react-dom",[1,18,2,0],(()=>t.e(416).then((()=>()=>t(416))))),127:()=>h("default","platform",[1,1,3,6],(()=>t.e(556).then((()=>()=>t(556))))),391:()=>h("default","redux",[1,4,2,1],(()=>t.e(128).then((()=>()=>t(128))))),613:()=>h("default","react-redux",[1,8,1,1],(()=>t.e(435).then((()=>()=>t(435))))),713:()=>h("default","redux-thunk",[1,2,4,2],(()=>t.e(912).then((()=>()=>t(912))))),339:()=>h("default","web-vitals",[1,2,1,4],(()=>t.e(269).then((()=>()=>t(269)))))},m={325:[127,391,613,713],339:[339],830:[830],900:[900]};t.f.consumes=(e,r)=>{t.o(m,e)&&m[e].forEach((e=>{if(t.o(c,e))return r.push(c[e]);var n=r=>{c[e]=0,t.m[e]=n=>{delete t.c[e],n.exports=r()}},o=r=>{delete c[e],t.m[e]=n=>{throw delete t.c[e],r}};try{var a=v[e]();a.then?r.push(c[e]=a.then(n).catch(o)):n(a)}catch(i){o(i)}}))}})(),(()=>{if("undefined"!==typeof document){var e=e=>new Promise(((r,n)=>{var o=t.miniCssF(e),a=t.p+o;if(((e,r)=>{for(var t=document.getElementsByTagName("link"),n=0;n<t.length;n++){var o=(i=t[n]).getAttribute("data-href")||i.getAttribute("href");if("stylesheet"===i.rel&&(o===e||o===r))return i}var a=document.getElementsByTagName("style");for(n=0;n<a.length;n++){var i;if((o=(i=a[n]).getAttribute("data-href"))===e||o===r)return i}})(o,a))return r();((e,r,t,n,o)=>{var a=document.createElement("link");a.rel="stylesheet",a.type="text/css",a.onerror=a.onload=t=>{if(a.onerror=a.onload=null,"load"===t.type)n();else{var i=t&&("load"===t.type?"missing":t.type),l=t&&t.target&&t.target.href||r,u=new Error("Loading CSS chunk "+e+" failed.\n("+l+")");u.code="CSS_CHUNK_LOAD_FAILED",u.type=i,u.request=l,a.parentNode&&a.parentNode.removeChild(a),o(u)}},a.href=r,t?t.parentNode.insertBefore(a,t.nextSibling):document.head.appendChild(a)})(e,a,null,r,n)})),r={673:0};t.f.miniCss=(t,n)=>{r[t]?n.push(r[t]):0!==r[t]&&{325:1}[t]&&n.push(r[t]=e(t).then((()=>{r[t]=0}),(e=>{throw delete r[t],e})))}}})(),(()=>{var e={673:0};t.f.j=(r,n)=>{var o=t.o(e,r)?e[r]:void 0;if(0!==o)if(o)n.push(o[2]);else if(/^(339|830|900)$/.test(r))e[r]=0;else{var a=new Promise(((t,n)=>o=e[r]=[t,n]));n.push(o[2]=a);var i=t.p+t.u(r),l=new Error;t.l(i,(n=>{if(t.o(e,r)&&(0!==(o=e[r])&&(e[r]=void 0),o)){var a=n&&("load"===n.type?"missing":n.type),i=n&&n.target&&n.target.src;l.message="Loading chunk "+r+" failed.\n("+a+": "+i+")",l.name="ChunkLoadError",l.type=a,l.request=i,o[1](l)}}),"chunk-"+r,r)}};var r=(r,n)=>{var o,a,i=n[0],l=n[1],u=n[2],f=0;if(i.some((r=>0!==e[r]))){for(o in l)t.o(l,o)&&(t.m[o]=l[o]);if(u)u(t)}for(r&&r(n);f<i.length;f++)a=i[f],t.o(e,a)&&e[a]&&e[a][0](),e[a]=0},n=self.webpackChunkampoffiline=self.webpackChunkampoffiline||[];n.forEach(r.bind(null,0)),n.push=r.bind(null,n.push.bind(n))})();var n=t(101);ampoffline=n})();
//# sourceMappingURL=remoteEntry.js.map