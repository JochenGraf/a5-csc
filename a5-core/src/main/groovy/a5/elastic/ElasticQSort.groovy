package a5.elastic

class ElasticQSort {

    static ArrayList<Map> parse(Map sort) {
        ArrayList<Map> sorts = []
        sort.each { k, v ->
            sorts << [(k + ".raw"): v]
        }
        sorts
    }
}