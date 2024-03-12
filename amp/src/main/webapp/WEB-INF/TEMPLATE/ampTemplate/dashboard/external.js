/*
 * When linking into client-side applications on AMP, this fixes the language
 * stored in the session
 * according to the URL parameter ?language=xx
 *
 */


/*http://stackoverflow.com/questions/979975/how-to-get-the-value-from-url-parameter */
var QueryString = function () {
  // This function is anonymous, is executed immediately and
  // the return value is assigned to QueryString!
  var query_string = {};
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    // If first entry with this name
    if (typeof query_string[pair[0]] === "undefined") {
      query_string[pair[0]] = pair[1];
      // If second entry with this name
    } else if (typeof query_string[pair[0]] === "string") {
      var arr = [ query_string[pair[0]], pair[1] ];
      query_string[pair[0]] = arr;
      // If third or later entry with this name
    } else {
      query_string[pair[0]].push(pair[1]);
    }
  }
  return query_string;
} ();

var distFolder = "build";

if (QueryString.language) {
  var request = new XMLHttpRequest();
  request.withCredentials = true;
  request.open('GET', ['/rest/translations/languages/', QueryString.language].join(""), false);
  request.send();
  document.cookie = "digi_language=" + QueryString.language + ";path=/";
}

window.location.replace([distFolder,"/","index.html"].join(""));