package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBean
import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.w3c.dom.Element
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * == BlDaoParser ==
 *
 *
 * @author wada shunsuke
 * @since  2013/11/26
 *
 */
class BlDaoParser {

    /**
     * == parse ==
     *
     *
     * @author wada shunsuke
     * @since  2013/11/27
     * @param app
     * @param list
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public static ArrayList<RsSqlTablesBean> parse(
        ApplicationProperties      app,
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
     * @since  2013/11/27
     * @param bean
     * @param inputStream
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public static ArrayList<RsSqlTablesBean> parse(
            SVNRequestBean  bean,
            InputStream     inputStream
    ) throws java.io.IOException, NullPointerException, SAXParseException {

        def resultSets = new ArrayList<RsSqlTablesBean>()
        try {
            DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance()
            DocumentBuilder        db        = factory.newDocumentBuilder()
            org.w3c.dom.Document   doc       = db.parse(inputStream)

            Element elem                     = doc.getDocumentElement()

            org.w3c.dom.NodeList nodeList = elem.getElementsByTagName("dao")

            if (nodeList.length == 0) {
                return resultSets
            }

            for (int i = 0; i < nodeList.length; i++){

                def node = nodeList.item(i)
                Element entry  = (Element)node

                RsSqlTablesBean result = new RsSqlTablesBean()
                result.pathAttr().setValue(bean.path())
                result.fileNameAttr().setValue(bean.fileName())
                result.revisionAttr().setValue(new BigDecimal(bean.revision()))
                result.tableNameAttr().setValue(entry.getAttribute("table"))
                result.crudTypeAttr().setValue(entry.getAttribute("type"))
                result.callTypeAttr().setValue("dao")

                def parent = (Element)entry.getParentNode()
                result.persisterNameAttr().setValue(parent.getAttribute("name"))

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
