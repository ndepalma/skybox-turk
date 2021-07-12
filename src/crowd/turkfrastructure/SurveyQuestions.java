package crowd.turkfrastructure;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import utils.TextEnum;

/*
 * If you'd like to ask multiple questions rather than an image HIT then this is the class for you.
 *
 * @author ndepalma@media.mit.edu
 */
public class SurveyQuestions extends HITjob {
    /*
     * This class will render out radio buttons (only select one). You'll have to specify the
     * question and possible answers.
     */
    public static class RadioQuestion<Options extends TextEnum> implements TurkRenderable {
        // Question to ask
        String question;
        public int selectHowManyMin;
        public int selectHowManyMax;
        Class<Options> hook = null;

        /**
         * Constructor
         *
         * @param question The question to ask
         * @param selectHowManyMin You provide some options to select from, this the min number they must select
         * @param selectHowManyMax You provide some options to select from, this the max number they can select.
         * @param inst The options to select from.
         */
        public RadioQuestion(String question, int selectHowManyMin, int selectHowManyMax, Class<Options> inst) throws Exception { 
            this.question = question;
            this.selectHowManyMax = selectHowManyMax;
            this.selectHowManyMin = selectHowManyMin;
            hook = inst;
            if(!hook.isEnum())
                throw new Exception("You didn't use an enum to make this RadioQuestion");
        }

        // an example
        enum val {
            one;
        }

        // Get them in string form to be rendered.
        private Options[] inStringForm() {
            return hook.getEnumConstants();
        }
		
        /**
         * This renders the radio buttons for mechanical turk.
         *
         * @return A string to be send to mechanical turk.
         */
        @Override
        public String renderTurkQuestion() {
            TextEnum[] vals = inStringForm();
            String out = "    <QuestionContent>\n"+
                "       <Text>"+question+"</Text>\n" +
                "    </QuestionContent>\n" +
                "	  <AnswerSpecification>\n" +
                "	  	<SelectionAnswer>\n" +
                "   		<MinSelectionCount>" +selectHowManyMin+ "</MinSelectionCount>\n" + 
                "			<MaxSelectionCount>" +selectHowManyMax+ "</MaxSelectionCount>\n" +
                "			<StyleSuggestion>"+(selectHowManyMax == 1 ? "radiobutton" : "multichooser")+"</StyleSuggestion>\n" +
                "			<Selections>\n";
            for(TextEnum val : vals) {
                out +=   "				<Selection>\n" +
                    "					<SelectionIdentifier>" + val + "</SelectionIdentifier>\n" +
                    "					<Text>" + val.getValue() + "</Text>\n" +
                    "				</Selection>\n";
            }
            
            return out +"			</Selections>\n" +
                "		</SelectionAnswer>\n" +
                "	  </AnswerSpecification>\n";
        }
    }

    /*
     * This is much simpler. This asks a single question and returns a free form answer from the worker.
     */
    public static class FreeformQuestion implements TurkRenderable {
        // The question to ask
        String question;
        int  min, max;
        boolean isNumeric;

        /**
         * Constructor
         *
         * @param question The question to ask
         * @param maxLength The max length in characters that you allow the user to specify.
         */
        public FreeformQuestion(String question, int maxLength) { 
            this.question = question;
            this.isNumeric = false;
            this.max = maxLength;
        }
		
        /**
         * Constructor
         *
         * @param question The question to ask
         * @param isNumeric Specifies whether to try to parse it into an integer
         * @param minNum The smallest number they can specify
         * @param maxNum The largest number they can specify
         */
        public FreeformQuestion(String question, boolean isnumeric, int minNum, int maxNum) {
            this.question = question;
            this.isNumeric = true;
            this.max = maxNum;
            this.min = minNum;
        }

        /**
         * Default constructor - unlimited answer
         *
         * @param question The question to ask
         */
        public FreeformQuestion(String question) {
            this(question, -1);
        }

        // Render the question to turk
        @Override
        public String renderTurkQuestion() {
            return "    <QuestionContent>\n"+
                "       <Text>"+question+"</Text>\n" +
                "    </QuestionContent>\n" +
                "	  <AnswerSpecification>\n" +
                "	  	<FreeTextAnswer>\n" +
                "   		<Constraints>\n" +  
                (isNumeric ? "			 <IsNumeric " + (min != -1 ? "minValue=\""+min+"\" ": "") + (max != -1 ? "maxValue=\""+max+"\" ": "")+" />\n" :
                 (max != -1 ? "			 <Length " + (min != -1 ? "minLength=\""+min+"\" ": "") + "maxLength=\""+max+"\" />\n" : "")) +
                "			</Constraints>\n" +
                "		</FreeTextAnswer>\n" +
                "	  </AnswerSpecification>\n";
        }
        
    }

    // All of the questions in sequential order
    List<TurkRenderable> allQuestions = new ArrayList<TurkRenderable>();

    /**
     * Render the whole list out
     *
     * @return Render the whole list to mechanical turk and then use other functions to encapsulate it.
     */
    public StringBuffer render() {
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                           "<QuestionForm xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2005-10-01/QuestionForm.xsd\">\n");
        int i = 0;
        for(TurkRenderable tr : allQuestions) {
            sb.append("	<Question>\n" +
                      " <QuestionIdentifier>"+ (i++) + "</QuestionIdentifier>\n");
            sb.append(tr.renderTurkQuestion());
            sb.append("	</Question>\n");
        }
        sb.append("</QuestionForm>\n");
        return sb;
    }

    /**
     * Adds a question to the list to be rendered. See above for possible options. 
     *
     * @param tr An item to add to the list. See the options above (radio or freeform)
     */
    public void addQuestion(TurkRenderable tr) {
        allQuestions.add(tr);
    }
}
