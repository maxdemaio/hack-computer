import enums.Command;


public class Parser {

    void parse() {

    }

    boolean hasCommands() {
        /* Are there more commands in the input? */
        return false;
    }

    void advance() {
        /*
        Reads the next command from the input and makes it the current
        command. Should be called only if hasMoreCommands() is true.
        Initially there is no current command.
        */
    }

    Command commandType() {
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
