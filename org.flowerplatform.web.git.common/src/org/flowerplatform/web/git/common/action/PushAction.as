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
package org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.PushView;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class PushAction extends ActionBase {
		
		private var configPush:Boolean;
		
		public function PushAction(configPush:Boolean = false) {
			this.configPush = configPush;
			
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/push.gif");
			if (configPush) {
				label = GitCommonPlugin.getInstance().getMessage("git.action.push.label");				
				orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.push.sortIndex"));				
			} else {
				label = GitCommonPlugin.getInstance().getMessage("git.action.pushFromRemote.label");
				orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.pushFromRemote.sortIndex"));			
			}			
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				if (configPush) {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REPOSITORY;
				} else {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTE;
				}				
			}
			return false;
		}
		
		override public function run():void {
			if (configPush) {
				var popup:PushView = new PushView();
				popup.node = TreeNode(selection.getItemAt(0));
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setViewContent(popup)
					.setWidth(450)
					.setHeight(400)		
					.show();
			} else {
				GitCommonPlugin.getInstance().service.push(TreeNode(selection.getItemAt(0)), null, null, null);
			}
		}
	}
}