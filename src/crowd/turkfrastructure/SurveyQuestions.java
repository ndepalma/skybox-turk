package crowd.turkfrastructure;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import utils.TextEnum;

public class SurveyQuestions extends HITjob {
	
	public static class RadioQuestion<Options extends TextEnum> implements TurkRenderable {
		String question;
		public int selectHowManyMin;
		public int selectHowManyMax;
		Class<Options> hook = null;
		public RadioQuestion(String question, int selectHowManyMin, int selectHowManyMax, Class<Options> inst) throws Exception { 
			this.question = question;
			this.selectHowManyMax = selectHowManyMax;
			this.selectHowManyMin = selectHowManyMin;
			hook = inst;
			if(!hook.isEnum())
				throw new Exception("You didn't use an enum to make this RadioQuestion");
		}
		
		enum val {
			one;
		}
		private Options[] inStringForm() {
			return hook.getEnumConstants();
		}
		
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
	
	public static class FreeformQuestion implements TurkRenderable {
		String question;
		int  min, max;
		boolean isNumeric;
		public FreeformQuestion(String question, int maxLength) { 
			this.question = question;
			this.isNumeric = false;
			this.max = maxLength;
		}
		
		public FreeformQuestion(String question, boolean isnumeric, int minNum, int maxNum) {
			this.question = question;
			this.isNumeric = true;
			this.max = maxNum;
			this.min = minNum;
		}
		public FreeformQuestion(String question) {
			this(question, -1);
		}
		
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

	List<TurkRenderable> allQuestions = new ArrayList<TurkRenderable>();
	
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
	
	public void addQuestion(TurkRenderable tr) {
		allQuestions.add(tr);
	}
}
