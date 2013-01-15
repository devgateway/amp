///////////////////////////////////////////////////////////////////////////////
//	Copyright (c) 2009 Team Axiis
//
//	Permission is hereby granted, free of charge, to any person
//	obtaining a copy of this software and associated documentation
//	files (the "Software"), to deal in the Software without
//	restriction, including without limitation the rights to use,
//	copy, modify, merge, publish, distribute, sublicense, and/or sell
//	copies of the Software, and to permit persons to whom the
//	Software is furnished to do so, subject to the following
//	conditions:
//
//	The above copyright notice and this permission notice shall be
//	included in all copies or substantial portions of the Software.
//
//	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//	OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//	HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//	WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//	OTHER DEALINGS IN THE SOFTWARE.
///////////////////////////////////////////////////////////////////////////////

package org.axiis.utils
{
	import mx.collections.ArrayCollection;
	
	/**
	 * This ObjectUtility class is primarily used to extract property values from dynamic objects
	 */
	public class ObjectUtils {
		
		/**
	 	 * Extracts propertyName from obj
	 	 * @param caller The object invoking this function.
	 	 * @param obj The object to extract a property value from
	 	 * @param propertyName The String representing a property to extract. propertyName supports a dot.dot syntax as well as Arrays 
	 	 * i.e propertyName="myObject.myArray[3].myOtherProperty" is a valid syntax
	 	 */
		public static  function getProperty(caller:Object, obj:Object, propertyName:Object):*
		{
			if(obj == null)
				return null;
				
			if (propertyName) {
				if (propertyName is Function) {
					return propertyName.call(caller,obj);
				}
			}	
			else
				return obj;
				
			var chain:Array=propertyName.split(".");
			if (chain.length == 1) {
				if (chain[0].charAt(0)=="[") {
					if(obj is Array) {
						return obj[chain[0].substr(1,chain[0].length-1)];
					}
					else if (obj is ArrayCollection) {
						return obj.getItemAt(int(chain[0].substr(1,chain[0].length-1)));
					}
				}
				else if (chain[0].indexOf("[")<0)  {//If we have an array return the array element
					//trace("ObjectUtil returning = " + obj[chain[0]]);
					return obj[chain[0]];
						
				}
				else {
					var element:Object= obj[chain[0].substr(0, chain[0].indexOf("["))];
					return element[chain[0].substr(chain[0].indexOf("[")+1,chain[0].indexOf("]")-chain[0].indexOf("[")-1)];
				}
			}
				
			else {
				if (chain[0].charAt(0)=="[") {
					if(obj is Array && obj.length > 0) {
						return getProperty(caller, obj[chain[0].substr(1,chain[0].length-1)],chain.slice(1,chain.length).join("."));
					}
					else if (obj is ArrayCollection && obj.length > 0) {
						return getProperty(caller, obj.getItemAt(int(chain[0].substr(1,chain[0].length-1))),chain.slice(1,chain.length).join("."));
					}
					else
						return null;
				}
				else if (chain[0].indexOf("[")<0)  //If we have an array return the array element
					return getProperty(caller, obj[chain[0]],chain.slice(1,chain.length).join("."));
				else
				{
						element= obj[chain[0].substr(0, chain[0].indexOf("["))];
						var index:String= (chain[0].substr(chain[0].indexOf("[")+1,chain[0].indexOf("]")-chain[0].indexOf("[")-1));
						return getProperty(caller, element[index],chain.slice(1,chain.length).join("."));
					}
					
			}
				
		}

	}
}