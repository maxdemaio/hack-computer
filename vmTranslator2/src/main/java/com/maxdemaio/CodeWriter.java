package com.maxdemaio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Translate vm commands into hack asm
 */
public class CodeWriter {
    /*
    Psuedo assembly examples for call/func/return

    ---

    (call f n) example:

    push return-address // (Using the label declared below)
        push LCL // Save LCL of the calling function
        push ARG // Save ARG of the calling function
        push THIS // Save THIS of the calling function
        push THAT // Save THAT of the calling function
        ARG = SP-n-5 // Reposition ARG (n ¼ number of args.)
        LCL = SP // Reposition LCL
        goto f // Transfer control
    (return-address) // Declare a label for the return-address

    ---

    (function f k) example:

    (f) // Declare a label for the function entry
        repeat k times: // k ¼ number of local variables
        PUSH 0 // Initialize all of them to 0

    ---

    (return) example:

    FRAME = LCL // FRAME is a temporary variable
    RET = *(FRAME-5) // Put the return-address in a temp. var.
    *ARG = pop() // Reposition the return value for the caller
    SP = ARG+1 // Restore SP of the caller
    THAT = *(FRAME-1) // Restore THAT of the caller
    THIS = *(FRAME-2) // Restore THIS of the caller
    ARG = *(FRAME-3) // Restore ARG of the caller
    LCL = *(FRAME-4) // Restore LCL of the caller
    goto RET // Goto return-address (in the caller’s code)
    */

    private FileWriter fw;

    // we use this for jumping around in conditionals like
    // lt/gt/eq
    // this way, we can goto false/continue labels based on result
    private int mJumpNumber = 0;

    private static String fileName = "";

    // when we generate labels to make them unique
    // done at the beginning and end of return commands
    private static int nLabelNum = 0;

