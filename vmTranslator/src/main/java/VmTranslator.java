import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VmTranslator {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream vmLangInput = new FileInputStream("");
        Parser myParser = new Parser(vmLangInput);
    }
}
