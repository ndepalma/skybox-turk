package crowd.turkfrastructure;

/*
 * Abstract class that represents the HIT. Anything that implements this must be able to
 * render the HIT HTML to be digested by mechanical turk.
 *
 * @author ndepalma@media.mit.edu
 */
public abstract class HITjob {
    /**
     * Render to mechanical turk. Must conform to the XML schema that mechanical turk expects.
     *
     * @return A string buffer that contains the HTML for AWS Mechanical Turk.
     */
     public abstract StringBuffer render();
}
