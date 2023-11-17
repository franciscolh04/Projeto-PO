package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Calculator;
import xxl.app.main.FileOpenFailedException;
import xxl.exceptions.FileHasChangedException;
import xxl.exceptions.UnavailableFileException;

/**
 * Open existing file.
 */
class DoOpen extends Command<Calculator> {

    DoOpen(Calculator receiver) {
        super(Label.OPEN, receiver);
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

        try {
            String _filename = Form.requestString(Prompt.openFile());
            _receiver.load(_filename);
        } catch (UnavailableFileException e) {
            throw new FileOpenFailedException(e);
        }
    }

}
