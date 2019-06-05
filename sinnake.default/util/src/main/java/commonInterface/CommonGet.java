package commonInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface CommonGet {
	
	default public <T, R> R convert(T value, Function<T, R> convert) {
		
		return Optional.ofNullable(value)
			.map(v -> convert.apply(v))
			.orElse(null);
	}
	
	default public <T, R> List<R> convert(List<T> value, Function<T, R> convert) {

		return Optional.ofNullable(value)
			.orElseGet(ArrayList::new)
		.stream()
		.map(v -> {
			return this.convert(v, convert);
		})
		.collect(Collectors.toList());
	}	
}
