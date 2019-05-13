package a5.elastic

import a5.mappable.Mappable

class ElasticQ extends Mappable {

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

    Map highlight

    Map aggregations

    Integer from

    Integer size

    ArrayList<Map> sort

    Map post_filter = [
            "bool": [
                    "must": [],
                    "should": [],
                    "must_not": [],
                    "minimum_should_match" : 1
            ]
    ]

    Map autocomplete

    ArrayList<String> _source

    Map filter

    ElasticQ queryString(String queryString) {
        if (queryString == null) return this
        ElasticQQuery.setQueryString(this.query, queryString)
        this
    }

    ElasticQ nested(String path) {
        if (path == null) return this
        Map innerOptions = [:]
        if (this.from != null) {
            innerOptions.from = this.from
            this.from = null
        }
        if (this.size != null) {
            innerOptions.size = this.size
            this.size = null
        }
        Map cloned = this.query.clone()
        Map nested = [
                "bool": [
                        "must": [[
                                "nested": [
                                        "path": path,
                                        "query": cloned,
                                        "inner_hits" : innerOptions
                                ]

                        ]]
                ]
        ]
        this.query = nested
        this
    }

    ElasticQ fields(ArrayList<String> fields) {
        if (fields == null) return this
        ElasticQQuery.setQueryStringFields(this.query, fields)
        this
    }

    ElasticQ filter(ArrayList<String> filter) {
        if (filter == null) return this
        ElasticQQuery.setFilter(this.query, filter)
        this
    }

    ElasticQ from(Integer from) {
        if (from == null) return this
        this.from = ElasticQFrom.parse(from)
        this
    }

    ElasticQ size(Integer size) {
        if (size == null) return this
        this.size = ElasticQSize.parse(size)
        this
    }

    ElasticQ sort(Map sort) {
        if (sort == null) return this
        this.sort = ElasticQSort.parse(sort)
        this
    }

    ElasticQ source(ArrayList<String> source) {
        if (source == null) return this
        this._source = ElasticQSource.parse(source)
        this
    }

    ElasticQ highlight(Boolean highlight, Boolean requireFieldMatch) {
        if (!highlight) return this
        this.highlight = ElasticQHighlight.parse(requireFieldMatch)
        this
    }

    ElasticQ facets(Map facets) {
        if (facets == null) return this
        this.aggregations = ElasticQFacets.parse(facets)
        this
    }

    ElasticQ postFilter(ArrayList<String> postFilter) {
        if (postFilter == null) return this
        ElasticQQuery.setFilter(this.post_filter, postFilter)
        this
    }

    ElasticQ autocomplete(Boolean autocomplete, String prefix) {
        if (!autocomplete) return this
        this.size = 0
        this.query = ElasticQAutocomplete.query(prefix)
        this.aggregations = ElasticQAutocomplete.aggregations(prefix)
        this
    }
}