import java.io.*;

public class Parser {
    int lineCount;
    FileInputStream fis;
    BufferedReader reader;
    String currentCommand;

    Parser(String fileName) {
        try {
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis));
            // init first line
            currentCommand = reader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean hasMoreCommands() {
        if(currentCommand != null) {
            return true;
        }
        return false;
    }

    String printCurrentCommand() {
        return currentCommand;
    }

    void advance() {
        // read next command and make it the curr command
        // only called if hasMoreCommands is true
        // initially there is no current command
        try {
            currentCommand = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
