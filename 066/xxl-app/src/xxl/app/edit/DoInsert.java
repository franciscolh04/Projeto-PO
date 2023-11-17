package xxl.app.edit;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
// FIXME import classes
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        // FIXME add fields
    }

    @Override
    protected final void execute() throws CommandException {
        // FIXME implement command
        try {
            String _range = Form.requestString(Prompt.address());
            String _content = Form.requestString(Prompt.content());
            _receiver.insertContents(_range, _content);
        } catch (UnrecognizedEntryException e) {
            throw new InvalidCellRangeException(e.getEntrySpecification());
        }
    }

}
