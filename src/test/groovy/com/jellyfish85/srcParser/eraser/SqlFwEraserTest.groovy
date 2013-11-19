package com.jellyfish85.srcParser.eraser

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
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
    }

    /*
    @Test
    public void testReplaceFrameworkOperator() throws Exception {

    }
    */
}
