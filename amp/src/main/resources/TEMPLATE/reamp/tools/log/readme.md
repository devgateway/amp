# Amp-debug
A debugging tool for AMP.

## Motivation
It's really annoying to have a lot of _console.logs_ in developer console, so you wanna reduce their number, but at the same time your wanna have as much debug as possible, to be able to understand what went wrong and often to predict what could go wrong from the logs. So how do you do that, how do you have less logs while having more logs?

## Introducing Amp-debug
Amp debug allows you to use several named(and namespaced) loggers, which you can turn on and off at will to see only relevant debug information, while at the same time hiding irrelevant information as well as hiding your debug from those who are not interested in it or shouln'd see it in the first place, i.e. other developers and end users. Amp debug also allows testers to attach the output from the console to the ticket, to provide additional information to the developer.

To give you an example, say you have these two debuggers, `amp:filters:view` and `amp:filters:model`. Normally you won't see none in the developer console, but you can access it by enabling debug, so by enabling `amp:filters:view` you will see its logs, but not `amp:filter:model` ones. You can also use wildcards to enable `amp:filters:*`, this way you'll see logs both from view and model, but not from other irrelevant to you parts of the application. You can also enable all debugs(i.e. all parts of the application) with `*`.

Currenlty amp-debug uses [this](https://www.npmjs.com/package/debug) library, so to enable/disable logs, you have to manipulate the 'debug' key in the local storage.
* In webkit browsers open developer tools(F12), go to Resources tab, then local storage, then create a new entry called _debug_ and set the according value, for example "amp:dashboard:*", "amp:dashboard:chart:*" etc
* You can also open the JS console and type in, for example, `localStorage.debug = "amp:gis"` 

## Why not just use debug?
So that we are not limited to this library only, we could add more tools or replace the debug completely in the future, but we wanna keep Amp-debug's API stable, so that swaping or adding debugging libraries would be easy and won't crash the app.

## Ok, how do I use it?
Include it in your libary, it is currenlty located in _reamp/tools/log_:

    var ampDebug = require('path/to/amp/debug');
Create a debugger:

    var filterDebug = ampDebug("amp:filters");
    
Use your debugger:

    filterDebug.log("New filters have been received. Applying them now")
    
## API
### log(a, b, c...)
Similar to _console.log_, will log a plain debug message
### warn(a, b, c...)
Like _console.warn_, will log a warning
### err(a, b, c...)
Like _console.error_, willl log an error(wich stack trace)
### onDebug(cb)
Takes a function that will be called if the current ampModule is being debugged, and not otherwise. Usefull when you wanna log big data or do some computations in order to produce the debug, and it doesn't make sense to do that unless during debug. For example:

    var chartDebugger = require('path/to/amp/debug')('amp:dashboard:chart');
    try{
        //antipatterns antipatterns bad practice antipatterns
    } catch(e){
        chartDebugger.onDebug(function(){
            chartDebugger.err("OH NOES!!! Something really terrible just happened, gonna dump EVERYTHING so that you can hopefully figure something out!")
            chartDebugger.log(everything.toJSON())
        });
    }
    
## Working on amp log
Go to `TEMPLATE\ampTemplate\ampModule\amp-log`. Install dependencies:

    npm install
Run webpack:

    webpack --watch
    
Edit `index.es6`.
### Tests
Tests are in `__tests__`. To run tests execute

    npm test