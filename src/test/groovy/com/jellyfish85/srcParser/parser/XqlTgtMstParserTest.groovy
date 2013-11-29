package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBean
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.junit.After
import org.junit.Before

import static org.junit.Assert.assertThat

import static org.hamcrest.CoreMatchers.*

import org.junit.Test

/**
 * == XqlTgtMstParserTest ==
 *
 *
 * @author wada shunsuke
 * @since  2013/11/28
 *
 */
class XqlTgtMstParserTest {

    XqlTgtMstParser parser = new XqlTgtMstParser()
    InputStream inputStream001 = null

    @Before
    void setUp() {
        inputStream001 = getClass().getResourceAsStream("/xml/xqlTgtMstTest-001.xml")
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
        bean.path = "test.path.xqlTgtMst"

        ArrayList<RsSqlTablesBean> list = parser.parse(bean, inputStream001)

        RsSqlTablesBean result = list.get(0)

        assertThat(result.tableNameAttr().value(),       is("MY_TABLE"))
        assertThat(result.crudTypeAttr().value(),        is("SELECT"))
        assertThat(result.callTypeAttr().value(),        is("tgtMst"))
    }
}
