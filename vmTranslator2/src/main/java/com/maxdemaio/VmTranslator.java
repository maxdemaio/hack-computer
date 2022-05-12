package com.maxdemaio;

import java.util.ArrayList;
import java.io.File;

public class VmTranslator {
    public static void main(String[] args) {
        File fileArg = new File(args[0]);
        File fOut;
        ArrayList<File> files = new ArrayList<>();

        // make sure only 1 arg
        if (args.length != 1)
            throw new IllegalArgumentException("incorrect usage. proper format: java VMTranslator (dir/fileArg)");
            // make sure correct file extension
        else if (fileArg.isFile() && !(args[0].endsWith(".vm")))
            throw new IllegalArgumentException("incorrect file type. enter a vm file or a dir with vm files. ");
        else {
            // arg is a single vm file
            // we start array list with just 1 element (File)
            if (fileArg.isFile() && args[0].endsWith(".vm")) {
                files.add(fileArg);
                String baseName = args[0].substring(0, args[0].length() - 3);
                fOut = new File(baseName + ".asm");
            } else {
                // arg is dir - go over all files in the dir
                // make array list have as many elements as there are vm files
                files = getVMFiles(fileArg);
                fOut = new File(fileArg + ".asm");
            }
        }
        // construct a CodeWriter to make asm output file
        CodeWriter codeWriter = new CodeWriter(fOut);

        // comment out this line for tests not needing bootstrap code
        // this adds setup for SP and Sys.init
        codeWriter.writeInit();

        for (File file : files) {
            // setup filename for current vm file in our CodeWriter
            // allows us to make labels according to filename for static push/pop
            // we maintain state of static vars for each jack class (vm file)
            codeWriter.setFileName(file);

            // make a Parser to parse VM input files
            // use a diff Parser for each vm file
            // note, we only use 1 CodeWriter bc 1 asm file
            Parser parser = new Parser(file);

            // go over VM commands in current vm file,
            // generate asm
            while (parser.hasMoreCommands()) {
                parser.advance();
                if (parser.commandType().equals("C_ARITHMETIC")) {
                    codeWriter.writeArithmetic(parser.arg1());
                } else if (parser.commandType().equals("C_PUSH") || parser.commandType().equals("C_POP")) {
                    codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                } else if (parser.commandType().equals("C_LABEL")) {
                    codeWriter.writeLabel(parser.arg1());
                } else if (parser.commandType().equals("C_GOTO")) {
                    codeWriter.writeGoto(parser.arg1());
                } else if (parser.commandType().equals("C_IF")) {
                    codeWriter.writeIf(parser.arg1());


                } else if (parser.commandType().equals("C_FUNCTION")) {
                    codeWriter.writeFunction(parser.arg1(), parser.arg2());
                } else if (parser.commandType().equals("C_RETURN")) {
                    codeWriter.writeReturn();
                } else if (parser.commandType().equals("C_CALL")) {
                    codeWriter.writeCall(parser.arg1(), parser.arg2());
                }

            }

        }

        codeWriter.close();

    }


    // get files in the directory argument into an arraylist
    public static ArrayList<File> getVMFiles(File directory) {
        File[] files = directory.listFiles();
        ArrayList<File> fResults = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".vm")) fResults.add(file);
            }
        }
        return fResults;

    }
}