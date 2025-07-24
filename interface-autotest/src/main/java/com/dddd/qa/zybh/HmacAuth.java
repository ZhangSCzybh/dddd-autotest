package com.dddd.qa.zybh;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sam.Sha
 * @date 2024/4/17
 * @since 1.0.0
 **/
public class HmacAuth {

    /**
     * HTTP 时间格式
     */
    private static final DateTimeFormatter HTTP_DATE_FORMAT = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);

    /**
     * 默认的 HMAC 算法
     */
    public static final String DEFAULT_HMAC_ALGO = "HmacSHA256";

    /**
     * 用户名 Key
     */
    private final String userName;

    /**
     * 加密池大小
     */
    private final int hmacPoolSize;

    /**
     * HMAC 加密池
     */
    private final Mac[] macInstances;

    /**
     * 密钥池索引
     */
    private AtomicInteger nextIndex;

    /**
     * HmacAuth 构造函数
     * 用于初始化 HmacAuth 对象，计算基于 HMAC 算法的认证信息，
     * 默认算法为 HmacSHA256，加密池大小为当前 CPU 核心数。
     *
     * @param userName 用户名 Key，用于认证标识。
     * @param secret   密钥，用于计算 HMAC 的密钥。
     */
    public HmacAuth(String userName, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        this(userName, secret, DEFAULT_HMAC_ALGO, Runtime.getRuntime().availableProcessors());
    }

    /**
     * HmacAuth 构造函数
     * 用于初始化 HmacAuth 对象，计算基于 HMAC 算法的认证信息，
     * 默认算法为 HmacSHA256。
     *
     * @param userName     用户名 Key，用于认证标识。
     * @param secret       密钥，用于计算 HMAC 的密钥。
     * @param hmacPoolSize 密钥池大小，用于缓存 HMAC 密钥。
     */
    public HmacAuth(String userName, String secret, int hmacPoolSize) throws NoSuchAlgorithmException, InvalidKeyException {
        this(userName, secret, DEFAULT_HMAC_ALGO, hmacPoolSize);
    }

    /**
     * HmacAuth 构造函数
     * 用于初始化 HmacAuth 对象，计算基于 HMAC 算法的认证信息。
     *
     * @param userName     用户名 Key，用于认证标识。
     * @param secret       密钥，用于计算 HMAC 的密钥。
     * @param hmacAlgo     HMAC 算法标识，指定用于计算 HMAC 的算法。
     * @param hmacPoolSize 密钥池大小，用于缓存 HMAC 密钥。
     */
    public HmacAuth(String userName, String secret, String hmacAlgo, int hmacPoolSize) throws NoSuchAlgorithmException, InvalidKeyException {

        if (hmacAlgo == null || secret == null || userName == null || hmacPoolSize <= 0) {
            throw new IllegalStateException("Required field cannot be empty.");
        }

        this.userName = userName;

        this.nextIndex = new AtomicInteger(0);
        this.hmacPoolSize = hmacPoolSize;

        // initPool 初始化加密池
        this.macInstances = new Mac[hmacPoolSize];
        for (int i = 0; i < hmacPoolSize; i++) {
            macInstances[i] = Mac.getInstance(hmacAlgo);
            macInstances[i].init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), hmacAlgo));
        }
    }

    /**
     * 生成 Hmac-Auth 加密认证 headers
     *
     * @param requestTarget 请求的目标路径，请求方法+请求URL。
     * @param body          请求体内容，部分认证方案会基于请求体内容计算 HMAC。
     * @return 认证的 headers, 以 Map 形式返回，包含 Authorization, Digest, 和 Date 字段
     * @throws NoSuchAlgorithmException 加密算法不支持时抛出
     */
    public Map<String, String> genHmacAuthHeaders(String requestTarget, String body) throws NoSuchAlgorithmException {

        // 验证成员变量是否已正确设置
        if (body == null || requestTarget == null) {
            throw new IllegalStateException("Required fields are not initialized");
        }

        // 生成body的sha256加密串
        String bodyDigest = generateBodyDigest(body);

        // 生成当前GMT时间，注意格式不能改变，必须形如：Wed, 17 Apr 2024 10:15:22 GMT
        String timeNow = HTTP_DATE_FORMAT.format(LocalDateTime.now(ZoneId.of("GMT")));

        // 拼装待签名的数据
        String signData = buildSignData(timeNow, bodyDigest, requestTarget);

        // 生成hmac签名
        String hmacSign = generateHmacSignature(signData);

        // 拼装headers
        Map<String, String> header = new HashMap<>(3);
        String auth = String.format("hmac username=\"%s\", algorithm=\"hmac-sha256\", headers=\"date digest @request-target\", signature=\"%s\"",
                userName, hmacSign);
        header.put("Authorization", auth);
        header.put("Digest", bodyDigest);
        header.put("Date", timeNow);

        return header;
    }

    /**
     * 生成消息体的摘要。
     * 此方法使用 SHA-256 算法计算消息体的哈希值，然后将哈希值编码为 Base64 字符串。
     *
     * @param body 消息体，用于计算摘要。
     * @return 消息体摘要的字符串表示，格式为"SHA-256=哈希值"。
     * @throws NoSuchAlgorithmException 如果无法找到 SHA-256 算法。
     */
    private String generateBodyDigest(String body) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digestHash = digest.digest(body.getBytes(StandardCharsets.UTF_8));
        String bodyHash = Base64.getEncoder().encodeToString(digestHash);

        return String.format("SHA-256=%s", bodyHash);
    }

    /**
     * 构建签名数据字符串。
     * 该方法将当前时间、消息体摘要和请求目标结合，格式化生成一个签名数据字符串。
     *
     * @param timeNow       当前时间，格式为字符串。
     * @param bodyDigest    消息体的摘要，用于验证消息完整性。
     * @param requestTarget 请求的目标路径，请求方法+请求URL。
     * @return 返回格式化后的签名数据字符串，包含时间、摘要和请求目标。
     */
    private String buildSignData(String timeNow, String bodyDigest, String requestTarget) {
        return String.format("date: %s\ndigest: %s\n@request-target: %s", timeNow, bodyDigest, requestTarget);
    }

    /**
     * 生成 HMAC 签名。
     *
     * @param signData 需要签名的数据字符串。
     * @return 返回使用 Base64 编码后的 HMAC 签名字符串。
     */
    private String generateHmacSignature(String signData) {

        int poolIndex = this.nextIndex.getAndIncrement();
        if (poolIndex >= 10240) {
            this.nextIndex.set(0);
        }

        Mac hmac = macInstances[poolIndex % this.hmacPoolSize];
        synchronized (hmac) {
            byte[] hmacHash = hmac.doFinal(signData.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacHash);
        }

    }

}
