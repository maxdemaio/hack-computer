import enums.Command;
import exceptions.MyAssemblerException;


public class Parser {

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
            String lCleanPrelim = line.replaceAll("\\(", "");
            String lCleanFinal = lCleanPrelim.replaceAll("\\)", "");
            return lCleanFinal;
        }

        return line;
    }

    String dest(String line) {
        // Example) D=D-A;JEQ - this would get "D"
        // Example) 0;JMP - this would get NULL
        // If it's not in the Hashtable, throw exception
        String[] parts = line.split("=");
        if (parts.length > 1) {
            return parts[0];
        }
        return "NULL";
    }

    String comp(String line) {
        // Example) D=D-A;JEQ - this would get "D-A"
        // Example) D=D-A
        // Example) 0;JMP - this would get 0
        // If it's not in the Hashtable, throw exception

        // Example 1 (=, ;)
        if (line.contains("=") && line.contains(";")) {
            // [D, D-A;JEQ]
            String[] prelimSplit = line.split("=");
            // [D-A, JEQ]
            String[] finalSplit = prelimSplit[1].split(";");
            return finalSplit[0];
        }
        // Example 2 (=)
        else if (line.contains("=") && !(line.contains(";"))) {
            String[] split = line.split("=");
            return split[1];
        }
        // Example 3 (;)
        else if (line.contains(";") && !(line.contains("="))) {
            String[] split = line.split(";");
            return split[0];
        }
        return line;
    }

    String jump(String line) {
        // Example) D=D-A;JEQ - this would get "JEQ"
        // Example) 0;JMP (JMP)
        // If it's not in the Hashtable, throw exception
        String[] parts = line.split(";");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }
        return "NULL";
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
        // Makes sure binary value is 15-bits
        if (binary.length() <= 15) {
            return ("0".repeat(16 - binary.length()) + binary);
        }

        throw new MyAssemblerException("binary string must be <= 15-bits");

    }
}
