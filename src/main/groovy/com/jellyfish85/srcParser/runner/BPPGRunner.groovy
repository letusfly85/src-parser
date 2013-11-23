package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSubjectidBlpathIdxDao
import com.jellyfish85.srcParser.generator.BPPGenerator

/**
 * == BPPGRunner ==
 *
 * @author wada shunsuke
 * @since  2013/11/23
 *
 */
class BPPGRunner {

    public static void main(String[] args) {
        def _context = new BaseRunner()
        _context.databaseInitialize()

        def conn = _context.getConnection()
        def app = _context.app

        def dao = new RsSubjectidBlpathIdxDao()
        def list = dao.findAll(conn)
        def _list = dao.convert(list)

        def generator = new BPPGenerator()
        generator.generate(app, _list)

        _context.databaseFinalize()
    }
}
