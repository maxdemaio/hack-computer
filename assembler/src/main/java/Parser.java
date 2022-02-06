import enums.Command;
import exceptions.MyAssemblerException;


public class Parser {
    // Start storing variables at index 16 of memory
    // For each storage of a new variable, we'd increment this value
    // This way we can store it in the next adjacent memory address
    // Set memory address of variable, memIndex++;
    public static int memIndex = 16;

    String cleanLine(String line) {
        String noWhiteSpace = removeWhitespace(line);
        return removeComments(noWhiteSpace);
    }

    String removeWhitespace(String line) {
        return line.replaceAll("\\s", "");
    }

    String removeComments(String line) {
        return line.replaceAll("//.+", "");
    }

    Command commandType(String line) {
        if (line.startsWith("@")) {
            return Command.A_COMMAND;
        } else if (line.startsWith("(")) {
            return Command.L_COMMAND;
        }
        return Command.C_COMMAND;
    }

    String symbol(String line) {
        // Returns the symbol or decimal
        // Xxx of the current command
        // @Xxx or (Xxx)
        if (line.contains("@")) {
            String aClean = line.replaceAll("@", "");
            return aClean;
        }

        if (line.contains("(")) {
            String lClean = line.replaceAll("\\(", "");
            String lCleanFinal = line.replaceAll("\\)", "");
            return lCleanFinal;
        }

        return line;
    }

    String dest(String line) {
        // Example) D=D-A;JEQ
        // This would get "D"
        // If it's not in the Hashtable, throw exception
        return "example dest";
    }

    String comp(String line) {
        // Example) D=D-A;JEQ
        // This would get "D-A"
        // If it's not in the Hashtable, throw exception
        return "example comp";
    }

    String jump(String line) {
        // Example) D=D-A;JEQ
        // This would get "JEQ"
        // If it's not in the Hashtable, throw exception
        return "example jump";
    }

    Boolean stringIsNum(String symbol) {
        // Check if A-instruction is a number or not
        if (symbol == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(symbol);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    String addZeros(String binary) throws MyAssemblerException {
        // Makes sure binary value is 16-bits
        if (binary.length() <= 16) {
            return ("0".repeat(16 - binary.length()) + binary);
        }

        throw new MyAssemblerException("binary string must be <= 16-bits");

    }
}
