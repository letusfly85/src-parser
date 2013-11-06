package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.w3c.dom.Element
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilderFactory

class SqlCdataParser {

    public SqlCdataParser() {}

    /**
     *
     * @param app Application Property sets
     * @param list of subversion bean
     * @return list of RS_SQL_CDATA bean
     */
    public static ArrayList<RsSqlCdataBean> parse(
      ArrayList<SVNRequestBean> list,
      ApplicationProperties app
    ) {
        def resultSets = new ArrayList<RsSqlCdataBean>()

        list.each {SVNRequestBean bean ->
            resultSets.add(parse(bean, app))
        }

        return resultSets
    }

    /**
     *
     *
     * @param bean
     * @param app
     * @return
     */
    public static ArrayList<RsSqlCdataBean> parse(
      SVNRequestBean bean,
      ApplicationProperties app
    ) {
        def resultSets = new ArrayList<RsSqlCdataBean>()

        try {
            def file = new File(app.workspace(), bean.path())
            if (!file.exists()) {
                new RuntimeException("file is not exists!")
            }

            def factory  = DocumentBuilderFactory.newInstance()
            def db       = factory.newDocumentBuilder()
            def doc      = db.parse(file)

            def attr  = doc.getDocumentElement()

            def persistentName =  ""
            resultSets.addAll(walkNode(bean, attr, app, persistentName))

        } catch (SAXParseException e) {
            println("[PARSE-ERROR]" + bean.path())

        } catch (Exception e) {
            e.printStackTrace()
            println("[RUNTIME-ERROR]" + bean.path())

        }

        return resultSets
    }

    /**
     *
     *
     * @param bean
     * @param attr
     * @param app
     * @param persistentName
     * @return
     */
    private static ArrayList<RsSqlCdataBean> walkNode(
        SVNRequestBean        bean,
        Element               attr,
        ApplicationProperties app,
        String                persistentName
    ) {
        def resultSets = new ArrayList<RsSqlCdataBean>()

        if (attr.getNodeName() == "sql") {
          def parent = (Element)attr.getParentNode()

          persistentName = parent.getAttribute("name")

          resultSets.addAll(gatherSqlText(bean, attr.getTextContent(), app, persistentName))
        }

        return resultSets
    }

    /**
     *
     *
     * @param bean
     * @param text
     * @param app
     * @param persistentName
     * @return
     */
    private static ArrayList<RsSqlCdataBean> gatherSqlText(
        SVNRequestBean        bean,
        String                text,
        ApplicationProperties app,
        String                persistentName
    ) {
        def resultSets = new ArrayList<RsSqlCdataBean>()

        def idx = 0
        text.split("\n").each {String line ->
            def entry  = new RsSqlCdataBean()
            entry.headRevisionAttr().setValue(new BigDecimal(bean.headRevision()))

            //TODO add entry attributes

            entry.persisterNameAttr().setValue(persistentName)
            entry.lineAttr().setValue(new BigDecimal(idx))
            entry.textAttr().setValue(line)


            if (line.trim() != "" && line.trim() != null) {
                idx += 1
                resultSets.add(entry)
            }
        }

        return resultSets
    }
}
