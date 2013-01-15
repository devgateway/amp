package org.axiis.data
{
	import mx.collections.ArrayCollection;
	
	/**
	 * This class is supports the DataSet class for aggregation and grouping operations and should not be instantiated externally
	 */
	public class DataGroup extends ArrayCollection
	{
		/**
		 * Constructor	
		 */
		public function DataGroup()
		{
		}
		
		/**
		 * Used to store the name of the group.  i.e. "Fruit"
		 */
		public var groupName:String;
		
		/**
		 * Used to store the unique value within its given group.  i.e. "Apple" or "Pear"
		 */
		public var name:String;
		
		/**
		 * The first row/unique object of data that is being used to create this group
		 */
		public var sourceData:Object;
		
		/**
		 * Child groups that may exist within this unique record
		 */
		public var groupedData:DataGroup;
		
		/**
		 * The parent DataGroup, if one exists
		 */
		public var parent:Object;
		
		/**
		 * Stores the sum aggregations for the group. 
		 * 
		 * note: Eventually we will support additional aggregation types like min/max/avg/std
		 */
		public var sums:Object=new Object(); 


	}
}