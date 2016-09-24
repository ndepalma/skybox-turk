package crowd.turkfrastructure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public abstract class NimbusURLHIT extends HITjob {
    int frameheight = 580;

    public abstract String renderURL();
    public NimbusURLHIT() {}
    public NimbusURLHIT(int frameHeight) {this.frameheight = frameHeight;}

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
