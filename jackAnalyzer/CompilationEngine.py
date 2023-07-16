#!/usr/bin/python3
from JackTokenizer import Token, Tokenizer
from textwrap import indent


class Peekable:
    def __init__(self, iterable):
        self.iterator = iter(iterable)
        self.cached = None

    def peek(self):
        if self.cached is None:
            self.cached = next(self.iterator)
            return self.cached
        else:
            return self.cached

    def __next__(self):
        if self.cached is None:
            return next(self.iterator)
        else:
            temp = self.cached
            self.cached = None
            return temp


class Compiler:
    def __init__(self, tokenizer):
        self.statementHandlers = {
            "let": self.compileLet,
            "if": self.compileIf,
            "while": self.compileWhile,
            "do": self.compileDo,
            "return": self.compileReturn
        }
        self.outURI = tokenizer.getOutURI()
        self.tokenIterator = Peekable(tokenizer.getTokens())
        compiled = self.compileClass()
        with open(self.outURI, "w+") as outFile:
            outFile.write(compiled)

    def eat(self, tokenType=None, tokenValue=None):
        token: Token = next(self.tokenIterator)
        if tokenType is not None:
            assert tokenType is token.tokenType, f"TokenType does not match {token.tokenType} is not {tokenType} for {token}"
        if tokenValue is not None:
            assert tokenValue == token.stringVal
        return token

    def compileClass(self):
        'Compiles 1 class definition'
        # grammar ="class" "identifier" "{" classVarDec* subroutineDec* "}"
        output = (f"{self.eat(Token.KEYWORD)}\n"  # keyword class
                  f"{self.eat(Token.IDENTIFIER)}\n"  # identifier classname
                  f"{self.eat(Token.SYMBOL)}\n"  # symbol {
                  )
        # classVarDec's
        while (self.tokenIterator.peek().stringVal.lower() in (
        "static", "field") and self.tokenIterator.peek().tokenType is Token.KEYWORD):
            output += f"{self.compileClassVarDec()}\n"
        # subroutine's
        while (self.tokenIterator.peek().stringVal.lower() in (
        "constructor", "function", "method") and self.tokenIterator.peek().tokenType is Token.KEYWORD):
            output += f"{self.compileSubroutine()}\n"

        output += f"{self.eat(Token.SYMBOL)}\n"  # symbol }

        return f"<class>\n{indent(output, '  ')}</class>\n"

    def compileClassVarDec(self):
        'Compiles any number of class variable declarations, starting with static or field and ending in a semicolon'
        # grammar = ('static'|'field') type varName (, varName) ;
        output = (f"{self.eat(Token.KEYWORD)}\n"  # ('static'|'field')
                  f"{self.eat()}\n"  # type
                  f"{self.eat(Token.IDENTIFIER)}\n"  # varName
                  )

        while (self.tokenIterator.peek().stringVal.lower() in (
        ",") and self.tokenIterator.peek().tokenType is Token.SYMBOL):
            output += f"{self.eat(Token.SYMBOL)}\n"  # ,
            output += f"{self.eat(Token.IDENTIFIER)}\n"  # varName

        output += f"{self.eat(Token.SYMBOL)}\n"  # ;
        return f"""<classVarDec>\n{indent(output, '  ')}</classVarDec>"""

    def compileSubroutine(self):
        # grammar = ('constructor'|'function'|'method') ('void'|type) subroutineName '(' parameterList ')' subRoutineBody

        output = f"{self.eat(Token.KEYWORD)}\n"  # ('constructor'|'function'|'method')
        output += f"{self.eat()}\n"  # ('void'|type)
        output += f"{self.eat(Token.IDENTIFIER)}\n"  # subroutineName
        output += f"{self.eat(Token.SYMBOL)}\n"  # '('
        # parameterList
        output += f"{self.compileParameterList()}\n"
        output += f"{self.eat(Token.SYMBOL)}\n"  # ')'
        output += f"{self.compileSubroutineBody()}\n"
        return f"""<subroutineDec>\n{indent(output, '  ')}</subroutineDec>"""

    def compileSubroutineBody(self):
        # grammar = '{' varDec* statements '}'
        output = f"{self.eat(Token.SYMBOL)}\n"  # '{'
        # subroutineBody
        while (self.tokenIterator.peek().stringVal.lower() in (
        "var") and self.tokenIterator.peek().tokenType is Token.KEYWORD):  # varDec*
            output += f"{self.compileVarDec()}\n"

        output += f"{self.compileStatements()}\n"  # statements
        output += f"{self.eat(Token.SYMBOL)}\n"  # '}'
        return f"""<subroutineBody>\n{indent(output, '  ')}</subroutineBody>"""

    def compileStatements(self):
        output = ""
        while (
                self.tokenIterator.peek().stringVal.lower() in self.statementHandlers and self.tokenIterator.peek().tokenType is Token.KEYWORD):
            output += f"{self.statementHandlers[self.tokenIterator.peek().stringVal.lower()]()}\n"
        return f"""<statements>\n{indent(output, '  ')}</statements>"""

    def compileParameterList(self):
        # grammar = ((type Varname) (',' type varName)*)?
        output = ""
        parameters = 0
        while (not self.tokenIterator.peek().stringVal.lower() == ")"):
            if (parameters > 0):
                output += f"{self.eat(Token.SYMBOL)}\n"  # ,
            output += f"{self.eat()}\n"  # type
            output += f"{self.eat(Token.IDENTIFIER)}\n"  # varName
            parameters += 1
        return f"""<parameterList>\n{indent(output, '  ')}</parameterList>"""

    def compileVarDec(self):
        # grammar = 'var' type varName (, varName) ;
        output = (f"{self.eat(Token.KEYWORD)}\n"  # 'var'
                  f"{self.eat()}\n"  # type
                  f"{self.eat(Token.IDENTIFIER)}\n"  # varName
                  )

        while (self.tokenIterator.peek().stringVal.lower() in (
        ",") and self.tokenIterator.peek().tokenType is Token.SYMBOL):
            output += f"{self.eat(Token.SYMBOL)}\n"  # ,
            output += f"{self.eat(Token.IDENTIFIER)}\n"  # varName

        output += f"{self.eat(Token.SYMBOL)}\n"  # ;

        return f"""<varDec>\n{indent(output, '  ')}</varDec>"""

    def compileDo(self):
        # grammar = 'do' subroutineCall ';'
        output = f"{self.eat(Token.KEYWORD)}\n"  # 'do'
        output += f"{self.compilesubroutineCall()}"  # subroutineCall
        output += f"{self.eat(Token.SYMBOL)}\n"  # ';'

        return f"""<doStatement>\n{indent(output, '  ')}</doStatement>"""

    def compilesubroutineCall(self):
        # grammar = subroutineName'('expressionList')' | (className|varName)'.'subroutineName'('expressionList')'
        output = f"{self.eat(Token.IDENTIFIER)}\n"  # subroutineName className or varName

        if self.tokenIterator.peek().stringVal.lower() in (".") and self.tokenIterator.peek().tokenType is Token.SYMBOL:
            output += f"{self.eat(Token.SYMBOL)}\n"  # '.'
            output += f"{self.eat(Token.IDENTIFIER)}\n"  # subroutineName

        output += f"{self.eat(Token.SYMBOL)}\n"  # '('
        output += f"{self.compileExpressionList()}\n"  # expressionList
        output += f"{self.eat(Token.SYMBOL)}\n"  # ')'
        return output

    def compileLet(self):
        # grammar = 'let' varName ('['expression']')? '=' expression ';'
        output = f"{self.eat(Token.KEYWORD)}\n"  # 'let'
        output += f"{self.eat(Token.IDENTIFIER)}\n"  # varName

        if self.tokenIterator.peek().stringVal.lower() in ("[") and self.tokenIterator.peek().tokenType is Token.SYMBOL:
            output += f"{self.eat(Token.SYMBOL)}\n"  # '['
            output += f"{self.compileExpression()}\n"  # expression
            output += f"{self.eat(Token.SYMBOL)}\n"  # ']'

        output += f"{self.eat(Token.SYMBOL)}\n"  # '='
        output += f"{self.compileExpression()}\n"  # expression
        output += f"{self.eat(Token.SYMBOL)}\n"  # ';'
        return f"""<letStatement>\n{indent(output, '  ')}</letStatement>"""

    def compileWhile(self):
        output = f"{self.eat(Token.KEYWORD)}\n"  # 'while'
        output += f"{self.eat(Token.SYMBOL)}\n"  # '('
        output += f"{self.compileExpression()}\n"  # expression
        output += f"{self.eat(Token.SYMBOL)}\n"  # ')'
        output += f"{self.eat(Token.SYMBOL)}\n"  # '{'
        output += f"{self.compileStatements()}\n"  # statements
        output += f"{self.eat(Token.SYMBOL)}\n"  # '}'
        return f"""<whileStatement>\n{indent(output, '  ')}</whileStatement>"""

    def compileReturn(self):
        # grammar = 'return' expression? ';'
        output = f"{self.eat(Token.KEYWORD)}\n"  # 'return'

        if not (self.tokenIterator.peek().stringVal.lower() in (
        ";") and self.tokenIterator.peek().tokenType is Token.SYMBOL):
            output += f"{self.compileExpression()}\n"  # expression

        output += f"{self.eat(Token.SYMBOL)}\n"  # ;

        return f"<returnStatement>\n{indent(output, '  ')}</returnStatement>"

    def compileIf(self):
        output = f"{self.eat(Token.KEYWORD)}\n"  # 'if'
        output += f"{self.eat(Token.SYMBOL)}\n"  # '('
        output += f"{self.compileExpression()}\n"  # expression
        output += f"{self.eat(Token.SYMBOL)}\n"  # ')'
        output += f"{self.eat(Token.SYMBOL)}\n"  # '{'
        output += f"{self.compileStatements()}\n"  # statements
        output += f"{self.eat(Token.SYMBOL)}\n"  # '}'

        if self.tokenIterator.peek().stringVal.lower() in (
        "else") and self.tokenIterator.peek().tokenType is Token.KEYWORD:
            output += f"{self.eat(Token.KEYWORD)}\n"  # 'else'
            output += f"{self.eat(Token.SYMBOL)}\n"  # '{'
            output += f"{self.compileStatements()}\n"  # statements
            output += f"{self.eat(Token.SYMBOL)}\n"  # '}'

        return f"""<ifStatement>\n{indent(output, '  ')}</ifStatement>"""

    def compileExpression(self):
        # grammar = term (op term)*
        output = f"{self.compileTerm()}\n"  # term

        while self.tokenIterator.peek().stringVal.lower() in (
        "+", "-", "*", "/", "&", "|", "<", ">", "=") and self.tokenIterator.peek().tokenType is Token.SYMBOL:
            output += f"{self.eat(Token.SYMBOL)}\n"  # op
            output += f"{self.compileTerm()}\n"  # term

        return f"<expression>\n{indent(output, '  ')}</expression>"

    def compileTerm(self):
        # grammar = integerConstant|stringconstant|keywordConstant|varName|varName'['expression']'|subRoutineCall|'('expression')'|unaryOp term
        peek = self.tokenIterator.peek()
        peekVal = peek.stringVal.lower()
        output = ""
        if peekVal in ("~", "-") and peek.tokenType is Token.SYMBOL:  # unaryOp term
            output += f"{self.eat(Token.SYMBOL)}\n"  # unaryOp ~ or -
            output += f"{self.compileTerm()}\n"  # term
        elif peekVal in ("(") and peek.tokenType is Token.SYMBOL:
            output += f"{self.eat(Token.SYMBOL)}\n"  # '('
            output += f"{self.compileExpression()}\n"  # expression
            output += f"{self.eat(Token.SYMBOL)}\n"  # ')'
        else:
            output += f"{self.eat()}\n"  # integerConstant, stringConstant, keywordConstant, varName, or subroutineCall
            peek = self.tokenIterator.peek()
            peekVal = peek.stringVal.lower()
            if peekVal in (".", "(") and peek.tokenType is Token.SYMBOL:  # subroutineCall
                if peekVal in ("."):
                    output += f"{self.eat(Token.SYMBOL)}\n"  # '.'
                    output += f"{self.eat(Token.IDENTIFIER)}\n"  # subroutineName

                output += f"{self.eat(Token.SYMBOL)}\n"  # '('
                output += f"{self.compileExpressionList()}\n"  # expression
                output += f"{self.eat(Token.SYMBOL)}\n"  # ')'
            elif peekVal in ("[") and peek.tokenType is Token.SYMBOL:  # array access
                output += f"{self.eat(Token.SYMBOL)}\n"  # '['
                output += f"{self.compileExpression()}\n"  # expression
                output += f"{self.eat(Token.SYMBOL)}\n"  # ']'

        return f"<term>\n{indent(output, '  ')}</term>"

    def compileExpressionList(self):
        # grammar = (expression (',' expression)*)?
        output = ""
        parameters = 0
        while self.tokenIterator.peek().stringVal.lower() not in (
        ")") or self.tokenIterator.peek().tokenType == "stringConstant":
            if (parameters > 0):
                output += f"{self.eat(Token.SYMBOL)}\n"  # ,
            output += f"{self.compileExpression()}\n"  # expression
            parameters += 1

        return f"<expressionList>\n{indent(output, '  ')}</expressionList>"