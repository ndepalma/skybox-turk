package crowd.turkfrastructure;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import javafx.util.Pair;

import utils.BasicUtils;
import utils.CallbackInterface;

import com.amazonaws.mturk.addon.HITQuestion;
import com.amazonaws.mturk.requester.Assignment;
import com.amazonaws.mturk.requester.AssignmentStatus;
import com.amazonaws.mturk.requester.Comparator;
import com.amazonaws.mturk.requester.HIT;
import com.amazonaws.mturk.requester.HITStatus;
import com.amazonaws.mturk.requester.Locale;
import com.amazonaws.mturk.requester.QualificationRequirement;
import com.amazonaws.mturk.requester.ReviewableHITStatus;
import com.amazonaws.mturk.service.axis.AsyncCallback;
import com.amazonaws.mturk.service.axis.RequesterService;
import com.amazonaws.mturk.util.ClientConfig;

public class TurkServer {

	public RequesterService service;

	//Defining the attributes of the HIT
	private static TurkServer singletonServerSand = null;
	private static TurkServer singletonServerReal = null;

	public static TurkServer getServer(boolean sandbox) {
		if(sandbox) {
			if(singletonServerSand == null)
				singletonServerSand = new TurkServer(true);
			return singletonServerSand;
		}
		if(singletonServerReal == null)
			singletonServerReal = new TurkServer(false);
		return singletonServerReal;
	}
	/**
	 * Constructor
	 *
	 */
	private TurkServer(boolean sandbox) {
		ClientConfig pcc = new ClientConfig();
		if(sandbox)
			pcc.setServiceURL(ClientConfig.SANDBOX_SERVICE_URL);
		else
			pcc.setServiceURL(ClientConfig.PRODUCTION_SERVICE_URL);
		pcc.setAccessKeyId("<ADD ACCESS KEY HERE>");
		pcc.setSecretAccessKey("<Add SECRET KEY HERE>");
		pcc.setRetryAttempts(10);
		pcc.setRetryDelayMillis(1000);
		Set<String> s = new HashSet<String>();
		s.add("Server.ServiceUnavailable");
		pcc.setRetriableErrors(s);
		service = new RequesterService(pcc);
	}
	
	public boolean isSandbox() {
		return service.getWebsiteURL().equals(ClientConfig.SANDBOX_SERVICE_URL);
	}

	/**
	 * Checks to see if there are sufficient funds in your account to run this sample.
	 * @return true if there are sufficient funds.  False if not.
	 */
	public boolean hasEnoughFund() {
		double balance = service.getAccountBalance();
		System.out.println("Got account balance: " + RequesterService.formatCurrency(balance));
		return balance > 0;
	}

	public HIT createHIT(String title, String description, String keywords,
			HITQuestion question, double reward, long assignmentDurationInSeconds,
			long autoApprovalDelayInSeconds, long lifetimeInSeconds, int numAssignments, String tag) {
		// This is an example of creating a qualification.
		// This is a built-in qualification -- user must be based in the US
//		QualificationRequirement qualReq = new QualificationRequirement();
//		qualReq.setQualificationTypeId(RequesterService.LOCALE_QUALIFICATION_TYPE_ID);
//		qualReq.setComparator(Comparator.EqualTo);
//		Locale country = new Locale();
//		country.setCountry("US");
//		qualReq.setLocaleValue(country);

		// The create HIT method takes in an array of QualificationRequirements
		// since a HIT can have multiple qualifications.
//		QualificationRequirement[] qualReqs = null;
//		qualReqs = new QualificationRequirement[] { qualReq };


		return service.createHIT(null, // HITTypeId
				title,
				description, keywords,
				question.getQuestion(),
				reward, assignmentDurationInSeconds,
				autoApprovalDelayInSeconds, lifetimeInSeconds,
				numAssignments, tag,
				null,
				null // responseGroup
				);
	}

	public String getWebsiteURL() {
		return service.getWebsiteURL();
	}

	public HIT getHITFromID(String ID) {
		return service.getHIT(ID);
	}

