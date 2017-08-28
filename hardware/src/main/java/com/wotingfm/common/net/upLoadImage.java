package com.wotingfm.common.net;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.wotingfm.common.application.BSApplication;

/**
 * 图片上传
 * 作者：xinLong on 2017/7/9 16:31
 * 邮箱：645700751@qq.com
 */
public class upLoadImage {
    public static upLoadImage INSTANCE;
    private String OSS_ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    private String ACCESS_ID = "LTAI1SiUYNVeHiNc";
    private String ACCESS_KEY = "tQ2aXUuRmWzeiPqO0EUZOWCsSsJnBv";
    public static OSSClient oss;
    public static String BUCKET_NAME = "woting";
    // 这里的objectKey其实就是服务器上的路径，即目录+文件名
    //String objectKey = keyPath + "/" + carArr[times] + ".jpg";
    public static String objectKey = "img_avatar/";
    public static String URL = "http://woting.oss-cn-beijing.aliyuncs.com/"+objectKey;
    /**
     * 单例模式创建网络连接
     *
     * @return
     */
    public static upLoadImage getInstance() {
        if (INSTANCE == null) {
            synchronized (upLoadImage.class) {
                if (INSTANCE == null) {
                    INSTANCE = new upLoadImage();
                }
            }
        }
        return INSTANCE;
    }

    private upLoadImage() {
        // ACCESS_ID,ACCESS_KEY是在阿里云申请的
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ACCESS_ID, ACCESS_KEY);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        // oss为全局变量，OSS_ENDPOINT是一个OSS区域地址
        oss = new OSSClient(BSApplication.mContext, OSS_ENDPOINT, credentialProvider, conf);
    }

}
