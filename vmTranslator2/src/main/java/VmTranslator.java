import java.io.IOException;

public class VmTranslator {
    public static void main(String[] args) throws IOException {
        // TODO: we should use the cmd line arg
        // if input is dir, we process all .vm files in dir
        // if input is *.vm we just translate that one file
        Parser myParser = new Parser("src/main/resources/BasicTest.vm");
        CodeWriter myCodeWriter = new CodeWriter("src/main/resources/BasicTest.asm");

        // while parser hasNext, we march through VM commands
        // generate assembly code for each VM command (several assembly commands)
        while (myParser.hasMoreCommands()) {
            String currentCommand = myParser.getCurrentCommand();
            System.out.println(currentCommand);

            // If it was a comment/empty line, continue to next line without writing
            if (currentCommand.length() == 0 || myParser.isWhiteSpace(currentCommand)) {
                myParser.advance();
                continue;
            }

            // Write comment of VM lang to ASM file
            myCodeWriter.writeVmComment(myParser.getCurrentCommand());

            // commandType
            CommandType cType = myParser.commandType();

            // init loc/args (null and -1)
            // This is so we can check if changed later
            String arg1 = null;
            int arg2 = -1;
            String segement = null;

            // arg1
            if (cType != CommandType.C_RETURN) {
                arg1 = myParser.arg1();
            }

            // segment
            if (cType != CommandType.C_ARITHMETIC ||
                    cType == CommandType.C_FUNCTION
                    || cType == CommandType.C_CALL) {
                segement = myParser.segement();
            }

            // arg2
            if (cType == CommandType.C_POP ||
                    cType == CommandType.C_PUSH ||
                    cType == CommandType.C_FUNCTION || cType == CommandType.C_CALL) {
                arg2 = myParser.arg2();
            }

            // push/pop or arith write
            if (cType == CommandType.C_POP ||
                    cType == CommandType.C_PUSH) {
                myCodeWriter.writePushPop(cType, segement, arg2);
            } else {
                myCodeWriter.writeArithmetic(currentCommand);
            }

            myParser.advance();
        }

        myParser.close();
        myCodeWriter.close();
    }
}
