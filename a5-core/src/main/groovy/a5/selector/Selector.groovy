package a5.selector

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Selector {

    static Map Selector = [
            "api": "api",
            "identifier": "identifier",
            "filter": "filter",
            "format": "format",
            "prefix": "prefix",
            "quality": "quality",
            "region": "region",
            "repository": "repository",
            "rotation": "rotation",
            "section": "section",
            "size": "size",
            "\$count": "count",
            "\$expand": "expand",
            "\$filter": "filter",
            "\$orderby": "orderby",
            "\$search": "search",
            "\$select": "select",
            "\$skip": "skip",
            "\$top": "top",
            "autocomplete": "autocomplete",
            "drill": "drill",
            "facets": "facets",
            "fields": "fields",
            "highlight": "highlight",
            "pretty": "pretty"
    ]

    ArrayList<String> selectors

    // URL Tokens

    String api

    String format

    String identifier

    String prefix

    String quality

    String repository

    String region

    String rotation

    String section

    String size

    // OData Request Parameters

    Boolean count

    ArrayList<String> expand

    ArrayList<String> filter

    Map orderby

    String search

    ArrayList<String> select

    Integer skip

    Integer top

    // Custom Request Parameters

    Boolean autocomplete

    ArrayList<String> drill

    Map facets

    ArrayList<String> fields

    Boolean highlight

    Boolean pretty

    protected Logger log = LoggerFactory.getLogger(getClass())


    Selector(Map params, ArrayList<String> selectors) {
        this.selectors = selectors
        Map selectorClass = [
                "api": SelectorString.class,
                "filter": SelectorFilter.class,
                "format": SelectorString.class,
                "identifier": SelectorString.class,
                "prefix": SelectorString.class,
                "quality": SelectorString.class,
                "region": SelectorString.class,
                "repository": SelectorString.class,
                "rotation": SelectorString.class,
                "section": SelectorString.class,
                "size": SelectorString.class,
                "\$count": SelectorBool.class,
                "\$expand": SelectorCommaStringList.class,
                "\$filter": SelectorFilter.class,
                "\$orderby": SelectorOrderby.class,
                "\$search": SelectorSearch.class,
                "\$select": SelectorCommaStringList.class,
                "\$skip": SelectorSkip.class,
                "\$top": SelectorTop.class,
                "autocomplete": SelectorBool.class,
                "drill": SelectorFilter.class,
                "facets": SelectorFacets.class,
                "fields": SelectorCommaStringList.class,
                "highlight": SelectorBool.class,
                "pretty": SelectorBool.class
        ]
        selectors.each {
            //            this[(Selector[it])] = selectorClass[(it)].parse((URLDecoder.decode(params[(it)] as String?:"","UTF-16")))
            this[(Selector[it])] = selectorClass[(it)].parse(params[(it)])
        }
        this.identifier = this.prefix == null ? this.identifier : "${this.prefix}/${this.identifier}"
    }

    Map asMap() {
        Map map = [:]
        this.selectors.each {
            map[(it)] = this[(Selector[it])]
        }
        map
    }
}