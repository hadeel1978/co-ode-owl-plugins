/**
 * 
 */
package uk.ac.manchester.cs.owl.lint.commons;

import org.semanticweb.owl.lint.ActingLint;
import org.semanticweb.owl.lint.Lint;
import org.semanticweb.owl.lint.LintVisitor;
import org.semanticweb.owl.lint.PatternBasedLint;

/**
 * @author Luigi Iannone
 * 
 */
public abstract class LintVisitorAdapter implements LintVisitor {
	/**
	 * @see org.semanticweb.owl.lint.LintVisitor#visitPatternPasedLint(org.semanticweb.owl.lint.PatternBasedLint)
	 */
	public void visitPatternPasedLint(PatternBasedLint<?> lint) {
	}

	/**
	 * @see org.semanticweb.owl.lint.LintVisitor#visitActingLint(org.semanticweb.owl.lint.ActingLint)
	 */
	public void visitActingLint(ActingLint<?> actingLint) {
	}

	/**
	 * @see org.semanticweb.owl.lint.LintVisitor#visitGenericLint(org.semanticweb.owl.lint.Lint)
	 */
	public void visitGenericLint(Lint<?> lint) {
	}
}