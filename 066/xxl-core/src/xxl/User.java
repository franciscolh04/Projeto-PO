package xxl;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing a user.
 */
public class User implements Serializable {
    private String _name = "root";
    private List<Spreadsheet> _spreasheets = null;

    public User() {
        _spreasheets = new ArrayList<>();
        // default user root
    }

    public User(String name) {
        _name = name;
        _spreasheets = new ArrayList<>();
    }

    /**
     * Adds a spreadsheet to the user.
     * @param spreadsheet the spreadsheet to be added.
     */
    public void addSpreadsheet(Spreadsheet spreadsheet) {
        _spreasheets.add(spreadsheet);
    }
}