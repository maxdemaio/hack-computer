import java.io.FileNotFoundException;
import java.io.IOException;

public class VmTranslator {
    public static void main(String[] args) throws IOException {
        // We should use the cmd line arg
        // if input is dir, we process all .vm files in dir
        // if input is *.vm we just translate that one file
        Parser myParser = new Parser("src/main/resources/BasicTest.vm");
        CodeWriter myCodeWriter = new CodeWriter("src/main/resources/BasicTest.asm");

        while (myParser.hasMoreCommands()) {
            String currentCommand = myParser.getCurrentCommand();

            // If it was a comment/empty line, continue to next line without writing
            if (currentCommand.length() == 0 || myParser.isWhiteSpace(currentCommand)) {
                myParser.advance();
                continue;
            }

            // commandType
            CommandType cType = myParser.commandType();

            // init loc/args (null and -1)
            // This is so we can check if changed later
            String arg1;
            int arg2 = -1;
            String location;

            // arg1
            if (cType != CommandType.C_RETURN) {
                arg1 = myParser.arg1();
            }

            // location
            if (cType != CommandType.C_ARITHMETIC ||
                    cType == CommandType.C_FUNCTION
                    || cType == CommandType.C_CALL) {
                location = myParser.location();
            }

            // arg2
            if (cType == CommandType.C_POP ||
                    cType == CommandType.C_PUSH ||
                    cType == CommandType.C_FUNCTION || cType == CommandType.C_CALL) {
                arg2 = myParser.arg2();
            }

            System.out.println(currentCommand);

            // TODO:
            // codewriter
            myCodeWriter.writeVmCommand(myParser.getCurrentCommand());

            // always write comment with current command first
            myParser.advance();
        }

        myParser.close();
        myCodeWriter.close();

        // while parser hasNext, we march through VM commands
        // generate assembly code for each VM command (several assembly commands)

    }
}
