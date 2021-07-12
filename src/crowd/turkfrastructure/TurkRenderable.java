package crowd.turkfrastructure;

/*
 * This interface enforces simple questions to be renderable. It's similar to HITjob in that you must
 * write mechanical turk XML schema but is less demanding about needing to render the /whole/ XML out
 * headers and all.
 *
 * @author ndepalma@media.mit.edu
 */
public interface TurkRenderable {
	public String renderTurkQuestion();
}
