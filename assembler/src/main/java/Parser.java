import enums.Command;


public class Parser {

    // Parse line given from Assembler
    Command parse(String line) {
        // Remove whitespace and return command type
        String cleanLine = removeWhitespace(line);
        return commandType(cleanLine);
    }

    String removeWhitespace(String line) {
        return "I'm a clean line!";
    }

    Command commandType(String line) {
        return Command.A_COMMAND;
    }

    String symbol() {
        return "example dest";
    }

    String dest() {
        return "example dest";
    }

    String comp() {
        return "example comp";
    }

    String jump() {
        return "example jump";
    }


}
