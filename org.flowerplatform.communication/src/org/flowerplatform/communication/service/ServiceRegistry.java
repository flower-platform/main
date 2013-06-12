package org.flowerplatform.communication.service;


import java.util.HashMap;
import java.util.Map;

/**
 * Central registry for application services, that may be invoked from
 * the Flex client, using {@link InvokeServiceMethodServerCommand}.
 * 
 * <p>
 * <strong>IMPORTANT NOTE:</strong> The service mechanism is a replacement/short hand of the 
 * original command system. The advantage is that there is less code to write (e.g. 3 methods
 * instead of 3 + 3 classes). The disadvantage is that there is no type checking (on the Flex side).
 * So <strong>please pay special attention when the signature of a service method changes</strong>,
 * because you won't get any compiler error. Look carefully in the whole workspace (using CTRL + H
 * and the name of the service or method), in order to modify ALL places that invokes that particular
 * method.
 * 
 * @see The class diagram, the mindmap and the wiki documentation.
 * @see InvokeServiceMethodServerCommand
 * 
 * @author Cristi
 * @flowerModelElementId _rmkTQFZbEeGgtLw8YArqtQ
 */
public class ServiceRegistry {
	
	/**
	 * Contains the registered services.
	 * 
	 * @flowerModelElementId _xBOlIFZbEeGgtLw8YArqtQ
	 */
	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * Registers a new service.
	 * 
	 * @flowerModelElementId _7KrhYFZbEeGgtLw8YArqtQ
	 */
	public void registerService(String id, Object serviceInstance) {
		map.put(id, serviceInstance);
	}

	/**
	 * Gets the service by its string id.
	 * 
	 * @return The registered service or <code>null</code> if nothing found.
	 * @flowerModelElementId _-fgvMFZbEeGgtLw8YArqtQ
	 */
	public Object getService(String id) {
		return map.get(id);
	}
}