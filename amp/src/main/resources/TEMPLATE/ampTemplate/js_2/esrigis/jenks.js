
Array.prototype.findIndex = function(value) {
	var ctr = "";
	for ( var i = 0; i < this.length; i++) {
		// use === to check for Matches. ie., identical (===), ;
		if (this[i] == value) {
			return i;
		}
	}
	return ctr;
};

function getGVF(featureSet, attribute, numClass) {
	var dataList = [];
	for (n = 0, nl = featureSet.length; n < nl; n++) {
		dataList.push(featureSet[n][attribute]);
	}
	// get the breaks:
	// THIS IS WHAT IS USED TO RENDER THE MAP:
	var breaks = getJenksBreaks(dataList, numClass);
	
	dataList.sort();
	
	var listMean = sum(dataList) / dataList.length;
	var SDAM = 0.0;
	// now iterate through all the values and add up the sqDev to get the SDAM:
	for ( var i = 0, il = dataList.length; i < il; i++) {
		var sqDev = Math.pow((dataList[i] - listMean), 2);
		SDAM += sqDev;
	}
	
	var SDCM = 0.0;
	var classStart;
	for ( var x = 0, xl = numClass; x < xl; x++) {
		if (breaks[x] == 0) {
			classStart = 0;
		} else {
			classStart = dataList.findIndex(breaks[x]);
			classStart += 1;
		}
		var classEnd = dataList.findIndex(breaks[x + 1]);
		var classList = dataList.slice(classStart, classEnd + 1);
		var classMean = sum(classList) / classList.length;
		var preSDCM = 0.0;
		for ( var j = 0, jl = classList.length; j < jl; j++) {
			var sqDev2 = Math.pow((classList[j] - classMean), 2);
			preSDCM += sqDev2;
		}
		SDCM += preSDCM;
	}
	//var varFit = (SDAM - SDCM) / SDAM;
	//console.log("varianceFit:", varFit)
	return breaks;
}

// returns an array of length numClass + 1
function getJenksBreaks(dataList, numClass) {
	
	dataList.sort(sortNumber);
	if (dataList.length <= numClass)
	{
		var kclass = [];
		for(var i = 0; i <= numClass - dataList.length; i++)
			kclass.push(0);
		for(var i = 0; i < dataList.length; i++)
			kclass.push(dataList[i]);
		return kclass;
	}
	
	var mat1 = [];
	for ( var x = 0, xl = dataList.length + 1; x < xl; x++) {
		var temp = [];
		for ( var j = 0, jl = numClass + 1; j < jl; j++) {
			temp.push(0);
		}
		mat1.push(temp);
	}
	var mat2 = [];
	for ( var i = 0, il = dataList.length + 1; i < il; i++) {
		var temp2 = [];
		for ( var c = 0, cl = numClass + 1; c < cl; c++) {
			temp2.push(0);
		}
		mat2.push(temp2);
	}
	
	for ( var y = 1, yl = numClass + 1; y < yl; y++) {
		mat1[0][y] = 1;
		mat2[0][y] = 0;
		for ( var t = 1, tl = dataList.length + 1; t < tl; t++) {
			mat2[t][y] = Infinity;
		}
		var v = 0.0;
	}
	
	for ( var l = 2, ll = dataList.length + 1; l < ll; l++) {
		var s1 = 0.0;
		var s2 = 0.0;
		var w = 0.0;
		for ( var m = 1, ml = l + 1; m < ml; m++) {
			var i3 = l - m + 1;
			var val = parseFloat(dataList[i3 - 1]);
			s2 += val * val;
			s1 += val;
			w += 1;
			v = s2 - (s1 * s1) / w;
			var i4 = i3 - 1;
			if (i4 != 0) {
				for ( var p = 2, pl = numClass + 1; p < pl; p++) {
					if (mat2[l][p] >= (v + mat2[i4][p - 1])) {
						mat1[l][p] = i3;
						mat2[l][p] = v + mat2[i4][p - 1];
					}
				}
			}
		}
		mat1[l][1] = 1;
		mat2[l][1] = v;
	}
	var k = dataList.length;
	var kclass = [];
	
	for (i = 0, il = numClass + 1; i < il; i++) {
		kclass.push(0);
	}
	
	kclass[numClass] = parseFloat(dataList[dataList.length - 1]);
	
	kclass[0] = parseFloat(dataList[0]);
	var countNum = numClass;
	while (countNum >= 2 && k >= 0) {
		var id = parseInt((mat1[k][countNum]) - 2);
		kclass[countNum - 1] = dataList[id];
		k = parseInt((mat1[k][countNum] - 1));
		countNum -= 1;
	}
	
	if (kclass[0] == kclass[1]) {
		kclass[0] = 0;
	}
	return kclass;
}

function sum(arr) {
	var result = 0, n = arr.length || 0; 
	while (n--) {
		result += +arr[n]; 
	}
	return result;
}
//sorting function - ascending numbers:
function sortNumber(a, b) {
	return a - b;
}