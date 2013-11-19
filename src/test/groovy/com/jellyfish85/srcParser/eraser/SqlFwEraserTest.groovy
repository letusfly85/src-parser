package com.jellyfish85.srcParser.eraser

import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.*

import org.junit.Test

/**
 * == SqlFwEraserTest ==
 *
 * @author wada shunsuke
 * @since  2013/11/19
 *
 */
public class SqlFwEraserTest {

    SqlFwEraser eraser = new SqlFwEraser()

    /**
     * == testReplaceFrameworkVariant ==
     *
     * check matched string returns True
     */
    @Test
    public void testReplaceFrameworkVariant() throws Exception {
        def str00 = "--% hoge "
        assertThat(eraser.replaceFrameworkVariant(str00), is("/* fw_flg */\n"))

        def str01 = "--%hoge"
        assertThat(eraser.replaceFrameworkVariant(str01), is("/* fw_flg */\n"))

        def str02 = "null"
        assertThat(eraser.replaceFrameworkVariant(str02), is("NULL\n"))

        def str03 = "亜亜亜"
        assertThat(eraser.replaceFrameworkVariant(str03), is("var\n"))

        def str04 = ":_ab"
        assertThat(eraser.replaceFrameworkVariant(str04), is(" 1 = 1\n"))

        def str05 = "       -- aaaaa"
        assertThat(eraser.replaceFrameworkVariant(str05), is("\n"))
    }

}
