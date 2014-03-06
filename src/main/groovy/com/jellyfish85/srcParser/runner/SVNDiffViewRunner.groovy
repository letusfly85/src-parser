package com.jellyfish85.srcParser.runner

import com.jellyfish85.srcParser.generator.SVNDiffViewGenerator
import org.apache.commons.io.FileUtils

class SVNDiffViewRunner {

    public static void main(String[] args) {

        SVNDiffViewGenerator generator = new SVNDiffViewGenerator()

        generator.generate()

        String fileName1 = getClass().getResource("/javascripts/stupidtable.js").getPath()
        String fileName2 = getClass().getResource("/javascripts/stupidtable.min.js").getPath()
        String fileName3 = getClass().getResource("/javascripts/jquery-1.8.2.js").getPath()
        String fileName4 = getClass().getResource("/javascripts/modernizr.custom.04022.js").getPath()
        String fileName5 = getClass().getResource("/css/show_commit_history.css").getPath()
        String fileName6 = getClass().getResource("/css/style_commit_history.css").getPath()
        String fileName7 = getClass().getResource("/css/bg_commit_history.jpg").getPath()
        String fileName8 = getClass().getResource("/css/normalize_commit_history.css").getPath()

        FileUtils.copyFile((new File(fileName1)), new File("output", "stupidtable.js"))
        FileUtils.copyFile((new File(fileName2)), new File("output", "stupidtable.min.js"))
        FileUtils.copyFile((new File(fileName3)), new File("output", "jquery-1.8.2.js"))
        FileUtils.copyFile((new File(fileName4)), new File("output", "modernizr.custom.04022.js"))
        FileUtils.copyFile((new File(fileName5)), new File("output", "show_commit_history.css"))
        FileUtils.copyFile((new File(fileName6)), new File("output", "style_commit_history.css"))
        FileUtils.copyFile((new File(fileName7)), new File("output", "bg_commit_history.jpg"))
        FileUtils.copyFile((new File(fileName8)), new File("output", "normalize_commit_history.css"))

    }

}
