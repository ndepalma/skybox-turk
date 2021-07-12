package utils;

/*
 * Callback interface that takes a specific type. If you implement this interface,
 * then you should be able to call the class with the templatized type. 
 *
 * @author ndepalma@media.mit.edu
 */
public interface CallbackInterface<Type> {
    /**
     * Call the class.
     *
     * @param val input
     */
    public void callback(Type val);
}
