package xxl;

import xxl.structures.Cell;
import xxl.structures.Content;
import xxl.content.Function;
import xxl.content.IntLit;
import xxl.content.Reference;
import xxl.content.StringLit;
import xxl.functions.BinFunc;
import xxl.functions.IntFunc;
import xxl.functions.BinFuncs.*;
import xxl.functions.IntFuncs.*;
import xxl.structures.ContiguousMemory;
import xxl.visitors.Evaluator;
import xxl.visitors.Detach;
import xxl.visitors.SearchFunction;
import xxl.visitors.SearchValue;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.Serial;
import java.io.Serializable;

import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    /** Number of lines of the spreadsheet */
    private int _numOfLines = 1;

    /** Number of columns of the spreadsheet */
    private int _numOfColumns = 1;

    /** Storage of the spreadsheet */
    private ContiguousMemory _storage = null;

    /** List of users of the spreadsheet */
    private List<User> _users = new ArrayList<>();

    /** Boolean that indicates if the spreadsheet has changed */
    private Boolean _change = false;

    /** Cut buffer of the spreadsheet */
    private Spreadsheet _cutBuffer = null;

    public Spreadsheet(int numOfLines, int numOfColumns) {
        _numOfLines = numOfLines;
        _numOfColumns = numOfColumns;
        _storage = new ContiguousMemory(numOfLines, numOfColumns);
    }

    /**
     * Adds a user to the spreadsheet.
     * 
     * @param user
     */
    public void addUser(User user) {
        _users.add(user);
    }

    /**
     * Verifies if the range is valid.
     * 
     * @param range the range to be verified.
     * @return true if the range is valid, false otherwise.
     */
    public Boolean isRangeValid(String range) {
        if (range.contains(":")) {
            // The range is an interval
            String[] splitRange = range.split(":"); 
            int l1 = Integer.parseInt(splitRange[0].substring(0, splitRange[0].indexOf(';')));
            int c1 = Integer.parseInt(splitRange[0].substring(splitRange[0].indexOf(';') + 1));
            int l2 = Integer.parseInt(splitRange[1].substring(0, splitRange[1].indexOf(';')));
            int c2 = Integer.parseInt(splitRange[1].substring(splitRange[1].indexOf(';') + 1));

            // Verifies if the range is valid
            if (l1 > _numOfLines || l2 > _numOfLines || c1 > _numOfColumns || c2 > _numOfColumns ||
                l1 == 0 || l2 == 0 || c1 == 0 || c2 == 0 || (l1 != l2 && c1 != c2)) {
                return false;
            }
        }
        else {
            // The range is a single cell
            String[] splitRange = range.split(";");
            int l1 = Integer.parseInt(splitRange[0]);
            int c1 = Integer.parseInt(splitRange[1]);

            // Verifies if the range is valid
            if (l1 > _numOfLines || c1 > _numOfColumns || l1 == 0 || c1 == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the interval between two cell's limits.
     * 
     * @param lim1 one limit.
     * @param lim2 the other limit.
     * @return the interval between the two limits.
     */
    public String[] interval(String lim1, String lim2) {
        int l1 = Integer.parseInt(lim1.substring(0, lim1.indexOf(';')));
        int c1 = Integer.parseInt(lim1.substring(lim1.indexOf(';') + 1));
        int l2 = Integer.parseInt(lim2.substring(0, lim2.indexOf(';')));
        int c2 = Integer.parseInt(lim2.substring(lim2.indexOf(';') + 1));
        String[] _interval = null;

        if(l1 == l2) {
            // Constant line
            int start = Math.min(c1, c2);
            int end = Math.max(c1, c2);
            _interval = new String[end - start + 1];
            for(int i = start; i <= end; i++) {
                _interval[i - start] = l1 + ";" + i;
            }
        }
        else if (c1 == c2) {
            // Constant column
            int start = Math.min(l1, l2);
            int end = Math.max(l1, l2);
            _interval = new String[end - start + 1];
            for(int i = start; i <= end; i++) {
                _interval[i - start] = i + ";" + c1;
            }
        }
        return _interval;
    }

    /**
     * Insert specified content in specified range.
     *
     * @param rangeSpecification
     * @param contentSpecification
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException {
        // Verifies if the range is valid
        if (!isRangeValid(rangeSpecification)) {
            throw new UnrecognizedEntryException(rangeSpecification);
        }

        if (contentSpecification.length() != 0) {
            Content content = recognizeContent(getCell(rangeSpecification), contentSpecification);

            if (rangeSpecification.contains(":")) {
                String ref = "";
                
                // The range is an interval
                String[] splitRange = rangeSpecification.split(":"); 
                int l1 = Integer.parseInt(splitRange[0].substring(0, splitRange[0].indexOf(';')));
                int c1 = Integer.parseInt(splitRange[0].substring(splitRange[0].indexOf(';') + 1));
                int l2 = Integer.parseInt(splitRange[1].substring(0, splitRange[1].indexOf(';')));
                int c2 = Integer.parseInt(splitRange[1].substring(splitRange[1].indexOf(';') + 1));

                // Iterates through the range and inserts the content in each cell
                if(l1 == l2) {
                    int start = Math.min(c1, c2);
                    int end = Math.max(c1, c2);
                    for(int i = start; i <= end; i++) {
                        // Constant line
                        if(getCell("" + l1 + ";" + i).getContent() != null) {
                            ref = getCell("" + l1 + ";" + i).getContent().accept(new Detach());
                            if(!ref.equals("")) {
                                getCell(ref).detachObserver((Reference) getCell("" + l1 + ";" + i).getContent());
                            }
                        }
                        _storage.insertContentCell("" + l1 + ";" + i, content);
                        calculateValue(getCell("" + l1 + ";" + i).getContent());
                        getCell("" + l1 + ";" + i).notifyObservers();
                    }
                }
                else if (c1 == c2) {
                    int start = Math.min(l1, l2);
                    int end = Math.max(l1, l2);
                    for(int i = start; i <= end; i++) {
                        // Constant column
                        if(getCell("" + i + ";" + c1).getContent() != null) {
                            ref = getCell("" + i + ";" + c1).getContent().accept(new Detach());
                            if(!ref.equals("")) {
                                getCell(ref).detachObserver((Reference) getCell("" + i + ";" + c1).getContent());
                            }
                        }
                        _storage.insertContentCell("" + i + ";" + c1, content);
                        calculateValue(getCell("" + i + ";" + c1).getContent());
                        getCell("" + i + ";" + c1).notifyObservers();
                    }
                }
            }
            else {
                // The range is a single cell
                _storage.insertContentCell(rangeSpecification, content);
                calculateValue(getCell(rangeSpecification).getContent());
                getCell(rangeSpecification).notifyObservers();
            }
        }
        _change = true;
    }

    /**
     * Calculates the value of a cell.
     * 
     * @param reference the reference to the cell.
     * @return the value of the cell.
     */
    public void calculateValue(Content cont) {
        if(cont != null) {
            cont.accept(new Evaluator()); 
        }
        
    }
    
    /**
     * Shows the value of a range of cells (interval or single cell).
     * 
     * @param range the range of cells.
     * @throws UnrecognizedEntryException if the range is invalid.
     * @return the value of the cell or the range of cells.
     */
    public String show(String range) throws UnrecognizedEntryException {
        StringBuilder _message = new StringBuilder();

        // Verifies if the range is valid
        if (!isRangeValid(range)) {
            throw new UnrecognizedEntryException(range);
        }
        
        if (range.contains(":")) {
            // The range is an interval
            String[] splitRange = range.split(":"); 
            int l1 = Integer.parseInt(splitRange[0].substring(0, splitRange[0].indexOf(';')));
            int c1 = Integer.parseInt(splitRange[0].substring(splitRange[0].indexOf(';') + 1));
            int l2 = Integer.parseInt(splitRange[1].substring(0, splitRange[1].indexOf(';')));
            int c2 = Integer.parseInt(splitRange[1].substring(splitRange[1].indexOf(';') + 1));

            // Iterates through the range and shows the value of each cell
            if(l1 == l2) {
                int start = Math.min(c1, c2);
                int end = Math.max(c1, c2);
                for(int i = start; i <= end; i++) {
                    // Constant line
                    Cell _cell = getCell("" + l1 + ";" + i);
                    calculateValue(getCell("" + l1 + ";" + i).getContent());
                    if (i != end) {
                        _message.append("" + l1 + ";" + i + "|" + _cell.toString() + "\n");
                    }
                    else {
                        _message.append("" + l1 + ";" + i + "|" + _cell.toString());
                    }
                }
            }
            else if (c1 == c2) {
                int start = Math.min(l1, l2);
                int end = Math.max(l1, l2);
                for(int i = start; i <= end; i++) {
                    // Constant column
                    Cell _cell = getCell("" + i + ";" + c1);
                    calculateValue(getCell("" + i + ";" + c1).getContent());
                    if (i != end) {
                        _message.append("" + i + ";" + c1 + "|" + _cell.toString() + "\n");
                    }
                    else {
                        _message.append("" + i + ";" + c1 + "|" + _cell.toString());
                    }
                }
            }
        }
        else {
            // The range is a single cell
            Cell _cell = getCell(range);
            calculateValue(_cell.getContent());
            _message.append(range + "|" + _cell.toString());

        }
        return _message.toString();
    }
    
    /**
     * Checks if the spreadsheet has changed.
     * 
     * @return 
     */
    public Boolean hasChanged() {
        return _change;
    }

    /**
     * Sets the state of the spreadsheet to changed or unchanged.
     * 
     * @param change
     */
    public void setChange(Boolean change) {
        _change = change;
    }

    /**
     * Recognizes the type of the content.
     * 
     * @param content the content to be recognized.
     * @return the content recognized.
     */
    public Content recognizeContent(Cell cell, String content) {
        Content _content = null;

        if (content.charAt(0) == '=') {
            if (Character.isDigit(content.charAt(1))) {
                // Creates Reference
                _content = new Reference(getCell(content.substring(1)).getContent(),content.substring(1));
                getCell(content.substring(1)).attachObserver((Reference)_content);
            }
            else {
                // Recognizes the type of the function
                _content = recognizeFunctionType(cell, content);
            }
        } else {
            if (Character.isDigit(content.charAt(0)) || content.charAt(0) == '-') {
                // Creates Int Literal
                _content = new IntLit(content);
            }
            else if (content.charAt(0) == '\'') {
                // Creates String Literal
                _content = new StringLit(content);
            }
        }
        
        return _content;
    }

    /**
     * Recognizes the type of the function.
     * 
     * @param function the function to be recognized.
     * @return the function recognized.
     */
    public Function recognizeFunctionType(Cell cell,String function) {
        String _funcName = function.substring(function.indexOf('=') + 1, function.indexOf('('));
        Function _function;

        if (_funcName.length() == 3) {
            String _arg1 = function.substring(function.indexOf('(') + 1, function.indexOf(','));
            String _arg2 = function.substring(function.indexOf(',') + 1, function.indexOf(')'));
            _function = recognizeBinFunc( _funcName, _arg1, _arg2);
        }
        else {
            String _arg1 = function.substring(function.indexOf('(') + 1, function.indexOf(':'));
            String _arg2 = function.substring(function.indexOf(':') + 1, function.indexOf(')'));
            _function = recognizeIntFunc(_funcName, _arg1, _arg2);
            
        }

        return _function;
    }

    /**
     * Recognizes the type of the binary function.
     * 
     * @param funcName the name of the function.
     * @param arg1 the first argument of the function.
     * @param arg2 the second argument of the function.
     * @return the binary function recognized.
     */
    public BinFunc recognizeBinFunc(String funcName, String arg1, String arg2) {
        BinFunc _function = null;
        Content _arg1 = null;
        Content _arg2 = null;
        if(arg1.contains(";")) {
            _arg1 = new Reference(_storage.getCell(arg1).getContent(), arg1);
            getCell(arg1).attachObserver((Reference) _arg1);
            
        }
        else {
            _arg1 = new IntLit(arg1);
        }
        if(arg2.contains(";")) {
            _arg2 = new Reference(_storage.getCell(arg2).getContent(), arg2);
            getCell(arg2).attachObserver((Reference) _arg2);
            
        }
        else {
            _arg2 = new IntLit(arg2);
        }


        if ( funcName.equals("ADD")) {
            _function = new Add(_arg1, _arg2);
        }
        else if (funcName.equals("SUB")) {
            _function = new Sub( _arg1, _arg2);
        }
        else if (funcName.equals("MUL")) {
            _function = new Mul( _arg1, _arg2);
        } 
        else if (funcName.equals("DIV")) {
            _function = new Div( _arg1, _arg2);
        }

        return _function;
    }

    /**
     * Recognizes the type of the interval function.
     * 
     * @param funcName the name of the function.
     * @param arg1 the first argument of the function.
     * @param arg2 the second argument of the function.
     * @return the interval function recognized.
     */
    public IntFunc recognizeIntFunc(String funcName, String arg1, String arg2) {
        IntFunc _function = null;
    
        String[] _interval = interval(arg1, arg2);
        Content[] _contents = new Content[_interval.length];
        int i = 0;
        if(isRangeValid(arg1 + ":" + arg2)) {
            for (String cell: _interval) {
                _contents[i] = new Reference(getCell(cell).getContent(),_interval[i]);
                getCell(cell).attachObserver((Reference) _contents[i]);
                i++;
            }
        }
        else {
            for (String cell: _interval) {
                _contents[i] = new Reference(new IntLit("#VALUE"),_interval[i]);
                i++;
            }
        }

        if ( funcName.equals("AVERAGE")) {
            _function = new Average(_contents);
        }
        else if (funcName.equals("PRODUCT")) {
            _function = new Product(_contents);
        }
        else if (funcName.equals("CONCAT")) {
            _function = new Concat(_contents);
        } 
        else if (funcName.equals("COALESCE")) {
            _function = new Coalesce(_contents);
        }

        return _function;
        
    }

    /**
     * Deletes the contents of a range of cells (interval or single cell).
     * 
     * @param rangeSpecification the range of cells.
     * @throws UnrecognizedEntryException if the range is invalid.
     */
    public void deleteContents(String rangeSpecification) throws UnrecognizedEntryException {
        // Verifies if the range is valid
        if (!isRangeValid(rangeSpecification)) {
            throw new UnrecognizedEntryException(rangeSpecification);
        }
        
        // Deletes content
        if (rangeSpecification.contains(":")) {
            String ref = "";

            // The range is an interval
            String[] splitRange = rangeSpecification.split(":"); 
            int l1 = Integer.parseInt(splitRange[0].substring(0, splitRange[0].indexOf(';')));
            int c1 = Integer.parseInt(splitRange[0].substring(splitRange[0].indexOf(';') + 1));
            int l2 = Integer.parseInt(splitRange[1].substring(0, splitRange[1].indexOf(';')));
            int c2 = Integer.parseInt(splitRange[1].substring(splitRange[1].indexOf(';') + 1));

            // Iterates through the range and deletes the content in each cell
            if(l1 == l2) {
                int start = Math.min(c1, c2);
                int end = Math.max(c1, c2);
                for(int i = start; i <= end; i++) {
                    if(getCell("" + l1 + ";" + i).getContent() != null) {
                        ref = getCell("" + l1 + ";" + i).getContent().accept(new Detach());
                        if(!ref.equals("")) {
                            getCell(ref).detachObserver((Reference) getCell("" + l1 + ";" + i).getContent());
                        }
                    }
                    // Constant line
                    _storage.deleteContentCell("" + l1 + ";" + i);
                    calculateValue(getCell("" + l1 + ";" + i).getContent());
                    getCell("" + l1 + ";" + i).notifyObservers();
                }
            }
            else if (c1 == c2) {
                int start = Math.min(l1, l2);
                int end = Math.max(l1, l2);
                for(int i = start; i <= end; i++) {
                    // Constant column
                    if(getCell("" + i + ";" + c1).getContent() != null) {
                        ref = getCell("" + i + ";" + c1).getContent().accept(new Detach());
                        if(!ref.equals("")) {
                            getCell(ref).detachObserver((Reference) getCell("" + i + ";" + c1).getContent());
                        }
                    }
                    _storage.deleteContentCell("" + i + ";" + c1);
                    calculateValue(getCell("" + i + ";" + c1).getContent());
                    getCell("" + i + ";" + c1).notifyObservers();
                }
            }
        }
        else {
            // The range is a single cell
            _storage.deleteContentCell(rangeSpecification);
            calculateValue(getCell(rangeSpecification).getContent());
            getCell(rangeSpecification).notifyObservers();
        }
        _change = true;
    }

    /** 
     * Searches for a value or a function in the spreadsheet.
     * 
     * @param value the value or function to be searched.
     * @param type the type of the search (value or function).
     * @return the entries found.
     */
    public String search(String value, String type) {
        ArrayList<String> entries = new ArrayList<>();

        for (int l = 0; l < _numOfLines; l++) {
            for (int c = 0; c < _numOfColumns; c++) {
                String _cellRef = "" + (l + 1) + ";" + (c + 1);
                Cell _cell = getCell(_cellRef);
                Content _cont = _cell.getContent();

                if (_cont != null) {
                    String flag = type.equals("value") ? _cont.accept(new SearchValue(value)) : _cont.accept(new SearchFunction(value));
                    calculateValue(_cont);

                    if (flag.equals("true")) {
                        entries.add(_cellRef + "|" + _cell.toString());
                    }

                    if (type.equals("function")) {
                        // Ordenar as entradas pelo nome da função, linha e coluna
                        Comparator<String> comparator = new Comparator<String>() {
                            @Override
                            public int compare(String entry1, String entry2) {
                                String _funcName1 = entry1.substring(entry1.indexOf('=') + 1, entry1.indexOf('('));
                                String _funcName2 = entry2.substring(entry2.indexOf('=') + 1, entry2.indexOf('('));
                                int _funcNameComparison = (_funcName1).compareTo(_funcName2);
            
                                if (_funcNameComparison != 0) {
                                    return _funcNameComparison;
                                }
            
                                int _line1 = Integer.parseInt(entry1.substring(0, entry1.indexOf(';')));
                                int _line2 = Integer.parseInt(entry2.substring(0, entry2.indexOf(';')));
            
                                if (_line1 != _line2) {
                                    return Integer.compare(_line1, _line2);
                                }
            
                                int _column1 = Integer.parseInt(entry1.substring(entry1.indexOf(';') + 1, entry1.indexOf('|')));
                                int _column2 = Integer.parseInt(entry2.substring(entry2.indexOf(';') + 1, entry2.indexOf('|')));
                                return Integer.compare(_column1, _column2);
            
                            }
                        };
                        Collections.sort(entries, comparator);
                    }
            
                }
            }
        }
        StringBuilder _message = new StringBuilder();
        int size = entries.size();
        for (int i = 0; i < size; i++) {
            _message.append(entries.get(i));
            if (i < size - 1) {
                _message.append("\n");
            }
        }

        return _message.toString();
    }


    /**
     * Gets the size of the range.
     * 
     * @param range the range to be measured.
     * @return the size of the range (number of lines and columns).
     */
    public int[] size(String range) {
        int _size[] = new int[2];
        String[] splitRange = range.split(":"); 
        int l1 = Integer.parseInt(splitRange[0].substring(0, splitRange[0].indexOf(';')));
        int c1 = Integer.parseInt(splitRange[0].substring(splitRange[0].indexOf(';') + 1));
        int l2 = Integer.parseInt(splitRange[1].substring(0, splitRange[1].indexOf(';')));
        int c2 = Integer.parseInt(splitRange[1].substring(splitRange[1].indexOf(';') + 1));
        _size[0] = Math.abs(l1 - l2) + 1;
        _size[1] = Math.abs(c1 - c2) + 1;
        return _size; 
    }

    /**
     * Inserts the content of the cut buffer in the range.
     * 
     * @param range the range to be inserted.
     */
    public void insertContentCutBuffer(String[] range) {
        int i = 0;
        for(int l = 1; l <= _cutBuffer.getLines(); l++) {
            for (int c = 1; c <= _cutBuffer.getColumns(); c++) {
                //calculateValue(getCell(range[i]).getContent());
                if (getCell(range[i]).getContent() != null) {
                    Content _content = getCell(range[i]).getContent().clone();
                    calculateValue(_content);
                    _cutBuffer.getCell(l + ";" + c).insertContent(_content);
                }
                i++;
            }
        }
    }
    
    /**
     * Copies the content of the range to the cut buffer.
     * 
     * @param range the range to be copied.
     * @throws UnrecognizedEntryException if the range is invalid.
     */
    public void copy(String range) throws UnrecognizedEntryException {
        // Verifies if the range is valid
        if (!isRangeValid(range)) {
            throw new UnrecognizedEntryException(range);
        }

        // Creates cut buffer
        if (range.contains(":")) {
            int _size[] = size(range);
            _cutBuffer = new Spreadsheet(_size[0], _size[1]);
            String[] _range = interval(range.substring(0, range.indexOf(':')), range.substring(range.indexOf(':') + 1));
            insertContentCutBuffer(_range);
        }
        else {
            _cutBuffer = new Spreadsheet(1, 1);
            if (getCell(range).getContent() != null) {
                Content _content = getCell(range).getContent().clone();
                calculateValue(_content);
                _cutBuffer.getCell("1;1").insertContent(_content);
            }
        }
    }

    /**
     * Cuts the content of the range to the cut buffer.
     * 
     * @param range the range to be cut.
     * @throws UnrecognizedEntryException if the range is invalid.
     */
    public void cut(String range) throws UnrecognizedEntryException {
        copy(range);
        deleteContents(range);
    }

    /**
     * Inserts the content of the cut buffer in the range.
     * 
     * @param range the range to be inserted.
     */
    public void insertContentOfCutBuffer(String range) {
        int l1 = 0;
        int c1 = 0;
       
        l1 = Integer.parseInt(range.substring(0, range.indexOf(';'))) - 1;
        c1 = Integer.parseInt(range.substring(range.indexOf(';') + 1)) - 1;
    
        for(int l = 1; l <= _cutBuffer.getLines() && l+l1 <= _numOfLines; l++) {
            for (int c = 1; c <= _cutBuffer.getColumns() && c + c1 <= _numOfColumns; c++) {
                getCell((l + l1) + ";" + (c + c1)).insertContent(_cutBuffer.getCell(l + ";" + c).getContent());
                calculateValue(getCell((l + l1) + ";" + (c + c1)).getContent());
            }
        }
    }

    /**
     * Pastes the content of the cut buffer in the range.
     * 
     * @param range the range to be pasted.
     */
    public void paste(String range) {

        if (range.contains(":")) {
            int _size[] = size(range);
            if(_size[0] == _cutBuffer.getLines() && _size[1] == _cutBuffer.getColumns()){
                String _range = range.substring(0, range.indexOf(':'));
                insertContentOfCutBuffer(_range);
            }
        }
        else {
            insertContentOfCutBuffer(range);
        }
        
    }

    /**
     * Shows the cut buffer.
     * 
     * @return the cut buffer.
     */
    public String showCutBuffer() {
        StringBuilder _message = new StringBuilder();

        if (_cutBuffer != null) {
            if (_cutBuffer.getLines() == 1 && _cutBuffer.getColumns() == 1) {
                _message.append("1;1|" + _cutBuffer.getCell("1;1").toString());
            }
            else {
                int _end = _cutBuffer.getLines() * _cutBuffer.getColumns();
                for(int l = 1, i = 0; l <= _cutBuffer.getLines(); l++) {
                    for (int c = 1; c <= _cutBuffer.getColumns(); c++) {
                        _message.append(l + ";" + c + "|" + _cutBuffer.getCell(l + ";" + c).toString());
                        if(i != _end) {
                            _message.append("\n");
                            i++;
                        }
                    }
                    
                }
            }
        }
        return _message.toString();
    }

    public int getLines() {
        return _numOfLines;
    }
    
    public int getColumns() {
        return _numOfColumns;
    }

    public Cell getCell(String key) {
        return _storage.getCell(key);
    }
}
