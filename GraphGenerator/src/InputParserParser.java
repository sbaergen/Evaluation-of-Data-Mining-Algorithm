// Generated from InputParser.g4 by ANTLR 4.5
/*
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class InputParserParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NL=1, WS=2, COMMENT=3, MULTICOMMENT=4, DIST=5, INT=6, DOUBLE=7, ID=8;
	public static final int
		RULE_parse = 0, RULE_file = 1, RULE_beginning = 2, RULE_distParams = 3;
	public static final String[] ruleNames = {
		"parse", "file", "beginning", "distParams"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'\n'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "NL", "WS", "COMMENT", "MULTICOMMENT", "DIST", "INT", "DOUBLE", 
		"ID"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 *
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "InputParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public InputParserParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ParseContext extends ParserRuleContext {
		public FileContext file() {
			return getRuleContext(FileContext.class,0);
		}
		public TerminalNode EOF() { return getToken(InputParserParser.EOF, 0); }
		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).enterParse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).exitParse(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			file();
			setState(9);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FileContext extends ParserRuleContext {
		public List<BeginningContext> beginning() {
			return getRuleContexts(BeginningContext.class);
		}
		public BeginningContext beginning(int i) {
			return getRuleContext(BeginningContext.class,i);
		}
		public List<TerminalNode> NL() { return getTokens(InputParserParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(InputParserParser.NL, i);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).exitFile(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_file);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INT) {
				{
				{
				setState(11);
				beginning();
				setState(12);
				match(NL);
				}
				}
				setState(18);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BeginningContext extends ParserRuleContext {
		public List<TerminalNode> INT() { return getTokens(InputParserParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(InputParserParser.INT, i);
		}
		public List<TerminalNode> DIST() { return getTokens(InputParserParser.DIST); }
		public TerminalNode DIST(int i) {
			return getToken(InputParserParser.DIST, i);
		}
		public List<DistParamsContext> distParams() {
			return getRuleContexts(DistParamsContext.class);
		}
		public DistParamsContext distParams(int i) {
			return getRuleContext(DistParamsContext.class,i);
		}
		public List<TerminalNode> DOUBLE() { return getTokens(InputParserParser.DOUBLE); }
		public TerminalNode DOUBLE(int i) {
			return getToken(InputParserParser.DOUBLE, i);
		}
		public BeginningContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_beginning; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).enterBeginning(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).exitBeginning(this);
		}
	}

	public final BeginningContext beginning() throws RecognitionException {
		BeginningContext _localctx = new BeginningContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_beginning);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19);
			match(INT);
			setState(20);
			match(INT);
			setState(21);
			match(INT);
			setState(22);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==DOUBLE) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(23);
			match(DIST);
			setState(24);
			distParams();
			setState(25);
			match(DIST);
			setState(26);
			distParams();
			setState(27);
			match(DIST);
			setState(28);
			distParams();
			setState(29);
			match(DIST);
			setState(30);
			distParams();
			setState(31);
			match(INT);
			setState(32);
			match(INT);
			setState(33);
			match(INT);
			setState(34);
			match(INT);
			setState(35);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==DOUBLE) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(36);
			match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DistParamsContext extends ParserRuleContext {
		public DistParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_distParams; }
	 
		public DistParamsContext() { }
		public void copyFrom(DistParamsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PoissonContext extends DistParamsContext {
		public TerminalNode INT() { return getToken(InputParserParser.INT, 0); }
		public PoissonContext(DistParamsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).enterPoisson(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).exitPoisson(this);
		}
	}
	public static class UniformContext extends DistParamsContext {
		public List<TerminalNode> INT() { return getTokens(InputParserParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(InputParserParser.INT, i);
		}
		public UniformContext(DistParamsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).enterUniform(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).exitUniform(this);
		}
	}
	public static class ExponentialContext extends DistParamsContext {
		public TerminalNode DOUBLE() { return getToken(InputParserParser.DOUBLE, 0); }
		public TerminalNode INT() { return getToken(InputParserParser.INT, 0); }
		public ExponentialContext(DistParamsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).enterExponential(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).exitExponential(this);
		}
	}
	public static class GaussianContext extends DistParamsContext {
		public List<TerminalNode> INT() { return getTokens(InputParserParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(InputParserParser.INT, i);
		}
		public TerminalNode DOUBLE() { return getToken(InputParserParser.DOUBLE, 0); }
		public GaussianContext(DistParamsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).enterGaussian(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof InputParserListener ) ((InputParserListener)listener).exitGaussian(this);
		}
	}

	public final DistParamsContext distParams() throws RecognitionException {
		DistParamsContext _localctx = new DistParamsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_distParams);
		int _la;
		try {
			setState(45);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new UniformContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(38);
				match(INT);
				setState(39);
				match(INT);
				}
				break;
			case 2:
				_localctx = new ExponentialContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(40);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==DOUBLE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				}
				break;
			case 3:
				_localctx = new GaussianContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(41);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==DOUBLE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(42);
				match(INT);
				setState(43);
				match(INT);
				}
				break;
			case 4:
				_localctx = new PoissonContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(44);
				match(INT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\n\62\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\3\3\3\3\3\7\3\21\n\3\f\3\16\3\24\13"+
		"\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\60\n\5\3\5\2\2\6\2\4\6\b\2\3"+
		"\3\2\b\t\61\2\n\3\2\2\2\4\22\3\2\2\2\6\25\3\2\2\2\b/\3\2\2\2\n\13\5\4"+
		"\3\2\13\f\7\2\2\3\f\3\3\2\2\2\r\16\5\6\4\2\16\17\7\3\2\2\17\21\3\2\2\2"+
		"\20\r\3\2\2\2\21\24\3\2\2\2\22\20\3\2\2\2\22\23\3\2\2\2\23\5\3\2\2\2\24"+
		"\22\3\2\2\2\25\26\7\b\2\2\26\27\7\b\2\2\27\30\7\b\2\2\30\31\t\2\2\2\31"+
		"\32\7\7\2\2\32\33\5\b\5\2\33\34\7\7\2\2\34\35\5\b\5\2\35\36\7\7\2\2\36"+
		"\37\5\b\5\2\37 \7\7\2\2 !\5\b\5\2!\"\7\b\2\2\"#\7\b\2\2#$\7\b\2\2$%\7"+
		"\b\2\2%&\t\2\2\2&\'\7\b\2\2\'\7\3\2\2\2()\7\b\2\2)\60\7\b\2\2*\60\t\2"+
		"\2\2+,\t\2\2\2,-\7\b\2\2-\60\7\b\2\2.\60\7\b\2\2/(\3\2\2\2/*\3\2\2\2/"+
		"+\3\2\2\2/.\3\2\2\2\60\t\3\2\2\2\4\22/";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}*/