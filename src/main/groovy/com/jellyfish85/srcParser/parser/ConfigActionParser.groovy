package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsConfigAttributesBean
import com.jellyfish85.srcParser.converter.ConvSVNRequestBean2RsConfigAttributesBean
import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.w3c.dom.Element
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class ConfigActionParser {

    public ConfigActionParser() {}

    public static ArrayList<RsConfigAttributesBean> parse(
            ApplicationProperties            app,
            ArrayList<SVNRequestBean>        list
    ) throws FileNotFoundException {
        def resultSets = new ArrayList<RsConfigAttributesBean>()

        list.each {SVNRequestBean bean ->
            def file = new File(app.workspace(), bean.path())
            if (!file.exists()) {
                throw new FileNotFoundException()
            }

            def path = (file).getPath()
            InputStream inputStream = getClass().getResourceAsStream(path)

            parse(bean, inputStream).each {RsConfigAttributesBean entry -> resultSets.add(entry)}

            inputStream.close()
        }

        return resultSets
    }

    /**
     * == parse ==
     *
     * @param app
     * @param bean
     * @param path
     * @return
     */
    public static ArrayList<RsConfigAttributesBean> parse(
            SVNRequestBean        bean,
            InputStream           inputStream
    ) {

        def resultSets = new ArrayList<RsConfigAttributesBean>()
        try {
            DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance()
            DocumentBuilder        db        = factory.newDocumentBuilder()
            org.w3c.dom.Document   doc       = db.parse(inputStream)

            Element elem                     = doc.getDocumentElement()
            org.w3c.dom.NodeList nodeList    = elem.getElementsByTagName("command")

            if (nodeList.length == 0) {
                return resultSets
            }

            for (int i = 0; i < nodeList.length; i++){

                def node = nodeList.item(i)
                Element entry  = (Element)node

                def commandName = entry.getAttribute("name")

                resultSets.addAll(parseByTag(bean, entry, commandName, "search"))
                resultSets.addAll(parseByTag(bean, entry, commandName, "excel"))
                resultSets.addAll(parseByTag(bean, entry, commandName, "entry"))

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

    /**
     *
     *
     * @param bean
     * @param entry
     * @param commandName
     * @param tagName
     * @return
     */
    public static ArrayList<RsConfigAttributesBean> parseByTag(
            SVNRequestBean bean,
            Element entry,
            String  commandName,
            String  tagName
    ) {

        def resultSets = new ArrayList<RsConfigAttributesBean>()
        def converter  = new ConvSVNRequestBean2RsConfigAttributesBean()

        def entryNodeList =  entry.getElementsByTagName(tagName)
        if (entryNodeList.length > 0) {

            for (int k = 0; k < entryNodeList.length; k++){
                def _node  = entryNodeList.item(k)
                def _entry = (Element)_node

                if (_node != null && _entry.hasAttribute("subjectid")) {
                    def entity = converter.convert(bean)

                    entity.actionNameAttr().setValue(commandName)
                    entity.subjectIdAttr().setValue(_entry.getAttribute("subjectid"))

                    resultSets.add(entity)
                }
            }
        }

        return resultSets
    }

}
