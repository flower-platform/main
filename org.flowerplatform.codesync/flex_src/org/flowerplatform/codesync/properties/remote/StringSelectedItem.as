/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation version 3.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
* 
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.codesync.properties.remote {
	import org.flowerplatform.properties.remote.SelectedItem;
	
	/**
	 * @author Mircea Negreanu
	 */
	[RemoteClass]
	public class StringSelectedItem extends SelectedItem {
		
		public var descriptorName:String;
		
		public var isRelation:Boolean;
		
		public function StringSelectedItem(descriptorName:String, isRelation:Boolean = false) {
			this.itemType = "descriptorElement";
			
			this.descriptorName = descriptorName;
			this.isRelation = isRelation;
		}
	}
}