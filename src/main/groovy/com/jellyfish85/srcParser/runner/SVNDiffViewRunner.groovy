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
        FileUtils.copyFile((new File(fileName1)), new File("output", "stupidtable.js"))
        FileUtils.copyFile((new File(fileName2)), new File("output", "stupidtable.min.js"))
        FileUtils.copyFile((new File(fileName3)), new File("output", "jquery-1.8.2.js"))

    }

}
