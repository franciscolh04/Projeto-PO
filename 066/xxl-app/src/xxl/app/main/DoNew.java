package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;
import xxl.exceptions.FileHasChangedException;

/**
 * Open a new file.
 */
class DoNew extends Command<Calculator> {

    DoNew(Calculator receiver) {
        super(Label.NEW, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.verifyChanges();
        } catch (FileHasChangedException e) {
            if(Form.confirm(Prompt.saveBeforeExit())) {
                DoSave save = new DoSave(_receiver);
                save.execute();
            }
        }
        int _lines = Form.requestInteger(Prompt.lines());
        int _columns = Form.requestInteger(Prompt.columns());
        _receiver.createSpreadsheet(_lines, _columns);
    }

}
