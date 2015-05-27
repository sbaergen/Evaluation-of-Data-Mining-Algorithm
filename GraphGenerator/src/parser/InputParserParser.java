// Generated from InputParser.g4 by ANTLR 4.5
package parser;

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
		WS=1, COMMENT=2, MULTICOMMENT=3, DIST=4, INT=5, DOUBLE=6, ID=7;
	public static final int
		RULE_beginning = 0, RULE_distParams = 1;
	public static final String[] ruleNames = {
		"beginning", "distParams"
	};

	private static final String[] _LITERAL_NAMES = {
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "WS", "COMMENT", "MULTICOMMENT", "DIST", "INT", "DOUBLE", "ID"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
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
		enterRule(_localctx, 0, RULE_beginning);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(4);
			match(INT);
			setState(5);
			match(INT);
			setState(6);
			match(INT);
			setState(7);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==DOUBLE) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(8);
			match(DIST);
			setState(9);
			distParams();
			setState(10);
			match(DIST);
			setState(11);
			distParams();
			setState(12);
			match(DIST);
			setState(13);
			distParams();
			setState(14);
			match(DIST);
			setState(15);
			distParams();
			setState(16);
			match(INT);
			setState(17);
			match(INT);
			setState(18);
			match(INT);
			setState(19);
			match(INT);
			setState(20);
			_la = _input.LA(1);
			if ( !(_la==INT || _la==DOUBLE) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(21);
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
		enterRule(_localctx, 2, RULE_distParams);
		int _la;
		try {
			setState(30);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				_localctx = new UniformContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(23);
				match(INT);
				setState(24);
				match(INT);
				}
				break;
			case 2:
				_localctx = new ExponentialContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(25);
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
				setState(26);
				_la = _input.LA(1);
				if ( !(_la==INT || _la==DOUBLE) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(27);
				match(INT);
				setState(28);
				match(INT);
				}
				break;
			case 4:
				_localctx = new PoissonContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(29);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\t#\4\2\t\2\4\3\t"+
		"\3\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3!\n\3\3\3\2\2\4\2\4\2\3\3\2\7"+
		"\b#\2\6\3\2\2\2\4 \3\2\2\2\6\7\7\7\2\2\7\b\7\7\2\2\b\t\7\7\2\2\t\n\t\2"+
		"\2\2\n\13\7\6\2\2\13\f\5\4\3\2\f\r\7\6\2\2\r\16\5\4\3\2\16\17\7\6\2\2"+
		"\17\20\5\4\3\2\20\21\7\6\2\2\21\22\5\4\3\2\22\23\7\7\2\2\23\24\7\7\2\2"+
		"\24\25\7\7\2\2\25\26\7\7\2\2\26\27\t\2\2\2\27\30\7\7\2\2\30\3\3\2\2\2"+
		"\31\32\7\7\2\2\32!\7\7\2\2\33!\t\2\2\2\34\35\t\2\2\2\35\36\7\7\2\2\36"+
		"!\7\7\2\2\37!\7\7\2\2 \31\3\2\2\2 \33\3\2\2\2 \34\3\2\2\2 \37\3\2\2\2"+
		"!\5\3\2\2\2\3 ";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}