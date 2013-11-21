package com.jellyfish85.srcParser.parser

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
 *
 *
 * @author wada shunsuke
 * @since  2013/11/22
 * @todo   all
 *
 */
class BlSubjectIdParserTest {

    BlSubjectIdParser parser = new BlSubjectIdParser()
    DocumentBuilderFactory factory = null
    DocumentBuilder        db      = null

    InputStream inputStream001 = null

    org.w3c.dom.Document   doc001  = null
    Element                elem001 = null

    org.w3c.dom.Document   doc002  = null
    Element                elem002 = null


    @Before
    void setUp() {
        inputStream001 = getClass().getResourceAsStream("/xml/blSubjectIdTest-001.xml")
        this.doc001       = db.parse(inputStream001)
        this.elem001      = doc001.getDocumentElement()
    }

    @After
    void tearDown() {
        //todo

        if (inputStream001 != null) {
            inputStream001.close()
        }
    }

    void testParse() {
        assertTrue(true)
    }
}