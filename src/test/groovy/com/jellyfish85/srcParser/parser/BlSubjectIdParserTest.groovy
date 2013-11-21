package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSubjectidBlpathIdxBean
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.junit.After
import org.junit.Before

import static org.junit.Assert.assertThat

import static org.hamcrest.CoreMatchers.*

import org.junit.Test

/**
 * == BlSubjectIdParserTest ==
 *
 * @author wada shunsuke
 * @since  2013/11/22
 *
 */
class BlSubjectIdParserTest {

    BlSubjectIdParser parser = new BlSubjectIdParser()
    InputStream inputStream001 = null


    @Before
    void setUp() {
        inputStream001 = getClass().getResourceAsStream("/xml/blSubjectIdTest-001.xml")
        inputStream001
    }

    @After
    void tearDown() {
        if (inputStream001 != null) {
            inputStream001.close()
        }
    }

    @Test
    void testParse() {
        SVNRequestBean bean  = new SVNRequestBean()

        ArrayList<RsSubjectidBlpathIdxBean> list = parser.parse(bean, inputStream001)

        RsSubjectidBlpathIdxBean result = list.get(0)

        assertThat(result.subjectGroupIdAttr().value(), is("my.subjectGroupId"))
        assertThat(result.subjectIdAttr().value(),      is("my.subjectId"))
    }
}