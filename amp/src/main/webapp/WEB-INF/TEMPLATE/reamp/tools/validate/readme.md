# Validate
Validates strings and inputs

## Examples
### With React/VirtualDom
    <input onkeypress={allow(numbers)}/>
### With jQuery
    $('input').keypress(allow(numbers));
### With VanillaJS
    document.querySelector('input').addEventListener('keypress', allow(numbers));

## Types/terminology
### Validator
A validator is a function that takes a string as argument an returns a boolean

    type Validator = (str: string) => boolean

### Decorator
A decorator is a function that takes a validator and returns another validator

    type Decorator = (validator: Validator) => Validator

### Listener
A listener is a function that receives a KeyPressEvent as it's argument and has the side effect of
calling prevent default on the event based on its inner logic.

    type Listener = (e: KeyPressEvent) => void

### Listener creator
A listener creator is a function that receives a validator as its argument and returns a listener
that prevents default on events based on the result of validator's execution

    type ListenerCreator = (validator: Validator) => Listener

## Constants
* MIN_YEAR
* MAX_YEAR

## API
### keyPressEventToString:(e: KeyPressEvent) => string
Receives a keypress event as its argument and returns a string that represents the event's target
potential value in the case the event is not prevented.

For example, consider you have and input containing *abc* and you press the *d* key, the result of
executing keyPressEventToString with that last event would be *abcd*.

This function is smart enough to take selection into account. Consider you have *abc* in an input,
and you select *bc* with your mouse or Shift + arrow, then press *d*, the result of executing
keyPressEventToString with that event would be *ad*.
### allow: ListenerCreator
A listener creator. Takes a validator as argument and returns a listener that will prevent default
whenever the validator returns false.

### number: Validator
A validator. Takes a string as its argument and returns true or false depending on whether that string
represents a valid number.
This validator has a gotcha:

    number("-1") === true
    number(".5") === true

however, as expected

    number("-") === false
    number(".") === false


thus, doing

    <input onkeypress={allow(number)}/>

will make it impossible for the user to introduce _"-"_ or "." into an empty input.
Use the _negative_ and _point_ decorator to go around this issue.

### negative: Decorator
A decorator. Decorates its argument so that the returned validator will return true for _"-"_

    number("-") === false
    negative(number)("-") === true

Keep in mind to post check the input before submitting, as you might be about to send just a _"-"_ string

### point: Decorator
A decorator. Decorates its argument so that the returned validator will return true for _"."_

    number(".") === false
    point(number)(".") === true

Keep in mind to post check the input before submitting, as you might be about to send just a _"."_ string


### inBounds: (from: number) => (to:number) => Validator
A function that takes a number, returns a function that takes another number with the same amount of
digits as the first one and returns a validator, i.e.:

    inBounds(1000)(2000):Validator

When applied to an listener creator, this validator has some uncommon behaviour:

    <input onkeypress={allow(inBounds(1000)(1234))}/>

Given that the above input is empty, the validator will only allow the user to input an "1",
as any other digit can not start a valid input.
Similarly, if the above input contains "1", the validator will only allow the user to input "0", "1" or "2",
as only 10xx, 11xx and 12xx can lead to valid inputs, while, for example, 13xx or 14xx can not possibly
yield a valid input in the end.
And so on.

### year: Validator
An alias for inBounds(1970)(2050)

    year = inBounds(1970)(2050)