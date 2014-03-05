package com.jellyfish85.srcParser.runner

import com.jellyfish85.srcParser.generator.SVNDiffCheckSumGenerator

class SVNDiffCheckSumRunner {

    public static void main(String[] args) {
        println("start")
        SVNDiffCheckSumGenerator generator = new SVNDiffCheckSumGenerator()

        generator.generate()

    }

}
