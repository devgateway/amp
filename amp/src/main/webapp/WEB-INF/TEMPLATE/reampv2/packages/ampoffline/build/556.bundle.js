/*! For license information please see 556.bundle.js.LICENSE.txt */
(self.webpackChunkampoffiline=self.webpackChunkampoffiline||[]).push([[556],{556:function(e,t,i){var r;e=i.nmd(e),function(){"use strict";var n={function:!0,object:!0},a=n[typeof window]&&window||this,o=n[typeof t]&&t,l=n.object&&e&&!e.nodeType&&e,s=o&&l&&"object"==typeof i.g&&i.g;!s||s.global!==s&&s.window!==s&&s.self!==s||(a=s);var b=Math.pow(2,53)-1,p=/\bOpera/,c=Object.prototype,u=c.hasOwnProperty,d=c.toString;function f(e){return(e=String(e)).charAt(0).toUpperCase()+e.slice(1)}function S(e){return e=O(e),/^(?:webOS|i(?:OS|P))/.test(e)?e:f(e)}function x(e,t){for(var i in e)u.call(e,i)&&t(e[i],i,e)}function h(e){return null==e?f(e):d.call(e).slice(8,-1)}function m(e){return String(e).replace(/([ -])(?!$)/g,"$1?")}function g(e,t){var i=null;return function(e,t){var i=-1,r=e?e.length:0;if("number"==typeof r&&r>-1&&r<=b)for(;++i<r;)t(e[i],i,e);else x(e,t)}(e,(function(r,n){i=t(i,r,n,e)})),i}function O(e){return String(e).replace(/^ +| +$/g,"")}var y=function e(t){var i=a,r=t&&"object"==typeof t&&"String"!=h(t);r&&(i=t,t=null);var n=i.navigator||{},o=n.userAgent||"";t||(t=o);var l,s,b=r?!!n.likeChrome:/\bChrome\b/.test(t)&&!/internal|\n/i.test(d.toString()),c="Object",u=r?c:"ScriptBridgingProxyObject",f=r?c:"Environment",y=r&&i.java?"JavaPackage":h(i.java),M=r?c:"RuntimeObject",w=/\bJava/.test(y)&&i.java,E=w&&h(i.environment)==f,v=w?"a":"\u03b1",P=w?"b":"\u03b2",C=i.document||{},B=i.operamini||i.opera,k=p.test(k=r&&B?B["[[Class]]"]:h(B))?k:B=null,W=t,R=[],A=null,I=t==o,F=I&&B&&"function"==typeof B.version&&B.version(),T=g([{label:"EdgeHTML",pattern:"Edge"},"Trident",{label:"WebKit",pattern:"AppleWebKit"},"iCab","Presto","NetFront","Tasman","KHTML","Gecko"],(function(e,i){return e||RegExp("\\b"+(i.pattern||m(i))+"\\b","i").exec(t)&&(i.label||i)})),G=function(e){return g(e,(function(e,i){return e||RegExp("\\b"+(i.pattern||m(i))+"\\b","i").exec(t)&&(i.label||i)}))}(["Adobe AIR","Arora","Avant Browser","Breach","Camino","Electron","Epiphany","Fennec","Flock","Galeon","GreenBrowser","iCab","Iceweasel","K-Meleon","Konqueror","Lunascape","Maxthon",{label:"Microsoft Edge",pattern:"(?:Edge|Edg|EdgA|EdgiOS)"},"Midori","Nook Browser","PaleMoon","PhantomJS","Raven","Rekonq","RockMelt",{label:"Samsung Internet",pattern:"SamsungBrowser"},"SeaMonkey",{label:"Silk",pattern:"(?:Cloud9|Silk-Accelerated)"},"Sleipnir","SlimBrowser",{label:"SRWare Iron",pattern:"Iron"},"Sunrise","Swiftfox","Vivaldi","Waterfox","WebPositive",{label:"Yandex Browser",pattern:"YaBrowser"},{label:"UC Browser",pattern:"UCBrowser"},"Opera Mini",{label:"Opera Mini",pattern:"OPiOS"},"Opera",{label:"Opera",pattern:"OPR"},"Chromium","Chrome",{label:"Chrome",pattern:"(?:HeadlessChrome)"},{label:"Chrome Mobile",pattern:"(?:CriOS|CrMo)"},{label:"Firefox",pattern:"(?:Firefox|Minefield)"},{label:"Firefox for iOS",pattern:"FxiOS"},{label:"IE",pattern:"IEMobile"},{label:"IE",pattern:"MSIE"},"Safari"]),$=j([{label:"BlackBerry",pattern:"BB10"},"BlackBerry",{label:"Galaxy S",pattern:"GT-I9000"},{label:"Galaxy S2",pattern:"GT-I9100"},{label:"Galaxy S3",pattern:"GT-I9300"},{label:"Galaxy S4",pattern:"GT-I9500"},{label:"Galaxy S5",pattern:"SM-G900"},{label:"Galaxy S6",pattern:"SM-G920"},{label:"Galaxy S6 Edge",pattern:"SM-G925"},{label:"Galaxy S7",pattern:"SM-G930"},{label:"Galaxy S7 Edge",pattern:"SM-G935"},"Google TV","Lumia","iPad","iPod","iPhone","Kindle",{label:"Kindle Fire",pattern:"(?:Cloud9|Silk-Accelerated)"},"Nexus","Nook","PlayBook","PlayStation Vita","PlayStation","TouchPad","Transformer",{label:"Wii U",pattern:"WiiU"},"Wii","Xbox One",{label:"Xbox 360",pattern:"Xbox"},"Xoom"]),X=function(e){return g(e,(function(e,i,r){return e||(i[$]||i[/^[a-z]+(?: +[a-z]+\b)*/i.exec($)]||RegExp("\\b"+m(r)+"(?:\\b|\\w*\\d)","i").exec(t))&&r}))}({Apple:{iPad:1,iPhone:1,iPod:1},Alcatel:{},Archos:{},Amazon:{Kindle:1,"Kindle Fire":1},Asus:{Transformer:1},"Barnes & Noble":{Nook:1},BlackBerry:{PlayBook:1},Google:{"Google TV":1,Nexus:1},HP:{TouchPad:1},HTC:{},Huawei:{},Lenovo:{},LG:{},Microsoft:{Xbox:1,"Xbox One":1},Motorola:{Xoom:1},Nintendo:{"Wii U":1,Wii:1},Nokia:{Lumia:1},Oppo:{},Samsung:{"Galaxy S":1,"Galaxy S2":1,"Galaxy S3":1,"Galaxy S4":1},Sony:{PlayStation:1,"PlayStation Vita":1},Xiaomi:{Mi:1,Redmi:1}}),K=function(e){return g(e,(function(e,i){var r=i.pattern||m(i);return!e&&(e=RegExp("\\b"+r+"(?:/[\\d.]+|[ \\w.]*)","i").exec(t))&&(e=function(e,t,i){var r={"10.0":"10",6.4:"10 Technical Preview",6.3:"8.1",6.2:"8",6.1:"Server 2008 R2 / 7","6.0":"Server 2008 / Vista",5.2:"Server 2003 / XP 64-bit",5.1:"XP",5.01:"2000 SP1","5.0":"2000","4.0":"NT","4.90":"ME"};return t&&i&&/^Win/i.test(e)&&!/^Windows Phone /i.test(e)&&(r=r[/[\d.]+$/.exec(e)])&&(e="Windows "+r),e=String(e),t&&i&&(e=e.replace(RegExp(t,"i"),i)),S(e.replace(/ ce$/i," CE").replace(/\bhpw/i,"web").replace(/\bMacintosh\b/,"Mac OS").replace(/_PowerPC\b/i," OS").replace(/\b(OS X) [^ \d]+/i,"$1").replace(/\bMac (OS X)\b/,"$1").replace(/\/(\d)/," $1").replace(/_/g,".").replace(/(?: BePC|[ .]*fc[ \d.]+)$/i,"").replace(/\bx86\.64\b/gi,"x86_64").replace(/\b(Windows Phone) OS\b/,"$1").replace(/\b(Chrome OS \w+) [\d.]+\b/,"$1").split(" on ")[0])}(e,r,i.label||i)),e}))}(["Windows Phone","KaiOS","Android","CentOS",{label:"Chrome OS",pattern:"CrOS"},"Debian",{label:"DragonFly BSD",pattern:"DragonFly"},"Fedora","FreeBSD","Gentoo","Haiku","Kubuntu","Linux Mint","OpenBSD","Red Hat","SuSE","Ubuntu","Xubuntu","Cygwin","Symbian OS","hpwOS","webOS ","webOS","Tablet OS","Tizen","Linux","Mac OS X","Macintosh","Mac","Windows 98;","Windows "]);function j(e){return g(e,(function(e,i){var r=i.pattern||m(i);return!e&&(e=RegExp("\\b"+r+" *\\d+[.\\w_]*","i").exec(t)||RegExp("\\b"+r+" *\\w+-[\\w]*","i").exec(t)||RegExp("\\b"+r+"(?:; *(?:[a-z]+[_-])?[a-z]+\\d+|[^ ();-]*)","i").exec(t))&&((e=String(i.label&&!RegExp(r,"i").test(i.label)?i.label:e).split("/"))[1]&&!/[\d.]+/.test(e[0])&&(e[0]+=" "+e[1]),i=i.label||i,e=S(e[0].replace(RegExp(r,"i"),i).replace(RegExp("; *(?:"+i+"[_-])?","i")," ").replace(RegExp("("+i+")[-_.]?(\\w)","i"),"$1 $2"))),e}))}function N(e){return g(e,(function(e,i){return e||(RegExp(i+"(?:-[\\d.]+/|(?: for [\\w-]+)?[ /-])([\\d.]+[^ ();/_-]*)","i").exec(t)||0)[1]||null}))}if(T&&(T=[T]),/\bAndroid\b/.test(K)&&!$&&(l=/\bAndroid[^;]*;(.*?)(?:Build|\) AppleWebKit)\b/i.exec(t))&&($=O(l[1]).replace(/^[a-z]{2}-[a-z]{2};\s*/i,"")||null),X&&!$?$=j([X]):X&&$&&($=$.replace(RegExp("^("+m(X)+")[-_.\\s]","i"),X+" ").replace(RegExp("^("+m(X)+")[-_.]?(\\w)","i"),X+" $2")),(l=/\bGoogle TV\b/.exec($))&&($=l[0]),/\bSimulator\b/i.test(t)&&($=($?$+" ":"")+"Simulator"),"Opera Mini"==G&&/\bOPiOS\b/.test(t)&&R.push("running in Turbo/Uncompressed mode"),"IE"==G&&/\blike iPhone OS\b/.test(t)?(X=(l=e(t.replace(/like iPhone OS/,""))).manufacturer,$=l.product):/^iP/.test($)?(G||(G="Safari"),K="iOS"+((l=/ OS ([\d_]+)/i.exec(t))?" "+l[1].replace(/_/g,"."):"")):"Konqueror"==G&&/^Linux\b/i.test(K)?K="Kubuntu":X&&"Google"!=X&&(/Chrome/.test(G)&&!/\bMobile Safari\b/i.test(t)||/\bVita\b/.test($))||/\bAndroid\b/.test(K)&&/^Chrome/.test(G)&&/\bVersion\//i.test(t)?(G="Android Browser",K=/\bAndroid\b/.test(K)?K:"Android"):"Silk"==G?(/\bMobi/i.test(t)||(K="Android",R.unshift("desktop mode")),/Accelerated *= *true/i.test(t)&&R.unshift("accelerated")):"UC Browser"==G&&/\bUCWEB\b/.test(t)?R.push("speed mode"):"PaleMoon"==G&&(l=/\bFirefox\/([\d.]+)\b/.exec(t))?R.push("identifying as Firefox "+l[1]):"Firefox"==G&&(l=/\b(Mobile|Tablet|TV)\b/i.exec(t))?(K||(K="Firefox OS"),$||($=l[1])):!G||(l=!/\bMinefield\b/i.test(t)&&/\b(?:Firefox|Safari)\b/.exec(G))?(G&&!$&&/[\/,]|^[^(]+?\)/.test(t.slice(t.indexOf(l+"/")+8))&&(G=null),(l=$||X||K)&&($||X||/\b(?:Android|Symbian OS|Tablet OS|webOS)\b/.test(K))&&(G=/[a-z]+(?: Hat)?/i.exec(/\bAndroid\b/.test(K)?K:l)+" Browser")):"Electron"==G&&(l=(/\bChrome\/([\d.]+)\b/.exec(t)||0)[1])&&R.push("Chromium "+l),F||(F=N(["(?:Cloud9|CriOS|CrMo|Edge|Edg|EdgA|EdgiOS|FxiOS|HeadlessChrome|IEMobile|Iron|Opera ?Mini|OPiOS|OPR|Raven|SamsungBrowser|Silk(?!/[\\d.]+$)|UCBrowser|YaBrowser)","Version",m(G),"(?:Firefox|Minefield|NetFront)"])),(l=("iCab"==T&&parseFloat(F)>3?"WebKit":/\bOpera\b/.test(G)&&(/\bOPR\b/.test(t)?"Blink":"Presto"))||/\b(?:Midori|Nook|Safari)\b/i.test(t)&&!/^(?:Trident|EdgeHTML)$/.test(T)&&"WebKit"||!T&&/\bMSIE\b/i.test(t)&&("Mac OS"==K?"Tasman":"Trident")||"WebKit"==T&&/\bPlayStation\b(?! Vita\b)/i.test(G)&&"NetFront")&&(T=[l]),"IE"==G&&(l=(/; *(?:XBLWP|ZuneWP)(\d+)/i.exec(t)||0)[1])?(G+=" Mobile",K="Windows Phone "+(/\+$/.test(l)?l:l+".x"),R.unshift("desktop mode")):/\bWPDesktop\b/i.test(t)?(G="IE Mobile",K="Windows Phone 8.x",R.unshift("desktop mode"),F||(F=(/\brv:([\d.]+)/.exec(t)||0)[1])):"IE"!=G&&"Trident"==T&&(l=/\brv:([\d.]+)/.exec(t))&&(G&&R.push("identifying as "+G+(F?" "+F:"")),G="IE",F=l[1]),I){if(function(e,t){var i=null!=e?typeof e[t]:"number";return!/^(?:boolean|number|string|undefined)$/.test(i)&&("object"!=i||!!e[t])}(i,"global"))if(w&&(W=(l=w.lang.System).getProperty("os.arch"),K=K||l.getProperty("os.name")+" "+l.getProperty("os.version")),E){try{F=i.require("ringo/engine").version.join("."),G="RingoJS"}catch(z){(l=i.system)&&l.global.system==i.system&&(G="Narwhal",K||(K=l[0].os||null))}G||(G="Rhino")}else"object"==typeof i.process&&!i.process.browser&&(l=i.process)&&("object"==typeof l.versions&&("string"==typeof l.versions.electron?(R.push("Node "+l.versions.node),G="Electron",F=l.versions.electron):"string"==typeof l.versions.nw&&(R.push("Chromium "+F,"Node "+l.versions.node),G="NW.js",F=l.versions.nw)),G||(G="Node.js",W=l.arch,K=l.platform,F=(F=/[\d.]+/.exec(l.version))?F[0]:null));else h(l=i.runtime)==u?(G="Adobe AIR",K=l.flash.system.Capabilities.os):h(l=i.phantom)==M?(G="PhantomJS",F=(l=l.version||null)&&l.major+"."+l.minor+"."+l.patch):"number"==typeof C.documentMode&&(l=/\bTrident\/(\d+)/i.exec(t))?(F=[F,C.documentMode],(l=+l[1]+4)!=F[1]&&(R.push("IE "+F[1]+" mode"),T&&(T[1]=""),F[1]=l),F="IE"==G?String(F[1].toFixed(1)):F[0]):"number"==typeof C.documentMode&&/^(?:Chrome|Firefox)\b/.test(G)&&(R.push("masking as "+G+" "+F),G="IE",F="11.0",T=["Trident"],K="Windows");K=K&&S(K)}if(F&&(l=/(?:[ab]|dp|pre|[ab]\d+pre)(?:\d+\+?)?$/i.exec(F)||/(?:alpha|beta)(?: ?\d)?/i.exec(t+";"+(I&&n.appMinorVersion))||/\bMinefield\b/i.test(t)&&"a")&&(A=/b/i.test(l)?"beta":"alpha",F=F.replace(RegExp(l+"\\+?$"),"")+("beta"==A?P:v)+(/\d+\+?/.exec(l)||"")),"Fennec"==G||"Firefox"==G&&/\b(?:Android|Firefox OS|KaiOS)\b/.test(K))G="Firefox Mobile";else if("Maxthon"==G&&F)F=F.replace(/\.[\d.]+/,".x");else if(/\bXbox\b/i.test($))"Xbox 360"==$&&(K=null),"Xbox 360"==$&&/\bIEMobile\b/.test(t)&&R.unshift("mobile mode");else if(!/^(?:Chrome|IE|Opera)$/.test(G)&&(!G||$||/Browser|Mobi/.test(G))||"Windows CE"!=K&&!/Mobi/i.test(t))if("IE"==G&&I)try{null===i.external&&R.unshift("platform preview")}catch(z){R.unshift("embedded")}else(/\bBlackBerry\b/.test($)||/\bBB10\b/.test(t))&&(l=(RegExp($.replace(/ +/g," *")+"/([.\\d]+)","i").exec(t)||0)[1]||F)?(K=((l=[l,/BB10/.test(t)])[1]?($=null,X="BlackBerry"):"Device Software")+" "+l[0],F=null):this!=x&&"Wii"!=$&&(I&&B||/Opera/.test(G)&&/\b(?:MSIE|Firefox)\b/i.test(t)||"Firefox"==G&&/\bOS X (?:\d+\.){2,}/.test(K)||"IE"==G&&(K&&!/^Win/.test(K)&&F>5.5||/\bWindows XP\b/.test(K)&&F>8||8==F&&!/\bTrident\b/.test(t)))&&!p.test(l=e.call(x,t.replace(p,"")+";"))&&l.name&&(l="ing as "+l.name+((l=l.version)?" "+l:""),p.test(G)?(/\bIE\b/.test(l)&&"Mac OS"==K&&(K=null),l="identify"+l):(l="mask"+l,G=k?S(k.replace(/([a-z])([A-Z])/g,"$1 $2")):"Opera",/\bIE\b/.test(l)&&(K=null),I||(F=null)),T=["Presto"],R.push(l));else G+=" Mobile";(l=(/\bAppleWebKit\/([\d.]+\+?)/i.exec(t)||0)[1])&&(l=[parseFloat(l.replace(/\.(\d)$/,".0$1")),l],"Safari"==G&&"+"==l[1].slice(-1)?(G="WebKit Nightly",A="alpha",F=l[1].slice(0,-1)):F!=l[1]&&F!=(l[2]=(/\bSafari\/([\d.]+\+?)/i.exec(t)||0)[1])||(F=null),l[1]=(/\b(?:Headless)?Chrome\/([\d.]+)/i.exec(t)||0)[1],537.36==l[0]&&537.36==l[2]&&parseFloat(l[1])>=28&&"WebKit"==T&&(T=["Blink"]),I&&(b||l[1])?(T&&(T[1]="like Chrome"),l=l[1]||((l=l[0])<530?1:l<532?2:l<532.05?3:l<533?4:l<534.03?5:l<534.07?6:l<534.1?7:l<534.13?8:l<534.16?9:l<534.24?10:l<534.3?11:l<535.01?12:l<535.02?"13+":l<535.07?15:l<535.11?16:l<535.19?17:l<536.05?18:l<536.1?19:l<537.01?20:l<537.11?"21+":l<537.13?23:l<537.18?24:l<537.24?25:l<537.36?26:"Blink"!=T?"27":"28")):(T&&(T[1]="like Safari"),l=(l=l[0])<400?1:l<500?2:l<526?3:l<533?4:l<534?"4+":l<535?5:l<537?6:l<538?7:l<601?8:l<602?9:l<604?10:l<606?11:l<608?12:"12"),T&&(T[1]+=" "+(l+="number"==typeof l?".x":/[.+]/.test(l)?"":"+")),"Safari"==G&&(!F||parseInt(F)>45)?F=l:"Chrome"==G&&/\bHeadlessChrome/i.test(t)&&R.unshift("headless")),"Opera"==G&&(l=/\bzbov|zvav$/.exec(K))?(G+=" ",R.unshift("desktop mode"),"zvav"==l?(G+="Mini",F=null):G+="Mobile",K=K.replace(RegExp(" *"+l+"$"),"")):"Safari"==G&&/\bChrome\b/.exec(T&&T[1])?(R.unshift("desktop mode"),G="Chrome Mobile",F=null,/\bOS X\b/.test(K)?(X="Apple",K="iOS 4.3+"):K=null):/\bSRWare Iron\b/.test(G)&&!F&&(F=N("Chrome")),F&&0==F.indexOf(l=/[\d.]+$/.exec(K))&&t.indexOf("/"+l+"-")>-1&&(K=O(K.replace(l,""))),K&&-1!=K.indexOf(G)&&!RegExp(G+" OS").test(K)&&(K=K.replace(RegExp(" *"+m(G)+" *"),"")),T&&!/\b(?:Avant|Nook)\b/.test(G)&&(/Browser|Lunascape|Maxthon/.test(G)||"Safari"!=G&&/^iOS/.test(K)&&/\bSafari\b/.test(T[1])||/^(?:Adobe|Arora|Breach|Midori|Opera|Phantom|Rekonq|Rock|Samsung Internet|Sleipnir|SRWare Iron|Vivaldi|Web)/.test(G)&&T[1])&&(l=T[T.length-1])&&R.push(l),R.length&&(R=["("+R.join("; ")+")"]),X&&$&&$.indexOf(X)<0&&R.push("on "+X),$&&R.push((/^on /.test(R[R.length-1])?"":"on ")+$),K&&(l=/ ([\d.+]+)$/.exec(K),s=l&&"/"==K.charAt(K.length-l[0].length-1),K={architecture:32,family:l&&!s?K.replace(l[0],""):K,version:l?l[1]:null,toString:function(){var e=this.version;return this.family+(e&&!s?" "+e:"")+(64==this.architecture?" 64-bit":"")}}),(l=/\b(?:AMD|IA|Win|WOW|x86_|x)64\b/i.exec(W))&&!/\bi686\b/i.test(W)?(K&&(K.architecture=64,K.family=K.family.replace(RegExp(" *"+l),"")),G&&(/\bWOW64\b/i.test(t)||I&&/\w(?:86|32)$/.test(n.cpuClass||n.platform)&&!/\bWin64; x64\b/i.test(t))&&R.unshift("32-bit")):K&&/^OS X/.test(K.family)&&"Chrome"==G&&parseFloat(F)>=39&&(K.architecture=64),t||(t=null);var V={};return V.description=t,V.layout=T&&T[0],V.manufacturer=X,V.name=G,V.prerelease=A,V.product=$,V.ua=t,V.version=G&&F,V.os=K||{architecture:null,family:null,version:null,toString:function(){return"null"}},V.parse=e,V.toString=function(){return this.description||""},V.version&&R.unshift(F),V.name&&R.unshift(G),K&&G&&(K!=String(K).split(" ")[0]||K!=G.split(" ")[0]&&!$)&&R.push($?"("+K+")":"on "+K),R.length&&(V.description=R.join(" ")),V}();a.platform=y,void 0===(r=function(){return y}.call(t,i,t,e))||(e.exports=r)}.call(this)}}]);
//# sourceMappingURL=556.bundle.js.map