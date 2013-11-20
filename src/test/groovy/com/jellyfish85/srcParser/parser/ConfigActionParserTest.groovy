package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsConfigAttributesBean
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.junit.After
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

    InputStream inputStream001 = null

    org.w3c.dom.Document   doc001  = null
    Element                elem001 = null

    org.w3c.dom.Document   doc002  = null
    Element                elem002 = null

    @Before
    void setup() {
        this.factory   = DocumentBuilderFactory.newInstance()
        this.db        = factory.newDocumentBuilder()

        this.inputStream001 = getClass().getResourceAsStream("/xml/configActionTest-001.xml")
        this.doc001       = db.parse(inputStream001)
        this.elem001      = doc001.getDocumentElement()

        /*
        InputStream inputStream002 = getClass().getResourceAsStream("/xml/configActionTest-001.xml")
        this.doc002       = db.parse(inputStream002)
        this.elem002      = doc002.getDocumentElement()
        */

    }

    @Test
    void testParseByTag() {
        SVNRequestBean bean  = new SVNRequestBean()

        String  commandName01  = "hoge.command"
        String  tagName01      = "entry"
        ArrayList<RsConfigAttributesBean> list01 = parser.parseByTag(bean, elem001, commandName01, tagName01)

        assertThat(list01.get(0).subjectIdAttr().value(), is("my.subjectId"))


        String  commandName02  = "hoge.command2"
        String  tagName02      = "search"
        ArrayList<RsConfigAttributesBean> list02 = parser.parseByTag(bean, elem001, commandName02, tagName02)

        assertThat(list02.get(0).subjectIdAttr().value(), is("my.subjectId2"))



        String  commandName03  = "hoge.command2"
        String  tagName03      = "search"
        ArrayList<RsConfigAttributesBean> list03 = parser.parseByTag(bean, elem001, commandName03, tagName03)

        assertThat(list03.get(0).subjectIdAttr().value(), is("my.subjectId2"))

    }

    @After
    void shutDown() {
        if (this.inputStream001 != null) {
            this.inputStream001.close()
        }
    }
}