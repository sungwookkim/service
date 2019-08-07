package commonInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface CommonGet {
	
	static public <T, R> R convert(T value, Function<T, R> convert) {
		
		return Optional.ofNullable(value)
			.map(v -> convert.apply(v))
			.orElse(null);
	}
	
	static public <T, R> List<R> convert(List<T> value, Function<T, R> convert) {

		return Optional.ofNullable(value)
			.orElseGet(ArrayList::new)
		.stream()
		.map(v -> convert(v, convert))
		.collect(Collectors.toList());
	}

	static public <T, R, U> Map<T, List<R>> convert(Map<T, List<U>> value, Function<U, R> convert) {
		
		return value.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey				
				, v -> convert(v.getValue(), convert) ));		
	}
}
