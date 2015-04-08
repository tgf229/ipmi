/*
 * GetSelInfo.java 
 * Created on 2011-08-11
 *
 * Copyright (c) Verax Systems 2011.
 * All rights reserved.
 *
 * This software is furnished under a license. Use, duplication,
 * disclosure and all other uses are restricted to the rights
 * specified in the written license agreement.
 */
package ipmi.coding.commands.sel;

import ipmi.coding.commands.CommandCodes;
import ipmi.coding.commands.IpmiCommandCoder;
import ipmi.coding.commands.IpmiVersion;
import ipmi.coding.commands.ResponseData;
import ipmi.coding.payload.CompletionCode;
import ipmi.coding.payload.IpmiPayload;
import ipmi.coding.payload.lan.IPMIException;
import ipmi.coding.payload.lan.IpmiLanRequest;
import ipmi.coding.payload.lan.IpmiLanResponse;
import ipmi.coding.payload.lan.NetworkFunction;
import ipmi.coding.protocol.AuthenticationType;
import ipmi.coding.protocol.IpmiMessage;
import ipmi.coding.security.CipherSuite;
import ipmi.common.TypeConverter;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Wrapper class for Get SEL Info request.
 */
public class GetSelInfo extends IpmiCommandCoder {

	/**
	 * Initiates GetSelInfo for both encoding and decoding.
	 * 
	 * @param version
	 *            - IPMI version of the command.
	 * @param cipherSuite
	 *            - {@link CipherSuite} containing authentication,
	 *            confidentiality and integrity algorithms for this session.
	 * @param authenticationType
	 *            - Type of authentication used. Must be RMCPPlus for IPMI v2.0.
	 */
	public GetSelInfo(IpmiVersion version, CipherSuite cipherSuite,
			AuthenticationType authenticationType) {
		super(version, cipherSuite, authenticationType);
	}

	@Override
	public byte getCommandCode() {
		return CommandCodes.GET_SEL_INFO;
	}

	@Override
	public NetworkFunction getNetworkFunction() {
		return NetworkFunction.StorageRequest;
	}

	@Override
	protected IpmiPayload preparePayload(int sequenceNumber)
			throws NoSuchAlgorithmException, InvalidKeyException {
		return new IpmiLanRequest(getNetworkFunction(), getCommandCode(), null,
				TypeConverter.intToByte(sequenceNumber % 64));
	}

	@Override
	public ResponseData getResponseData(IpmiMessage message)
			throws IllegalArgumentException, IPMIException,
			NoSuchAlgorithmException, InvalidKeyException {
		if (!isCommandResponse(message)) {
			throw new IllegalArgumentException(
					"This is not a response for Get SEL Info command");
		}
		if (!(message.getPayload() instanceof IpmiLanResponse)) {
			throw new IllegalArgumentException("Invalid response payload");
		}
		if (((IpmiLanResponse) message.getPayload()).getCompletionCode() != CompletionCode.Ok) {
			throw new IPMIException(
					((IpmiLanResponse) message.getPayload())
							.getCompletionCode());
		}

		byte[] raw = message.getPayload().getIpmiCommandData();

		if (raw == null || raw.length != 14) {
			throw new IllegalArgumentException(
					"Invalid response payload length: " + raw.length);
		}

		GetSelInfoResponseData responseData = new GetSelInfoResponseData();

		responseData.setSelVersion(TypeConverter
				.littleEndianBcdByteToInt(raw[0]));

		byte[] buffer = new byte[4];

		buffer[0] = raw[1];
		buffer[1] = raw[2];
		buffer[2] = 0;
		buffer[3] = 0;

		responseData.setEntriesCount(TypeConverter
				.littleEndianByteArrayToInt(buffer));

		System.arraycopy(raw, 5, buffer, 0, 4);

		responseData.setAdditionTimestamp(TypeConverter
				.decodeDate(TypeConverter.littleEndianByteArrayToInt(buffer)));

		System.arraycopy(raw, 9, buffer, 0, 4);

		responseData.setEraseTimestamp(TypeConverter.decodeDate(TypeConverter
				.littleEndianByteArrayToInt(buffer)));

		return responseData;
	}

}