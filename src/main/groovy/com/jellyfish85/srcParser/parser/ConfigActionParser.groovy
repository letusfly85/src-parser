package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsConfigAttributesBean
import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.apache.commons.io.FilenameUtils
import org.w3c.dom.Element
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class ConfigActionParser {

    public ConfigActionParser() {}

    public static ArrayList<RsConfigAttributesBean> parse(
            ApplicationProperties            app,
            ArrayList<SVNRequestBean>        list
    ) {
        def resultSets = new ArrayList<RsConfigAttributesBean>()

        list.each {SVNRequestBean bean ->
            parse(app, bean).each {RsConfigAttributesBean entry -> resultSets.add(entry)}
        }

        return resultSets
    }

    public static ArrayList<RsConfigAttributesBean> parse(
            ApplicationProperties app,
            SVNRequestBean        bean
    ) {
        def resultSets = new ArrayList<RsConfigAttributesBean>()

        try {
            def file = new File(app.workspace(), bean.path())
            if (!file.exists()) {
                new RuntimeException("file is not exists!")
            }

            DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance()
            DocumentBuilder        db        = factory.newDocumentBuilder()
            org.w3c.dom.Document   doc       = db.parse(file)

            /*if (file.getName() != "command-config-jyb-tp.xml") {
                return resultSets
            }*/

            Element elem                     = doc.getDocumentElement()
            org.w3c.dom.NodeList nodeList    = elem.getElementsByTagName("command")

            if (nodeList.length == 0) {
                return resultSets
            }

            for (int i = 0; i < nodeList.length; i++){

                def node = nodeList.item(i)
                Element entry  = (Element)node

                def commandName = entry.getAttribute("name")

                def searchNodeList =  entry.getElementsByTagName("search")
                if (searchNodeList.length > 0) {

                    for (int k = 0; k < searchNodeList.length; k++){
                        def _node  = searchNodeList.item(k)
                        def _entry = (Element)_node

                        if (_node != null && _entry.hasAttribute("subjectid")) {
                            def entity = new RsConfigAttributesBean()

                            entity.headRevisionAttr().setValue(new BigDecimal(bean.headRevision()))
                            entity.fileNameAttr().setValue(bean.fileName())
                            entity.pathAttr().setValue(bean.path())
                            entity.revisionAttr().setValue(new BigDecimal(bean.revision()))
                            entity.authorAttr().setValue(bean.author())
                            entity.commitYmdAttr().setValue(bean.commitYmd())
                            entity.commitHmsAttr().setValue(bean.commitHms())
                            entity.extensionAttr().setValue(FilenameUtils.getExtension(bean.path()))

                            entity.actionNameAttr().setValue(commandName)
                            entity.subjectIdAttr().setValue(_entry.getAttribute("subjectid"))

                            resultSets.add(entity)
                        }
                    }
                }

                def excelNodeList =  entry.getElementsByTagName("excel")
                if (excelNodeList.length > 0) {

                    for (int k = 0; k < excelNodeList.length; k++){
                        def _node  = excelNodeList.item(k)
                        def _entry = (Element)_node

                        if (_node != null && _entry.hasAttribute("subjectid")) {
                            def entity = new RsConfigAttributesBean()
                            entity.headRevisionAttr().setValue(new BigDecimal(bean.headRevision()))
                            entity.fileNameAttr().setValue(bean.fileName())
                            entity.pathAttr().setValue(bean.path())
                            entity.revisionAttr().setValue(new BigDecimal(bean.revision()))
                            entity.authorAttr().setValue(bean.author())
                            entity.commitYmdAttr().setValue(bean.commitYmd())
                            entity.commitHmsAttr().setValue(bean.commitHms())
                            entity.extensionAttr().setValue(FilenameUtils.getExtension(bean.path()))

                            entity.actionNameAttr().setValue(commandName)
                            entity.subjectIdAttr().setValue(_entry.getAttribute("subjectid"))

                            resultSets.add(entity)
                        }
                    }
                }

                def entryNodeList =  entry.getElementsByTagName("entry")
                if (entryNodeList.length > 0) {

                    for (int k = 0; k < entryNodeList.length; k++){
                        def _node  = entryNodeList.item(k)
                        def _entry = (Element)_node

                        if (_node != null && _entry.hasAttribute("subjectid")) {
                            def entity = new RsConfigAttributesBean()
                            entity.headRevisionAttr().setValue(new BigDecimal(bean.headRevision()))
                            entity.fileNameAttr().setValue(bean.fileName())
                            entity.pathAttr().setValue(bean.path())
                            entity.revisionAttr().setValue(new BigDecimal(bean.revision()))
                            entity.authorAttr().setValue(bean.author())
                            entity.commitYmdAttr().setValue(bean.commitYmd())
                            entity.commitHmsAttr().setValue(bean.commitHms())
                            entity.extensionAttr().setValue(FilenameUtils.getExtension(bean.path()))

                            entity.actionNameAttr().setValue(commandName)
                            entity.subjectIdAttr().setValue(_entry.getAttribute("subjectid"))

                            resultSets.add(entity)
                        }
                    }
                }
            }

            println("[TARGET]" + bean.path())

        } catch (SAXParseException e) {
            println("[PARSE-ERROR]" + bean.path())

        } catch (Exception e) {
            e.printStackTrace()
            println("[RUNTIME-ERROR]" + bean.path())

        }

        return resultSets
    }

}
