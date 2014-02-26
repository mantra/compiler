package mantra.errors;

/**
 * A complex enumeration of all the error messages that the tool can issue.
 */
public enum ErrorType {
	// T O O L  E R R O R S

	INTERNAL_ERROR(1, "internal error: <arg> <arg2><if(exception&&verbose)>: <exception>" +
				   "<stackTrace; separator=\"\\n\"><endif>", ErrorSeverity.ERROR),

	INVALID_CMDLINE_ARG(10,				"unknown command-line option '<arg>'", ErrorSeverity.ERROR),
	CANNOT_WRITE_FILE(11,				"cannot write file '<arg>': <arg2>", ErrorSeverity.ERROR),
	WARNING_TREATED_AS_ERROR(12,		"warning treated as error", ErrorSeverity.ERROR_ONE_OFF),
	NO_MODEL_TO_TEMPLATE_MAPPING(13,	"no mapping to template name for output model class '<arg>'", ErrorSeverity.ERROR),
	CODE_GEN_TEMPLATES_INCOMPLETE(14,	"missing code generation template '<arg>'", ErrorSeverity.ERROR),
	CODE_TEMPLATE_ARG_ISSUE(15, 		"code generation template '<arg>' has missing, misnamed, or incomplete arg list; missing '<arg2>'", ErrorSeverity.ERROR),
	STRING_TEMPLATE_WARNING(22, 		"template error: <arg> <arg2><if(exception&&verbose)>: <exception>" +
				   						"<stackTrace; separator=\"\\n\"><endif>", ErrorSeverity.WARNING),

	// S Y N T A X  E R R O R S

	// S E M A N T I C  E R R O R S

	;

	/**
	 * The error or warning message, in StringTemplate 4 format using {@code <}
	 * and {@code >} as the delimiters. Arguments for the message may be
	 * referenced using the following names:
	 *
	 * <ul>
	 * <li>{@code arg}: The first template argument</li>
	 * <li>{@code arg2}: The second template argument</li>
	 * <li>{@code arg3}: The third template argument</li>
	 * <li>{@code verbose}: {@code true} if verbose messages were requested; otherwise, {@code false}</li>
	 * <li>{@code exception}: The exception which resulted in the error, if any.</li>
	 * <li>{@code stackTrace}: The stack trace for the exception, when available.</li>
	 * </ul>
	 */
	public final String msg;
	/**
	 * The error or warning number.
	 *
	 * <p>The code should be unique, and following its
	 * use in a release should not be altered or reassigned.</p>
	 */
    public final int code;
	/**
	 * The error severity.
	 */
    public final ErrorSeverity severity;

	/**
	 * Constructs a new {@link ErrorType} with the specified code, message, and
	 * severity.
	 *
	 * @param code The unique error number.
	 * @param msg The error message template.
	 * @param severity The error severity.
	 */
	ErrorType(int code, String msg, ErrorSeverity severity) {
        this.code = code;
		this.msg = msg;
        this.severity = severity;
	}
}
