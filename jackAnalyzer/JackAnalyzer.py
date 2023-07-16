# !/usr/bin/python3
import os
from sys import argv
from JackTokenizer import Tokenizer
from CompilationEngine import Compiler

if __name__ == "__main__":
    ' Usage: python3 JackAnalyzer.py {jackFile or repo with jack files} {optional t for tokens}'
    # Get 1st command line argument
    path = os.path.abspath(argv[1])
    emitTokens = False

    # We want to make a Tokenizer which yields all tokens for each input file
    if os.path.isdir(path):
        tokenizers = [Tokenizer(os.path.join(path, filename)) for filename in os.listdir(path) if
                      filename.endswith(".jack")]
    elif os.path.isfile(path):
        tokenizers = [Tokenizer(argv[1])]
    else:
        raise IOError("not a file or directory")

    for tokenizer in tokenizers:
        Compiler(tokenizer)
        if emitTokens:
            tokenizer.emitTokens()
