package io.github.aliyundrive4j.common.utils;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpCall;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import io.github.aliyundrive4j.common.enums.AliyunDriveInfoEnums;
import io.github.aliyundrive4j.common.enums.AliyunDriveCodeEnums;
import io.github.aliyundrive4j.common.exception.AliyunDriveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Objects;

/**
 * description: AliyunHttpUtils
 *
 * @ClassName : AliyunHttpUtils
 * @Date 2022/12/15 11:52
 * @Author puye(0303)
 * @PackageName io.github.aliyundrive4j.utils
 */
public class AliyunHttpUtils {

    /**
     * 系统Http请求工具日志
     */
    private static final Logger log = LoggerFactory.getLogger(AliyunHttpUtils.class);
    /**
     * 系统Http请求工具类实例
     */
    private static AliyunHttpUtils INSTANCE;
    /**
     * 系统Http请求对象
     */
    private static final HTTP HTTP = OkHttps.getHttp();
    /**
     * 私有化构造，启用单例模式
     */
    private AliyunHttpUtils() { }

    /**
     * 获得系统HttpUtils对象实例
     * @return 返回一个HttpUtils请求对象
     */
    public static AliyunHttpUtils getInstance() {
        if (Objects.isNull(INSTANCE)) {
            synchronized (AliyunHttpUtils.class) {
                log.info("初始化加载HttpIUtils工具类对象");
                INSTANCE = new AliyunHttpUtils();
            }
        }
        return INSTANCE;
    }

    /**
     * 直接发起Get请求 不携带任何参数
     * @param requestUrl 请求地址
     * @return 返回一个请求的结果字符串
     */
    public String doGetWithNoParams(String requestUrl) {
        HttpCall httpCall = HTTP.async(requestUrl).get();
        HttpResult result = httpCall.getResult();
        return getResponse(result);
    }

    /**
     * 进行基础的常见的Get请求
     * @param requestUrl 请求地址
     * @param requestParams 请求参数
     * @return 返回一个请求的最终json响应字符串
     */
    public String doGetWithParams(String requestUrl, Map<String, String> requestParams){
        HttpCall httpCall = HTTP.async(requestUrl) .addUrlPara(requestParams).get();
        HttpResult httpResult = httpCall.getResult();
        return getResponse(httpResult);
    }

    /**
     * 封装内部的返回基本响应的方法
     * @param httpResult httpResult 请求结果对象
     * @return 返回一个相应的字符串内容
     */
    private String getResponse(HttpResult httpResult) {
        String responseStr;
        if (Objects.isNull(httpResult)) {
            throw new AliyunDriveException(AliyunDriveCodeEnums.valueOf("a"));
        }
        if (AliyunDriveInfoEnums.ALIYUN_DRIVE_HTTP_STATUS_OK.getEnumsIntegerValue() == httpResult.getStatus()) {
            responseStr = httpResult.getBody().toString();
            httpResult.getBody().close();
            httpResult.close();
        }else {
            throw new AliyunDriveException(AliyunDriveCodeEnums.ERROR_HTTP);
        }
        return responseStr;
    }

}