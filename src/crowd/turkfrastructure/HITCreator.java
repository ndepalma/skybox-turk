package crowd.turkfrastructure;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.amazonaws.mturk.addon.HITQuestion;
import com.amazonaws.mturk.requester.Comparator;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.requester.HITStatus;
import com.amazonaws.mturk.requester.Locale;
import com.amazonaws.mturk.requester.QualificationRequirement;
import com.amazonaws.mturk.service.axis.RequesterService;

import utils.BasicUtils;
import utils.TextEnum;

public class HITCreator {
	private static String description = "This is a question generated by a robot from the MIT Media Lab.";
	private static int numAssignments = 1;
	private static double reward = 0.25;

	private static long assignmentDurationInSeconds = 45 ; // 1 hour
	private static long autoApprovalDelayInSeconds = 60 * 60 * 24 * 15; // 15 days
	private static long lifetimeInSeconds = 60 * 60 * 24 * 3; // 3 days

	private static String requesterAnnotation = "sample";
	private static String keywords = "MIT, robotics";

	/**
	 * Creates the simple survey.
	 *
	 */
	public static HIT createSimpleSurvey(String title, SurveyQuestions survey, boolean sandbox) {
		try {
			HITQuestion question = new HITQuestion();
			question.setQuestion(survey.render().toString());

			//Creating the HIT and loading it into Mechanical Turk
			HIT hit = TurkServer.getServer(sandbox).createHIT(title,
					getDescription(), keywords,
					question,
					getReward(), getAssignmentDurationInSeconds(),
					getAutoApprovalDelayInSeconds(), getLifetimeInSeconds(),
					getNumAssignments(), requesterAnnotation);

			return hit;
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
		return null;
	}

    public static Entry<HIT,String> createFindImageQuestionFromBI(String title, BufferedImage img, boolean sandbox) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Long uuid = Math.abs(UUID.randomUUID().getMostSignificantBits());
		String filename = uuid.toString() +".png";
		try {
			ImageIO.write(img, "png", baos);
			byte[] arrin = baos.toByteArray();
			String b64 = utils.Base64.encode(arrin);

			boolean same = true;
			byte[] arrout = utils.Base64.decode(b64);
			assert(arrout.length == arrin.length);
			for(int i =0;i < arrin.length;i++) {
				if(arrout[i] != arrin[i])
					same = false;
			}
			assert(same);
			String str_o = "data:image/png;base64," + b64;
			System.out.println(b64);
			String urlParameters  = "im="+URLEncoder.encode(str_o, "UTF-8")+"&filename="+filename;
			byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
			int    postDataLength = postData.length;
			String request        = "http://nimbus.media.mit.edu/servlets/crowdlink-1.0-SNAPSHOT-standalone/capture";
			URL    url            = new URL( request );
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();
			conn.setDoOutput( true );
			conn.setInstanceFollowRedirects( false );
			conn.setRequestMethod( "POST" );
			conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty( "charset", "utf-8");
			conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
			conn.setUseCaches( false );
			DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
			wr.write( postData );
			// this waits for a response
			conn.getResponseMessage();
			conn.disconnect();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

		FindImageQuestion fiq = new FindImageQuestion(title, filename);
		return new SimpleImmutableEntry<HIT,String>(createExternalHit(title, fiq, sandbox), filename);

	}
	public static HIT createExternalHit(String title, HITjob hitj, boolean sandbox) {
		try {
			HITQuestion question = new HITQuestion();

			question.setQuestion(hitj.render().toString());

			//Creating the HIT and loading it into Mechanical Turk
			HIT hit = TurkServer.getServer(sandbox).createHIT(title,
					getDescription(), keywords,
					question,
					getReward(), getAssignmentDurationInSeconds(),
					getAutoApprovalDelayInSeconds(), getLifetimeInSeconds(),
					getNumAssignments(), requesterAnnotation);

			return hit;
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
		return null;
	}


	public static String getDescription() {
		return description;
	}

	public static void setDescription(String description) {
		HITCreator.description = description;
	}

	public static int getNumAssignments() {
		return numAssignments;
	}

	public static void setNumAssignments(int numAssignments) {
		HITCreator.numAssignments = numAssignments;
	}

	public static double getReward() {
		return reward;
	}

	public static void setReward(double reward) {
		HITCreator.reward = reward;
	}

	public static long getAssignmentDurationInSeconds() {
		return assignmentDurationInSeconds;
	}

	public static void setAssignmentDurationInSeconds(
			long assignmentDurationInSeconds) {
		HITCreator.assignmentDurationInSeconds = assignmentDurationInSeconds;
	}

	public static long getAutoApprovalDelayInSeconds() {
		return autoApprovalDelayInSeconds;
	}

	public static void setAutoApprovalDelayInSeconds(
			long autoApprovalDelayInSeconds) {
		HITCreator.autoApprovalDelayInSeconds = autoApprovalDelayInSeconds;
	}

	public static long getLifetimeInSeconds() {
		return lifetimeInSeconds;
	}

	public static void setLifetimeInSeconds(long lifetimeInSeconds) {
		HITCreator.lifetimeInSeconds = lifetimeInSeconds;
	}
}
