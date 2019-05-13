package a5.elastic

import a5.config.ConfigIndex

class ElasticQQueryString {

    static Map matchAll() {
        ["query_string": ["query": "*"]]
    }

    static void setQueryString(Map query, String queryString) {
        query.query_string.query = queryString
    }

    void setFields(ArrayList<String> fields) {
        this.query.query_string.fields = []
        this.query.query_string["fields"] = fields
    }
}