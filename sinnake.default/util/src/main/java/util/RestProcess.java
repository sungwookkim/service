package util;

import java.util.Optional;
import java.util.function.Supplier;

import com.sinnake.entity.ResultEntity;

public class RestProcess<T> {
	Supplier<ResultEntity<T>> call = null;
	Supplier<ResultEntity<T>> fail = null;	
	
	public RestProcess() { }
	
	public RestProcess<T> call(Supplier<ResultEntity<T>> call) {
		this.call = call;
		return this;
	}
	
	public RestProcess<T> fail(Supplier<ResultEntity<T>> fail) {
		this.fail = fail;
		return this;
	}
	
	public ResultEntity<T> exec() {
		
		ResultEntity<T> rtn = null;
		
		try {
			rtn = Optional.of(this.call)
				.map(Supplier::get)
				.get();
		} catch(Exception e) {
			rtn = Optional.of(this.fail)
				.map(Supplier::get)
				.get();

			throw e;
		}
		
		return rtn;
	}
}
