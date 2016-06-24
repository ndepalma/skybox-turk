package utils;

public interface TextEnum<E extends Enum<E> & TextEnum>{
	public String getValue();
}