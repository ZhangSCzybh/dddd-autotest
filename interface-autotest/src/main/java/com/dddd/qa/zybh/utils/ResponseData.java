package com.dddd.qa.zybh.utils;

import java.util.List;

/**
 * @author zhangsc
 * @date 2024年07月17日 14:31:15
 * @packageName com.dddd.qa.zybh.utils
 * @className ResponseData
 * @describe 这也好像没用到了。。。
 */
public class ResponseData {
    private int code;
    private String msg;
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<OrderData> rows;
        private int total;
        private int totalPage;

        public List<OrderData> getRows() {
            return rows;
        }

        public void setRows(List<OrderData> rows) {
            this.rows = rows;
        }
    }
}
