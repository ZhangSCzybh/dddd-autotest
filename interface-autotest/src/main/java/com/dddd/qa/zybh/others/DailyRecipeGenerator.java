package com.dddd.qa.zybh.others;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DailyRecipeGenerator {

    private static final String SPOONACULAR_API_KEY = "e37a5a24d04049be87eb4ce99d623158";
    private static final String SPOONACULAR_BASE_URL = "https://api.spoonacular.com/recipes/random";
    private static final String BAIDU_APP_ID = "20240902002139088";
    private static final String BAIDU_SECRET_KEY = "r2Wql9hSTvaGw_9YigSH";
    private static final String BAIDU_TRANSLATE_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";


    public static void main(String[] args) throws UnsupportedEncodingException {
        for (int i = 1; i <=3 ; i++ ) {
            List<String> randomRecipes = getRandomRecipes(1);
            if (!randomRecipes.isEmpty()) {
                List<String> translatedRecipes = translateRecipes(randomRecipes);
                if (!translatedRecipes.isEmpty()) {
                    System.out.println("今日推荐菜谱" + i + ":" + randomRecipes + translatedRecipes);

                } else {
                    System.out.println("翻译失败，请检查API密钥和网络连接。");
                }
            } else {
                System.out.println("未能获取菜谱，请检查API密钥和网络连接。");
            }
        }
    }



    /**
     * 从Spoonacular API获取指定数量的随机菜谱。
     * @param numberOfRecipes 需要获取的菜谱数量
     * @return 包含英文菜谱名称的列表
     */
    private static List<String> getRandomRecipes(int numberOfRecipes) {
        String url = SPOONACULAR_BASE_URL + "?number=" + numberOfRecipes + "&apiKey=" + SPOONACULAR_API_KEY;
        String responseBody = HttpUtil.get(url);

        if (StrUtil.isNotBlank(responseBody)) {
            return parseRecipes(responseBody);
        } else {
            System.err.println("请求失败，未收到响应。");
        }
        return new ArrayList<>();
    }

    /**
     * 解析API响应中的菜谱名称。
     * @param responseBody API响应体
     * @return 包含英文菜谱名称的列表
     */
    private static List<String> parseRecipes(String responseBody) {
        JSONObject json = JSONUtil.parseObj(responseBody);
        JSONArray recipesArray = json.getJSONArray("recipes");
        List<String> recipes = new ArrayList<>();
        for (int i = 0; i < recipesArray.size(); i++) {
            JSONObject recipe = recipesArray.getJSONObject(i);
            String title = recipe.getStr("title");
            recipes.add(title);
        }
        return recipes;
    }

    /**
     * 使用百度翻译API将英文菜谱名称翻译成中文。
     * @param recipes 英文菜谱名称列表
     * @return 翻译后的中文菜谱名称列表
     */
    private static List<String> translateRecipes(List<String> recipes) throws UnsupportedEncodingException {
        List<String> translatedRecipes = new ArrayList<>();
        for (String recipe : recipes) {
            String translatedRecipe = translateText(recipe);
            if (StrUtil.isNotBlank(translatedRecipe)) {
                translatedRecipes.add(translatedRecipe);
            }
        }
        return translatedRecipes;
    }

    /**
     * 使用百度翻译API将文本翻译成中文。
     * @param text 待翻译的文本
     * @return 翻译后的中文文本
     */
    private static String translateText(String text) throws UnsupportedEncodingException {
        String salt = String.valueOf(System.currentTimeMillis());
        String sign = DigestUtil.md5Hex(BAIDU_APP_ID + text + salt + BAIDU_SECRET_KEY);
        String q = java.net.URLEncoder.encode(text, String.valueOf(StandardCharsets.UTF_8));

        String url = BAIDU_TRANSLATE_URL + "?appid=" + BAIDU_APP_ID + "&q=" + q + "&from=en&to=zh&salt=" + salt + "&sign=" + sign;
        String responseBody = HttpUtil.get(url);

        if (StrUtil.isNotBlank(responseBody)) {
            JSONObject json = JSONUtil.parseObj(responseBody);
            JSONArray transResult = json.getJSONArray("trans_result");
            if (transResult != null && transResult.size() > 0) {
                JSONObject result = transResult.getJSONObject(0);
                return result.getStr("dst");
            }
        }
        return "";
    }
}