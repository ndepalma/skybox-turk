package utils;

import java.util.ArrayList;

import filtertree.Filter;

public class ReducePattern<InType, OutType> {
	volatile ArrayList<OutType> reduced = new ArrayList<OutType>();
	
	public ArrayList<OutType> reduce(final InType[] dataIn, final Filter<InType, OutType> func) {
		ArrayList<Thread> allThreads = new ArrayList<Thread>();
		for(final InType data : dataIn) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					OutType ot = func.update(data);
					reduced.add(ot);
				}
			});
			t.start();
			allThreads.add(t);
		}
		
		for(Thread t : allThreads) {
			try {
				t.join();
			} catch(InterruptedException ie) {}
		}
		
		return reduced;
	}
}
