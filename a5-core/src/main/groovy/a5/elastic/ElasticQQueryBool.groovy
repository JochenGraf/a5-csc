package a5.elastic

class ElasticQQueryBool {

    Map query = [
            "bool": [
                "must": [],
                "should": [],
                "must_not": [],
                "minimum_should_match" : 1
            ]
    ]

    private Map comparator(int start, ArrayList<String> filter) {
        String left = filter[start + 1] + ".raw"
        String comparator = filter[start + 2]
        String right = filter[start + 3]
        switch(comparator) {
            case "eq":
                ["term": [(left): right]]
                break
            case "ne":
                ["not": ["term": [(left): right]]]
                break
            case "gt":
                ["range": [(left): [("gt"): right]]]
                break
            case "ge":
                ["range": [(left): [("gte"): right]]]
                break
            case "lt":
                ["range": [(left): [("lt"): right]]]
                break
            case "le":
                ["range": [(left): [("lte"): right]]]
                break
            default:
                ["range": [(left): [(comparator): right]]]
                break
        }
    }

    ElasticQQueryBool(ArrayList<String> filter) {
        for (int i = 0; i < filter.size(); i += 4) {
            switch(filter[i]) {
                case "and":
                    this.query.bool.must << comparator(i, filter)
                    break
                case "or":
                    this.query.bool.should << comparator(i, filter)
                    break
                case "not":
                    this.query.bool.must_not << comparator(i, filter)
                    break
                default:
                    //TODO: throw exception
                    break
            }
        }
    }
}