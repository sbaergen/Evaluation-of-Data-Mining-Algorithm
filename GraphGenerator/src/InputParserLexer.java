// Generated from InputParser.g4 by ANTLR 4.5
/*
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class InputParserLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NL=1, WS=2, COMMENT=3, MULTICOMMENT=4, DIST=5, INT=6, DOUBLE=7, ID=8;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"CHAR", "NUM", "US", "DQ", "NL", "WS", "COMMENT", "MULTICOMMENT", "DIST", 
		"INT", "DOUBLE", "ID"
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


	public InputParserLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "InputParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\n\\\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\5\2\35\n\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7"+
		"\6\7(\n\7\r\7\16\7)\3\7\3\7\3\b\3\b\3\b\5\b\61\n\b\3\b\5\b\64\n\b\3\b"+
		"\3\b\3\b\3\b\3\t\3\t\3\t\3\t\5\t>\n\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\13"+
		"\6\13H\n\13\r\13\16\13I\3\f\7\fM\n\f\f\f\16\fP\13\f\3\f\3\f\6\fT\n\f\r"+
		"\f\16\fU\3\r\6\rY\n\r\r\r\16\rZ\2\2\16\3\2\5\2\7\2\t\2\13\3\r\4\17\5\21"+
		"\6\23\7\25\b\27\t\31\n\3\2\6\4\2C\\c|\3\2\62;\5\2\13\f\17\17\"\"\n\2G"+
		"GIIRRWWggiirrww_\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2"+
		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\3\34\3\2\2\2\5\36\3"+
		"\2\2\2\7 \3\2\2\2\t\"\3\2\2\2\13$\3\2\2\2\r\'\3\2\2\2\17\60\3\2\2\2\21"+
		"9\3\2\2\2\23D\3\2\2\2\25G\3\2\2\2\27N\3\2\2\2\31X\3\2\2\2\33\35\t\2\2"+
		"\2\34\33\3\2\2\2\35\4\3\2\2\2\36\37\t\3\2\2\37\6\3\2\2\2 !\7a\2\2!\b\3"+
		"\2\2\2\"#\7$\2\2#\n\3\2\2\2$%\7\f\2\2%\f\3\2\2\2&(\t\4\2\2\'&\3\2\2\2"+
		"()\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*+\3\2\2\2+,\b\7\2\2,\16\3\2\2\2-.\7\61"+
		"\2\2.\61\7\61\2\2/\61\7%\2\2\60-\3\2\2\2\60/\3\2\2\2\61\63\3\2\2\2\62"+
		"\64\13\2\2\2\63\62\3\2\2\2\63\64\3\2\2\2\64\65\3\2\2\2\65\66\5\13\6\2"+
		"\66\67\3\2\2\2\678\b\b\2\28\20\3\2\2\29:\7\61\2\2:;\7,\2\2;=\3\2\2\2<"+
		">\13\2\2\2=<\3\2\2\2=>\3\2\2\2>?\3\2\2\2?@\7,\2\2@A\7\61\2\2AB\3\2\2\2"+
		"BC\b\t\2\2C\22\3\2\2\2DE\t\5\2\2E\24\3\2\2\2FH\5\5\3\2GF\3\2\2\2HI\3\2"+
		"\2\2IG\3\2\2\2IJ\3\2\2\2J\26\3\2\2\2KM\5\5\3\2LK\3\2\2\2MP\3\2\2\2NL\3"+
		"\2\2\2NO\3\2\2\2OQ\3\2\2\2PN\3\2\2\2QS\7\60\2\2RT\5\5\3\2SR\3\2\2\2TU"+
		"\3\2\2\2US\3\2\2\2UV\3\2\2\2V\30\3\2\2\2WY\5\3\2\2XW\3\2\2\2YZ\3\2\2\2"+
		"ZX\3\2\2\2Z[\3\2\2\2[\32\3\2\2\2\f\2\34)\60\63=INUZ\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}*/