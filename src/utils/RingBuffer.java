package utils;

import java.lang.reflect.Array;

public class RingBuffer <TypeName>{
	TypeName []buffer;
	int pos;
	int windowSize;
	public abstract class AddReplace {
		abstract void handle(TypeName receive, TypeName remove);
	}
	private AddReplace handler;
	
	@SuppressWarnings("unchecked")
	RingBuffer(int _windowSize, Class<TypeName> classType, AddReplace _handler) {
		buffer = (TypeName[]) Array.newInstance(classType, _windowSize);
		pos =0;
		setHandler(_handler);
		windowSize = _windowSize;
	}
	
	RingBuffer(int _windowSize, Class<TypeName> classType) {
		this(_windowSize, classType, null);
	}
	
	public void push(TypeName newElement) {
		if(handler != null)
			handler.handle(newElement, buffer[pos]);
		buffer[pos] = newElement;
		pos = ++pos % windowSize;
	}

	public void setHandler(AddReplace handler) {
		this.handler = handler;
	}
	
}
