package com.maxdemaio;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Parses vm file
 * Reads vm commands line by line
 * Determines the command type
 * Removes all white space & comments
 * Provides a setup for the CodeWriter class to write asm
 */
public class Parser {
    private static List<String> arithCmds = Arrays.asList("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not");
    private String mCurrCommand = null;
    private Scanner mScanner = null;
    private String mArg0 = null;
    private String mArg1 = null;
    private String mArg2 = null;
    private String mCmdType = null;

    // open scanner to iterate over each line of current vm file
    public Parser(File file) {
        try {
            mScanner = new Scanner(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    // more commands to parse?
    public boolean hasMoreCommands() {
        boolean hasMore = false;
        if (mScanner.hasNextLine()) {
            hasMore = true;
        }
        return hasMore;
    }

    // reads next cmd from input and makes it curr command;
    // only called if hasMoreCommands() is true
    // initially there's no current command
    public void advance() {
        String strLine = mScanner.nextLine();
        while (strLine.equals("") || hasComments(strLine)) {
            if (hasComments(strLine)) {
                strLine = removeComments(strLine);
            }
            if (strLine.trim().equals("")) {
                strLine = mScanner.nextLine();
            }
        }

        // take curr command
        // split on spaces into an array of strings
        // grab args from this list based on size
        mCurrCommand = strLine;
        String[] cmds = mCurrCommand.split(" ");
        mArg0 = cmds[0];
        if (cmds.length > 1) {
            mArg1 = cmds[1];
        }
        if (cmds.length > 2) {
            mArg2 = cmds[2];
        }

        // determine command type based on args
        // ex) pop -> C_POP
        if (mArg0.equals("push")) {
            mCmdType = "C_PUSH";
        } else if (mArg0.equals("pop")) {
            mCmdType = "C_POP";
        } else if (arithCmds.contains(mArg0)) {
            // any of the arith commands (add, sub, neg, eq, etc.)
            mCmdType = "C_ARITHMETIC";
        }
        else if (mArg0.equals("label")) {
            mCmdType = "C_LABEL";
        }
        else if (mArg0.equals("goto")) {
            mCmdType = "C_GOTO";
        }
        else if (mArg0.equals("if-goto")) {
            mCmdType = "C_IF";
        }
        else if (mArg0.equals("function")) {
            mCmdType = "C_FUNCTION";
        }
        else if (mArg0.equals("return")) {
            mCmdType = "C_RETURN";
        }
        else if (mArg0.equals("call")) {
            mCmdType = "C_CALL";
        }
    }

    // test if curr line has comments
    private boolean hasComments(String strLine) {
        boolean bHasComments = false;
        if (strLine.contains("//")) {
            bHasComments = true;
        }
        return bHasComments;
    }

    // rm comments from line
    private String removeComments(String strLine) {
        String strNoComments = strLine;
        if (hasComments(strLine)) {
            int offSet = strLine.indexOf("//");
            strNoComments = strLine.substring(0, offSet).trim();

        }
        return strNoComments;
    }

    // returns type for curr vm command,
    // C_ARITHMETIC returned for all arithmetic commands
    public String commandType() {
        return mCmdType;
    }

    // return first arg of current command,
    // in case of C_ARITHMETIC the command itself (add, sub, etc)
    // not be called if current command is C_RETURN
    public String arg1() {
        String strArg1 = null;
        if (mCmdType.equals("C_ARITHMETIC")) {
            strArg1 = mArg0;
        } else if (!(mCmdType.equals("C_RETURN"))) {
            // anything other than c_return
            // grab args[1] instead of args[0]
            strArg1 = mArg1;
        }
        return strArg1;
    }

    // returns 2nd arg of curr command
    // only called if curr command is C_PUSH, C_POP, C_FUNCTION, or C_CALL
    public int arg2() {
        int nArg2 = 0;
        if (mCmdType.equals("C_PUSH") || mCmdType.equals("C_POP") || mCmdType.equals("C_FUNCTION") || mCmdType.equals("C_CALL")) {
            nArg2 = Integer.parseInt(mArg2);
        }
        return nArg2;
    }

}