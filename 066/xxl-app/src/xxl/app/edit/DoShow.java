package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class for searching functions.
 */
class DoShow extends Command<Spreadsheet> {

    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
        addStringField("range", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _display.popup(_receiver.show(stringField("range")));
        } catch (UnrecognizedEntryException e) {
            throw new InvalidCellRangeException(e.getEntrySpecification());
        }
    }

}
