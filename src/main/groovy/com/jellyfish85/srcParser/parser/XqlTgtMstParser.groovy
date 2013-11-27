package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBean
import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.bean.SVNRequestBean

/**
 * == XqlTgtMstParser ==
 *
 *
 * @author wada shunsuke
 * @since  2013/11/27
 *
 */
class XqlTgtMstParser {

    /**
     * == parse ==
     *
     *
     * @author wada shunske
     * @since  2013/11/27
     * @param app
     * @param list
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public static ArrayList<RsSqlTablesBean> parse(
            ApplicationProperties      app,
            ArrayList<SVNRequestBean>  list
    ) throws java.io.IOException, NullPointerException {


        def workspace = app.workspace()

        def resultSets = new ArrayList<RsSqlTablesBean>()

        list.each {SVNRequestBean bean ->
            def file = new File(workspace, bean.path())

            InputStream inputStream = new FileInputStream(file)

            //parse(bean, inputStream).each {RsSqlTablesBean entry -> resultSets.add(entry)}

            inputStream.close()
        }

        return resultSets
    }
}