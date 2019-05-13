package a5.selector

enum State {
    IN_VALUE,
    OUT_VALUE
}

class Parser {

    String input

    State state = State.OUT_VALUE

    String token = ""

    String quote

    int size

    int pos = -1

    ArrayList<String> filter = []

    Parser(String input) {
        this.input = input
        this.size = input.size()
    }

    String next() {
        if (pos + 1 >= size) return null
        String next = input[pos]
        pos++
        next
    }

    String peek() {
        input[pos]
    }

    void eat() {
        token += peek()
    }

    void flush() {
        if (token == "") return
        filter << token
        token = ""
    }

    void space() {
        state == State.IN_VALUE ? eat() : flush()
    }

    void singleQuote() {
        if (state == State.IN_VALUE && quote == "\"") {
            state = State.OUT_VALUE
            quote = ""
        } else if (state == State.OUT_VALUE) {
            state = State.IN_VALUE
            quote = "\""
        } else {
            eat()
        }
    }

    void doubleQuote() {
        if (state == State.IN_VALUE && quote == "'") {
            state = State.OUT_VALUE
            quote = ""
        } else if (state == State.OUT_VALUE) {
            state = State.IN_VALUE
            quote = "'"
        } else {
            eat()
        }
    }

    void parenthesis() {
        flush()
        eat()
        flush()
    }
}

class SelectorFilter {

    static ArrayList<String> parse(String paramValue) {
        if (paramValue == null || paramValue == "") return null
        Parser parser = new Parser(paramValue)
        while(parser.next() != null) {
            switch(parser.peek()) {
                case " ":
                    parser.space()
                    break
                case "\"":
                    parser.doubleQuote()
                    break
                case "'":
                    parser.singleQuote()
                    break
                case "(":
                case ")":
                    parser.parenthesis()
                    break
                default:
                    parser.eat()
                    break
            }
        }
        parser.flush()
        parser.filter
    }
}