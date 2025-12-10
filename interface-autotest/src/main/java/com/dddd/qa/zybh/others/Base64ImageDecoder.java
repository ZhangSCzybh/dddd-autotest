package com.dddd.qa.zybh.others;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author zhangsc
 * @date 2025年12月09日 14:03:10
 * @packageName com.dddd.qa.zybh.utils
 * @className Base64ImageDecoder
 * @describe 解析验证码图片
 */
public class Base64ImageDecoder {
    public static void main(String[] args) {
        String base64Data =   "/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAZAEEDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD06mM7IxyhK+q8/mP8M1y/je5uYrext4t6288pWZkk8vIwAELHgAgt144z2NZFn9osdbsprDTjZW8kgjlQXwlilPOBnIG/BOAT1xx6+hLEcs+W1/8Ag/I55Td7I6nWPEtno0lrFLFcTyXJKxpboGbIxwQSDn5hxRp3iW01C/Ni9vd2V0U3pFeReW0i85K8nOMf5wcYniy5vBq2hRBvIM85RBu3hTuQBnXoSMggA8EZye0V2x0jxjo8moypqF5LGLeORD5RjySoZk5zksec4+9xkV0qzNFzJar7v8juaK871nTIfEXxB+ya3L5VlDbD7LEJwhc4y3ynkk8ncvGEAPIIFbSYZJfiZq9ha3dtcLDbcTXkC3DHHlgqWBViQTt5JxtwcnmtVRv16XJcm78ivb5f180juI9ftZtVm0+KOaSWNN4dArK/Tpg574yQBkHmtGJ5HyXi8tf4QWBb8QOB+ZrhbCwaX4k6pbzXt3IBaqXcOI3fiPgsgUjt93HTnIznrP7A0xv9fbfa/T7ZI1xt/wB3zC23PfGM4HpScYLeX3L/ADsTape7/wAv8/zNKiiisjQpz6Zb3eftW+cZJUOxAXII4AwM4J56+9VLbwvo1ncx3EFioljO5Szs2D64JIrXoqHTg3doTSe5napoWm615X9oW3neVnZ87LjOM9CPQVDp3hfRtKuxdWdisc4BUOXZsZ643E4+v1rXoqyrs47xfo0mo6hBKfC8GsxrFtV1vfs0kZySQxyNynIx6Hd607wxoOop4j1LxHq1vDaT3aCKK1hkDeWgIHzEDBOETkHueB0HX0Vp7R8vKPm0Ma+8K6LqV5Jd3dl5k8mNz+a4zgADgHHQCtHTdDtdGtmt7OAW0TOXZXkJOcAZ+Yk9h09K0bD/AI/Y/wAf5Gq1ZczbsK72JfKT/nvH+Tf4UVFRRZ9xH//Z";
        try {
            // 解码 Base64 字符串
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // 保存为 JPEG 文件
            try (FileOutputStream fos = new FileOutputStream("/Users/zhangshichao/Documents/Workspace/dddd-autotest/interface-autotest/src/main/resources/pic/captcha.jpg")) {
                fos.write(imageBytes);
            }

            System.out.println("验证码图像已保存为 captcha.jpg");
            System.out.println("请在当前目录下查看 captcha.jpg 文件");
        } catch (IOException e) {
            System.err.println("解码或保存失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
