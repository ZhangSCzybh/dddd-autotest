package com.yolocast.qa.zsc.others;

import java.io.*;
import java.util.Scanner;

/**
 * @author zhangsc
 * @date 2022-07-07 下午7:21
 */
public class TestDemo{

    public static File file = new File("/Users/zhangshichao/Downloads/stream_log");
    public static String keyFile = "/Users/zhangshichao/Downloads/stream_data/keyFilterResult.txt";
    public static String tsFile = "/Users/zhangshichao/Downloads/stream_data/tsFilterResult.txt";
    public static String m3u8File = "/Users/zhangshichao/Downloads/stream_data/m3u8FilterResult.txt";
    public static String withoutPicFile = "/Users/zhangshichao/Downloads/stream_data/withoutPicFilterResult.txt";

    public static String line;


    public static void main(String[] args) throws IOException {
        //筛选文件夹内所有文件关键字所在行，保存至filterResult.txt 923033656514056193
        System.out.print("输入关键字：");
        String str = (new Scanner(System.in)).next();
        keyFilter(str);

        // 流量分类：直播流量 & 点播流量   387702302979878436
        System.out.print("流量分类：1、直播流量；2、点播流量；请输入对应数字：");
        int flowType = (new Scanner(System.in)).nextInt();

        switch (flowType){
            case 1:
                // 直播流量：总流量、ts文件流量、m3u8(flv)流量
                System.out.print("选择需要计算的类型格式：ts格式输入:txspiseq ｜｜ m3u8、flv格式输入:txSecret ｜｜ 所有格式输入:all！ -->请输入：");
                String streamType = (new Scanner(System.in)).next();
                switch (streamType){

                        //筛选ts文件格式的流量，计算ts流量
                    case "txspiseq":
                        tsFlow(streamType);
                        break;

                        //筛选m3u8格式的流量，计算flv流量
                    case "txSecret":
                        m3u8Flow(streamType);
                        break;

                        //筛选所有格式流量，计算总流量
                    case "all":
                        totalLiveFlow(streamType);
                        break;

                    default:
                        break;
                }
                break;

            case 2:
                // 点播流量：总流量、去除图片流量
                System.out.print("选择需要计算的类型格式：1、总流量统计（包含图片）withjpg｜｜ 2、总流量（去除图片）withoutjpg -->请输入对应字母：");
                String demandFlowType = (new Scanner(System.in)).next();
                switch(demandFlowType){
                        //筛选所有格式,计算总流量
                    case "withjpg":
                        totalDemandFlow(demandFlowType);
                        break;

                        //去除jpg图片流量,
                    case "withoutjpg":
                        totalDemandFlowWithoutJpg(demandFlowType);
                        break;

                    default:
                        break;
                }
                break;
        }


        }


