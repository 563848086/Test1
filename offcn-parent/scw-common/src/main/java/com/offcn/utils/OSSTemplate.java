package com.offcn.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.ToString;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Auther: lhq
 * @Date: 2020/9/7 10:12
 * @Description:
 */
@Data
@ToString
public class OSSTemplate {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String bucketDomain;

    public String upload(InputStream inputStream, String fileName) throws Exception {
        //1.重新修改上传文件的名称
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获得上传文件所在文件夹（目录）的名称
        String folderName = sdf.format(new Date());

        //2.重新生成文件名称
        fileName = UUID.randomUUID().toString().replace("-", "") + "_" + fileName;

        //3.执行上传文件
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, "pic/" + folderName + "/" + fileName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        inputStream.close();

        // return "https://scw20200907.oss-cn-beijing.aliyuncs.com/pic/test.jpg";
        return "https://" + bucketDomain + "/pic/" + folderName + "/" + fileName;
    }

}
