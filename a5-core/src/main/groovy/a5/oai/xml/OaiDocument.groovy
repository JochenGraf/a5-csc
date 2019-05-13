package a5.oai.xml

import a5.oai.OaiPmh

abstract class OaiDocument {

    static final XML_DECLARATION = '<?xml version="1.0" encoding="UTF-8"?>\n'

    static final XMLNS = ' xmlns="http://www.openarchives.org/OAI/2.0/"'

    static final XMLNS_XSI = ' xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"'

    static final XSI_SCHEMALOCATION = ' xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/' +
            ' http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd"'

    protected stringBuilder = new StringBuilder()

    def getResponseBody() {
        process()
        stringBuilder
    }

    abstract void process()

    protected startDocument() {
        stringBuilder << XML_DECLARATION
        stringBuilder << '<OAI-PMH'
        stringBuilder << XMLNS
        stringBuilder << XMLNS_XSI
        stringBuilder << XSI_SCHEMALOCATION
        stringBuilder << '>'
        stringBuilder << '<responseDate>'
        stringBuilder << new Date().format(OaiPmh.DATE_FORMAT)
        stringBuilder << '</responseDate>'
    }

    protected endDocument() {
        stringBuilder << '</OAI-PMH>'
    }
}