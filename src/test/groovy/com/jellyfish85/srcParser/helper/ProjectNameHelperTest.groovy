package com.jellyfish85.srcParser.helper

import com.jellyfish85.srcParser.utils.Stream2StringUtils
import org.junit.After
import org.junit.Before

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.*

import org.junit.Test

/**
 * == ProjectNameHelperTest ==
 *
 *
 * @author wada shunsuke
 * @since  2013/11/23
 *
 */
class ProjectNameHelperTest {

    Stream2StringUtils s2s         = new Stream2StringUtils()
    InputStream inputStream        = null
    ArrayList<String> projectNames = new ArrayList<String>()

    ProjectNameHelper helper = new ProjectNameHelper()

    @Before
    void setUp() {
        inputStream = getClass().getResourceAsStream("/properties/projectNames.txt")
        projectNames = s2s.stream2StringAry(inputStream)
    }

    @After
    void tearDown() {
        if (inputStream != null) {
            inputStream.close()
        }
    }

    @Test
    void testGetProjectName() {
        def testPath01 = "/src/main/resources/com/jellyfish85/HOGE/HOGE_PROJECT/query"
        def testPath02 = "/src/main/resources/com/jellyfish85/XXXX_PROJECT/query"

        assertThat(helper.getProjectName(projectNames, testPath01), is("/HOGE/HOGE_PROJECT"))
        assertFalse(helper.getProjectName(projectNames, testPath02) == "HOGE_PROJECT" )
    }
}
