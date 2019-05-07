package util;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SinnakeValidate {
	private String chkVal;
	private Set<Predicate<String>> validateList = new HashSet<>();
	
	public SinnakeValidate(String chkVal) {
		this.chkVal = Optional.ofNullable(chkVal).orElse("");
	}
	
	public SinnakeValidate required() {
		this.setPrdicate((s) 
			-> s != null 
				&& !"".equals(s)
				&& s.length() > 0
				&& s.replaceAll("\\p{Z}", "").length() > 0);
		
		return this;
	}
	
	public SinnakeValidate checkBoxRequired() {
		this.required();

		this.setPrdicate((s) -> "on".equals(s));
			
		return this;
	}
	
	public SinnakeValidate minLen(int minLen) {
		this.setPrdicate((s) -> s.length() >= minLen);
		
		return this;
	}
	
	public SinnakeValidate maxLen(int maxLen) {
		this.setPrdicate((s) -> s.length() <= maxLen);
		
		return this;
	}
	
	public SinnakeValidate pwdValid() {
		this.setPrdicate((s) -> 
			this.patternFind(s, "[\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\!]")
			&& this.patternFind(s, "[a-z]")
			&& this.patternFind(s, "[0-9]") );

		return this;
	}
	
	public SinnakeValidate idValid() {
		this.customFind("^[A-Za-z]{1}[A-Za-z0-9]{4,15}$");
		
		return this;
	}
	
	public SinnakeValidate blankValid() {
		this.setPrdicate((s) -> !this.patternFind(s, "[\\s]"));
		
		return this;
	}

	public SinnakeValidate equalTo(String chkVal) {
		this.setPrdicate((s) -> s.equals(chkVal));
		
		return this;
	}
	
	public SinnakeValidate number() {
		this.custom("[0-9]+");

		return this;
	}

	public SinnakeValidate custom(String regex) {
		this.setPrdicate((s) -> this.pattern(s, regex));
		return this;
	}
	
	public SinnakeValidate customFind(String regex) {
		this.setPrdicate((s) -> this.patternFind(s, regex));
		
		return this;
	}
	
	public Boolean getValidResult() {
		return this.validateList.stream()
			.map(v -> v.test(this.chkVal))
			.filter(r -> r == false)
			.count() <= 0;
	}

	/***********/
	/* private */
	/***********/
	private boolean pattern(String chkVal, String regex) {
		return chkVal.matches(regex);
	}
	
	private boolean patternFind(String chkVal, String regex) {
		return Pattern.compile(regex).matcher(chkVal).find();
	}

	private void setPrdicate(Predicate<String> predicate) {
		this.validateList.add(predicate);
	}
}
