package a5.elastic

class ElasticResult {

    Map json

    Integer total

    Map firstHit

    String firstHitId

    Map firstInnerHit

    ArrayList<Map> hits

    Map innerHits

    ArrayList<Map> sort

    ArrayList<Map> highlight

    Map aggregations

    ElasticResult(Map json) {
        if(json) {
            this.json = json
            if (hasInnerHits(json)) {
                ArrayList<Map> inner = this.json.hits?.hits?.inner_hits
                this.total = 0
                this.firstInnerHit = [:]
                this.innerHits = [:]
                inner[0]?.each { k, v ->
                    this.firstInnerHit[k] = v.hits?.hits?.getAt(0)?._source
                }
                inner.each { innerHit ->
                    innerHit.each { k, v ->
                        if (this.innerHits[k] == null) this.innerHits[k] = []
                        this.total += v.hits.total
                        v.hits?.hits?.each { hit ->
                            this.innerHits[k] << hit._source
                        }
                    }
                }
            } else {
                this.total = this.json.hits?.total
                this.firstHit = this.json.hits?.hits?.getAt(0)?._source
                this.firstHitId = this.json.hits?.hits?.getAt(0)?._id
                this.hits = this.json.hits?.hits?._source
                this.sort = this.json.hits?.hits?.sort
                this.highlight = this.json.hits?.hits?.highlight
                this.aggregations = this.json.aggregations
            }
        }
        else
        {
            this.json= [:]
            this.firstHit=[:]
            this.firstHitId=''
            this.firstInnerHit=[:]
            this.hits=[]
            this.innerHits=[:]
            this.sort=[]
            this.highlight=[]
            this.aggregations=[:]

        }
    }

    private static boolean hasInnerHits(Map json) {
        json?.hits?.hits?.inner_hits?.getAt(0) != null
    }
}