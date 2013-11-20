package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsConfigAttributesBean
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.junit.Before
import org.w3c.dom.Element

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertThat

import static org.hamcrest.CoreMatchers.*

import org.junit.Test

/**
 * == ConfigActionParserTest ==
 *
 * @todo
 */
class ConfigActionParserTest {

    ConfigActionParser parser = new ConfigActionParser()
    DocumentBuilderFactory factory = null
    DocumentBuilder        db      = null
    org.w3c.dom.Document   doc     = null
    Element                elem    = null


    @Before
    void setup() {
        InputStream inputStream = getClass().getResourceAsStream("/xml/configActionTest-001.xml")

        this.factory   = DocumentBuilderFactory.newInstance()
        this.db        = factory.newDocumentBuilder()
        this.doc       = db.parse(inputStream)
        this.elem      = doc.getDocumentElement()

    }

    @Test
    void testParseByTag() {
        SVNRequestBean bean  = new SVNRequestBean()
        String  commandName  = "hoge.command"
        String  tagName      = "entry"
        ArrayList<RsConfigAttributesBean> list = parser.parseByTag(bean, elem, commandName, tagName)

        assertThat(list.get(0).subjectIdAttr().value(), is("my.subjectId"))
    }
}