package cm.deity.base.https.config;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Author : chenming
 * @Description :
 * @Date : 2018/3/27 21:40
 * @VERSION : 1.0
 */
public class HttpConfig {
    /**
     * post普通参数
     */
    private Map<String, Object> paramMap;
    /**
     * 文件列表
     */
    private Map<String, List<File>> fileMap;
    private String acceptCharset;
    /**
     * 授权密匙，以接口方安全验证为准
     */
    private String authorization;
    private String contentType;
    /**
     * 请求来源
     */
    private String referer;
    /**
     * 客户端信息，伪造浏览器
     */
    private String userAgent;
    /**
     * 主机地址或域名
     */
    private String host;

    /**
     * cookie信息
     */
    private String cookie;

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, List<File>> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, List<File>> fileMap) {
        this.fileMap = fileMap;
    }

    public String getAcceptCharset() {
        return acceptCharset;
    }

    public void setAcceptCharset(String acceptCharset) {
        this.acceptCharset = acceptCharset;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}