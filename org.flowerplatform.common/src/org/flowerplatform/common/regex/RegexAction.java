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
package org.flowerplatform.common.regex;

/**
 * @author Cristina Constantinescu
 */
public abstract class RegexAction implements Runnable {

	protected String name;
	
	protected String description;

	public String getName() {
		return name;
	}

	public RegexAction setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public RegexAction setDescription(String description) {
		this.description = description;
		return this;
	}

	public abstract void executeAction(RegexProcessingSession param);
				
	@Override
	public void run() {		
	}
	
}
