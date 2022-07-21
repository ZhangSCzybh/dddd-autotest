package com.yolocast.qa.zsc.others;

import java.io.*;
import java.util.Scanner;


public class TestDemo{

    public static void main(String [] args){

        TestDemo testDemo = new TestDemo();
        Student student = new Student();

        testDemo.getStudent(student);
        student.setName("111");


        System.out.println("main方法打印：" + student.getName());
    }


    public void getStudent(Student st){


        st.setName("xiayq");
        System.out.println("getStudent方法" + st.getName());

    }














     static class Student{
        private String name ;

        public String getName(){
            return name;

        }
        public void setName(String name){
            this.name  = name;
        }
    }
}
