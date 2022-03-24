import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class VmTranslator {
    public static void main(String[] args) throws FileNotFoundException {
        // We should use the cmd line arg
        // if input is dir, we process all .vm files in dir
        // if input is *.vm we just translate that one file

        FileInputStream vmLangInput = new FileInputStream("");
        FileOutputStream assemblyOutput = new FileOutputStream("");
        Parser myParser = new Parser(vmLangInput);
        CodeWriter myCodeWriter = new CodeWriter(assemblyOutput);

        // while parser hasNext, we march through VM commands
        // generate assembly code for each VM command (several assembly commands)

    }
}
