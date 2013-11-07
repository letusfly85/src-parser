package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.srcParser.utils.ProjectNameUtils
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.apache.commons.io.FilenameUtils
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilderFactory

class SqlCdataParser {

    def getProjectName(String path)  {

    }

    public SqlCdataParser() {}

    /**
     *
     * @param  app Application Property sets
     * @param  list of subversion bean
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

            //todo specify project name
            def projectName = ""

            resultSets.addAll(walkNode(bean, attr, projectName, persistentName))

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
        Node                  attr,
        String                projectName,
        String                persistentName
    ) {
        def resultSets = new ArrayList<RsSqlCdataBean>()

        if (attr.getNodeName() == "sql") {
          def parent = (Element)attr.getParentNode()

          persistentName = parent.getAttribute("name")

          resultSets.addAll(gatherSqlText(bean, projectName, attr.getTextContent(), persistentName))
        }

        def children = attr.getChildNodes()
        for (int i = 0; i < children.length; i++) {
            def child = children.item(i)
            resultSets.addAll(walkNode(bean, child, projectName, persistentName))
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
        String                projectName,
        String                text,
        String                persistentName
    ) {
        def resultSets = new ArrayList<RsSqlCdataBean>()

        def idx = 0
        text.split("\n").each {String line ->
            def entry  = new RsSqlCdataBean()

            entry.headRevisionAttr().setValue(new BigDecimal(bean.headRevision()))
            entry.projectNameAttr().setValue(projectName)
            entry.fileNameAttr().setValue(bean.fileName())
            entry.pathAttr().setValue(bean.path())
            entry.persisterNameAttr().setValue(persistentName)
            entry.lineAttr().setValue(new BigDecimal(idx))
            entry.textAttr().setValue(line)
            entry.revisionAttr().setValue(new BigDecimal(bean.revision()))
            entry.authorAttr().setValue(bean.author())
            entry.commitYmdAttr().setValue(bean.commitYmd())
            entry.commitHmsAttr().setValue(bean.commitHms())
            entry.extensionAttr().setValue(FilenameUtils.getExtension(bean.path()))

            if (line.trim() != "" && line.trim() != null) {
                idx += 1
                resultSets.add(entry)
            }
        }

        return resultSets
    }
}
