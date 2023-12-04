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
5. let [model, SideEffect] = response
6. if the SideEffect is not null, launch it async
7. go to 2
```

Translated in English, basically that means that you have a *model* that describes the state of your application, a
[pure](https://en.wikipedia.org/wiki/Pure_function) optimized *view* that translates that model into DOM in reasonable time and a pure
 *update* function, that given a tuple (userAction, oldModel) can either produce a new model or a (model,
 SideEffect) tuple, the app is then rerendered with the new model. That implies that the only way to mutate the state
 of your app is by updating the model, the next possible state and the view of your app are being deduced from it.

 Concerning side effects, they are explicit ways for your app to specify it needs to communicate with the outside
  world, like doing an AJAX request. Side effects run async, and after they finish they produce a new action that is then piped into
  the update function.

Your model is just a plain data structure, preferably immutable, because that will allow for certain
types of optimizations and would prevent accidental mutations, and it must not contain any logic. It can, however
 have aggregating/filtering getters.

Your view is just a plain function that predictably returns DOM for a given set of arguments and declaratively
 specifies how the user can interact with your app. Naturally, it must not produce any side effects or else
 that would make it unpredictable.

Your update function is just a plain function that for any user interaction knows how to update your model in such a way,
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

The view layer is just React.js component. During it's first stages of development AMP-MVUSE bounced back and forth between React
and VirtualDOM, ultimately React was chosen for its more developed ecosystem. AMP-MVUSE can, however, reimplement
VirtualDOM support in the matters of minutes.

The update function is just a plain function that takes two arguments: the action and the current model; and is expected
to return a new model or a side effect tupled with that model.

Actions are functions created by _AMP.actions_ function.
 It's a JS work around ELM's [tagged unions](https://en.wikipedia.org/wiki/Tagged_union).

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
Before we proceed to our view, let's have the actions ready, we'll have two actions, one representing the increment and
other the decrement.
```js
export var actions = AMP.actions({
  Increment: null,
  Decrement: null
})
```
We export out actions so that other modules can listen to it.
_null_ means that the action takes no payload. If it took one parameter
we would specify its type like this:
```js
AMP.actions({
  takesNumber: "number",
  takesString: "string",
  takesInstanceOfModel: Model
});
```
If an action takes several parameters we specify their types in an array:
```js
AMP.actions({
  takesSeveral: ["number", "string", "Model"]
})
```
#### View
The view is just a React component. You can use plain React components
but the library comes with a nifty _AMP.view_ function.
```js
export var view = AMP.view((model: Model, actions) => (
  <p className="example-counter">
      <i className="glyphicon glyphicon-minus" onClick={actions.Increment}/>
      {value}
      <i className="glyphicon glyphicon-plus" onClick={actions.Decrement}/>
  </p>
));
```
Notice that the view already invokes our actions when +/- buttons are clicked.

#### Update
We're almost done. Let's make our update function:
```js
export function update(action, model: Model) => actions.match(action, {
  Increment: () => model.value(model.value() + 1),
  Decrement: () => model.value(model.value() - 1)
});
```
We use the _match_ method on our _actions_ variable that we created earlier.
It will call the corresponding callback for the action and will ensure that all
the actions are handled.

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
We'll only need an action for when the user clicks the "Save" button
button. However, we'll also need to handle the actions of the _Counter_ submodule.
We do this by creating an action that receives a _Counter_ action as its payload
(yes, actions can receive other actions as params ;) )
```js
export var actions = AMP.actions({
  save: null,
  counter: Counter.actions
})
```
#### View
Now, lets proceed to the view
```js
export var view = AMP.view((model, actions) => (
  <div>
      <Counter.view actions={actions.counter()} model={model.counter()}/>
      {model.status()}
      <button onClick={e => address.send(new Save()}/>Save</button>
  </div>
))
```
As you see, we reuse the Counter module, but notice its view's props.
By invoking _actions.counter_ we get a copy of _Counter.actions_ that are modified
in such a way that they will get sent to our _update_ function, rather than to
_Counter.update_.
Next we give it its submodule within our module, this is where it's going to keep
its state, we will contain the Counter within that submodel.

As you can also see, our view emits the Save action, but in order to process it
 in our _update_ function, we'll need to cover SideEffects

#### Side effects
Side effects are just plain functions
```js
var saveSideEffect = actions => fetch("/rest/counter", {
    method: 'post',
    credentials: 'same-origin',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      value: model.counter().value()
    })
  }).then(response => response.status >= 200 && response.status < 300 ?
    actions.saveSucceeded() :
    actions.saveFailed(response.statusText)
  )
