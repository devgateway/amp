package org.axiis.layouts.scale
{
	/**
	 * An all-static class which provides basic linear interpolation methods
	 */
	public class ScaleUtils
	{
		/**
		 * Performs linear interpolation, finding the value that is <code>percent</code>
		 * percent between <code>min</code> and <code>max</code>.
		 */
		public static function lerp(percent:Number, min:Number, max:Number):Number
		{
			return percent * (max - min) + min;
		}
		
		/**
		 * Returns the percentage between min and max where you would find value.
		 * 
		 * For example, if <code>min</code> is 10 and <code>max</code> is 20, passing in <code>value</code> of 15 would return .5. 
		 */
		public static function inverseLerp(value:Number, min:Number, max:Number):Number
		{
			//I know there is a more elegant way to calculate the below with polynomials, I am just too dense 
			//To figure it out so for now we work the permutations. - TG  11/6/09
			
			var toReturn:Number;
			
			if (value >=0 && min >=0) {
				toReturn=(value-min)/(max-min);
			}
			else if (value < 0 && min >=0) {
				toReturn=(value-min)/(max-min);
			}
			else if (value == 0 && min < 0) {
				toReturn=-min/(max-min);
			}
			else if (value >=0 && min < 0) {
				toReturn=(value-min)/(max-min);
			}
			else if (value < 0 && min < 0) {
				toReturn=(-min+value)/(max-min);
			}
			
		//	trace("value = " + value + " min=" + min + " max=" + max + " return=" + toReturn);
			
			return toReturn;
		}
	}
}