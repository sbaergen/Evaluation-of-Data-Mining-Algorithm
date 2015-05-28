// Generated from /Users/seanbaergen/Documents/Summer Research/Evaluation-of-Data-Mining-Algorithm/GraphGenerator/src/parser/InputParser.g4 by ANTLR 4.5
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
	void enterParse(@NotNull InputParserParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParserParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(@NotNull InputParserParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputParserParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(@NotNull InputParserParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParserParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(@NotNull InputParserParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link InputParserParser#beginning}.
	 * @param ctx the parse tree
	 */
	void enterBeginning(@NotNull InputParserParser.BeginningContext ctx);
	/**
	 * Exit a parse tree produced by {@link InputParserParser#beginning}.
	 * @param ctx the parse tree
	 */
	void exitBeginning(@NotNull InputParserParser.BeginningContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Uniform}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void enterUniform(@NotNull InputParserParser.UniformContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Uniform}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void exitUniform(@NotNull InputParserParser.UniformContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Exponential}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void enterExponential(@NotNull InputParserParser.ExponentialContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Exponential}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void exitExponential(@NotNull InputParserParser.ExponentialContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Gaussian}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void enterGaussian(@NotNull InputParserParser.GaussianContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Gaussian}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void exitGaussian(@NotNull InputParserParser.GaussianContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Poisson}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void enterPoisson(@NotNull InputParserParser.PoissonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Poisson}
	 * labeled alternative in {@link InputParserParser#distParams}.
	 * @param ctx the parse tree
	 */
	void exitPoisson(@NotNull InputParserParser.PoissonContext ctx);
}