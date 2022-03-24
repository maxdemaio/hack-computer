import java.io.FileInputStream;

public class Parser {
    FileInputStream input;

    Parser(FileInputStream input) {
        this.input = input;
    }

    boolean hasMoreCommands() {
        return false;
    }

    void advance() {
        // read next command and make it the curr command
        // only called if hasMoreCommands is true
        // initially there is no current command
    }

    CommandType commandType() {
        // return type of current VM command
        return CommandType.C_ARITHMETIC;
    }

    String arg1() {
        // returns first arg of the current command
        // if c_arith the command itself (add, sub) is returned
        return "add";
    }

    int arg2() {
        // returns the second arg of the curr command
        // Only called if current command is c_push/pop/func/call
        return 2;
    }
}
