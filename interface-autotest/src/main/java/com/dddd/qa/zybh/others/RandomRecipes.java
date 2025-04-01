package com.dddd.qa.zybh.others;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangsc
 * @date 2024年09月02日 18:18:45
 * @packageName com.dddd.qa.zybh.others
 * @className RandomRecipes
 */
public class RandomRecipes {
    //Common.jenkinsUrl+"/src/main/resources/dddd/
    private static final String CSV_FILE_PATH = "/Users/zhangshichao/Documents/Workspace/dddd-autotest/interface-autotest/src/main/resources/test-dddd/recipes.csv"; // CSV 文件路径

    public static void main(String[] args) {
        List<String> recipes = loadRecipesFromCSV(CSV_FILE_PATH);

        if (recipes.isEmpty()) {
            System.out.println("没有找到任何菜谱！");
            return;
        }

        // 打乱列表顺序
        Collections.shuffle(recipes);

        // 输出前三个菜作为每日推荐
        List<String> dailyRecipes = recipes.subList(0, Math.min(3, recipes.size()));
        System.out.println("今日推荐菜谱:");
        for (String recipe : dailyRecipes) {
            System.out.println(recipe);
        }
    }

    /**
     * 从 CSV 文件加载菜谱列表
     * @param csvFilePath CSV 文件的路径
     * @return 包含菜谱名称的列表
     */
    private static List<String> loadRecipesFromCSV(String csvFilePath) {
        List<String> recipes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                recipes.add(line.trim()); // 添加每一行的内容，并去掉首尾空白
            }
        } catch (IOException e) {
            System.err.println("读取 CSV 文件时发生错误: " + e.getMessage());
        }
        return recipes;
    }
}