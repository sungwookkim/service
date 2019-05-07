package commonInterface;

import com.sinnake.entity.ResultEntity;

public interface CommonProcess<T> {
	public ResultEntity<T> process();	
	public ResultEntity<T> validate();
}
