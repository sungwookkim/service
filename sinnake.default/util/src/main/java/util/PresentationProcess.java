package util;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sinnake.entity.ResultEntity;

public class PresentationProcess<T> {
	ResultEntity<T> resultEntity = null;
	ResponseEntity<ResultEntity<T>> responseEntity = null;
	
	Supplier<ResultEntity<T>> process = null;
	Supplier<ResultEntity<T>> exception = null;
	
	HttpStatus httpStatus = HttpStatus.OK;
	
	public PresentationProcess() { }

	public PresentationProcess(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	public PresentationProcess<T> status(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
		return this;
	}
	
	public PresentationProcess<T> process(Supplier<ResultEntity<T>> process) {
		this.process = process;
		return this;
	}
	
	public PresentationProcess<T> exception(Supplier<ResultEntity<T>> exception) {
		this.exception = exception;
		return this;
	}
	
	public ResponseEntity<ResultEntity<T>> exec() {
		try {
			this.resultEntity = Optional.of(this.process)
				.map(Supplier::get)
				.get();
		} catch(Exception e) {
			this.httpStatus = HttpStatus.EXPECTATION_FAILED;
			
			this.resultEntity = Optional.ofNullable(this.exception)
				.map(Supplier::get)
				.orElse(new ResultEntity<>("-99"));
		} finally {
			this.responseEntity = new ResponseEntity<ResultEntity<T>>(this.resultEntity, this.httpStatus);
		}
		
		return this.responseEntity;
	}
}