	public void getResults(HIT hit, boolean waitForAll, CallbackInterface<String[]> callback) {
		if(waitForAll) {
			do {
				try {
					System.out.println("Sleeping");
					Thread.sleep(1000);
					hit = getHITFromID(hit.getHITId());
				} catch(Exception e) {
					e.printStackTrace();
				}
				System.out.println("status: " + hit.getHITStatus());
			} while(hit.getHITStatus() == HITStatus.Assignable || hit.getHITStatus() == HITStatus.Unassignable);
		}

		Assignment[] assignments = service.getAllAssignmentsForHIT(hit.getHITId());
		ArrayList<String> allResults = new ArrayList<String>();

		for(Assignment assignment : assignments) {
			boolean doneWithIt = false;
			do {
				System.out.println("status: " + assignment.getAssignmentStatus());
				if(assignment.getAssignmentStatus() == AssignmentStatus.Approved || assignment.getAssignmentStatus() == AssignmentStatus.Submitted) {
					doneWithIt = true;
					allResults.add(assignment.getAnswer());
				} else if(waitForAll) {
					try {
						System.out.println("Sleeping");
						Thread.sleep(1000);
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					doneWithIt = true;
				}
			} while(!doneWithIt);
		}
		// bin up the results and send to callback
		if(callback != null && allResults.size() > 0) {

			String[] allres = new String[allResults.size()];
			allres = allResults.toArray(allres);
			callback.callback(allres);
		}
	}

	public void getApprovableAssignments(HIT hit, boolean waitForAll, CallbackInterface<Assignment[]> callback) {
		if(waitForAll) {
			do {
				try {
					System.out.println("Sleeping");
					Thread.sleep(1000);
					hit = getHITFromID(hit.getHITId());
				} catch(Exception e) {
					e.printStackTrace();
				}
				System.out.println("status: " + hit.getHITStatus());
			} while(hit.getHITStatus() == HITStatus.Assignable || hit.getHITStatus() == HITStatus.Unassignable);
		}

		Assignment[] assignments = service.getAllAssignmentsForHIT(hit.getHITId());
		ArrayList<Assignment> allResults = new ArrayList<Assignment>();

		for(Assignment assignment : assignments) {
			boolean doneWithIt = false;
			do {
				System.out.println("status: " + assignment.getAssignmentStatus());
				if(assignment.getAssignmentStatus() == AssignmentStatus.Submitted) {
					doneWithIt = true;
					allResults.add(assignment);
				} else if(waitForAll) {
					try {
						System.out.println("Sleeping");
						Thread.sleep(1000);
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					doneWithIt = true;
				}
			} while(!doneWithIt);
		}
		// bin up the results and send to callback
		if(callback != null && allResults.size() > 0) {

			Assignment[] allres = new Assignment[allResults.size()];
			allres = allResults.toArray(allres);
			callback.callback(allres);
		}
	}
	public List<String> getCurrentResults(HIT hit) {
		Assignment[] assignments = service.getAllAssignmentsForHIT(hit.getHITId());
		ArrayList<String> allResults = new ArrayList<String>();

		for(Assignment assignment : assignments) {
			if(assignment.getAssignmentStatus() == AssignmentStatus.Approved) {
				allResults.add(assignment.getAnswer());
			}
		}
		return allResults;
	}

	public void waitAndApprove(HIT h, final boolean sandbox) {
		boolean areAllApproved = false;
		int maxassign = h.getMaxAssignments();
		while(!areAllApproved) {
			BasicUtils.safeSleep(4000);
			System.out.println("next status");
//			int pending = h.getNumberOfAssignmentsCompleted();
//
//			System.out.println("approvable assignments: " + pending + " ... waiting for " + maxassign);
//			if(pending == maxassign)
//				areAllApproved = true;


			Assignment[] assignments = service.getAllAssignmentsForHIT(h.getHITId());
			if(assignments.length > 0)
				areAllApproved = true;
			for(Assignment a : assignments) {
				System.out.println("Status: " + a.getAssignmentStatus());
				AssignmentStatus as = a.getAssignmentStatus();
				if(as==AssignmentStatus.Submitted) {
					System.out.println("Approving...");
					service.approveAssignment(a.getAssignmentId(), "");
				} else if(as != AssignmentStatus.Approved) {
					areAllApproved = false;

				}
			}
		}

		TurkServer.getServer(sandbox).getApprovableAssignments(h, false, new CallbackInterface<Assignment[]>() {
			@Override
			public void callback(Assignment[] val) {
				System.out.println("Approvable assignment - approving!");
				for(Assignment a : val) {
					TurkServer.getServer(sandbox).approve(a);
				}
			}
		});
	}


	public boolean allAssignmentsSubmitted(HIT h, final boolean sandbox) {
            boolean areAllSubmitted = true;
            Assignment[] assignments = service.getAllAssignmentsForHIT(h.getHITId());
            for(Assignment a : assignments) {
                AssignmentStatus as = a.getAssignmentStatus();
                if(as!=AssignmentStatus.Submitted)
                    areAllSubmitted = false;
            }
            return areAllSubmitted;
        }




	public void approveAndRollover(HIT h, int times, String filename, String title, final boolean sandbox) {
		List<String> anss;
		for(int i = 0;i < times;i++) {
			waitAndApprove(h, sandbox);
			if(i+1 != times) {
				// rollover
				anss = TurkServer.getServer(sandbox).getCurrentResults(h);
				for(int j = 0;j < anss.size();j++) {
					String an = anss.get(j);
					System.out.println(" ANSWER " + j + ": " + an);
				}
				String firstStr = anss.get(0);

				int[] ans = FindImageQuestion.parseAnswer(firstStr);
				FindImageQuestion fiq = new FindImageQuestion(title + " Please reselect a different box if it is incorrect. If it is correct, then just click submit.", filename, ans);
				h = HITCreator.createExternalHit(title, fiq, sandbox);
				System.out.println("Created new rollover HIT: " + h.getHITId());
				System.out.println("ROLLING OVER");
				h = TurkServer.getServer(false).getHITFromID(h.getHITId());
			}
		}
	}

	public void approve(Assignment a) {
		service.approveAssignment(a.getAssignmentId(), "");
	}

	public HIT[] getAllHits() {
		HIT[] allHITS = service.searchAllHITs();
		return allHITS;
	}

	public void printHITSandStatus() {
		int numhits = service.getTotalNumHITsInAccount();
		System.out.println("--------------------------");
		System.out.println("     |  " + numhits + "  |");
		System.out.println("--------------------------");
		HIT[] allHITS = service.searchAllHITs();
		for(HIT hit : allHITS) {
			System.out.println("   - " + hit.getTitle() + ": " + hit.getHITReviewStatus() + ", " + hit.getHITStatus());
			for(Assignment ass : service.getAssignmentsForHIT(hit.getHITId(), 1, true)) {
				System.out.println("      * " + ass.getAnswer());
			}
		}
	}
	public boolean allApproved(HIT h) {
		Assignment[] allass = service.getAllAssignmentsForHIT(h.getHITId());
		if(allass == null || allass.length == 0)
			return false;
		for(Assignment a:allass) {
			if(a.getAssignmentStatus() != AssignmentStatus.Approved)
				return false;
		}
		return true;
	}
	}
