package com.jellyfish85.srcParser.helper

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertThat
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * == SqlRegexHelperTest ==
 *
 *
 */
class SqlRegexHelperTest {

    SqlRegexHelper helper = new SqlRegexHelper()

    /**
     * == isNotWhenScopeMatched ==
     *
     * check matched string returns True
     */
    @Test
    public void isNotWhenScopeMatched() {
        def str = "\t\t\t\tWHEN NOT MATCHED THEN"
        assertTrue(helper.isMergeOpeMatched(str))
    }

    /**
     * == isWhenScopeMatched ==
     *
     * check matched string returns True
     *
     */
    @Test
    public void isWhenScopeMatched() {
        def str = " WHEN MATCHED THEN"
        assertTrue(helper.isMergeOpeMatched(str))
    }

    /**
     * == isXXXScopeNotMatched ==
     *
     * check not matched string returns False
     *
     */
    @Test
    public void isXXXScopeNotMatched() {
        def str = "WHEN XXX MATCHED THEN"
        assertFalse(helper.isMergeOpeMatched(str))
    }

    /**
     * == isParenScopeMatched ==
     *
     * check matched string returns True
     *
     */
    @Test
    public void isParenScopeMatched() {
        def str = " ("
        assertTrue(helper.isOperator(str))
    }

    /**
     * == isWhenOnlyScopeMatched ==
     *
     * check matched string returns True
     *
     */
    @Test
    public void isWhenOnlyScopeMatched() {
        def str = " WHEN"
        assertTrue(helper.isOperator(str))
    }

    /**
     * == isElseOnlyScopeMatched ==
     *
     * check matched string returns True
     *
     */
    @Test
    public void isElseOnlyScopeMatched() {
        def str = " ELSE "
        assertTrue(helper.isOperator(str))
    }

    //todo check sql operator path

}
