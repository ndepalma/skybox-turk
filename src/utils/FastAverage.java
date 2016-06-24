package utils;

public class FastAverage extends RingBuffer<Float> {
	int window;
	float summedAverage = 0.0f;
	float realAverage =  0.0f;
	public FastAverage(int _window) {
		super(_window, Float.class);
		setHandler( new AddReplace() {
			@Override
			void handle(Float receive, Float remove) {
				if(remove != null)
					summedAverage += receive - remove;
				else
					summedAverage += receive;
				realAverage = summedAverage / window;
			}
			
		});
		window = _window;
	}
	
	public float getAverage() {
		return realAverage;
	}
}