```
What happened here? Well, we create a side effect, that will send a post request to our server and, in case it
succeeded, it will emit a saveSucceeded action, otherwise a saveFailed action carrying a payload describing the reason for
failure.

Oh, by the way, let's add those actions:
```js
export var actions = AMP.actions({
  save: null,
  counter: Counter.actions,
  saveSucceeded: null,
  saveFailed: "string"
})
```
Sweet! Now let's get that _update_ function done!

#### Update
First of all, as we said in the view section, the Counter's actions will now go to our update function. We don't
really care about them, so let's just forward them for now.
```js
export var update = (action, model: Model) => actions.match(action, {
  counter: counterAction =>
      model.counter(Counter.update(counterAction, model.counter()))
});
```
So what happened is we gave the Counter's update function its submodel from within our model, then we updated it with
its return value. This procedure is called forwarding, and there's also a utility function for that.
```js
export var update = (action, model: Model) => actions.match(action, {
  counter: counterAction =>
      AMP.updateSubmodel(['counter'], Counter.update, counterAction, model)
});
```
Here we just the specify the scope of the update, an update function as well as the action and the model.

Now let's do the fun part, the saving:
```js
export var update = (action, model: Model) => actions.match(action, {
  counter: counterAction =>
      AMP.updateSubmodel(['counter'], Counter.update, counterAction, model),

  save: () => [model.status("Saving"), saveSideEffect]
});
```
The update function can return a model or a tuple [model, sideEffect]
(actually the first is sugar for the second), so that's what's happenning here.
We update the model so that the status is now "Saving", and we also specify that
we want to run _saveSideEffect_ after the model is applied.
All that is left is to process success and failure:
```js
export var update = (action, model: Model) => actions.match(action, {
  counter: counterAction =>
      AMP.updateSubmodel(['counter'], Counter.update, counterAction, model),

  save: () => [model.status("Saving"), saveSideEffect],

  saveSuccess: () => model.status("Saved!"),

  saveFailed: reason => model.status(`Save failed because: ${reason}`)
});
```

And that's it! However, just for the sake of example, let's say that we want the counter to be updated
whenever it changes, without a "Save" button. Thanks to the MVUSE architecure that's trivial:
```js
export var update = (action, model: Model) => actions.match(action, {
  counter: counterAction => [
    AMP.updateSubmodel(['counter'], Counter.update, counterAction, model)
      .status("Saving"),
    saveSideEffect
  ],

  saveSuccess: () => model.status("Saved!"),

  saveFailed: reason => model.status(`Save failed because: ${reason}`)
});
```
Here, whenever a Counter update is needed, we ask it to update, then we also set the "Saving" status on the
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

export var actions = AMP.actions({
  counter: ["number", Counter.actions]
})

export var view = AMP.view((model, actions) => (
  <div>
    {model.counters().map((model, index) =>
      <Counter.view
        actions={actions.counter(index)}
        model={model.get(index)}
        key={index}
      />
    )}
  </div>
));
```
As you can see, this time the _counter_ actions has two params: first one is a
number, and the second is a _Counter.action_. In the view, we then invoke this action
passing it only the first param, by doing this we will get a set of _Counter.actions_
that will be tagged with this param so that we can use that tag in the _update_
function.

### Update
Now in update function we do:
```js
export var update = (action, model: Model) => actions.match(action, {
  counter: tag =>
    AMP.updateSubmodel(['counters', tag], Counter.update, action.originalAction, model)
});
```
What happens here is that all the actions the counters emit are now prepended
with a "number" parameter(the index of the counter), we use that parameter
to deduce the path of a specific counter submodel within our model, then we just
forward that to _Counter.update_

## API
