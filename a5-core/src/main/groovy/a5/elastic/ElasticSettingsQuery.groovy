package a5.elastic

class ElasticSettingsQuery extends ElasticSettings {

    static final String FILTER_AUTOCOMPLETE = "autocomplete_filter"

    static final String FILTER_FULLTEXT = "fulltext_filter"

    static final String ANALYZER_AUTOCOMPLETE = "autocomplete_analyzer"

    static final String ANALYZER_FULLTEXT = "fulltext_analyzer"

    static Map AutocompleteFilter = [
            "type": "shingle",
            "min_shingle_size": 2,
            "max_shingle_size": 3,
            "filler_token": "",
            "output_unigrams": true
    ]

    static Map AutocompleteAnalyzer = [
            "type": "custom",
            "tokenizer": "standard",
            "filter": ["standard", "lowercase", FILTER_AUTOCOMPLETE]
    ]

    static Map FulltextFilter = [
            "type": "stop",
            "stopwords": ["_english_", "_german_"]
    ]

    static Map FulltextAnalyzer = [
            "type": "custom",
            "tokenizer": "standard",
            "filter": [FILTER_FULLTEXT, "standard", "lowercase"]
    ]

    ElasticSettingsQuery() {
        this.filters[(FILTER_AUTOCOMPLETE)] = AutocompleteFilter
        this.filters[(FILTER_FULLTEXT)] = FulltextFilter
        this.analyzers[(ANALYZER_AUTOCOMPLETE)] = AutocompleteAnalyzer
        this.analyzers[(ANALYZER_FULLTEXT)] = FulltextAnalyzer
    }
}