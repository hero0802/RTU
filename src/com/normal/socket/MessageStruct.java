package com.normal.socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.normal.function.Func;

public class MessageStruct {
	protected final Log log = LogFactory.getLog(MessageStruct.class);
	private Func fun = new Func();
	private short CMDHeader = (short) 0xC4D5;// commandHeader 2 命令开始字段
	private short Length = 22; // length 2 后接数据长度
	private short CMDId; // 2 命令id
	private String CallID = fun.RandomWord(8); // 8 业务流水号
	private short SeqNum = (short) 0; // 2 业务内序号
	private String Reserved = ""; // 8 保留字段。
	private short CheckSum = (short) 0x0000;// checksum 2 校验码 //18

/*	public int SegNum() {
		int num = uml.getGpsNum() + 2;
		int a = -1;
		if (num > 249) {
			a = num - 250;
		} else {
			a = num;
		}
		 log.info("SegNUM============"+a); 
		return a;
	}*/

	public short getCMDHeader() {
		return CMDHeader;
	}

	public void setCMDHeader(short cMDHeader) {
		CMDHeader = cMDHeader;
	}

	public short getLength() {
		return Length;
	}

	public void setLength(short length) {
		Length = length;
	}

	public short getCMDId() {
		return CMDId;
	}

	public void setCMDId(short cMDId) {
		CMDId = cMDId;
	}

	public String getCallID() {
		return CallID;
	}

	public void setCallID(String callID) {
		CallID = callID;
	}

	public short getSeqNum() {
		return SeqNum;
	}

	public void setSeqNum(short seqNum) {
		SeqNum = seqNum;
	}

	public String getReserved() {
		return Reserved;
	}

	public void setReserved(String reserved) {
		Reserved = reserved;
	}

	public short getCheckSum() {
		return CheckSum;
	}

	public void setCheckSum(short checkSum) {
		CheckSum = checkSum;
	}

}
