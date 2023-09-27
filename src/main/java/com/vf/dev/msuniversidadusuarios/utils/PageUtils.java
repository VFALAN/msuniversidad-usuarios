package com.vf.dev.msuniversidadusuarios.utils;

public class PageUtils {
    public static int totalPages(int pPageSize, Long pTotalRecords) {
        int mTotalPageInt = 0;
        if (pTotalRecords > 0 && pPageSize > 0) {
            mTotalPageInt = (int) (pTotalRecords / pPageSize);
            if (pTotalRecords % pPageSize != 0) {
                mTotalPageInt++;
            }
        }
        return mTotalPageInt;
    }


}
