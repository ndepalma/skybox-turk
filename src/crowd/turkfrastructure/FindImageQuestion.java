package crowd.turkfrastructure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.DocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * This question let's users annotate an image and label the bounding box..
 *
 * @author ndepalma@media.mit.edu
 */
public class FindImageQuestion extends HITjob {
    // URL to the image
    public final String imageURL;
    // Question to ask the user
    public String question;
    // A wrapper to use around the image to provide the interface
    public String externalURL;

    /**
     * Constructor - doesn't provide a priori answer to overlay to the user.
     *
     * @param question A question you'd like to ask the worker.
     * @param imgURL A link to the URL to the image. You can upload this image into a standard directory.
     */
    public FindImageQuestion(String question, String imgURL) {
        imageURL = imgURL;
        
        try {
            this.question = URLEncoder.encode(question + " Select it with your mouse and click Submit.", "UTF-8").replaceAll("\\+","%20");
            this.externalURL = "	<ExternalURL>https://nimbus.media.mit.edu/selector/selectbox.html?file=images/" + imageURL + "&amp;titlequestion=" + question +"</ExternalURL>\n";
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Constructor - this one provides an a priori answer to prompt the user with. The user can correct that user.
     *
     * @param question A question you'd like to ask the worker.
     * @param imgURL A link to the URL to the image. You can upload this image into a standard directory.
     * @param ans A bounding box in the order (x,y,width,height)
     */
    public FindImageQuestion(String question, String imgURL, int[] ans) {
        imageURL = imgURL;
        
        try {
            this.question = URLEncoder.encode(question, "UTF-8").replaceAll("\\+","%20");
            this.externalURL = "	<ExternalURL>https://nimbus.media.mit.edu/selector/selectbox.html?file=images/" + 
                imageURL + 
                "&amp;titlequestion=" + 
                question +
                "&amp;startx=" + ans[0] +
                "&amp;starty=" + ans[1] +
                "&amp;width=" + ans[2] +
                "&amp;height=" + ans[3] +
                "</ExternalURL>\n";
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * Renders the HTML for mechanical turk to digest. 
     *
     * @return A StringBuffer that contains the rendered HTML for mechanical turk.
     */
    @Override
    public StringBuffer render() {
        //		"&assignmentId=a88a838aF88"
        return 
            new StringBuffer(
                             "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                             "<ExternalQuestion xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2006-07-14/ExternalQuestion.xsd\">\n" +
                             externalURL +
                             "	<FrameHeight>580</FrameHeight>\n" +
                             "</ExternalQuestion>");
    }

    /**
     * Parses the answer from the XML returned from mechanical turk.
     *
     * @param xmlIn A string that contains the returned XML from mechanical turk. 
     * @return A bounding box in the order (x,y,width,height)
     */
    public static int[] parseAnswer(String xmlIn) {
        int[] out = new int[4];
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        
        byte[] bytes = xmlIn.getBytes();
        Document doc = null;
        try {
            // Create xml reader
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(new ByteArrayInputStream(bytes));

            // Get the answer element for parsing
            NodeList e = doc.getElementsByTagName("Answer");
            for(int i = 0;i < 4;i++) {
                Node ans = e.item(i);
                Node qi = null;
                Node va = null;

                // Extract the metadata
                for(int j = 0; j < ans.getChildNodes().getLength();j++) {
                    Node c = ans.getChildNodes().item(j);
                    if(c.getNodeName().equals("QuestionIdentifier"))
                        qi = c;
                    else if(c.getNodeName().equals("FreeText"))
                        va = c;
                }
                
                // Parse the answer content to the array. Assumes the order is the same in the posted answer from the javascript.
                //				System.out.println("Key:" + qi.getTextContent() + ",val:"+va.getTextContent());
                out[i] = (int)Float.parseFloat(va.getTextContent());
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return out;
    }
}
