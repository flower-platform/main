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
package org.flowerplatform.communication.tree.remote;



/**
 * Holds information for a tree node like its name and its type.
 * 
 * <p>
 * Used to communicate with the client side.
 * 
 * @see NodeInfo
 * 
 * @author Cristi
 * @author Cristina
 * 
 * 
 */
public class PathFragment {
	
	/**
	 * 
	 */
	private String name;
	
	/**
	 * 
	 */
	private String type;

	/**
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 */
	public PathFragment() {		
	}
	
	/**
	 * 
	 */
	public PathFragment(String name, String type) {	
		this.name = name;
		this.type = type;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof PathFragment))
			return false;
		PathFragment other = (PathFragment) obj;
		
		if (this.name == null && other.name != null)
			return false;
		if (!this.name.equals(other.name))
			return false;
		
		if (this.type == null && other.type != null)
			return false;
		if (!this.type.equals(other.type))
			return false;
		
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {		
		return "[name=" + getName() + ",type=" + getType() + "]";
	}
			
}