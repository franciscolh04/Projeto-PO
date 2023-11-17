package xxl.structures;

import xxl.structures.ContiguousMemory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class representing the memory of a spreadsheet application.
 */
public class ContiguousMemory extends StorageStruct {

    /** Map containing the cells of the spreadsheet */
    private Map<String, Cell> _map = new TreeMap<>();
        
    public ContiguousMemory(int maxLines, int maxColumns) {
        for (int l = 0; l < maxLines; l++) {
            for (int c = 0; c < maxColumns; c++) {
                _map.put(convertPosToString(l + 1, c + 1), new Cell());
            }
        }
    }

    /**
     * Converts a position of a cell in the spreadsheet to a string.
     * 
     * @param line the line of the cell.
     * @param column the column of the cell.
     * @return the string representing the position of the cell.
     */
    public String convertPosToString(int line, int column) {
        return line + ";" + column;
    }

    /**
     * Inserts the specified content into the cell.
     * 
     * @param key the key of the cell in the map.
     * @param content the content to be inserted.
     */
    public void insertContentCell(String key, Content content) {
        Cell cell = _map.get(key);
        cell.insertContent(content);
    }
    
    /**
     * Deletes the content of the cell.
     * @param key the key of the cell in the map.
     */
    public void deleteContentCell(String key) {
        Cell cell = _map.get(key);
        cell.deleteContent();
    }

    /**
     * Gets the content of the cell.
     * 
     * @param key the key of the cell in the map.
     * @return the content of the cell.
     */
    public Content getCellContent(String key) {
        Cell cell = _map.get(key);
        return cell.getContent();
    }

    /**
     * @return the map which contains the cells.
     */
    public Map<String, Cell> getMap() {
        return _map;
    }

    /**
     * @param key the key of the cell in the map.
     * @return the cell.
     */
    public Cell getCell(String key) {
        return _map.get(key);
    }

    /**
     * @return the number of cells in the map.
     */
    public int getSize() {
        return _map.size();
    }
}
