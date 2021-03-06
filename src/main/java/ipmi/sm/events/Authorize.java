/*
 * Authorize.java 
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
import ipmi.coding.security.CipherSuite;
import ipmi.sm.StateMachine;
import ipmi.sm.states.Authcap;
import ipmi.sm.states.OpenSessionWaiting;

/**
 * Performs transition from {@link Authcap} to {@link OpenSessionWaiting}.
 * 
 * @see StateMachine
 */
public class Authorize extends Default {

	private int sessionId;
	
	public int getSessionId() {
		return sessionId;
	}

	public Authorize(CipherSuite cipherSuite, int sequenceNumber,
			PrivilegeLevel privilegeLevel, int sessionId) {
		super(cipherSuite, sequenceNumber, privilegeLevel);
		this.sessionId = sessionId;
	}

}
