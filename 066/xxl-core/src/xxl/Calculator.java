package xxl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;
import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;
import xxl.exceptions.FileHasChangedException;
import java.io.FileNotFoundException;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;

    /** Filename of the current spreadsheet. */
    private String _filename = "";

    /** Calculator's active user who's managing the spreadsheets. */
    private User _activeUser = new User();

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if (!_spreadsheet.hasChanged()) {
            return;
        }
        if (_filename.length() == 0) {
            throw new MissingFileAssociationException();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)))) {
            oos.writeObject(_spreadsheet);
            _spreadsheet.setChange(false);
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _filename = filename;
        save();
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            _filename = filename;
            _spreadsheet = (Spreadsheet) ois.readObject();
            _spreadsheet.setChange(false);
        } catch (IOException | ClassNotFoundException e) {
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file.
     * @throws ImportFileException if there is some error while processing the file.
     */
    public void importFile(String filename) throws ImportFileException {

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String currentLine = reader.readLine();
            int numOfLines = Integer.parseInt(currentLine.substring(currentLine.indexOf('=') + 1));
            currentLine = reader.readLine();
            int numOfColumns = Integer.parseInt(currentLine.substring(currentLine.indexOf('=') + 1));

            createSpreadsheet(numOfLines, numOfColumns);

            if ((currentLine = reader.readLine()) != null) {
                _spreadsheet.setChange(true);
            }

            while (currentLine != null) {
                String[] splitline = currentLine.split("\\|");
                if (splitline.length == 1) {
                    _spreadsheet.insertContents(splitline[0], "");
                } else {
                    _spreadsheet.insertContents(splitline[0], splitline[1]);
                }
                currentLine = reader.readLine();
            }
        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(filename, e);
        }
    }

    /** 
     * @return the current spreadsheet.
     */
    public Spreadsheet getSpreadsheet() {
        return _spreadsheet;
    }

    /**
     * Creates a new spreadsheet with a given number of lines and columns.
     * 
     * @param numOfLines the number of lines.
     * @param numOfColumns the number of columns.
     */
    public void createSpreadsheet(int numOfLines, int numOfColumns) {
        _spreadsheet = new Spreadsheet(numOfLines, numOfColumns);
        _spreadsheet.addUser(_activeUser);
        _activeUser.addSpreadsheet(_spreadsheet);
    }
    
    /**
     * Verifies if the current spreadsheet has changed.
     * 
     * @throws FileHasChangedException if the current spreadsheet has changed.
    */
    public void verifyChanges() throws FileHasChangedException {
        if (getSpreadsheet() != null) {
            if(getSpreadsheet().hasChanged()) {
                throw new FileHasChangedException();
            }
        }
    }
    
}
