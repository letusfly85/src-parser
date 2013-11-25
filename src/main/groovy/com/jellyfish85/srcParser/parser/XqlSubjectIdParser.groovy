package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSubjectidXqlpathIdxBean
import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.w3c.dom.Element
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * == XqlSubjectIdParser ==
 *
 *
 * @author wada shunsuke
 * @since  2013/11/25
 *
 */
class XqlSubjectIdParser {

    /**
     * == parse ==
     *
     *
     * @author wada shunsuke
     * @since  2013/11/25
     * @param app
     * @param list
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public static ArrayList<RsSubjectidXqlpathIdxBean> parse(
            ApplicationProperties      app,
            ArrayList<SVNRequestBean>  list
    ) throws java.io.IOException, NullPointerException {
        def workspace = app.workspace()

        def resultSets = new ArrayList<RsSubjectidXqlpathIdxBean>()

        list.each {SVNRequestBean bean ->
            def file = new File(workspace, bean.path())

            InputStream inputStream = new FileInputStream(file)

            parse(bean, inputStream).each {RsSubjectidXqlpathIdxBean entry -> resultSets.add(entry)}

            inputStream.close()
        }
    }

    /**
     * == parse ==
     *
     *
     * @author wada shunsuke
     * @since  2013/11/25
     * @param bean
     * @param inputStream
     * @return
     * @throws SAXParseException
     */
    public static ArrayList<RsSubjectidXqlpathIdxBean> parse(
            SVNRequestBean        bean,
            InputStream           inputStream
    ) throws SAXParseException {

        def resultSets = new ArrayList<RsSubjectidXqlpathIdxBean>()
        try {
            DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance()
            DocumentBuilder        db        = factory.newDocumentBuilder()
            org.w3c.dom.Document   doc       = db.parse(inputStream)

            Element elem                     = doc.getDocumentElement()

            org.w3c.dom.NodeList nodeList = elem.getElementsByTagName("subject")

            if (nodeList.length == 0) {
                return resultSets
            }

            for (int i = 0; i < nodeList.length; i++){

                def node = nodeList.item(i)
                Element entry  = (Element)node

                RsSubjectidXqlpathIdxBean result = new RsSubjectidXqlpathIdxBean()
                result.pathAttr().setValue(bean.path())
                result.fileNameAttr().setValue(bean.fileName())
                result.revisionAttr().setValue(new BigDecimal(bean.revision()))
                result.subjectIdAttr().setValue(entry.getAttribute("id"))

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