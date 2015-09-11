import Model from "./model";
export class SideEffect{
  constructor(model: Model, cb){
    var ran = false;
    this.model = model;
    this.unleash = address => {
      if(ran){
        throw {
          name: "SideEffectException",
          message: "Side effect already unleashed!"
        }
      } else {
        ran = true;
        cb(address);
      }
    }
  }
}

export class TimeoutSideEffect extends SideEffect{
  constructor(model, time, cb){
    super(model, address => {
      setTimeout(() => cb(address), time)
    });
  }
}