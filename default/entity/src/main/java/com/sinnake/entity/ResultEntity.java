package com.sinnake.entity;

import com.google.gson.Gson;

public class ResultEntity<T> {
	
	private String code;
	private T result;
	
	public ResultEntity(String code, T result) {
		this.code = code;
		this.result = result;
	}

	@SuppressWarnings("unchecked")
	public ResultEntity(String code) {
		this.code = code;
		this.result = (T) "";
	}

	public String getCode() { return code; }
	public T getResult() { return (T)result; }
	
	/*
	 * Sucess method
	 */
	public static ResultCode sucessCodeResultCode() {
		return ResultCode.SUCESS;
	}
	
	public static String sucessCodeString() {
		return sucessCodeResultCode().getCode();
	}
	
	public static int sucessCodeInt() {
		return sucessCodeResultCode().getCodeInt();
	}
	
	/*
	 * Fail method
	 */
	public static ResultCode failResultcode() {
		return ResultCode.FAIL;
	}

	public static String failCodeString() {
		return failResultcode().getCode();
	}
	
	public static int failCodeInt() {
		return failResultcode().getCodeInt();
	}

	/*
	 * Util
	 */
	public boolean sucess() {
		return sucessCodeResultCode().getCode().equals(this.code);
	}

	public static boolean sucess(String code) {
		return sucessCodeResultCode().getCode().equals(code);
	}
	
	public static boolean sucess(int code) {
		return sucessCodeResultCode().getCodeInt() == code;
	}
	
	@Override
	public String toString() {	
		return new Gson().toJson(this);
	}	
	
	public enum ResultCode {
		
		SUCESS(0)
		, FAIL(-1);
		
		private int code;
		
		private ResultCode(int code) {
			this.code = code;
		}
		
		public int getCodeInt() {
			return this.code;
		}
		
		public String getCode() {
			return String.valueOf(this.code);
		}
	}
}
