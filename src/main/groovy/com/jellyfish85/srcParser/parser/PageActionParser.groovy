package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsPageActionIdxBean
import com.jellyfish85.dbaccessor.src.mainte.tool.RsSvnSrcInfoBean
import com.jellyfish85.srcParser.utils.ApplicationProperties

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

        def file = new File(app.workspace(), bean.pathAttr().value())
        if (!file.exists()) {
            new RuntimeException("file is not exists!")
        }

        DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance()
        DocumentBuilder        db        = factory.newDocumentBuilder()
        org.w3c.dom.Document   doc       = db.parse(file)

        Element elem                     = doc.getDocumentElement()
        NodeList nodeList                = elem.getElementsByTagName("action")
        nodeList.each {Node node ->

            Element entry  = (Element)node

            def entity = new RsPageActionIdxBean()

            entity.fileNameAttr().setValue(bean.fileNameAttr().value())
            entity.headRevisionAttr().setValue(bean.headRevisionAttr().value())

            entity.actionNameAttr().setValue(entry.getAttribute("id"))

            //entity.subjectId      = elem.getAttribute("subjectId")

            resultSets.add(entity)
        }

        return resultSets
    }
}