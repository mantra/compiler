package mantra;


/** Defines behavior of object able to handle error messages from Mantra including
 *  both tool errors like "can't write file" and grammar ambiguity warnings.
 *  To avoid having to change tools that use Mantra (like GUIs), I am
 *  wrapping error data in Message objects and passing them to the listener.
 *  In this way, users of this interface are less sensitive to changes in
 *  the info I need for error messages.
 */
public interface MantraToolListener {
	public void info(String msg);
	public void error(MantraMessage msg);
	public void warning(MantraMessage msg);
}
