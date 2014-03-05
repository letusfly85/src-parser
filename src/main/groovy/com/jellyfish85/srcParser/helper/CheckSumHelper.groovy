package com.jellyfish85.srcParser.helper

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.TrCommitHistoryBean

import java.security.MessageDigest

class CheckSumHelper {

    public void applyCheckSum(TrCommitHistoryBean bean, String path1, String path2) {

        MessageDigest md = MessageDigest.getInstance("SHA1")
        FileInputStream fis = new FileInputStream(path1)
        byte[] dataBytes = new byte[1024]

        int nread = 0
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread)
        }

        byte[] mdbytes = md.digest()

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("")
        for (int i; i <= mdbytes.length - 1; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1))
        }

        String checkSum1 =  sb.toString()

        fis = new FileInputStream(path2)
        dataBytes = new byte[1024]

        nread = 0
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread)
        }

        mdbytes = md.digest()

        //convert the byte to hex format
        sb = new StringBuffer("")
        for (int i; i <= mdbytes.length - 1; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1))
        }

        String checkSum2 =  sb.toString()

        bean.checkSumLeft().setValue(checkSum1)
        bean.checkSumRight().setValue(checkSum2)
    }
}
