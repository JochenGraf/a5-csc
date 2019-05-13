package a5.api

class ApiMediaInterceptor {

    private void validate() {
        /*
        if (!(request.method == "GET" || request.method == "HEAD"))
            throw new MethodNotAllowedException(request.method)
        else if (params.version && params.version != "latest")
            throw new VersionParameterNotSupportedException(params.version)
        else if (params.section && !(params.section =~ /^(full|\d+,?|\d+,\d+)$/))
            throw new InvalidSectionParameterException(params.section)
        else if (params.region && !(params.region =~ /^(full|\d+,\d+,\d+,\d+)$/))
            throw new InvalidRegionParameterException(params.region)
        else if (params.size && !(params.size =~ /^(full|\d+,\d+|,\d+|\d+,)$/))
            throw new InvalidSizeParameterException(params.size)
        else if (params.rotation && !(params.rotation =~ /^(0|90|180|270)$/))
            throw new InvalidRotationParameterException(params.rotation)
        else if (params.filter && !(params.filter =~ /^(none|gray|thumbnail|waveform|spectrum)$/))
            throw new InvalidFilterParameterException(params.filter)
        else if (params.quality && !(params.quality =~ /^(default|high|medium|low)$/))
            throw new InvalidQualityParameterException(params.quality)
        */
    }

    boolean before() {
        validate()
        header("Access-Control-Allow-Origin", "*" )
        header("Access-Control-Allow-Methods", "GET")
        true
    }

    boolean after() { true }
}