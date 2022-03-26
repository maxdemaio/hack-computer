import java.io.*;
import java.util.HashMap;
import java.util.List;

public class Parser {
    int lineCount;
    FileInputStream fis;
    BufferedReader reader;
    String currentCommand;
    HashMap<String, CommandType> commandMap;

    Parser(String fileName) {
        try {
            fis = new FileInputStream(fileName);
            reader = new BufferedReader(new InputStreamReader(fis));
            // init first line
            currentCommand = reader.readLine();

            commandMap = new HashMap<>();
            commandMap.put("pop", CommandType.C_POP);
            commandMap.put("push", CommandType.C_PUSH);
            commandMap.put("sub", CommandType.C_ARITHMETIC);
            commandMap.put("if", CommandType.C_IF);
            commandMap.put("func", CommandType.C_FUNCTION);
            commandMap.put("return", CommandType.C_RETURN);
            commandMap.put("call", CommandType.C_CALL);
            commandMap.put("add", CommandType.C_ARITHMETIC);
            commandMap.put("goto", CommandType.C_GOTO);
            commandMap.put("label", CommandType.C_LABEL);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean hasMoreCommands() {
        if (currentCommand != null) {
            return true;
        }
        return false;
    }

    Boolean isWhiteSpace(String line) {
        if (line.isBlank()) {
            return true;
        }
        return false;
    }

    String removeComments(String line) {
        return line.replaceAll("//.+", "");
    }

    String getCurrentCommand() {
        // remove comments
        currentCommand = removeComments(currentCommand);
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
        String[] commandList = currentCommand.split(" ");
        String firstStr = commandList[0];
        return commandMap.get(firstStr);
    }

    String arg1() {
        // returns first arg of the current command
        // if c_arith the command itself (add, sub) is returned
        String[] commandList = currentCommand.split(" ");
        String firstStr = commandList[0];
        return firstStr;
    }

    int arg2() {
        // returns the second arg of the curr command
        // Only called if current command is c_push/pop/func/call
        String[] commandList = currentCommand.split(" ");
        String lastStr = commandList[commandList.length - 1];
        return Integer.parseInt(lastStr);
    }

    void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
