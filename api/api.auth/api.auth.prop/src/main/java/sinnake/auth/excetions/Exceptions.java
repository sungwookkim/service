package sinnake.auth.excetions;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.google.gson.Gson;

@Configuration
@RestControllerAdvice
public class Exceptions {

	public Exceptions() { }
	
	@ExceptionHandler({
		BadSqlGrammarException.class
		, Exception.class
		, ServletException.class})
	public void exception(Exception e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		new Send(resp)
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.sendError(ExceptionsEnum.EXCEPTION);
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public void noHandlerFoundException(NoHandlerFoundException e, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		new Send(resp)
			.status(HttpStatus.NOT_FOUND)
			.sendError(ExceptionsEnum.NO_HANDLER_FOUND_EXCEPTION);
	}
	
	enum ExceptionsEnum {
		
		EXCEPTION("-1", "Server Error")
		, NO_HANDLER_FOUND_EXCEPTION("-2", "no handler found exception")
		, ACCESS_DENIED_EXCEPTION("-3", "access denied exception")		
		, BAD_CREDENTIALS_EXCEPTION("-4", "bad credentials exception")
		, LOCKED_EXCEPTION("-5", "locked exception");
		
		String code;
		String msg;
		
		private ExceptionsEnum(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		public String getCode() { return this.code; }
		public String getMsg() { return this.msg; }
	}
	
	class Send {
		private HttpServletResponse response;
		private HttpStatus httpStatus;
		
		public Send(HttpServletResponse response) {
			this.response = response;
		}
		
		public Send status(HttpStatus httpStatus) {
			this.httpStatus = httpStatus;
			
			return this;
		}
		
		public void sendError(ExceptionsEnum exceptionsEnum) throws IOException {
			HashMap<String, String> body = new HashMap<>();
			body.put("code", exceptionsEnum.getCode());
			body.put("msg", exceptionsEnum.getMsg());
			
			this.response.sendError(this.httpStatus.value(), new Gson().toJson(body));
		}
	}
	
	
}
