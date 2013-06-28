package org.flowerplatform.communication.tree;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService2;
import org.flowerplatform.communication.tree.remote.PathFragment;

/**
 * Holds information on server regarding a tree node.
 * 
 * <p>
 * It's build as a tree structure.
 * 
 * @author Cristi
 * @author Cristina
 * @flowerModelElementId _NmbWcKTpEeGJQ4vD1xX4gA
 */
public class NodeInfo2 {
	
	/**
	 * @see Getter doc.
	 * @flowerModelElementId _NmckkaTpEeGJQ4vD1xX4gA
	 */
	private Object node;

	/**
	 * @see Getter doc.
	 * @flowerModelElementId _NmeZwKTpEeGJQ4vD1xX4gA
	 */
	private NodeInfo2 parent;

	/**
	 * @see Getter doc.
	 * @flowerModelElementId _NmfA0KTpEeGJQ4vD1xX4gA
	 */
	private PathFragment pathFragment;
	
	/**
	 * @see Getter doc.
	 * @flowerModelElementId _NmlHcaTpEeGJQ4vD1xX4gA
	 */
	private List<NodeInfo2> children = new ArrayList<NodeInfo2>();
	
	/**
	 * @see Getter doc.
	 * @flowerModelElementId _Nmg2AKTpEeGJQ4vD1xX4gA
	 */
	private List<NodeInfoClient2> clients = new ArrayList<NodeInfoClient2>();
		
	/**
	 * @flowerModelElementId _xSZFkRN0EeKR8sYuzDGiDQ
	 */
	private List<NodeInfoClient2> clientsReadOnly = Collections.unmodifiableList(clients);
	
	private Map<String, Object> customData;
	/**
	 * The server object representing the node on client.
	 * @flowerModelElementId _NmwtoKTpEeGJQ4vD1xX4gA
	 */
	public Object getNode() {
		return node;
	}

	/**
	 * @flowerModelElementId _i12ntMBrEeG5PP70DrXYIQ
	 */
	public void setNode(Object node) {
		this.node = node;
	}

	/**
	 * The child nodes of the current node. Needed
	 * for cleanup, when a node is removed from the server.
	 * 
	 * @see GenericTreeStatefulService#dispatchContentUpdate()
	 * @flowerModelElementId _Nm1mIaTpEeGJQ4vD1xX4gA
	 */
	public List<NodeInfo2> getChildren() {
		return children;
	}

	/**
	 * @flowerModelElementId _i1310MBrEeG5PP70DrXYIQ
	 */
	public void setChildren(List<NodeInfo2> children) {
		this.children = children;
	}

	/**
	 * The parent {@link NodeInfo2}.
	 * Used to create this node's structure.
	 * @flowerModelElementId _Nm8T0aTpEeGJQ4vD1xX4gA
	 */
	public NodeInfo2 getParent() {
		return parent;
	}

	/**
	 * @flowerModelElementId _i14c4cBrEeG5PP70DrXYIQ
	 */
	public void setParent(NodeInfo2 parent) {
		this.parent = parent;
	}

	/**
	 * The path for the node.
	 * Used to communicate with clients.
	 * @flowerModelElementId _Nm_XIqTpEeGJQ4vD1xX4gA
	 */
	public PathFragment getPathFragment() {
		return pathFragment;
	}

	/**
	 * @flowerModelElementId _i15D8cBrEeG5PP70DrXYIQ
	 */
	public void setPathFragment(PathFragment pathFragment) {
		this.pathFragment = pathFragment;
	}

	/**
	 * @flowerModelElementId _xSelIBN0EeKR8sYuzDGiDQ
	 */
	public List<NodeInfoClient2> getClients() {
		return clientsReadOnly;
	}
	
	/**
	 * @flowerModelElementId _9NOu0BEiEeKYjqFAQECmkA
	 */
	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	/**
	 * @flowerModelElementId _nLLDUBOHEeKR8sYuzDGiDQ
	 */	
	public ReentrantReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

	/**
	 * @flowerModelElementId _jAmp4BEiEeKYjqFAQECmkA
	 */
	public NodeInfoClient2 getNodeInfoClientByCommunicationChannelThreadSafe(CommunicationChannel communicationChannel, String statefulClientId, GenericTreeStatefulService2 service) {
		readWriteLock.readLock().lock();
		try {
			// we use iteration with for instead of for each, because new elements might
			// be added to the list, and the iterator would complain. Cf. this method comment,
			// this kind of concurrent read/write access is valid. However, we wouldn't have liked
			// the opposite: to have elements removed. That's why we use the read/write lock
			for (int i = 0; i < clients.size(); i++) {
				NodeInfoClient2 client = clients.get(i);
				if (client.getCommunicationChannel().equals(communicationChannel) &&
					(statefulClientId == null || client.getStatefulClientId(service).equals(statefulClientId))) {
					return client;
				}
			}
			
			// no client found
			return null;
		} finally {
			readWriteLock.readLock().unlock();
		}
	}

	/**
	 * @flowerModelElementId _pEUf0BEiEeKYjqFAQECmkA
	 */
	public NodeInfoClient2 removeNodeInfoClientByCommunicationChannel(CommunicationChannel communicationChannel, String statefulClientId, GenericTreeStatefulService2 service) {
		readWriteLock.writeLock().lock();
		try {
			for (Iterator<NodeInfoClient2> iter = clients.iterator(); iter.hasNext(); ) {
				NodeInfoClient2 client = iter.next();
				if (client.getCommunicationChannel().equals(communicationChannel) &&
					(statefulClientId == null || client.getStatefulClientId(service).equals(statefulClientId))) {
					// found
					iter.remove();
					if (statefulClientId != null) { // only this node must be removed, so return
						return client;
					}
				}
			}
			return null;
		} finally {
			readWriteLock.writeLock().unlock();
		}
	}

	/**
	 * @flowerModelElementId _yaTXwBEiEeKYjqFAQECmkA
	 */
	public void addNodeInfoClient(NodeInfoClient2 client) {
		clients.add(client);
	}

	/**
	 * @flowerModelElementId _57obMBEiEeKYjqFAQECmkA
	 */
	public String toString() {
		return String.format("%s[path=%s]", getClass().getSimpleName(), getPathFragment());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NodeInfo2)) {
			return false;
		}
		return node.equals(((NodeInfo2) obj).node);
	}

	public Map<String, Object> getCustomData() {
		if (customData == null) {
			customData = new HashMap<String, Object>();
		}
		return customData;
	}

}