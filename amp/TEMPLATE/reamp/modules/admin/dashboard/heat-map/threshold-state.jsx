import Threshold from "./threshold.jsx";

export default class ThresholdState {
  constructor(nextProps, refs) {
    this._invalidIds = new Set([]);
    this._invalidRangeIds = new Set([]);
    this._initIdValue(nextProps);
    this._initNextLimits(nextProps, refs);
  }
  
  _initIdValue(nextProps) {
  	let map = new Map();
  	this.idValue = nextProps.data.reduce(function(map, threshold) {
    	map.set(threshold.id, threshold.amountFrom);
    	return map;
    }, map);
  }
  
  _initNextLimits(nextProps, refs) {
  	let sortedThresholds = this._getSortedValues();
    for(let threshold of nextProps.data) {
        let nextLimit = this._findNextLimit(threshold.amountFrom, sortedThresholds);
    	if (refs[threshold.id] !== undefined) {
    		refs[threshold.id].to = nextLimit; 
    	} else {
    		threshold.to = nextLimit;
    	}
    }
  }
  
  _getSortedValues() {
  	let sortedThresholds = Array.from(this.idValue.values(), v => v);
  	return sortedThresholds.sort(function(a, b) {return a-b});
  }
  
  _findNextLimit(value, sortedThresholds) {
  	let nextLimit = sortedThresholds.find(function(amount) {return amount > value});
  	return (nextLimit === undefined) ? 100 : nextLimit;
  }
  
  getInvalidRanges() {
  	return this._invalidRangeIds;
  }
  
  getIdValue() {
  	return this.idValue;
  }
  
  validateThresholdChange(elem, refs) {
    let invalidCount = this._validateValue(elem, refs);
    this._invalidIds.forEach(id => invalidCount += this._validateValue(refs[id], refs));
    this._validateRanges(refs);
    this._updateToLimits(refs);
    return invalidCount === 0 && this._invalidRangeIds.size === 0;
  }
  
  _validateValue(elem, refs) {
    this.idValue.set(elem.props.id, elem.value);
    let sameValueElements = new Map([...this.idValue].filter(([key, value]) => value == elem.value)); 
    if (sameValueElements.size > 1) {
    	for (let [key, value] of sameValueElements) {
    		refs[key].flagValidity(Threshold.STATUS.DUPLICATE);
    		this._invalidIds.add(key);
    	}
    	return sameValueElements.size;
    } else if (elem.value === null) {
    	refs[elem.props.id].flagValidity(Threshold.STATUS.INVALID);
    	return 1;
    } else {
    	this._invalidIds.delete(elem.props.id);
    	refs[elem.props.id].flagValidity(Threshold.STATUS.OK);
    	return 0;
    }
  }
  
  _updateToLimits(refs) {
  	let sortedValues = this._getSortedValues();
    for(let [key, value] of this.idValue) {
      refs[key].to = this._findNextLimit(refs[key].value, sortedValues);
    }
  }
  
  _validateRanges(refs) {
  	// we'll check against next only, to not repeat the same issue twice
  	let max = Object.keys(refs).length -2;
  	let invalidRangeIds = new Set([]);
  	let thresholdList = Object.values(refs);
  	thresholdList.forEach(function(threshold, index, array) {
  	  let next = array[index + 1];
  	  if (index < max && threshold.value >= next.value) {
  	  	invalidRangeIds.add(index);
  	  }
  	});
  	this._invalidRangeIds = invalidRangeIds;
  }
}