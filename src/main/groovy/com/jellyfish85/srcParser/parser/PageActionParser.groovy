package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsPageActionIdxBean
import com.jellyfish85.dbaccessor.src.mainte.tool.RsSvnSrcInfoBean
import com.jellyfish85.srcParser.utils.ApplicationProperties
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import org.w3c.dom.Element
import org.w3c.dom.NodeList

class PageActionParser {

    public PageActionParser() {}

    public static ArrayList<RsPageActionIdxBean> parse(
            ApplicationProperties app,
            ArrayList<RsSvnSrcInfoBean> list
    ) {

        def resultSets = new ArrayList<RsPageActionIdxBean>()

        list.each {RsSvnSrcInfoBean bean ->
            parse(app, bean).each {
                RsPageActionIdxBean x -> resultSets.add(x)
            }
        }
        return resultSets
    }

    public static ArrayList<RsPageActionIdxBean> parse(
            ApplicationProperties app,
            RsSvnSrcInfoBean bean
    ) {
        def resultSets = new ArrayList<RsPageActionIdxBean>()

        try {
            def file = new File(app.workspace(), bean.pathAttr().value())
            if (!file.exists()) {
                new RuntimeException("file is not exists!")
            }

            DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance()
            DocumentBuilder        db        = factory.newDocumentBuilder()
            org.w3c.dom.Document   doc       = db.parse(file)

            Element elem                     = doc.getDocumentElement()
            NodeList nodeList                = elem.getElementsByTagName("action")

            if (nodeList.length == 0) {
                return resultSets
            }

            for (int i = 0; i < nodeList.length; i++){

                def node = nodeList.item(i)
                Element entry  = (Element)node

                def _nodeList = entry.getElementsByTagName("param")
                if (_nodeList.length > 0) {
                    for (int k = 0; k < nodeList.length; k++){
                        def _node = _nodeList.item(k)

                        def _entry = (Element)_node
                        if (_node != null && _entry.hasAttribute("key") &&_entry.getAttribute("key") == "cmd") {
                            def entity = new RsPageActionIdxBean()
                            entity.headRevisionAttr().setValue(bean.headRevisionAttr().value())
                            entity.projectNameAttr().setValue(bean.projectNameAttr().value())
                            entity.fileNameAttr().setValue(bean.fileNameAttr().value())
                            entity.pathAttr().setValue(bean.pathAttr().value())
                            entity.revisionAttr().setValue(bean.revisionAttr().value())
                            entity.authorAttr().setValue(bean.authorAttr().value())
                            entity.commitYmdAttr().setValue(bean.commitYmdAttr().value())
                            entity.commitHmsAttr().setValue(bean.commitHmsAttr().value())
                            entity.extensionAttr().setValue(bean.extensionAttr().value())

                            entity.actionNameAttr().setValue(_entry.getAttribute("value"))

                            resultSets.add(entity)
                        }
                    }
                    println("[TARGET]" + bean.pathAttr().value())
                }
            }

        } catch (SAXParseException e) {
            println("[PARSE-ERROR]" + bean.pathAttr().value())

        } catch (Exception e) {
            e.printStackTrace()
            println("[RUNTIME-ERROR]" + bean.pathAttr().value())

        }

        return resultSets
    }
}