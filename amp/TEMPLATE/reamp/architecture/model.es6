"use strict";
var shallowCopy = obj => {
  var dest = {};
  Object.keys(obj).forEach(key => dest[key] = obj[key]);
  return dest;
};

export default class Model{
  constructor(_data = {}){
    this.__data = shallowCopy(_data);

    Object.keys(this.__data).forEach(key =>
      this[key] = valOrUpdate => {
        if('undefined' === typeof valOrUpdate) return this.get(key);
        if("function" == typeof valOrUpdate) return this.update(key, valOrUpdate);
        return this.set(key, valOrUpdate);
      }
    );

    this.__ = key => this.translations().get(key);
  }

  steal(target, ...keys){
    return keys.reduce((result, key) => result.set(key, target.get(key)), this);
  }

  get(key){
    return this.__data[key];
  }

  set(key, val){
    if(this.get(key) === val) return this;
    var clone = shallowCopy(this.__data);
    clone[key] = val;
    return new this.constructor(clone);
  }

  sortKeys(){
    return new this.constructor(this.keys().sort().reduce((accum, key) => {
      accum[key] = this.get(key);
      return accum;
    }, {}));
  }

  unset(key){
    var clone = shallowCopy(this.__data);
    delete clone[key];
    return new this.constructor(clone);
  }

  update(key, cb){
    return this.set(key, cb(this.get(key)));
  }

  getIn(path:Array<string>){
    return path.reduce((model, key) => model.get(key), this);
  }

  setIn(path:Array<string>, value):Model{
    var head = path[0], tail = path.slice(1);
    return !tail.length ? this.set(head, value) : this.update(head, model => model.setIn(tail, value));
  }

  unsetIn(path:Array<string>):Model{
    var head = path[0], tail = path.slice(1);
    return !tail.length ? this.unset(head) : this.update(head, model => model.unsetIn(tail));
  }

  updateIn(path:Array<string>, cb):Model{
    var head = path[0], tail = path.slice(1);
    return !tail.length ? this.update(head, cb) : this.update(head, model => model.updateIn(tail, cb));
  }

  keys():Array<string>{
    return Object.keys(this.__data);
  }

  has(key):boolean{
    return "undefined" != typeof this.get(key);
  }

  hasIn(path:Array<string>):boolean{
    var head = path[0];
    var tail = path.slice(1);
    return tail.length ? this.get(head).hasIn(tail) : this.has(head);
  }

  entries(){
    return this.keys().map(this.get.bind(this));
  }

  find(cb){
    return this.entries().find(cb);
  }

  filterEntries(cb){
    return new this.constructor(this.entries().filter(cb));
  }

  filter(cb){
    return this.keys().reduce(
      (result, key) => cb(this.get(key)) ? result : result.unset(key)
    , this)
  }

  reduce(cb, maybeInitial){
    return this.entries().reduce(cb, maybeInitial);
  }

  size(){
    return this.entries().length;
  }

  sort(cb){
    return new this.constructor(this.entries().sort(cb));
  }

  mapEntries(...args){
    return this.entries().map(...args);
  }

  map(cb){
    return this.keys().reduce(
      (result, key) => result.set(key, cb(this.get(key), key))
    , this);
  }

  head(){
    return this.entries()[0];
  }

  pop(){
    return this.entries().pop();
  }

  push(value){
    return this.set(this.size() + 1, value);
  }

  pushIn(path, value){
    var head = path[0], tail = path.slice(1);
    return !head ? this.push(value) : this.update(head, model => model.pushIn(tail, value));
  }

  some(...args){
    return this.entries().some(...args);
  }

  every(...args){
    return this.entries().every(...args);
  }

  equals(b: Model):boolean{
    return this.keys().length == b.keys().length &&
      this.keys().every( key => b.has(key) && (this.get(key) instanceof Model ?
                                                this.get(key).equals(b.get(key)) :
                                                this.get(key) == b.get(key))
                                              );
  }

  empty(){
    return !this.keys().length;
  }

  toJS(){
    var obj = {};
    this.keys().forEach(key => {
      var entry = this.get(key);
      obj[key] = entry instanceof Model ? entry.toJS() : entry;
    });
    return obj;
  }

  toJSON(){
    return JSON.stringify(this.toJS());
  }
}