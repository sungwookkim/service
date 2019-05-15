package util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.sinnake.entity.ResultEntity;

public class RestProcess<T> {
	Supplier<ResultEntity<T>> call = null;
	Supplier<ResultEntity<T>> fail = null;	
	Function<Exception, ResultEntity<T>> funcFail = null;
	
	public RestProcess() { }
	
	public RestProcess<T> call(Supplier<ResultEntity<T>> call) {
		this.call = call;
		return this;
	}
	
	public RestProcess<T> fail(Supplier<ResultEntity<T>> fail) {
		this.fail = fail;
		return this;
	}
	
	public RestProcess<T> fail(Function<Exception, ResultEntity<T>> funcFail) {
		this.funcFail = funcFail;
		return this;
	}	
	
	public ResultEntity<T> exec() {
		
		ResultEntity<T> rtn = null;
		
		try {
			rtn = Optional.of(this.call)
				.map(Supplier::get)
				.get();
		} catch(Exception e) {
			rtn = Optional.ofNullable(this.funcFail)
				.map(f -> f.apply(e))
				.orElseGet(() -> {
					return Optional.ofNullable(this.fail)
						.map(Supplier::get)
						.get();
				});					

			//throw e;
		}
		
		return rtn;
	}
}
