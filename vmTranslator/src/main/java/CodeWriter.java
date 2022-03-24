import java.io.FileOutputStream;
import java.io.IOException;

public class CodeWriter {
    FileOutputStream output;

    CodeWriter(FileOutputStream output) {
        this.output = output;
    }

    void setFileName(String fileName) {
        // inform that the translation of new vm file has started
        System.out.println("Starting translation of VM program " + fileName + "!");
        System.out.println();
    }

    void writeArithmetic(String command) {
        // Write the assembly code that is the translation of arith cmd
    }

    void writePushPop(String command, int index) {
        // write assembly code that is the translation of given command
        // where command is either c_push/pop
    }

    void close() throws IOException {
        this.output.close();
    }
}
