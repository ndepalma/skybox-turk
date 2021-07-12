package utils;
/**
 * This is a complicated interface that allows us to get the text from an enumeration. Say you create
 * a TextEnum{RED,BLUE,GREEN}, then you should be able to .getValue() and get the String version of it rather than the int.
 *
 * @author ndepalma@media.mit.edu
 */

public interface TextEnum<E extends Enum<E> & TextEnum>{
    // Stringify the enumeration
    public String getValue();
}
