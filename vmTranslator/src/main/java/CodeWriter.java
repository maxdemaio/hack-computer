import java.io.*;

public class CodeWriter {
    // heap ~ memory that isn't the stack
    // stack ~ all info stored in the stack

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

    /*
    example 1: push constant 10
    // constant is completely virtual
    // we can use @value assembly commands
    // RAM[0] SP - stack pointer that points to
    // topmost location in the stack
    @10
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1
    */

    /*
    example 2: push local i
       @LCL
       D=M

       @i
       A=D+A // offset base address to get local[i]
       D=M // D register is equal to the value of local[i]

       @SP
       A=M // causes A to take value inside of the selected mem reg (stack pointer) (0 is ref, other is value)
       M=D // ram[stack pointer location] is equal to local[i]
       @SP
       M=M+1
     */

    /*
    example 3: pop arg i
       // R13 = @segment + i
       @ARG
       D=M // D=base address of arg

       @i
       D=D+A // set D equal to address of arg[i]

       @R13
       M=D // R13 is equal to the address of arg[i]

       // &R13 = D
       @SP
       M=M-1 // SP = RAM[address of value at top of stack]
       @SP
       A=M // A takes ref to value of top of stack
       D=M // D takes value of top of stack

       @R13
       A=M // A is equal to address of arg[i]
       M=D // arg[i] is equal to value of top of stack
     */

    /*
    example 4: add/sub
        @SP
        AM=M-1 // make stack pointer now look at last element so it'll get overwritten in future
        D=M // set D-register to 1/2 of the operands

        A=A-1 // get the other operand 2/2
        M=D+M // set 2/2 to the result OR if sub (M=D-M)
     */

    private int arthJumpFlag;
    private String outputFileName;
    private File fout;
    private FileOutputStream fos;
    private BufferedWriter bw;

    CodeWriter(String outputFileName) throws FileNotFoundException {
        this.outputFileName = outputFileName;
        fout = new File(outputFileName);
        fos = new FileOutputStream(fout);
        bw = new BufferedWriter(new OutputStreamWriter(fos));
    }

    /**
     * write asm for the given vm arithmetic command
     *
     * @param command
     */
    public void writeArithmetic(String command) throws IOException {

        if (command.equals("add")) {

            write(arithTemplate1() + "M=M+D\n");

        } else if (command.equals("sub")) {

            write(arithTemplate1() + "M=M-D\n");

        } else if (command.equals("and")) {

            write(arithTemplate1() + "M=M&D\n");

        } else if (command.equals("or")) {

            write(arithTemplate1() + "M=M|D\n");

        } else if (command.equals("gt")) {

            write(arithTemplate2("JLE"));//not <=
            arthJumpFlag++;

        } else if (command.equals("lt")) {

            write(arithTemplate2("JGE"));//not >=
            arthJumpFlag++;

        } else if (command.equals("eq")) {

            write(arithTemplate2("JNE"));//not <>
            arthJumpFlag++;

        } else if (command.equals("not")) {

            write("@SP\nA=M-1\nM=!M\n");

        } else if (command.equals("neg")) {

            write("D=0\n@SP\nA=M-1\nM=D-M\n");

        } else {

            throw new IllegalArgumentException("Call writeArithmetic() for a non-arithmetic command");

        }
    }

