package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Auther: lhq
 * @Date: 2020/9/7 09:40
 * @Description:
 */
public class OSSTest {

    public static void main(String[] args) throws Exception {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4FfAyMFbAvtoEaUXVhdw";
        String accessKeySecret = "PdQdqksmKBQh5vBMCvErT3LWnvznip";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = new FileInputStream(new File("C:\\1.jpg"));
        ossClient.putObject("scw20200907", "pic/test.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
