package org.flowerplatform.communication.tree;


import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService2;

/**
 * @see NodeInfo
 * 
 * @author Cristi
 * @author Cristina
 * @flowerModelElementId _NmOiIKTpEeGJQ4vD1xX4gA
 */
public class NodeInfoClient2 {
	
	/**
	 * @flowerModelElementId _NmOiIqTpEeGJQ4vD1xX4gA
	 */
	private CommunicationChannel communicationChannel;
	
	/**
	 * @flowerModelElementId _NmPJMKTpEeGJQ4vD1xX4gA
	 */
	private int treeNumber = -1;
	
	/**
	 * @flowerModelElementId _i1v6AsBrEeG5PP70DrXYIQ
	 */
	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}

	/**
	 * @flowerModelElementId _i1v6BMBrEeG5PP70DrXYIQ
	 */
	public void setCommunicationChannel(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;		
	}
	
	/**
	 * @flowerModelElementId _yOo3UBFDEeKNlYFNXVVOOw
	 */
	public int getTreeNumber() {
		return treeNumber;
	}

	/**
	 * @flowerModelElementId _yOtIwBFDEeKNlYFNXVVOOw
	 */
	public void setTreeNumber(int treeNumber) {
		this.treeNumber = treeNumber;
	}

	public String getStatefulClientId(GenericTreeStatefulService2 service) {
		return service.getStatefulClientPrefixId() + " " + treeNumber;		
	}
	
	/**
	 * @flowerModelElementId _i1xIIcBrEeG5PP70DrXYIQ
	 */
	public NodeInfoClient2(CommunicationChannel communicationChannel, String statefulClientId, GenericTreeStatefulService2 service) {		
		this.communicationChannel = communicationChannel;
		this.treeNumber = Integer.parseInt(statefulClientId.substring(statefulClientId.length() - 1));		
	}
		
	@Override
	public String toString() {
		return String.format("NodeInfoCl[%s,treNb=%s]", getCommunicationChannel(), getTreeNumber());
	}
	
}