package com.jellyfish85.srcParser.generator

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSubjectidBlpathIdxBean
import com.jellyfish85.srcParser.helper.ProjectNameHelper
import com.jellyfish85.srcParser.utils.ApplicationProperties
import org.apache.commons.io.FileUtils
import org.apache.commons.lang.StringUtils

/**
 * == BPPGenerator ==
 *
 * @author wada shunsuke
 * @since  2013/11/23
 *
 */
class BPPGenerator {

    public static void generate(
            ApplicationProperties app,
            ArrayList<RsSubjectidBlpathIdxBean> list
    ) {

        def helper = new ProjectNameHelper()

        def targetFolder = new File(app.outputProp())
        if (!targetFolder.exists()) {
            FileUtils.forceMkdir(targetFolder)
        }
        FileUtils.cleanDirectory(targetFolder)

        def targetFile = new File(targetFolder, app.bpp())

        PrintWriter pw = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(targetFile),"UTF-8")))

        def targetProjectNames = app._targetProjectNames()
        list.each {RsSubjectidBlpathIdxBean bean ->
            def projectName = helper.getProjectName(targetProjectNames, bean.pathAttr().value())

            if (StringUtils.isEmpty(projectName)) {
                def removePath   = app.ap() + projectName + app.logicPath()
                def relativePath = bean.pathAttr().value().replace(removePath, "")

                pw.write(bean.subjectIdAttr().value() + "=" + relativePath)
                pw.write("\n")

                println(bean.subjectIdAttr().value() + "=" + relativePath)

            } else {
                println("[WARN][Project Name is null]" + bean.pathAttr().value())
            }
        }

        pw.close()
    }
}
