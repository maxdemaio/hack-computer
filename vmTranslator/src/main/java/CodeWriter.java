import java.io.FileOutputStream;
import java.io.IOException;

public class CodeWriter {
    String output;

    // heap ~ used as below
    // stack ~ all info stored in the stack

    // constant is completely virtual
    // we can use @value assembly commands

    // RAM[0] SP - stack pointer that points to
    // topmost location in the stack

    // RAM[1] LCL - local pointer that points to
    // base address of current VM function's local segment

    // RAM[2] ARG - arg pointer that points to
    // base address of current VM function's arg segment

    // RAM[3] - points to the base of the current this segment
    // (within heap)

    // RAM[4] - points to the base of the current that segment
    // (within heap)

    // RAM[5-12] holds contents of the temp segment
    // RAM [13-15] used by VM as general purpose registers

    CodeWriter(String output) {
        this.output = output;
    }

    void setFileName(String fileName) {
        // inform that the translation of new vm file has started
        System.out.println("Starting translation of VM program " + fileName + "!");
        System.out.println();
    }

    void writeArithmetic(String command) {
        // Write the assembly code that is the translation of arith cmd
        // example:
        // vm: add

        // assembly:
        // ~~~
    }

    void writePushPop(String command, int index) {
        // write assembly code that is the translation of given command
        // where command is either c_push/pop
    }

    void close() throws IOException {
        this.output.close();
    }
}
