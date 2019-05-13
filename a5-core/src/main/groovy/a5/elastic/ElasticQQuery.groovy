package a5.elastic

class Parser {

    ArrayList<String> filter

    int size

    int pos = -1

    Stack<Map> boolStack = new Stack<Map>()

    ArrayList<Map> actual

    Parser(ArrayList<String> filter, Map bool) {
        this.filter = filter
        this.size = filter.size()
        this.boolStack.push(bool)
        this.should()
    }

    String next() {
        if (pos + 1 >= size) return null
        String next = filter[pos]
        pos++
        next
    }

    String left() {
        filter[pos - 1]
    }

    String right() {
        filter[pos + 1]
    }

    String peek() {
        filter[pos]
    }

    Map hasParent(String indexType, Map query) {
        query: [
                has_parent: [
                        type: indexType,
                        query: query
                ]
        ]
    }

    void must() {
        actual = boolStack.peek().must
    }

    void should() {
        actual = boolStack.peek().should
    }

    void mustNot() {
        actual = boolStack.peek().must_not
    }

    void term() {
        if (left().contains("/")) {
            def tok = left().split("/")
            actual << hasParent(tok[0], ["term": [(tok[1] + ".raw"): right()]])
        } else {
            if (right() == "null") {
                actual << ["not": ["exists": ["field": left()]]]
            } else {
                actual << ["term": [(left() + ".raw"): right()]]
            }
        }
    }

    void not() {
        if (left().contains("/")) {
            def tok = left().split("/")
            actual << hasParent(tok[0], ["not": ["term": [(tok[1] + ".raw"): right()]]])
        } else {
            if (right() == "null") {
                actual << ["exists": ["field": left()]]
            } else {
                actual << ["not": ["term": [(left() + ".raw"): right()]]]
            }
        }
    }

    void gt() {
        if (left().contains("/")) {
            def tok = left().split("/")
            actual << hasParent(tok[0], ["range": [(tok[1] + ".raw"): [("gt"): right()]]])
        } else {
            actual << ["range": [(left() + ".raw"): [("gt"): right()]]]
        }
    }

    void gte() {
        if (left().contains("/")) {
            def tok = left().split("/")
            actual << hasParent(tok[0], ["range": [(tok[1] + ".raw"): [("gte"): right()]]])
        } else {
            actual << ["range": [(left() + ".raw"): [("gte"): right()]]]
        }
    }

    void lt() {
        if (left().contains("/")) {
            def tok = left().split("/")
            actual << hasParent(tok[0], ["range": [(tok[1] + ".raw"): [("lt"): right()]]])
        } else {
            actual << ["range": [(left() + ".raw"): [("lt"): right()]]]
        }
    }

    void lte() {
        if (left().contains("/")) {
            def tok = left().split("/")
            actual << hasParent(tok[0], ["range": [(tok[1] + ".raw"): [("lte"): right()]]])
        } else {
            actual << ["range": [(left() + ".raw"): [("lte"): right()]]]
        }
    }

    void nestedOpen() {
        Map query = [
                "bool": [
                        "must": [
                                ["query_string": ["query": "*"]]
                        ],
                        "should": [],
                        "must_not": [],
                        "minimum_should_match" : 1
                ]
        ]
        actual << query
        boolStack.push(query.bool)
        should()
    }

    void nestedClose() {
        boolStack.pop()
        should()
    }
}

class ElasticQQuery {

    static void setQueryString(Map query, String queryString) {
        query.bool.must[0].query_string.query = queryString
    }

    static void setQueryStringFields(Map query, ArrayList<String> fields) {
        query.bool.must[0].query_string.fields = fields
    }

    static void setFilter(Map query, ArrayList<String> filter) {
        if (filter == null || filter.size() == 0) return
        Parser parser = new Parser(filter, query.bool)
        while(parser.next() != null) {
            switch(parser.peek()) {
                case "(":
                    parser.nestedOpen()
                    break
                case ")":
                    parser.nestedClose()
                    break
                case "or":
                    parser.should()
                    break;
                case "and":
                    parser.must()
                    break
                case "not":
                    parser.mustNot()
                    break
                case "eq":
                    parser.term()
                    break
                case "ne":
                    parser.not()
                    break
                case "gt":
                    parser.gt()
                    break
                case "ge":
                    parser.gte()
                    break
                case "lt":
                    parser.lt()
                    break
                case "le":
                    parser.lte()
                    break
                default:
                    break
            }
        }
    }
}