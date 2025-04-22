if(!window.DHTMLSuite)var DHTMLSuite = new Object();
/************************************************************************************************************
*	Color functions
*
*	Created:			August, 23rd, 2006
*	@class Purpose of class:	This class provides some methods for working with colors.
*			
* 	Update log:
*
************************************************************************************************************/

/**
* @constructor
* @class This class provides some methods for working with colors.
* @version 1.0
* @author	Alf Magne Kalleland(www.dhtmlgoodies.com)
**/

DHTMLSuite.colorHelp = function()
{
	
	
}

DHTMLSuite.colorHelp.prototype = {

	// {{{ baseConverter()
    /**
     *	converts numbers from different number systems(example: Decimal to octal)
     * 	
     *	@param mixed numberToConvert - Number to convert
     *	@param int oldBase - Convert from which base(8 = octal, 10 = decimal, 16 = hexadecimal)
     *	@param int newBase - Convert to which base(8 = octal, 10 = decimal, 16 = hexadecimal)
     *	
     *	@return String number in new base.(Example: decimal "16" returns "F" when converted to hexadecimal)
     *	@type String
     *
     * @public
     */	
    	
	baseConverter : function(numberToConvert,oldBase,newBase) {
		numberToConvert = numberToConvert + "";
		numberToConvert = numberToConvert.toUpperCase();
		var listOfCharactersOfCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		var dec = 0;
		for (var i = 0; i <=  numberToConvert.length; i++) {
			dec += (listOfCharacters.indexOf(numberToConvert.charAt(i))) * (Math.pow(oldBase , (numberToConvert.length - i - 1)));
		}
		numberToConvert = "";
		var magnitude = Math.floor((Math.log(dec))/(Math.log(newBase)));
		for (var i = magnitude; i >= 0; i--) {
			var amount = Math.floor(dec/Math.pow(newBase,i));
			numberToConvert = numberToConvert + listOfCharacters.charAt(amount); 
			dec -= amount*(Math.pow(newBase,i));
		}
		if(numberToConvert.length==0)numberToConvertToConvert=0;
		return numberToConvert;
	}
	// }}}	
	,

	// {{{ getHSV()
    /**
     *	Converts a RGB color to HSV
     * 	
     *	@param String rgbColor - Example: #FF12AB or FF12AB
     *	@return Array H,S,B = Hue, Saturation and Brightness
     *	@type Array
     *
     * @public
     */		
	getHSV : function(rgbColor){
		rgbColor = rgbColor.replace('#','');		
		
		red = baseConverter(rgbColor.substr(0,2),16,10);
		green = baseConverter(rgbColor.substr(2,2),16,10);
		blue = baseConverter(rgbColor.substr(4,2),16,10);
		if(red.length==0)red=0;
		if(green.length==0)green=0;
		if(blue.length==0)blue=0;
		red = red/255;
		green = green/255;
		blue = blue/255;
		
		maxValue = Math.max(red,green,blue);
		minValue = Math.min(red,green,blue);
		
		var hue = 0;
		
		if(maxValue==minValue){
			hue = 0;
			saturation=0;
		}else{
			if(red == maxValue){
				hue = (green - blue) / (maxValue-minValue)/1;	
			}else if(green == maxValue){
				hue = 2 + (blue - red)/1 / (maxValue-minValue)/1;	
			}else if(blue == maxValue){
				hue = 4 + (red - green) / (maxValue-minValue)/1;	
			}
			saturation = (maxValue-minValue) / maxValue;
		}
		hue = hue * 60; 
		valueBrightness = maxValue;
		
		if(valueBrightness/1<0.5){
			//saturation = (maxValue - minValue) / (maxValue + minValue);
		}
		if(valueBrightness/1>= 0.5){
			//saturation = (maxValue - minValue) / (2 - maxValue - minValue);
		}	
			
		
		returnArray = [hue,saturation,valueBrightness];
		return returnArray;
	}
	// }}}	
	,
	// {{{ toRgb()
    /**
     *	Converts a RGB color to HSV
     * 	
     *	@param Int hue - Degrees - Position on color wheel. Value between 0 and 359
     *	@param float saturation - Intensity of color(value between 0 and 1)
     *	@param float valueBrightness - Brightness(value between 0 and 1)
     *
     *	@return String RGBColor - example #FF00FF
     *	@type String
     *
     * @public
     */		
	toRgb : function(hue,saturation,valueBrightness){
		Hi = Math.floor(hue / 60);
		if(hue==360)Hi=0;
		f = hue/60 - Hi;
		p = (valueBrightness * (1- saturation)).toPrecision(2);
		q = (valueBrightness * (1 - (f * saturation))).toPrecision(2);
		t = (valueBrightness * (1 - ((1-f)*saturation))).toPrecision(2);
	
		switch(Hi){
			case 0:
				red = valueBrightness;
				green = t;
				blue = p;				
				break;
			case 1: 
				red = q;
				green = valueBrightness;
				blue = p;
				break;
			case 2: 
				red = q;
				green = valueBrightness;
				blue = t;
				break;
			case 3: 
				red = p;
				green = q;;
				blue = valueBrightness;
				break;
			case 4:
				red = t;
				green = p;
				blue = valueBrightness;
				break;
			case 5:
				red = valueBrightness;
				green = p;
				blue = q;
				break;
		}
		
		if(saturation==0){
			red = valueBrightness;
			green = valueBrightness;
			blue = valueBrightness;		
		}
		
		red*=255;
		green*=255;
		blue*=255;
	
		red = Math.round(red);
		green = Math.round(green);
		blue = Math.round(blue);	
		
		red = baseConverter(red,10,16);
		green = baseConverter(green,10,16);
		blue = baseConverter(blue,10,16);
		
		red = red + "";
		green = green + "";
		blue = blue + "";
	
		while(red.length<2){
			red = "0" + red;
		}	
		while(green.length<2){
			green = "0" + green;
		}	
		while(blue.length<2){
			blue = "0" + "" + blue;
		}
		rgbColor = "#" + red + "" + green + "" + blue;
		return rgbColor.toUpperCase();
	}
	// }}}	
	,
	// {{{ findColorByDegrees()
    /**
     *	Returns RGB color from a position on the color wheel
     * 	
     *	@param String rgbColor - Rgb color to calculate degrees from
     *	@param Float degrees - How many degrees to move on the color wheel(clockwise)
     *
     *	@return String RGBColor - new rgb color - example #FF00FF
     *	@type String
     *
     * @public
     */	
	findColorByDegrees : function(rgbColor,degrees){
		rgbColor = rgbColor.replace('#','');
		myArray = this.getHSV(rgbColor);
		myArray[0]+=degrees;
		if(myArray[0]>=360)myArray[0]-=360;
		if(myArray[0]<0)myArray[0]+=360;	
		return toRgb(myArray[0],myArray[1],myArray[2]);
	}
	// }}}	
	,
	// {{{ findColorByBrightness()
    /**
     *	Returns a new rgb color after change of brightness
     * 	
     *	@param String rgbColor - RGB start color
     *	@param Int brightness - Change in brightness (value between -100 and 100)
     *
     *	@return String RGBColor - new rgb color - example #FF00FF
     *	@type String
     *
     * @public
     */		
	findColorByBrightness : function(rgbColor,brightness){
		
		rgbColor = rgbColor.replace('#','');
		myArray = thhis.getHSV(rgbColor);
		
		myArray[2]+=brightness/100;
		if(myArray[2]>1)myArray[2]=1;
		if(myArray[2]<0)myArray[2]=0;	
		
		myArray[1]+=brightness/100;
		if(myArray[1]>1)myArray[1]=1;
		if(myArray[1]<0)myArray[1]=0;		
		
		return toRgb(myArray[0],myArray[1],myArray[2]);			
	}
	// }}}
	,
	// {{{ getRgbFromNumbers()
    /**
     *	Returns a color in RGB format(e.g.: #FFEECC from numeric values of red, green and blue)
     * 	
     *	@param Int red - Amount of red(0-255)
     *	@param Int green - Amount of green(0-255)
     *	@param Int blue - Amount of blue(0-255)
	 *
     *
     *	@return String RGBColor - new rgb color - example #FF00FF
     *	@type String
     *
     * @public
     */	
	getRgbFromNumbers : function(red,green,blue)
	{
		red = this.baseConverter(red,10,16);
		if(red.length==0)red = '0' + red;
		green = this.baseConverter(green,10,16);
		if(green.length==0)green = '0' + green;
		blue = this.baseConverter(blue,10,16);
		if(blue.length==0)blue = '0' + blue;
		return '#' + red + green + blue;
	} 
    
    
	
}