    // opens output file and gets ready to write to it
    public CodeWriter(File file) {
        try {
            fw = new FileWriter(file);
            setFileName(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // translation of new vm file starts here
    public void setFileName(File file) {
        fileName = file.getName();
    }

    // we use comments to write the corresponding
    // vm lang command above so easy for debugging
    // 1 vm command -> many asm commands
    public void writeComment(String strComment) {
        try {
            fw.write("//" + strComment + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // writes the asm code for given vm arithmetic cmd
    public void writeArithmetic(String strCommand) {
        String strACode = null;
        if (strCommand.equals("add")) {
            strACode = new StringBuilder()
                    .append(getArithFormat1())
                    .append("M=M+D\n").toString();
        } else if (strCommand.equals("sub")) {
            strACode = new StringBuilder()
                    .append(getArithFormat1())
                    .append("M=M-D\n").toString();
        } else if (strCommand.equals("neg")) {
            strACode = new StringBuilder()
                    .append("D=0\n")
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("M=D-M\n").toString();
        } else if (strCommand.equals("eq")) {
            strACode = new StringBuilder()
                    .append(getArithFormat1())
                    .append(getArithFormat2("JNE")).toString();
            mJumpNumber++;
        } else if (strCommand.equals("gt")) {
            strACode = new StringBuilder()
                    .append(getArithFormat1())
                    .append(getArithFormat2("JLE")).toString();
            mJumpNumber++;
        } else if (strCommand.equals("lt")) {
            strACode = new StringBuilder()
                    .append(getArithFormat1())
                    .append(getArithFormat2("JGE")).toString();
            mJumpNumber++;
        } else if (strCommand.equals("and")) {
            strACode = new StringBuilder()
                    .append(getArithFormat1())
                    .append("M=M&D\n").toString();
        } else if (strCommand.equals("or")) {
            strACode = new StringBuilder()
                    .append(getArithFormat1())
                    .append("M=M|D\n").toString();
        } else if (strCommand.equals("not")) {
            strACode = new StringBuilder()
                    .append("@SP\n")
                    .append("A=M-1\n")
                    .append("M=!M\n").toString();
        }
        try {
            fw.write(strACode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // writes the assembly code for curr vm command
    // this is where cmd is either C_PUSH or C_POP
    // we are given segment and index as well
    public void writePushPop(String strCommand, String strSegment, int nIndex) {
        String strAcode = "";
        if (strCommand.equals("C_PUSH")) {
            if (strSegment.equals("static")) {
                strAcode = new StringBuilder()
                        .append("@").append(fileName).append(nIndex).append("\n")
                        .append("D=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n").toString();
            } else if (strSegment.equals("this")) {
                strAcode = getPushFormat1("THIS", nIndex);
            } else if (strSegment.equals("local")) {
                strAcode = getPushFormat1("LCL", nIndex);

            } else if (strSegment.equals("argument")) {
                strAcode = getPushFormat1("ARG", nIndex);

            } else if (strSegment.equals("that")) {
                strAcode = getPushFormat1("THAT", nIndex);

            } else if (strSegment.equals("constant")) {
                // note, constant is completely virtual
                strAcode = new StringBuilder()
                        .append("@").append(nIndex).append("\n")
                        .append("D=A\n")
                        .append("@SP\n")
                        .append("A=M\n")
                        .append("M=D\n")
                        .append("@SP\n")
                        .append("M=M+1\n").toString();

            } else if (strSegment.equals("pointer") && nIndex == 0) {
                strAcode = getPushFormat2("THIS");
            } else if (strSegment.equals("pointer") && nIndex == 1) {
                strAcode = getPushFormat2("THAT");
            } else if (strSegment.equals("temp")) {
                // per specification in textbook
                strAcode = getPushFormat1("R5", nIndex + 5);
            }

        } else if (strCommand.equals("C_POP")) {
            if (strSegment.equals("static")) {
                strAcode = new StringBuilder()
                        .append("@").append(fileName).append(nIndex)
                        .append("\nD=A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n").toString();
            } else if (strSegment.equals("this")) {
                strAcode = getPopFormat1("THIS", nIndex);
            } else if (strSegment.equals("local")) {
                strAcode = getPopFormat1("LCL", nIndex);

            } else if (strSegment.equals("argument")) {
                strAcode = getPopFormat1("ARG", nIndex);

            } else if (strSegment.equals("that")) {
                strAcode = getPopFormat1("THAT", nIndex);

            } else if (strSegment.equals("constant")) {
                // note, constant is completely virtual
                strAcode = new StringBuilder()
                        .append("@").append(nIndex).append("\n")
                        .append("D=A\n")
                        .append("@SP\n")
                        .append("A=M\n")
                        .append("M=D\n")
                        .append("@SP\n")
                        .append("M=M+1\n").toString();
            } else if (strSegment.equals("pointer") && nIndex == 0) {
                strAcode = getPopFormat2("THIS");
            } else if (strSegment.equals("pointer") && nIndex == 1) {
                strAcode = getPopFormat2("THAT");
            } else if (strSegment.equals("temp")) {
                // per specification in textbook
                strAcode = getPopFormat1("R5", nIndex + 5);
            }
        }
        try {
            fw.write(strAcode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // format for arith command
    // works for all except for neg/not
    public String getArithFormat1() {
        return new StringBuilder()
                .append("@SP\n")
                .append("AM=M-1\n")
                .append("D=M\n")
                .append("A=A-1\n").toString();
    }

    // get 2nd part of format for arithmetic commands -
    // only for et, gt, and lt
    public String getArithFormat2(String strJump) {
        return new StringBuilder()
                .append("D=M-D\n")
                .append("@FALSE").append(mJumpNumber).append("\n")
                .append("D;")
                .append(strJump)
                .append("\n@SP\n")
                .append("A=M-1\n")
                .append("M=-1\n")
                .append("@CONTINUE").append(mJumpNumber)
                .append("\n0;JMP\n")
                .append("(FALSE").append(mJumpNumber).append(")\n")
                .append("@SP\n")
                .append("A=M-1\n")
                .append("M=0\n")
                .append("(CONTINUE").append(mJumpNumber).append(")\n").toString();
    }

    // format for pushing onto stack given the segment and index
    // for this, local, argument, that, and temp
    public String getPushFormat1(String strSegment, int nIndex) {
        String strACode;
        strACode = new StringBuilder()
                .append("@").append(strSegment)
                .append("\nD=M\n@").append(nIndex).append("\n")
                .append("A=D+A\n")
                .append("D=M\n")
                .append("@SP\n")
                .append("A=M\n")
                .append("M=D\n")
                .append("@SP\n")
                .append("M=M+1\n").toString();
        return strACode;
    }

    // get format for pushing onto stack given the segment
    // for static & pointer
    public String getPushFormat2(String strSegment) {
        String strAcode;
        strAcode = new StringBuilder()
                .append("@").append(strSegment)
                .append("\nD=M\n")
                .append("@SP\n")
                .append("A=M\n")
                .append("M=D\n")
                .append("@SP\n")
                .append("M=M+1\n").toString();
        return strAcode;
    }

    // format for popping off of stack given the segment and index
    // for this, local, argument, that, and temp
    public String getPopFormat1(String strSegment, int nIndex) {
        String strAcode;
        strAcode = new StringBuilder().append("@").append(strSegment)
                .append("\nD=M\n@")
                .append(nIndex).append("\n")
                .append("D=D+A\n")
                .append("@R13\n")
                .append("M=D\n")
                .append("@SP\n")
                .append("AM=M-1\n")
                .append("D=M\n")
                .append("@R13\n")
                .append("A=M\n")
                .append("M=D\n").toString();
        return strAcode;
    }

    // format for popping off of stack given the segment -
    // for static & pointer
    public String getPopFormat2(String strSegment) {
        String strAcode;
        strAcode = new StringBuilder().append("@").append(strSegment)
                .append("\nD=A\n")
                .append("@R13\n")
                .append("M=D\n")
                .append("@SP\n")
                .append("AM=M-1\n")
                .append("D=M\n")
                .append("@R13\n")
                .append("A=M\n")
                .append("M=D\n").toString();
        return strAcode;
    }

    // close output file
    public void close() {
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // writes asm code for vm initialization
    // bootstrap code at beginning of output file
    public void writeInit() {
        try {
            fw.write(new StringBuilder()
                    .append("@256\n")
                    .append("D=A\n")
                    .append("@SP\n")
                    .append("M=D\n").toString());
            writeCall("Sys.init", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // writes asm code that is translation of label command
    // string with any sequence of letters, digits, underscore, dot, and colon not beginning with digit
    public void writeLabel(String strLabel) {
        try {
            fw.write(new StringBuilder()
                    .append("(").append(strLabel).append(")\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write asm code for goto vm command
    public void writeGoto(String strLabel) {
        try {
            fw.write(new StringBuilder()
                    .append("@").append(strLabel)
                    .append("\n0;JMP\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write asm code for vm if-goto command
    public void writeIf(String strLabel) {
        try {
            fw.write(new StringBuilder()
                    .append(getArithFormat1()).append("@").append(strLabel)
                    .append("\nD;JNE\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write asm code for vm call command
    public void writeCall(String strFunctionName, int nNumArgs) {
        String strLabel = "RETURN_LABEL" + nLabelNum;
        nLabelNum++;
        try {
            fw.write(new StringBuilder()
                    .append("@").append(strLabel).append("\n")
                    .append("D=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n")
                    .append(getPushFormat2("LCL"))
                    .append(getPushFormat2("ARG"))
                    .append(getPushFormat2("THIS"))
                    .append(getPushFormat2("THAT"))
                    .append("@SP\n")
                    .append("D=M\n")
                    .append("@5\n")
                    .append("D=D-A\n")
                    .append("@")
                    .append(nNumArgs).append("\n")
                    .append("D=D-A\n")
                    .append("@ARG\n")
                    .append("M=D\n")
                    .append("@SP\n")
                    .append("D=M\n")
                    .append("@LCL\n")
                    .append("M=D\n")
                    .append("@")
                    .append(strFunctionName).append("\n0;JMP\n(")
                    .append(strLabel).append(")\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write asm code for vm return command
    public void writeReturn() {
        try {
            fw.write(new StringBuilder()
                    .append("@LCL\n")
                    .append("D=M\n")
                    .append("@FRAME\n")
                    .append("M=D\n")
                    .append("@5\n")
                    .append("A=D-A\n")
                    .append("D=M\n")
                    .append("@RET\n")
                    .append("M=D\n")
                    .append(getPopFormat1("ARG", 0))
                    .append("@ARG\n")
                    .append("D=M\n")
                    .append("@SP\n")
                    .append("M=D+1\n")
                    .append("@FRAME\n")
                    .append("D=M-1\n")
                    .append("AM=D\n")
                    .append("D=M\n")
                    .append("@THAT\n")
                    .append("M=D\n")
                    .append("@FRAME\n")
                    .append("D=M-1\n")
                    .append("AM=D\n")
                    .append("D=M\n")
                    .append("@THIS\n")
                    .append("M=D\n")
                    .append("@FRAME\n")
                    .append("D=M-1\n")
                    .append("AM=D\n")
                    .append("D=M\n")
                    .append("@ARG\n")
                    .append("M=D\n")
                    .append("@FRAME\n")
                    .append("D=M-1\n")
                    .append("AM=D\n")
                    .append("D=M\n")
                    .append("@LCL\n")
                    .append("M=D\n")
                    .append("@RET\n")
                    .append("A=M\n")
                    .append("0;JMP\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write asm code for vm function command
    public void writeFunction(String strFunctionName, int nNumLocals) {
        try {
            fw.write(new StringBuilder()
                    .append("(").append(strFunctionName).append(")\n").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int nC = 0; nC < nNumLocals; nC++) {
            // initialize local segement with 0s for as many locals vars there are
            writePushPop("C_PUSH", "constant", 0);
        }
    }

}