"use strict";(self.webpackChunkreampv2=self.webpackChunkreampv2||[]).push([[8662],{88662:function(t,n,r){function i(t,n){if((r=(t=n?t.toExponential(n-1):t.toExponential()).indexOf("e"))<0)return null;var r,i=t.slice(0,r);return[i.length>1?i[0]+i.slice(2):i,+t.slice(r+1)]}function e(t){return(t=i(Math.abs(t)))?t[1]:NaN}r.r(n),r.d(n,{FormatSpecifier:function(){return c},format:function(){return m},formatDefaultLocale:function(){return M},formatLocale:function(){return v},formatPrefix:function(){return d},formatSpecifier:function(){return u},precisionFixed:function(){return y},precisionPrefix:function(){return b},precisionRound:function(){return x}});var o,a=/^(?:(.)?([<>=^]))?([+\-( ])?([$#])?(0)?(\d+)?(,)?(\.\d+)?(~)?([a-z%])?$/i;function u(t){if(!(n=a.exec(t)))throw new Error("invalid format: "+t);var n;return new c({fill:n[1],align:n[2],sign:n[3],symbol:n[4],zero:n[5],width:n[6],comma:n[7],precision:n[8]&&n[8].slice(1),trim:n[9],type:n[10]})}function c(t){this.fill=void 0===t.fill?" ":t.fill+"",this.align=void 0===t.align?">":t.align+"",this.sign=void 0===t.sign?"-":t.sign+"",this.symbol=void 0===t.symbol?"":t.symbol+"",this.zero=!!t.zero,this.width=void 0===t.width?void 0:+t.width,this.comma=!!t.comma,this.precision=void 0===t.precision?void 0:+t.precision,this.trim=!!t.trim,this.type=void 0===t.type?"":t.type+""}function s(t,n){var r=i(t,n);if(!r)return t+"";var e=r[0],o=r[1];return o<0?"0."+new Array(-o).join("0")+e:e.length>o+1?e.slice(0,o+1)+"."+e.slice(o+1):e+new Array(o-e.length+2).join("0")}u.prototype=c.prototype,c.prototype.toString=function(){return this.fill+this.align+this.sign+this.symbol+(this.zero?"0":"")+(void 0===this.width?"":Math.max(1,0|this.width))+(this.comma?",":"")+(void 0===this.precision?"":"."+Math.max(0,0|this.precision))+(this.trim?"~":"")+this.type};var f={"%":function(t,n){return(100*t).toFixed(n)},b:function(t){return Math.round(t).toString(2)},c:function(t){return t+""},d:function(t){return Math.abs(t=Math.round(t))>=1e21?t.toLocaleString("en").replace(/,/g,""):t.toString(10)},e:function(t,n){return t.toExponential(n)},f:function(t,n){return t.toFixed(n)},g:function(t,n){return t.toPrecision(n)},o:function(t){return Math.round(t).toString(8)},p:function(t,n){return s(100*t,n)},r:s,s:function(t,n){var r=i(t,n);if(!r)return t+"";var e=r[0],a=r[1],u=a-(o=3*Math.max(-8,Math.min(8,Math.floor(a/3))))+1,c=e.length;return u===c?e:u>c?e+new Array(u-c+1).join("0"):u>0?e.slice(0,u)+"."+e.slice(u):"0."+new Array(1-u).join("0")+i(t,Math.max(0,n+u-1))[0]},X:function(t){return Math.round(t).toString(16).toUpperCase()},x:function(t){return Math.round(t).toString(16)}};function h(t){return t}var l,m,d,p=Array.prototype.map,g=["y","z","a","f","p","n","\xb5","m","","k","M","G","T","P","E","Z","Y"];function v(t){var n,r,i=void 0===t.grouping||void 0===t.thousands?h:(n=p.call(t.grouping,Number),r=t.thousands+"",function(t,i){for(var e=t.length,o=[],a=0,u=n[0],c=0;e>0&&u>0&&(c+u+1>i&&(u=Math.max(1,i-c)),o.push(t.substring(e-=u,e+u)),!((c+=u+1)>i));)u=n[a=(a+1)%n.length];return o.reverse().join(r)}),a=void 0===t.currency?"":t.currency[0]+"",c=void 0===t.currency?"":t.currency[1]+"",s=void 0===t.decimal?".":t.decimal+"",l=void 0===t.numerals?h:function(t){return function(n){return n.replace(/[0-9]/g,(function(n){return t[+n]}))}}(p.call(t.numerals,String)),m=void 0===t.percent?"%":t.percent+"",d=void 0===t.minus?"\u2212":t.minus+"",v=void 0===t.nan?"NaN":t.nan+"";function M(t){var n=(t=u(t)).fill,r=t.align,e=t.sign,h=t.symbol,p=t.zero,M=t.width,y=t.comma,b=t.precision,x=t.trim,w=t.type;"n"===w?(y=!0,w="g"):f[w]||(void 0===b&&(b=12),x=!0,w="g"),(p||"0"===n&&"="===r)&&(p=!0,n="0",r="=");var k="$"===h?a:"#"===h&&/[boxX]/.test(w)?"0"+w.toLowerCase():"",S="$"===h?c:/[%p]/.test(w)?m:"",z=f[w],A=/[defgprs%]/.test(w);function N(t){var a,u,c,f=k,h=S;if("c"===w)h=z(t)+h,t="";else{var m=(t=+t)<0||1/t<0;if(t=isNaN(t)?v:z(Math.abs(t),b),x&&(t=function(t){t:for(var n,r=t.length,i=1,e=-1;i<r;++i)switch(t[i]){case".":e=n=i;break;case"0":0===e&&(e=i),n=i;break;default:if(!+t[i])break t;e>0&&(e=0)}return e>0?t.slice(0,e)+t.slice(n+1):t}(t)),m&&0===+t&&"+"!==e&&(m=!1),f=(m?"("===e?e:d:"-"===e||"("===e?"":e)+f,h=("s"===w?g[8+o/3]:"")+h+(m&&"("===e?")":""),A)for(a=-1,u=t.length;++a<u;)if(48>(c=t.charCodeAt(a))||c>57){h=(46===c?s+t.slice(a+1):t.slice(a))+h,t=t.slice(0,a);break}}y&&!p&&(t=i(t,1/0));var N=f.length+t.length+h.length,j=N<M?new Array(M-N+1).join(n):"";switch(y&&p&&(t=i(j+t,j.length?M-h.length:1/0),j=""),r){case"<":t=f+t+h+j;break;case"=":t=f+j+t+h;break;case"^":t=j.slice(0,N=j.length>>1)+f+t+h+j.slice(N);break;default:t=j+f+t+h}return l(t)}return b=void 0===b?6:/[gprs]/.test(w)?Math.max(1,Math.min(21,b)):Math.max(0,Math.min(20,b)),N.toString=function(){return t+""},N}return{format:M,formatPrefix:function(t,n){var r=M(((t=u(t)).type="f",t)),i=3*Math.max(-8,Math.min(8,Math.floor(e(n)/3))),o=Math.pow(10,-i),a=g[8+i/3];return function(t){return r(o*t)+a}}}}function M(t){return l=v(t),m=l.format,d=l.formatPrefix,l}function y(t){return Math.max(0,-e(Math.abs(t)))}function b(t,n){return Math.max(0,3*Math.max(-8,Math.min(8,Math.floor(e(n)/3)))-e(Math.abs(t)))}function x(t,n){return t=Math.abs(t),n=Math.abs(n)-t,Math.max(0,e(n)-e(t))+1}M({thousands:",",grouping:[3],currency:["$",""]})}}]);
//# sourceMappingURL=8662.bundle.js.map