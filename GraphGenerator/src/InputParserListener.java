// Generated from InputParser.g4 by ANTLR 4.5
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link InputParserParser}.
 */
public interface InputParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link InputParserParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(InputParserParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParserParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(InputParserParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputParserParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(InputParserParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParserParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(InputParserParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputParserParser#beginning}.
	 * @param ctx the parse tree
	 */
	void enterBeginning(InputParserParser.BeginningContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParserParser#beginning}.
	 * @param ctx the parse tree
	 */
	void exitBeginning(InputParserParser.BeginningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Uniform}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void enterUniform(InputParserParser.UniformContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Uniform}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void exitUniform(InputParserParser.UniformContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Exponential}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void enterExponential(InputParserParser.ExponentialContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Exponential}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void exitExponential(InputParserParser.ExponentialContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Gaussian}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void enterGaussian(InputParserParser.GaussianContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Gaussian}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void exitGaussian(InputParserParser.GaussianContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Poisson}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void enterPoisson(InputParserParser.PoissonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Poisson}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void exitPoisson(InputParserParser.PoissonContext ctx);
}