    /**
     * write asm for the given vm command
     * where the command is PUSH or POP
     *
     * @param command PUSH or POP
     * @param segment
     * @param index
     */
    public void writePushPop(CommandType command, String segment, int index) throws IOException {

        if (command == CommandType.C_PUSH) {

            if (segment.equals("constant")) {

                write("@" + index + "\n" +
                        "D=A\n" +
                        "@SP\n" +
                        "A=M\n" +
                        "M=D\n" +
                        "@SP\n" +
                        "M=M+1\n");

            } else if (segment.equals("local")) {

                write(pushTemplate1("LCL", index, false));

            } else if (segment.equals("argument")) {

                write(pushTemplate1("ARG", index, false));

            } else if (segment.equals("this")) {

                write(pushTemplate1("THIS", index, false));

            } else if (segment.equals("that")) {

                write(pushTemplate1("THAT", index, false));

            } else if (segment.equals("temp")) {

                write(pushTemplate1("R5", index + 5, false));

            } else if (segment.equals("pointer") && index == 0) {

                write(pushTemplate1("THIS", index, true));

            } else if (segment.equals("pointer") && index == 1) {

                write(pushTemplate1("THAT", index, true));

            } else if (segment.equals("static")) {

                write(pushTemplate1(String.valueOf(16 + index), index, true));

            }

        } else if (command == CommandType.C_POP) {

            if (segment.equals("local")) {

                write(popTemplate1("LCL", index, false));

            } else if (segment.equals("argument")) {

                write(popTemplate1("ARG", index, false));

            } else if (segment.equals("this")) {

                write(popTemplate1("THIS", index, false));

            } else if (segment.equals("that")) {

                write(popTemplate1("THAT", index, false));

            } else if (segment.equals("temp")) {
                // per book specification we take the index + 5
                write(popTemplate1("R5", index + 5, false));

            } else if (segment.equals("pointer") && index == 0) {

                write(popTemplate1("THIS", index, true));

            } else if (segment.equals("pointer") && index == 1) {

                write(popTemplate1("THAT", index, true));

            } else if (segment.equals("static")) {

                write(popTemplate1(String.valueOf(16 + index), index, true));

            }
        } else {
            throw new IllegalArgumentException("Call writePushPop() for a non-pushpop command");
        }
    }

    /**
     * template for add sub and logical or
     *
     * @return
     */
    private String arithTemplate1() {
        return "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n";
    }

    /**
     * template for gt lt eq
     *
     * @param type JLE JGT JEQ
     * @return
     */
    private String arithTemplate2(String type) {
        return "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "A=A-1\n" +
                "D=M-D\n" +
                "@FALSE" + arthJumpFlag + "\n" +
                "D;" + type + "\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=-1\n" +
                "@CONTINUE" + arthJumpFlag + "\n" +
                "0;JMP\n" +
                "(FALSE" + arthJumpFlag + ")\n" +
                "@SP\n" +
                "A=M-1\n" +
                "M=0\n" +
                "(CONTINUE" + arthJumpFlag + ")\n";
    }


    /**
     * template for push local,this,that,argument,temp,pointer,static
     *
     * @param segment
     * @param index
     * @param isDirect (is this command direct addressing?)
     * @return
     */
    private String pushTemplate1(String segment, int index, boolean isDirect) {
        // when it's a pointer, read the data stored in THIS or THAT
        // when its static, read the data stored in that address
        String noPointerCode = (isDirect) ? "" : "@" + index + "\n" + "A=D+A\nD=M\n";

        return "@" + segment + "\n" +
                "D=M\n" +
                noPointerCode +
                "@SP\n" +
                "A=M\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M+1\n";
    }

    /**
     * template for pop local,this,that,argument,temp,pointer,static
     *
     * @param segment
     * @param index
     * @param isDirect (is this command direct addressing?)
     * @return
     */
    private String popTemplate1(String segment, int index, boolean isDirect) {
        // when it is a pointer R13 will store the address of THIS or THAT
        // when it is a static R13 will store the index address
        String noPointerCode = (isDirect) ? "D=A\n" : "D=M\n@" + index + "\nD=D+A\n";

        return "@" + segment + "\n" +
                noPointerCode +
                "@R13\n" +
                "M=D\n" +
                "@SP\n" +
                "AM=M-1\n" +
                "D=M\n" +
                "@R13\n" +
                "A=M\n" +
                "M=D\n";
    }


    void writeVmComment(String command) throws IOException {
        bw.write("// " + command);
        bw.newLine();
        return;
    }


    void write(String command) throws IOException {
        bw.write(command);
    }


    void close() throws IOException {
        bw.close();
        return;
    }
}
