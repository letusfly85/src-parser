package com.jellyfish85.srcParser.parser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBean
import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.w3c.dom.Element
import org.xml.sax.SAXParseException

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * == BlDaoParser ==
 *
 *
 * @author wada shunsuke
 * @since  2013/11/26
 *
 */
class BlDaoParser {

    public static ArrayList<RsSqlTablesBean> pares(
        ApplicationProperties      app,
        ArrayList<SVNRequestBean>  list
    ) throws java.io.IOException, NullPointerException {


        def workspace = app.workspace()

        def resultSets = new ArrayList<RsSqlTablesBean>()

        list.each {SVNRequestBean bean ->
            def file = new File(workspace, bean.path())

            InputStream inputStream = new FileInputStream(file)

            //todo
            //parse(bean, inputStream).each {RsSqlTablesBean entry -> resultSets.add(entry)}

            inputStream.close()
        }

        return resultSets
    }

}
