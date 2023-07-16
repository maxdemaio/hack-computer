import re


class Token:
    # Terminal lexical elements (tokens)
    SYMBOL = "symbol"
    STRCONST = "stringConstant"
    INTCONST = "integerConstant"
    KEYWORD = "keyword"
    IDENTIFIER = "identifier"

    def __init__(self, token):
        self.stringVal = token
        symbols = set(("{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~"))
        keywords = set(("CLASS", "METHOD", "FUNCTION", "CONSTRUCTOR", "INT", "BOOLEAN", "CHAR", "VOID", "VAR", "STATIC",
                        "FIELD", "LET", "DO", "IF", "ELSE", "WHILE", "RETURN", "TRUE", "FALSE", "NULL", "THIS"))
        if token in symbols:
            self.tokenType = Token.SYMBOL
            self.symbol = token
        elif token.startswith('"'):
            self.tokenType = Token.STRCONST
            # Take only the value inside the string
            self.stringVal = token[1:-1]
        elif token.isdigit():
            self.tokenType = Token.INTCONST
            self.intVal = int(token)
        elif token.upper() in keywords:
            self.tokenType = Token.KEYWORD
        elif bool(re.match(r"[a-z][a-z0-9]*", token, flags=re.I)):
            # (pattern, string to be searched, ignore case flag)
            # Sequence of letters, digits, and underscores not starting with a digit
            self.tokenType = Token.IDENTIFIER
        else:
            raise Exception(f'Token unrecognised "{token}"')

    def __str__(self) -> str:
        return f"<{self.tokenType}> {self.xmlEscape(self.stringVal)} </{self.tokenType}>"

    def xmlEscape(self, string):
        return string.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace('"', "&quot;")


class Tokenizer:
    def __init__(self, fileURI: str):
        self.fileURI = fileURI

    def getTokens(self):
        # Creates generator for processed commands
        with open(self.fileURI, 'r') as jackFile:
            contents = self.clean(jackFile.read())

            # Loop over the cleaned contents
            for line in contents.splitlines():
                if line:
                    if '"' in line:
                        lineParts = re.split(r'("[^"]*")', line)
                        for part in lineParts:
                            if part.startswith('"'):
                                yield Token(part)
                            else:
                                for token in self.splitParts(part):
                                    yield Token(token)
                    else:
                        for token in self.splitParts(line):
                            yield Token(token)

    def clean(self, text: str) -> str:
        # Removes line comments and leading and trailing whitespace from input file
        replacer = re.compile(r"\/\*[\s\S]*?\*\/|([^:]|^)\/\/.*$", flags=re.M)
        cleaned = replacer.sub("", text).replace("\\\n", "")
        return cleaned

    def splitParts(self, line):
        # Breaks non-strings expressions into tokens
        line = line.strip()
        parts = list(filter(None, re.findall(r"([\{\}()[\].,;+\-\*/&\|<>=~ ]|\w+)", line)))
        if len(parts) == 1:
            yield line
        else:
            for part in parts:
                for subpart in self.splitParts(part):
                    yield subpart

    def emitTokens(self):
        outURI = self.getOutURIT()
        myGenerator = self.getTokens()
        with open(outURI, "w+") as outFile:
            outFile.write("<tokens>\n")
            for token in myGenerator:
                outFile.write(token.__str__())
                outFile.write("\n")
            outFile.write("</tokens>")

    def getOutURI(self) -> str:
        return self.fileURI.partition(".jack")[0] + "Parsed" + ".xml"

    def getOutURIT(self) -> str:
        return self.fileURI.partition(".jack")[0] + "MyTokens" + ".xml"
