/*
 * AuthenticationCapabilitiesReceived.java 
 * Created on 2011-08-22
 *
 * Copyright (c) Verax Systems 2011.
 * All rights reserved.
 *
 * This software is furnished under a license. Use, duplication,
 * disclosure and all other uses are restricted to the rights
 * specified in the written license agreement.
 */
package ipmi.sm.events;

import ipmi.coding.commands.PrivilegeLevel;
import ipmi.sm.StateMachine;
import ipmi.sm.states.Authcap;
import ipmi.sm.states.AuthcapWaiting;

/**
 * Performs transition from {@link AuthcapWaiting} to {@link Authcap}.
 * 
 * @see StateMachine
 */
public class AuthenticationCapabilitiesReceived extends StateMachineEvent {
	private int sessionId;
	private PrivilegeLevel privilegeLevel;
	
	public AuthenticationCapabilitiesReceived(int sessionId, PrivilegeLevel privilegeLevel) {
		this.sessionId = sessionId;
		this.privilegeLevel = privilegeLevel;
	}
	
	public int getSessionId() {
		return sessionId;
	}
	public PrivilegeLevel getPrivilegeLevel() {
		return privilegeLevel;
	}
	
	
}
