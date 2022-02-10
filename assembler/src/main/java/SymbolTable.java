import java.util.Map;
import java.util.HashMap;


public class SymbolTable {
    // Start storing variables at index 16 of memory
    // For each storage of a new variable, we'd increment this value
    // This way we can store it in the next adjacent memory address
    // Set memory address of variable, memIndex++;
    public int memIndex = 16;

    public Map<String, String> symbols;

    public SymbolTable() {
        //  Symbol Table starting with 23 pre-defined symbols
        symbols = new HashMap<String, String>();

        symbols.put("SP", "0");
        symbols.put("LCL", "1");
        symbols.put("ARG", "2");
        symbols.put("THIS", "3");
        symbols.put("THAT", "4");
        symbols.put("R0", "0");
        symbols.put("R1", "1");
        symbols.put("R2", "2");
        symbols.put("R3", "3");
        symbols.put("R4", "4");
        symbols.put("R5", "5");
        symbols.put("R6", "6");
        symbols.put("R7", "7");
        symbols.put("R8", "8");
        symbols.put("R9", "9");
        symbols.put("R10", "10");
        symbols.put("R11", "11");
        symbols.put("R12", "12");
        symbols.put("R13", "13");
        symbols.put("R14", "14");
        symbols.put("R15", "15");
        symbols.put("SCREEN", "16384");
        symbols.put("KBD", "24576");
    }

    public void addVariable(String key) {
        symbols.put(key, Integer.toString(memIndex));
        memIndex++;
    }

    public void addLabel(String key, String value) {
        symbols.put(key, value);
    }

    // Check if symbol exists
    public boolean containKey(String key) {
        return symbols.containsKey(key);
    }

    public String getValue(String val) {
        return symbols.get(val);
    }

}
