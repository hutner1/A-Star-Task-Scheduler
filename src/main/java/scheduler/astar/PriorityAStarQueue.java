package scheduler.astar;

import java.util.concurrent.PriorityBlockingQueue;

@SuppressWarnings("serial")
public class PriorityAStarQueue<T> extends PriorityBlockingQueue<T>{

	@Override
	public boolean add(T t) {

		if (this.contains(t)) {
			return false;
		} else {
			super.add(t);
			return true;
		}
	}
}
