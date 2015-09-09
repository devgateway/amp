# AMP-MVUSE

MVUSE(Model View Update [Side Effect]) is an architecture for React/VirtualDOM-like libraries
inspired by [ELM architecture](https://github.com/evancz/elm-architecture-tutorial).

AMP-MVUSE is a React.js implementation of that architecture.

## Abstract
Say

* **M** is a type/set of all possible states your application can have
* **D** is a type/set of all possible DOM states your application can have
* **A** is a type/set representing all the possible ways the user can interact with your app
* **S** is a set of all possible side effects your app can produce, that also contains a zero element, meaing "no side
effect"

in that case the three pillars of a MVU app are:

* model &isin; M
* view: M -> D
* update: A X M -> M x S

Alternatively, MVUSE can be described algorithmically as:
```
1. let model = initialModel
2. call view(model), apply its result to DOM
3. wait for user interaction or side effect finishing
4. let response = update(userInteraction, model)
5. let (model, _) = response
6. let (_, SideEffect) = response
7. if the SideEffect is not null, launch it async
5. go to 2
```

Translated in English, basically that means that you have a *model* that describes the state of your application, a
[pure](https://en.wikipedia
.org/wiki/Pure_function) optimized *view* that translates that model into DOM in reasonable time and a pure
 *update* function, that given a tuple (userAction, oldModel) can either produce a new model or a (model,
 SideEffect) tuple, the app is then rerendered with the new model. That implies that the only way to mutate the state
 of your app is by updating the model, the next possible state and the view of your app are being deduced from it.

 Concerning side effects, they are explicit ways for your app to specify it needs to communicate with the outside
  world, i.e. do an AJAX request, they run async, and after they finish they produce a new action that is the piped into
  the update function.

Your model is just plain a plain data structure, preferably immutable, because that will allow for certain
types of optimizations and would prevent accidental mutations, and it must not contain any logic. You can, however
 have aggregating/filtering getters.

Your view is just a plain function that predictably returns DOM for a given set of arguments and declaratively
 specifies how the user can interact with your app. Naturally, it must not produce any side effects or else
 that would make it unpredictable.

You update is just a plain function that for any user interaction knows how to update your model in such a way,
that your app would transition to the desirable state.

You side effects are just plain functions that run async, they're a way for your app to explicitly, declaratively
specify the types of communication with outside world that it requires, they also force you to plan for those
communications failing and reacting accordingly.

## Motivation aka wasn't "Flux good enough for you?"
I find that Flux works well when building monolithic applications from scratch, however, I found it quite troublesome
to develop Flux submodules that then can be plugged into other existing Flux architecture. As well as transitioning
an already existing application to Flux on a module-by-module basis.

## Implementation
This particular implementation of MVU uses a custom immutable Model type for its state. Other Immutable solutions
  mainly ImmutableJS and SeemlessImmutable were rejected for the lack of possibility of subclassing for type safety
  and performance reasons accordingly.

The view layer is just React.js During it's first stages of development AMP-MVUSE bounced back and forth between React
and VirtualDOM, ultimately React was chosen for its more developed ecosystem. AMP-MVUSE can, however, reimplement
VirtualDOM support in the matters of minutes.

The update function is just a plain function that takes two arguments: the action and the current model; and is expected
to return a new model or a side effect tupled with that model.

Actions are instances of subclasses of _Action_ class, they can carry models with them and they can call back with
new actions. We use classes rather than primitives or objects in order to be able to type check the actions as well as
 make them carry payload. It's a JS work around ELM's [tagged unions](https://en.wikipedia.org/wiki/Tagged_union).

## Tutorial
### A counter
For starters, let's implement a simple counter. It has a value, and a plus and minus buttons that can increment and
decrement it.
#### Model
First, we'll need a model. We'll just need it to hold the current value of the counter. We'll subclass the AMP.Model
in order to make it immutable as well as make use of type annotations.
```js
import * as AMP from "path/to/architecture"
export class Model extends AMP.Model{
    value: number
}
```
Next, by conventions, we'll export a variable named _model_ that'll represent the initial state of our module.
```js
export var model: Model = new Model({
    value: 0
});
```
##### Resolvable models
In case you need to do AJAX in order to evaluate your initial model, the convention is to export a function
called _init_ that will return a promise that will resolve with your module's initial state.
```js
export init = () => fetch('/rest/counter')
    .then(response => response.json())
    .then(data => new Model({value: data.value}))
```

#### Actions
Before we proceed to our view, let's have the actions ready, we'll have two actions, on representing the increment and
other the decrement. First, we'll subclass the AMP.Action class creating a superclass for all of our module's actions
 and exporting it, that way we can type check the actions sent to our _update_ function as well as let other modules
  detect our actions.
```js
export class Action extends AMP.Action{}
```
Now we'll create the actual actions:
```js
class Increment extends Action{}
class Decrement extends Action{}
```
Notice that we've subclassed the internal _Action_ class, not _AMP.Action_, that fact will come in handy later, when
we write our update function.

#### View
The view is just a React component. We'll subclass the AMP.View class because it has the _shouldComponentUpdate_
already implemented
```js
class Counter extends AMP.View{
    render(){
        var {address, model} = this.props;
        return (
            <p className="example-counter">
                <i className="glyphicon glyphicon-minus" onClick={e => address.send(new Increment())}/>
                {value}
                <i className="glyphicon glyphicon-plus" onClick={e => address.send(new Decrement())}/>
            </p>
        )
    }
}
Counter.propTypes.model = React.PropTypes.instanceOf(Model);//type checking!
export {Counter as view}
```

Notice that the view already sends instances of our actions when +/- buttons are clicked.

#### Update
We're almost done. Let's make our update function:
```js
export function update(action: Action, model: Model):Model{
    if(action instanceof Increment){
        return model.value(model.value() + 1);
    }
    if(action instanceof Decrement){
        return model.value(model.value() - 1);
    }
}
```
Notice that we added type annotations to our function, that way we will receive a meaningfull error when it gets
invalid arguments or it returns and invalid state(mostly because of a fallthrough condition), hopefully during
 development. Use type annotations, they really do help to prevent a lot of silly bugs.


### A saveable counter
For the sake of example, let's say we now want a counter that can tell its value to the server. But we will not rewrite
our counter from the previous example, we'll reuse it.
#### Model
As before, we start with our model. We need our model to contain the state of the counter we're reusing, but we also
want it to contain the status message, so:
```js
import * as AMP from "path/to/architecture";
import * as Counter from "./counter";
export class Model extends AMP.Model{
    counter: Counter.Model,
    status: string
}

export var model = new Model({
    counter: Counter.model,
    status: ""
});
```
#### Actions
As before, let's create a superclass for our actions. And we're also need an action for when the user clicks the "Save"
button;
```js
export class Action extends AMP.Action{}
export class Save extends Action{}
```
#### View
Now, lets proceed to the view
```js
class SaveableCounter extends AMP.View{
    render(){
        var {address, model} = this.props;
        return (
            <div>
                <Counter.view address={address} model={model.counter()}/>
                {model.status()}
                <button onClick={e => address.send(new Save()}/>Save</button>
            </div>
        )
    }
}
Counter.propTypes.model = React.PropTypes.instanceOf(Model);
export {SaveableCounter as view}
```
As you see, we reuse the Counter module, but notice its view's props. The address, we just pass it down, meaning the
counter will send its actions to our _update_ function, where we can decide what we want to do with them.
Next we give it its submodule within our module, we'll actually contain the Counter within that submodel.

As you can also see, our view emits the Save action, but in order to process it in our _update_ function, we'll need to
cover SideEffects

#### Side effects
We create a side effect by subclassing the AMP.SideEffect class.
```js
class SaveSideEffect extends AMP.SideEffect{
  constructor(model){
    super(model, address => {
      fetch("/rest/counter", {
        method: 'post',
        credentials: 'same-origin',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          value: model.counter().value()
        })
      }).then(response => {
        if (response.status >= 200 && response.status < 300) {
          address.send(new SaveSuccess());
        } else {
          address.send(new SaveFailed(response.statusText))
        }
      });
    });
  }
}
```
What happened here? Well, we create a side effect, that will send a post request to our server and, in case it
succeeded, it will emit a SaveSuccess action, otherwise a SaveFailed action carrying a payload describing the reason for
failure.

Oh, by the way, let's creat those actions:
```js
export class SaveSuccess extends Action{}
export class SaveFailed extends Action{
    constructor(reason){
        this.reason = () => reason;//notice how we only make a getter so that the payload isn't accidentally mutated
    }
}
```

Sweet! Now let's get that _update_ function done!

#### Update
First of all, as we said in the view section, the Counter's actions will now go to our update function. We don't
really care about them, so let's just forward them for now.
```js
export function update(action: Action, model: Model):Model|SaveSideEffect{
    if(action instanceof Counter.Action){
        return model.counter(Counter.update(action, model.counter()))
    }
}
```
So what happened is we gave the Counter's update function its submodel from within our model, then we updated it with
its return value. This procedure is called forwarding, and there's also a utility function for that.
```js
export function update(action: Action, model: Model):Model|SaveSideEffect{
    if(action instanceof Counter.Action){
        return AMP.updateSubmodel['counter'], Counter.update, action, model);
    }
}
```
Here we just the specify the scope of the update, and update function as well as the action and the model.

Now let's do the fun part, the saving:
```js
export function update(action: Action, model: Model):Model|SaveSideEffect{
    if(action instanceof Counter.Action){
        return AMP.updateSubmodel(['counter'], Counter.update, action, model);
    }
    if(action instanceof Save){
        return new SaveSideEffect(model.status("Saving"));
    }
}
```
Here, we just update the model so that the status is now "Saving", we create a new SaveSideEffect and we
return that instance coupled with the new model.

All that is left is to process success and failure:
```js
export function update(action: Action, model: Model):Model|SaveSideEffect{
    if(action instanceof Counter.Action){
        return AMP.updateSubmodel(['counter'], Counter.update, action, model);
    }
    if(action instanceof Save){
        return new SaveSideEffect(model.status("Saving"));
    }
    if(action instanceof SaveSuccess){
        return model.status("Saved!");
    }
    if(action instanceof SaveFailed){
        return model.status("Save failed because: " + action.reason())
    }
}
```

And that's it! However, just for the sake of example, let's say that we want the counter to be updated
whenever it changes, without a "Save" button. Thanks to the MVUSE architecure that's trivial:
```js
export function update(action: Action, model: Model):Model|SaveSideEffect{
    if(action instanceof Counter.Action){
        return new SaveSideEffect(
            AMP.updateSubmodel(['counter'], Counter.update, action, model)
                .status("Saving")
        );
    }
    if(action instanceof SaveSuccess){
        return model.status("Saved!");
    }
    if(action instanceof SaveFailed){
        return model.status("Save failed because: " + action.reason())
    }
}
```
Here, whenever the Counter is updated, we ask it to update, then we also set the "Saving" status on the
model it returned, and finally we return the side effect coupled with that state.

### More counters
Say we now want to have over 9000 counters on the page at the same time. Well...
```js
import * as AMP from "path/to/architecture";
import * as Counter from "./counter";
export class Model extends AMP.Model{
    counters: Array<Counter.Model>
}

var counters = [];
for(var c = 0; c < 9001; c++){
    counters.push(Counter.model)
}

export var model = new Model({
    counters: counters
});

class Over9000Counters extends AMP.View{
    render(){
        var {address, model} = this.props;
        return (
            <div>
                {model.counters().map(model => <Counter.view address={address} model={model}/>)}
            </div>
        )
    }
}
Over9000Counter.propTypes.model = React.PropTypes.instanceof(Model);
export {Over9000Counters as view}
```

Ok, so we handled submodel scoping just fine, but wait, we gave all of those 9000 counters the address
of our update function, now they're all gonna call to us! How are gonna tell them apart for action forwarding?

Well, introducing....

#### Packages
Packages are actions that contain other actions(that includes other packages) as well as some taxonomy
associated with them. We create packages by subclassing the AMP.Package class.
```js
class CounterAction extends AMP.Package{}
```
Then we use them with address' _usePackage_ method, like this:
```js
class Over9000Counters extends AMP.View{
    render(){
        var {address, model} = this.props;
        return (
            <div>
                {model.counters().map((model, index) =>
                    <Counter.view
                        address={address.usePackage(CounterAction, index)}
                        model={model}
                    />)
                }
            </div>
        )
    }
}
```
The first argument is the type of the package you want to use, the second is the tag to use to mark
the actions coming to that specific address.
### Update
Now in update function we do:
```js
export function update(action: Action|CounterAction, model: Model):Model{
    if(action instanceof CounterAction){
        var path = ['counters', action.getTag()];
        return AMP.updateSubmodel(path, Counter.update, action.originalAction, model);
    }
}
```
What happens here is that all the actions the counters emit are now wrapped into the CounterAction package,
so you don't need to process individual Counter actions anymore. We get the tag of the package with
_.getTag_ and we can access the wrapped original action with _.originalAction_, which we then forward to
 the Counter.

## API