package org.flowerplatform.blazeds.endpoint;

import java.io.IOException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.security.auth.Subject;

import org.flowerplatform.communication.IPrincipal;

import flex.messaging.FlexContext;
import flex.messaging.io.amf.AmfMessageDeserializer;

/**
 * @author Sorin
 * @author Florin
 * 
 * @flowerModelElementId _g8LpUG22EeGQ6LdvAwMt-w
 */
public class FlowerWebSecureAmfMessageDeserializer extends AmfMessageDeserializer {
	
	@Override
	public Object readObject() throws ClassNotFoundException, IOException {		
		IPrincipal principal = (IPrincipal) FlexContext.getUserPrincipal();		
		Object returnObject = null;

		if (principal == null) {
			returnObject = super.readObject();	
		} else {
			try {
				// Subject.doAsPrivileged: A new AccessControlContext will be created that will have a ProtectionDomain (for this bundle).
				// To ACC will be added a ProtectionDomain for blazeds (com.crispico.flexbridge/lib/flex-messaging-core.jar)
				// and a protection domain of the plugin in which the command resides. All these protection domains will be associated with the principal.
				// As a result the instantiation of the command will be safe (that code can execute only if all the protection domain 
				// from ACC will have the requiered priviledges). 
				returnObject = Subject.doAsPrivileged(principal.getSubject(),
									new PrivilegedExceptionAction<Object>() {
			
										@Override
										public Object run() throws Exception {
											return FlowerWebSecureAmfMessageDeserializer.super.readObject();	
										}										
									}, null);
			} catch (PrivilegedActionException e) {
				if (e.getCause() instanceof ClassNotFoundException) {
					throw (ClassNotFoundException)e.getCause();
				} else if (e.getCause() instanceof IOException) {
					throw (IOException)e.getCause();
				} else if (e.getCause() instanceof RuntimeException) {
					throw (RuntimeException)e.getCause();
				}
				else {
					throw new RuntimeException(e.getCause());
				}
			}
		}
		return returnObject;
	}
}