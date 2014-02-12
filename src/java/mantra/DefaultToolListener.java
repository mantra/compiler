package mantra;

import org.stringtemplate.v4.ST;

public class DefaultToolListener implements MantraToolListener {
	public Tool tool;

	public DefaultToolListener(Tool tool) { this.tool = tool; }

	@Override
	public void info(String msg) {
		if (tool.errMgr.formatWantsSingleLineMessage()) {
			msg = msg.replace('\n', ' ');
		}
		System.out.println(msg);
	}

	@Override
	public void error(MantraMessage msg) {
		ST msgST = tool.errMgr.getMessageTemplate(msg);
		String outputMsg = msgST.render();
		if (tool.errMgr.formatWantsSingleLineMessage()) {
			outputMsg = outputMsg.replace('\n', ' ');
		}
		System.err.println(outputMsg);
	}

	@Override
	public void warning(MantraMessage msg) {
		ST msgST = tool.errMgr.getMessageTemplate(msg);
		String outputMsg = msgST.render();
		if (tool.errMgr.formatWantsSingleLineMessage()) {
			outputMsg = outputMsg.replace('\n', ' ');
		}
		System.err.println(outputMsg);
	}
}