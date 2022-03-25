import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class VmTranslator {
    public static void main(String[] args) throws FileNotFoundException {
        // We should use the cmd line arg
        // if input is dir, we process all .vm files in dir
        // if input is *.vm we just translate that one file
        Parser myParser = new Parser("src/main/resources/BasicTest.vm");
        while (myParser.hasMoreCommands()) {
            String currentCommand = myParser.getCurrentCommand();

            // remove comments
            currentCommand = myParser.removeComments(currentCommand);

            // If it was an comment/empty line, continue to next line without writing
            if (currentCommand.length() == 0 || myParser.isWhiteSpace(currentCommand)) {
                myParser.advance();
                continue;
            }

            System.out.println(currentCommand);
            myParser.advance();
        }

        myParser.close();

        // while parser hasNext, we march through VM commands
        // generate assembly code for each VM command (several assembly commands)

    }
}
