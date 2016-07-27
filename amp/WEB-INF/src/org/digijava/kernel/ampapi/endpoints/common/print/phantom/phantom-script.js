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
            webpage.render(imageFile);
            phantom.exit();
        }, 200);
    }
});