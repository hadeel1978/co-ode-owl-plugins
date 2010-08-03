package org.coode.oppl.variablemansyntax.generated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.coode.oppl.entity.OWLEntityRenderer;
import org.coode.oppl.variablemansyntax.bindingtree.BindingNode;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLEntityVisitorEx;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;

public class RegExpGeneratedValue implements
		SingleValueGeneratedValue<Collection<OWLEntity>>,
		OWLEntityVisitorEx<List<String>> {
	private final OWLEntityRenderer renderer;
	private final SingleValueGeneratedValue<String> expression;
	private final Collection<OWLEntity> candidates = new ArrayList<OWLEntity>();
	private final Map<OWLEntity, List<String>> matches = new HashMap<OWLEntity, List<String>>();
	private boolean matched = false;

	public RegExpGeneratedValue(Collection<? extends OWLObject> candidates,
			SingleValueGeneratedValue<String> exp, OWLEntityRenderer r) {
		for (OWLObject o : candidates) {
			if (o instanceof OWLEntity) {
				this.candidates.add((OWLEntity) o);
			}
		}
		this.expression = exp;
		this.renderer = r;
	}

	public List<Collection<OWLEntity>> computePossibleValues() {
		Collection<OWLEntity> unmodifiableCollection = Collections
				.unmodifiableCollection(this.candidates);
		List<Collection<OWLEntity>> toReturn = Arrays
				.asList(unmodifiableCollection);
		return toReturn;
	}

	public Collection<OWLEntity> getGeneratedValue(BindingNode node) {
		String exp = this.expression.getGeneratedValue(node);
		return this.getMatches(exp);
	}

	private Collection<OWLEntity> getMatches(String exp) {
		this.matched = true;
		if (exp != null) {
			Pattern regExpression = Pattern.compile(exp);
			List<OWLEntity> toReturn = new ArrayList<OWLEntity>();
			for (OWLEntity e : this.candidates) {
				String toMatch = this.renderer.render(e);
				List<String> group = actualMatch(regExpression, toMatch);
				if (group.size() > 0) {
					this.matches.put(e, group);
					toReturn.add(e);
				}
			}
			return toReturn;
		}
		return Collections.emptyList();
	}

	public static List<String> actualMatch(Pattern regExpression, String toMatch) {
		List<String> group = new ArrayList<String>();
		try {
			Matcher m = regExpression.matcher(toMatch);
			if (m.lookingAt()) {
				MatchResult mr = m.toMatchResult();
				for (int i = 0; i <= mr.groupCount(); i++) {
					group.add(mr.group(i));
				}
			}
			//			Matcher m = regExpression.matcher(toMatch);
			//			while (m.find()) {
			//				System.out.println("RegExpGeneratedValue.actualMatch() "
			//						+ m.group());
			//			}
			//			//			m.lookingAt();
			//			MatchResult mr = m.toMatchResult();
			//			for (int i = 0; i < mr.groupCount(); i++) {
			//				group.add(mr.group(i));
			//			}
			//			System.out.println("RegExpGeneratedValue.actualMatch() "
			//					+ mr.groupCount());
			//			if (group.size() == 0) {
			//				// if no groups are found, either there is no group or the expression does not match the input
			//				// if it matches the input, then the whole input is returned
			//				//XXX the behaviour is not perfect, since probably the whole input should be returned as the first group, but the current use relies on numbering the first explicit group as 0, so won't change it for now
			//				m.reset();
			//				if (m.matches()) {
			//					group.add(toMatch);
			//				}
			//			}
		} catch (IllegalStateException e) {
			// XXX needs logging or something
			// System.out.println("RegExpGeneratedValue.actualMatch() Matching \""
			// + regExpression + "\" to \"" + toMatch
			// + "\": no match found");
		}
		return group;
	}

	private List<String> retrieve(OWLEntity e) {
		if (!this.matched) {
			new RuntimeException(
					"Error: matches have not been made yet. Call getGeneratedValue(BindingNode) first. Empty list returned.")
					.printStackTrace();
		}
		if (this.matches.containsKey(e)) {
			return this.matches.get(e);
		}
		return Collections.emptyList();
	}

	public List<String> visit(OWLClass cls) {
		return this.retrieve(cls);
	}

	public List<String> visit(OWLObjectProperty property) {
		return this.retrieve(property);
	}

	public List<String> visit(OWLDataProperty property) {
		return this.retrieve(property);
	}

	public List<String> visit(OWLIndividual individual) {
		return this.retrieve(individual);
	}

	public List<String> visit(OWLDataType dataType) {
		return this.retrieve(dataType);
	}

	@Override
	public String toString() {
		return this.expression.toString();
	}

	public boolean isMatched() {
		return this.matched;
	}

	public void accept(SingleValueGeneratedValueVisitor visitor) {
		visitor.visitRegExpGeneratedValue(this);
	}

	public <O> O accept(SingleValueGeneratedValueVisitorEx<O> visitor) {
		return visitor.visitStringGeneratedValue(this);
	}

	/**
	 * @return the expression
	 */
	public SingleValueGeneratedValue<String> getExpression() {
		return this.expression;
	}
}