        /** 
         * 
         * @author zhangsc
         * @date 2022/7/8 下午3:59
 * @param str
         */
    public static void keyFilter(String str) throws IOException {
        BufferedWriter ou = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(keyFile), "utf-8"));
        File flist[] = file.listFiles();
        if (flist == null || flist.length == 0) {
            return;
        }
        for (File f : flist) {
            if (f.isDirectory()) {
                //这里将列出所有的文件夹
                //System.out.println("Dir==>" + f.getAbsolutePath());
            } else {
                //这里将列出所有的文件
                System.out.println("file==>" + f.getAbsolutePath());
                BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.indexOf(str) > 0) {
                        ou.write(line + "\n");
                    }
                }
                System.out.println("streamfilter筛选成功！");
                in.close();
            }
        }
        ou.close();
    }

    /** 
     * 
     * @author zhangsc
     * @date 2022/7/8 下午3:39
     * @param streamType  
     */
    public static void tsFlow(String streamType ) throws IOException {
        BufferedReader in1 = new BufferedReader(new InputStreamReader(new FileInputStream(keyFile), "utf-8"));
        BufferedWriter ou1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tsFile), "utf-8"));
        while ((line = in1.readLine()) != null) {
            if (line.indexOf(streamType) > 0) {
                ou1.write(line + "\n");
            }
        }
        in1.close();
        ou1.close();
        System.out.println("ts文件筛选成功");
        //计算ts文件格式的流量
        String line2 ="";
        int tssum = 0;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(tsFile), "GB2312"));
        while(line2 != null){
            line2 = bufferedReader.readLine();
            //split返i回的是一个数组，将line 切割传给每个数组下标
            String pp [] = new String[5];
            //这里注意split会空指针异常，这里得加个判断
            if(line2 ==null ){
                break;
            }
            pp = line2.split(" ");
            //Integer包装类，将split返回的数组pp[5]
            System.out.println("ts流量：" + pp[4].substring(0));
            Integer year = Integer.valueOf(pp[4].substring(0));

            tssum += year.intValue();
        }
        bufferedReader.close();
        System.out.println("ts流量总和:"+tssum);
    }

    
    /** 
     *
     * @author zhangsc
     * @date 2022/7/8 下午3:39
     * @param streamType  
     */
    public static void m3u8Flow(String streamType) throws IOException {
        BufferedReader in2 = new BufferedReader(new InputStreamReader(new FileInputStream(keyFile), "utf-8"));
        BufferedWriter ou2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(m3u8File), "utf-8"));
        while ((line = in2.readLine()) != null) {
            if (line.indexOf(streamType) > 0) {
                ou2.write(line + "\n");
            }
        }
        in2.close();
        ou2.close();
        System.out.println("m3u8文件筛选成功");
        //计算m3u8文件格式的流量
        String line1 = "";
        int m3u8Sum = 0;
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(m3u8File), "GB2312"));
        while(line1 != null){
            line1 = bufferedReader2.readLine();
            //split返回的是一个数组，将line 切割传给每个数组下标
            String pp [] = new String[5];
            //这里注意split会空指针异常，这里得加个判断
            if(line1 ==null ){
                break;
            }
            pp = line1.split(" ");
            //Integer包装类，将split返回的数组pp[5]
            System.out.println("m3u8&flv流量：" + pp[4].substring(0));
            Integer year = Integer.valueOf(pp[4].substring(0));
            m3u8Sum += year.intValue();
        }
        bufferedReader2.close();
        System.out.println("m3u8&flv流量总和:"+m3u8Sum);

    }
    
    
    /**
     *
     * @author zhangsc
     * @date 2022/7/8 下午3:40
     * @param streamType
     */
    public static void totalLiveFlow(String streamType) throws IOException {
        BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new FileInputStream(keyFile), "GB2312"));
        String line3 = "";
        int Sum = 0;
        while(line3 != null){
            line3 = bufferedReader3.readLine();
            //split返回的是一个数组，将line 切割传给每个数组下标
            String pp [] = new String[5];
            //这里注意split会空指针异常，这里得加个判断
            if(line3 ==null ){
                break;
            }
            pp = line3.split(" ");
            //Integer包装类，将split返回的数组pp[5]
            System.out.println("所有流量：" + pp[4].substring(0));
            Integer year = Integer.valueOf(pp[4].substring(0));
            Sum += year.intValue();
        }
        bufferedReader3.close();
        System.out.println("所有流量总和:"+Sum);
    }

    
    /**
     *
     * @author zhangsc
     * @date 2022/7/8 下午3:45
     * @param demandFlowType
     */
    public static void totalDemandFlow(String demandFlowType) throws IOException {
        BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new FileInputStream(keyFile), "GB2312"));
        String line4 = "";
        int picSum = 0;
        while(line4 != null){
            line4 = bufferedReader3.readLine();
            //split返回的是一个数组，将line 切割传给每个数组下标
            String pp [] = new String[5];
            //这里注意split会空指针异常，这里得加个判断
            if(line4 ==null ){
                break;
            }
            pp = line4.split(" ");
            //Integer包装类，将split返回的数组pp[5]
            System.out.println("所有流量：" + pp[4].substring(0));
            Integer year = Integer.valueOf(pp[4].substring(0));
            picSum += year.intValue();
        }
        bufferedReader3.close();
        System.out.println("所有流量总和:" + picSum);
    }
    
    
    /** 
     * 
     * @author zhangsc
     * @date 2022/7/8 下午3:48
     * @param demandFlowType  
     */
    public static void totalDemandFlowWithoutJpg(String demandFlowType ) throws IOException {
        BufferedReader in3 = new BufferedReader(new InputStreamReader(new FileInputStream(keyFile), "utf-8"));
        BufferedWriter ou3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(withoutPicFile), "utf-8"));
        while ((line = in3.readLine()) != null) {
            if (line.indexOf(demandFlowType) < 0) {
                ou3.write(line + "\n");
            }
        }
        in3.close();
        ou3.close();
        System.out.println("去除图片筛选成功");
        BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(new FileInputStream(withoutPicFile), "GB2312"));
        String line5 = "";
        int withoutPicSum = 0;
        while(line5 != null){
            line5 = bufferedReader4.readLine();
            //split返回的是一个数组，将line 切割传给每个数组下标
            String pp [] = new String[5];
            //这里注意split会空指针异常，这里得加个判断
            if(line5 ==null ){
                break;
            }
            pp = line5.split(" ");
            //Integer包装类，将split返回的数组pp[5]
            System.out.println("所有流量：" + pp[4].substring(0));
            Integer year = Integer.valueOf(pp[4].substring(0));
            withoutPicSum += year.intValue();
        }
        bufferedReader4.close();
        System.out.println("所有流量总和:" + withoutPicSum);
    }


}
