// args to be pass to the script
// args[1]: URI to the html page, ie: file///tmp-path/file.html
// args[2]: URI to the output file, ie: /tmp-path/image-file.png
// args[3]: width
// args[4]: height
// args[5]: (optional) URI to the javascript file to execute: /tmp-path/script.js
var webpage = require('webpage').create();
var system = require('system');
var imageFile = system.args[2];
webpage.viewportSize = {width: system.args[3], height: system.args[4]};
webpage.open(system.args[1], function (status) {
    if (status !== 'success') {
        console.log('Unable to load the address!');
        phantom.exit(1);
    } else {
        if(system.args[5]) {
            console.log(webpage.injectJs(system.args[5]));
        }
        window.setTimeout(function () {
            webpage.render(imageFile, {format: 'png', quality: '100'});
            // webpage.render(pdfFilePath); // this should be to generate the PDF
            phantom.exit();
        }, 200);
    }
});