package xxl.exceptions;

import java.io.Serial;

/** Thrown when the user tries to create a new spreadsheet with unsaved changes. */
public class FileHasChangedException extends Exception {

	@Serial
	private static final long serialVersionUID = 202308312359L;

}
