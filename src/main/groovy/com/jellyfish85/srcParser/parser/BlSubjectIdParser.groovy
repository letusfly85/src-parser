package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSubjectidBlpathIdxBean
import com.jellyfish85.srcParser.utils.SrcParserProp
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.w3c.dom.Element
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * == BlSubjectIdParser ==
 *
 * gather subjectIds by parsing bl files
 *
 * @author wada shunsuke
 * @since  2013/11/21
 *
 */
class BlSubjectIdParser {

    /**
     * == parse ==
     *
     * @author wada shunsuke
     * @since  2013/11/21
     * @param list
     * @param path
     * @return
     * @throws IOException
     */
    public static ArrayList<RsSubjectidBlpathIdxBean> parse (
            SrcParserProp      app,
            ArrayList<SVNRequestBean>  list
    ) throws java.io.IOException, NullPointerException {

        def workspace = app.workspace()

        def resultSets = new ArrayList<RsSubjectidBlpathIdxBean>()

        list.each {SVNRequestBean bean ->
            def file = new File(workspace, bean.path())

            InputStream inputStream = new FileInputStream(file)

            parse(bean, inputStream).each {RsSubjectidBlpathIdxBean entry -> resultSets.add(entry)}

            inputStream.close()
        }

        return resultSets
    }

    /**
     * == parse ==
     *
     * @author wada shunsuke
     * @since  2013/11/21
     * @param bean
     * @param inputStream
     * @return
     * @throws SAXParseException
     */
    public static ArrayList<RsSubjectidBlpathIdxBean> parse(
            SVNRequestBean        bean,
            InputStream           inputStream
    ) throws SAXParseException {

        def resultSets = new ArrayList<RsSubjectidBlpathIdxBean>()
        try {
            DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance()
            DocumentBuilder        db        = factory.newDocumentBuilder()
            org.w3c.dom.Document   doc       = db.parse(inputStream)

            Element elem                     = doc.getDocumentElement()

            def subjectGroupId = elem.getAttribute("subjectGroupId")
            org.w3c.dom.NodeList nodeList = elem.getElementsByTagName("businessLogic")

            if (nodeList.length == 0) {
                return resultSets
            }

            for (int i = 0; i < nodeList.length; i++){

                def node = nodeList.item(i)
                Element entry  = (Element)node

                RsSubjectidBlpathIdxBean result = new RsSubjectidBlpathIdxBean()
                result.pathAttr().setValue(bean.path())
                result.fileNameAttr().setValue(bean.fileName())
                result.revisionAttr().setValue(new BigDecimal(bean.revision()))
                result.subjectGroupIdAttr().setValue(subjectGroupId)
                result.subjectIdAttr().setValue(entry.getAttribute("subjectId"))

                result.updateFlgAttr().setValue("0")
                result.newFlgAttr().setValue("0")

                resultSets.add(result)
            }

            println("[TARGET]" + bean.path())

        } catch (Exception e) {
            e.printStackTrace()
            println("[RUNTIME-ERROR]" + bean.path())

        }

        return resultSets
    }
}