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

public class FindImageQuestion extends HITjob {
	public final String imageURL;
	public String question;
	public String externalURL;
	
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

	public static int[] parseAnswer(String xmlIn) {
		int[] out = new int[4];
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		byte[] bytes = xmlIn.getBytes();
		Document doc = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new ByteArrayInputStream(bytes));
			NodeList e = doc.getElementsByTagName("Answer");
			for(int i = 0;i < 4;i++) {
				Node ans = e.item(i);
				Node qi = null;
				Node va = null;
				
				for(int j = 0; j < ans.getChildNodes().getLength();j++) {
					Node c = ans.getChildNodes().item(j);
					if(c.getNodeName().equals("QuestionIdentifier"))
						qi = c;
					else if(c.getNodeName().equals("FreeText"))
						va = c;
				}
				
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
