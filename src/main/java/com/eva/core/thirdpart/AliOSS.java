package com.eva.core.thirdpart;

import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.eva.core.model.AppConfig;
import com.eva.core.utils.Utils;
import io.swagger.annotations.ApiModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@ApiModel("阿里云OSS")
@Component
public class AliOSS {

    /**
     * 上传文件
     *
     * @param inputStream 流
     * @param fileKey 文件的key
     */
    public void upload(String bucket, InputStream inputStream, String fileKey) throws IOException {
        com.aliyun.oss.OSS ossClient = this.getOSSClient();
        ossClient.putObject(bucket, fileKey, inputStream);
        ossClient.shutdown();
    }

    /**
     * 下载文件
     *
     * @param fileKey 文件key
     * @return InputStream
     */
    public InputStream download(String bucket, String fileKey) {
        com.aliyun.oss.OSS ossClient = this.getOSSClient();
        OSSObject file = ossClient.getObject(bucket, fileKey);
        return file.getObjectContent();
    }

    /**
     * 获取OSS客户端对象
     */
    private com.aliyun.oss.OSS getOSSClient() {
        AppConfig.AliYunOSSConfig aliYunOSSConfig = Utils.AppConfig.getOss().getAliyun();
        return new OSSClientBuilder().build(aliYunOSSConfig.getEndpoint(), aliYunOSSConfig.getAccessKeyId(), aliYunOSSConfig.getAccessKeySecret());
    }
}
