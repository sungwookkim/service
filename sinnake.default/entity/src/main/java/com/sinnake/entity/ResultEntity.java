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
