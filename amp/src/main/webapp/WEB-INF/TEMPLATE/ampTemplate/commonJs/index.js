var jQuery = require('jquery');

// boostrap looks for jquery in the global namespace, so put it there.
window.jQuery = jQuery;
require('bootstrap/dist/js/bootstrap');

// Unfortunately, we can't just override $ on our export...
// various backbone methods get $ from Backbone directly.
require('backbone').$ = jQuery;

// nvd3 will want d3 global sigh...
window.d3 = require('d3');


/*


ping walter re. pp footer
follow up on share urls
client-side cacheing


expense functionality in t3
sign printouts of all receipts
ask for full per diem rate
20005 zip (in contract)

*/
