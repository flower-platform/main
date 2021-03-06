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
package org.flowerplatform.communication.tree.remote {
	import flash.events.Event;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.flexutil.tree.TreeList;
	
	/**
	 * Model element used by the tree to display data. Instances come from
	 * Java.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 
	 * 
	 */
	[Bindable]
	[RemoteClass]
	[SecureSWF(rename="off")]
	public class TreeNode {
		
		private var _label:String;
		
		private var _icon:String;
				
		private var _hasChildren:Boolean;
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var children:ArrayCollection;
				
		/** 
		 * 
		 */
		[SecureSWF(rename="off")]
		public var parent:TreeNode;
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var pathFragment:PathFragment;
				 
		[SecureSWF(rename="off")]
		public var customData:Object;
		
		[SecureSWF(rename="off")]
		/**
		 * 
		 */
		public function get label():String {
			return _label;
		}

		/**
		 * @private
		 */
		public function set label(value:String):void {
			_label = value;
			dispatchEvent(new Event(TreeList.UPDATE_TREE_RENDERER_EVENT));
		}

		public function get icon():String {
			return _icon;
		}

		public function set icon(value:String):void {
			_icon = value;
			dispatchEvent(new Event(TreeList.UPDATE_TREE_RENDERER_EVENT));
		}

		[SecureSWF(rename="off")]
		/**
		 * 
		 */
		public function get hasChildren():Boolean {
			return _hasChildren;
		}

		/**
		 * @private
		 */
		public function set hasChildren(value:Boolean):void {
			_hasChildren = value;
			dispatchEvent(new Event(TreeList.UPDATE_TREE_RENDERER_EVENT));
		}

        /**
         * 
         */
        public function getPathForNode(addRootNode:Boolean = false):ArrayCollection {
        	var node:TreeNode = this;
        	if (node == null) {
				return null;
			}
			if(!addRootNode && node.parent == null) {
        		return null;
        	}
        	var path:ArrayCollection = new ArrayCollection();
        	while (addRootNode ? node != null :  node.parent != null) {        		
        		path.addItemAt(node.pathFragment, 0);
        		node = node.parent;
        	}			
			return path;
		}
		
		/**
		 * Based on a given <code>delimiter</code>,
		 * returns the full path of this node by concatenating
		 * all path fragments names.
		 * 
		 */ 
		public function getPath(delimiter:String="/"):String {
			var paths:ArrayCollection = getPathForNode();
			var path:String = "";
			for each (var pathFragment:PathFragment in paths) {
				if (path != "") {
					path += delimiter;
				}
				path += pathFragment.name;
			}
			return path;
		}
       
        /**
         * Iterates through all tree structure 
         * to find the corresponding node for given <code>path</code>.
         * Note: if problems, this can be optimized by using a hash map of paths.
         *  
         * 
         */
		 public static function getNodeByPath(path:ArrayCollection, parent:TreeNode = null):TreeNode {							
			if (path == null) {
				return parent;
			}			
			var node:TreeNode = parent;			
			for each (var pathFragment:PathFragment in path) {				
				parent = node;
				node = null;
				for each (var child:TreeNode in parent.children) {
					if (child.pathFragment.name == pathFragment.name &&
						child.pathFragment.type == pathFragment.type) {
						node = child;
						break;
					}
				}
				if (node == null) {
					return null;
				}
			}
			return node;			
		}	
		 
	}
	
}