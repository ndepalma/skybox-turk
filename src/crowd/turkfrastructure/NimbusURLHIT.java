package crowd.turkfrastructure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/*
 * This abstract class will wrap a URL that we specify to be digested by mechanical turk. It writes
 * the most external headers so you don't have to and defines the schema we are using. 
 *
 * @author ndepalma@media.mit.edu
 */
public abstract class NimbusURLHIT extends HITjob {
    int frameheight = 580;

    // Return the URL on our host server.
    public abstract String renderURL();

    // Constructor
    public NimbusURLHIT() {}

    // Constructor that also includes the frame height.
    public NimbusURLHIT(int frameHeight) {
        this.frameheight = frameHeight;
    }

    /**
     * Render it out
     *
     * @return A string buffer to give to amazon mechanical turk.
     */
    @Override
    public StringBuffer render() {
        return
            new StringBuffer(
                             "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                             "<ExternalQuestion xmlns=\"http://mechanicalturk.amazonaws.com" +
                             "/AWSMechanicalTurkDataSchemas/2006-07-14/ExternalQuestion.xsd\">\n<ExternalURL>\n" +
                             renderURL() + "\n</ExternalURL>\n" +
                             "	<FrameHeight>"+ frameheight+"</FrameHeight>\n" +
                             "</ExternalQuestion>");
    }
}
