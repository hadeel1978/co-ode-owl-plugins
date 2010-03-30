/* Generated By:JJTree: Do not edit this line. MAEpropertyChainExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=MAE,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.manchester.mae.parser;

import java.util.List;

public class MAEpropertyChainExpression extends SimpleNode {
	private String content = "";
	private List<MAEpropertyChainCell> cells;

	// protected int id;
	public MAEpropertyChainExpression(int id) {
		super(id);
		// this.id = id;
	}

	// public MAEpropertyChainExpression(ArithmeticsParser p, int id) {
	// super(p, id);
	// }
	/** Accept the visitor. **/
	@Override
	public Object jjtAccept(ArithmeticsParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	@Override
	public String toString() {
		if (this.content.length() == 0 && this.cells.size() > 0) {
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < this.cells.size() - 1; i++) {
				b.append(this.cells.get(i).toString());
				b.append(" o ");
			}
			b.append(this.cells.get(this.cells.size() - 1).toString());
			this.content = b.toString();
		}
		return this.content;
	}

	@Override
	public boolean equals(Object obj) {
		// equals and hashcode are the same as for superclass
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// equals and hashcode are the same as for superclass
		return super.hashCode();
	}

	public void setCells(List<MAEpropertyChainCell> children) {
		this.cells = children;
	}

	public List<MAEpropertyChainCell> getCells() {
		return this.cells;
	}
}
/*
 * JavaCC - OriginalChecksum=b87fc0e48f60e4ecbafb84a65a320cdc (do not edit this
 * line)
 */