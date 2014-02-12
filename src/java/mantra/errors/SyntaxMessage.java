package mantra.errors;

import mantra.MantraMessage;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

/** A problem with the syntax of your antlr grammar such as
 *  "The '{' came as a complete surprise to me at this point in your program"
 */
public class SyntaxMessage extends MantraMessage {
	/** Most of the time, we'll have a token and so this will be set. */
	public Token offendingToken;

	public SyntaxMessage(ErrorType etype,
								String fileName,
								Token offendingToken,
								RecognitionException antlrException,
								Object... args)
	{
		super(etype, antlrException, args);
		this.fileName = fileName;
		this.offendingToken = offendingToken;
		if ( offendingToken!=null ) {
			line = offendingToken.getLine();
			charPosition = offendingToken.getCharPositionInLine();
		}
	}

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    @Override
    public RecognitionException getCause() {
        return (RecognitionException)super.getCause();
    }
}
