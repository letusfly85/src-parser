package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBean
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.junit.After
import org.junit.Before

import static org.junit.Assert.assertThat

import static org.hamcrest.CoreMatchers.*

import org.junit.Test

/**
 *
 *
 *
 */
class BlDaoParserTest {

    BlDaoParser parser = new BlDaoParser()
    InputStream inputStream001 = null

    @Before
    void setUp() {
        inputStream001 = getClass().getResourceAsStream("/xml/blDaoTest-001.xml")
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
        bean.path = "test.path"

        ArrayList<RsSqlTablesBean> list = parser.parse(bean, inputStream001)

        RsSqlTablesBean result = list.get(0)

        assertThat(result.tableNameAttr().value(),       is("EMPLOYEE"))
        assertThat(result.crudTypeAttr().value(),        is("merge"))
        assertThat(result.persisterNameAttr().value(),   is("EMPLOYEE_MERGE"))
    }
}
