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
package org.flowerplatform.orion.server;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.flowerplatform.editor.model.IModelAccessController;

/**
 * @author Cristina Constantinescu
 */
public class OrionModelAccessController implements IModelAccessController {

	@Override
	public URI getURIFromFile(Object file) {		
		return URI.createFileURI(((File) file).getAbsolutePath());
	}

}
