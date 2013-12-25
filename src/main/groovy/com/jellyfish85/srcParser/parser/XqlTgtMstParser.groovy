package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBean
import com.jellyfish85.srcParser.utils.SrcParserProp
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.w3c.dom.Element
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * == XqlTgtMstParser ==
 *
 *
 * @author wada shunsuke
 * @since  2013/11/27
 *
 */
class XqlTgtMstParser {

    /**
     * == parse ==
     *
     *
     * @author wada shunske
     * @since  2013/11/27
     * @param app
     * @param list
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public static ArrayList<RsSqlTablesBean> parse(
            SrcParserProp      app,
            ArrayList<SVNRequestBean>  list
    ) throws java.io.IOException, NullPointerException {


        def workspace = app.workspace()

        def resultSets = new ArrayList<RsSqlTablesBean>()

        list.each {SVNRequestBean bean ->
            def file = new File(workspace, bean.path())

            InputStream inputStream = new FileInputStream(file)

            parse(bean, inputStream).each {RsSqlTablesBean entry -> resultSets.add(entry)}

            inputStream.close()
        }

        return resultSets
    }
    
    /**
     * == parse ==
     *
     *
     * @author wada shunsuke
     * @since  2013/11/28
     * @param bean
     * @param inputStream
     * @return
     * @throws org.xml.sax.SAXParseException
     */
    public static ArrayList<RsSqlTablesBean> parse(
            SVNRequestBean        bean,
            InputStream           inputStream
    ) throws SAXParseException {

        def resultSets = new ArrayList<RsSqlTablesBean>()
        try {
            DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance()
            DocumentBuilder        db        = factory.newDocumentBuilder()
            Document               doc       = db.parse(inputStream)

            Element elem                     = doc.getDocumentElement()

            NodeList nodeList = elem.getElementsByTagName("parameter")

            if (nodeList.length.equals(0)) {
                return resultSets
            }

            for (int i = 0; i < nodeList.length; i++){

                def node = nodeList.item(i)
                Element entry  = (Element)node
                if (entry.hasAttribute("tgtMst")) {
                    RsSqlTablesBean result = new RsSqlTablesBean()
                    result.pathAttr().setValue(bean.path())
                    result.fileNameAttr().setValue(bean.fileName())
                    result.revisionAttr().setValue(new BigDecimal(bean.revision()))
                    result.tableNameAttr().setValue(entry.getAttribute("tgtMst"))
                    result.crudTypeAttr().setValue("SELECT")
                    result.callTypeAttr().setValue("tgtMst")

                    resultSets.add(result)
                }
            }

            println("[TARGET]" + bean.path())

        } catch (Exception e) {
            e.printStackTrace()
            println("[RUNTIME-ERROR]" + bean.path())

        }

        return resultSets
    }
}