package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
// FIXME import classes
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Copy command.
 */
class DoCopy extends Command<Spreadsheet> {

    DoCopy(Spreadsheet receiver) {
        super(Label.COPY, receiver);
        // FIXME add fields
        addStringField("copy", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        // FIXME implement command
        try {
            _receiver.copy(stringField("copy"));
        } catch (UnrecognizedEntryException e) {
            throw new InvalidCellRangeException(e.getEntrySpecification());
        }
    }

}
