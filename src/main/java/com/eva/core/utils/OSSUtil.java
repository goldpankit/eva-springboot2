package com.eva.core.utils;

import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.core.thirdpart.AliOSS;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * OSS工具类
 */
@Slf4j
@Component
public class OSSUtil {

    // 文件大小限制
    private final ThreadLocal<Integer> maxSize = new ThreadLocal<>();

    // 文件类型限制
    private final ThreadLocal<String[]> fileTypes = new ThreadLocal<>();

    @Resource
    private AliOSS aliOSS;

    /**
     * 上传图片
     *
     * @param imageFile 图片文件
     * @return 文件访问路径
     */
    public UploadResult uploadImage(MultipartFile imageFile) {
        return this.uploadImage(imageFile, null);
    }

    /**
     * 上传图片
     *
     * @param imageFile 图片文件
     * @param businessPath 业务路径，如使用"/avatar"表示用户头像路径，"/goods/cover"表示商品封面图片等
     * @return 文件访问路径
     */
    public UploadResult uploadImage(MultipartFile imageFile, String businessPath) {
        return this.uploadImage(Utils.AppConfig.getOss().getAliyun().getCommonBucketName(), imageFile, businessPath);
    }

    /**
     * 上传图片
     *
     * @param imageFile 图片文件
     * @param businessPath 业务路径，如使用"/avatar"表示用户头像路径，"/goods/cover"表示商品封面图片等
     * @return 文件访问路径
     */
    public UploadResult uploadImage(String bucket, MultipartFile imageFile, String businessPath) {
        try {
            // 设置图片文件默认类型限制
            if (this.fileTypes.get() == null || this.fileTypes.get().length == 0) {
                this.setFileTypes(".jpg,.jpeg,.png,.gif");
            }
            // 验证文件
            this.checkUpload(imageFile);
            // 执行上传
            String fileKey = this.doUpload(bucket, imageFile, businessPath);
            // 返回上传结果
            if (StringUtils.isBlank(businessPath)) {
                String accessUri = Utils.AppConfig.getOss().getAccessPrefix() + "/image?f=" + fileKey;
                return new UploadResult(imageFile.getOriginalFilename(), fileKey, accessUri);
            }
            String accessUri = businessPath + "?f=" + fileKey;
            return new UploadResult(imageFile.getOriginalFilename(), fileKey, accessUri);
        } catch (Exception e) {
            log.error("图片上传失败", e);
            throw new BusinessException(ResponseStatus.SERVER_ERROR.getCode(), "图片上传失败");
        }
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件访问路径
     */
    public UploadResult upload(MultipartFile file) {
        return this.upload(file, null);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @param businessPath 业务路径，如使用"/contract"表示合同文件，"/contract/attach"表示合同附件
     * @return UploadResult
     */
    public UploadResult upload(MultipartFile file, String businessPath) {
        return this.upload(Utils.AppConfig.getOss().getAliyun().getCommonBucketName(), file, businessPath);
    }

    /**
     * 上传文件
     *
     * @param bucket 存储空间名称
     * @param file 文件
     * @param businessPath 业务路径，如使用"/contract"表示合同文件，"/contract/attach"表示合同附件
     * @return UploadResult
     */
    public UploadResult upload(String bucket, MultipartFile file, String businessPath) {
        try {
            // 验证文件
            this.checkUpload(file);
            // 执行上传
            String fileKey = this.doUpload(bucket, file, businessPath);
            // 返回上传结果
            if (StringUtils.isBlank(businessPath)) {
                String accessUri = Utils.AppConfig.getOss().getAccessPrefix() + "/attach?f=" + fileKey;
                return new UploadResult(file.getOriginalFilename(), fileKey, accessUri);
            }
            String accessUri = businessPath + "?f=" + fileKey;
            return new UploadResult(file.getOriginalFilename(), fileKey, accessUri);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ResponseStatus.SERVER_ERROR.getCode(), "文件上传失败");
        }
    }

    /**
     * 下载
     *
     * @param fileKey 文件在存储空间中的key
     * @return InputStream
     */
    public InputStream download (String fileKey) {
        return this.download(Utils.AppConfig.getOss().getAliyun().getCommonBucketName(), fileKey);
    }

    /**
     * 下载
     *
     * @param bucket 存储空间名称
     * @param fileKey 文件在存储空间中的key
     * @return InputStream
     */
    public InputStream download(String bucket, String fileKey) {
        return aliOSS.download(bucket, fileKey);
    }

    /**
     * 设置文件大小限制
     *
     * @param maxSize 最大值（单位M）
     * @return OSSUtil
     */
    public OSSUtil setMaxSize(int maxSize) {
        this.maxSize.set(maxSize);
        return this;
    }

    /**
     * 设置文件类型限制（多个类型使用","隔开，如".jpg,jpeg,.png"）
     *
     * @param fileTypes 文件类型
     * @return OSSUtil
     */
    public OSSUtil setFileTypes (String fileTypes) {
        this.fileTypes.set(fileTypes.split(","));
        return this;
    }

    /**
     * 执行文件上传
     *
     * @param bucket 存储空间名称
     * @param file 文件
     * @param directory 在bucket中存储的目录
     * @return fileKey
     */
    private String doUpload(String bucket, MultipartFile file, String directory) throws IOException {
        String fileId = UUID.randomUUID().toString();
        String fileKey = fileId;
        // 指定存储目录
        if (StringUtils.isNotBlank(directory)) {
            String directoryPath = directory.startsWith("/") ? directory.substring(1) : directory;
            fileKey = directoryPath + "/" + fileId;
        }
        // 补充文件后缀
        fileKey += getFileExtension(file);
        // 执行上传
        aliOSS.upload(bucket, file, fileKey);
        return fileKey;
    }

    /**
     * 获取文件后缀
     *
     * @param file 文件
     * @return 文件后缀
     */
    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !originalFilename.isEmpty()) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                return originalFilename.substring(dotIndex);
            }
        }
        return "";
    }

    /**
     * 验证文件上传
     *
     * @param file 文件
     */
    private void checkUpload(MultipartFile file) {
        try {
            // 大小验证
            if (this.maxSize.get() != null) {
                if(this.maxSize.get() * 1024 * 1024 < file.getSize()) {
                    throw new BusinessException(ResponseStatus.NOT_ALLOWED.getCode(), "文件大小超过限制");
                }
            }
            // 格式验证
            if (this.fileTypes.get() != null && this.fileTypes.get().length > 0) {
                // 获取文件名称
                String filename = file.getOriginalFilename();
                // 无后缀 && 存在格式限制
                if (filename == null) {
                    throw new BusinessException(ResponseStatus.NOT_ALLOWED.getCode(), "文件格式不正确");
                }
                filename = filename.toLowerCase();
                // 验证是否存在预设的格式
                boolean isFault = true;
                for (String fileType: this.fileTypes.get()) {
                    if (filename.endsWith(fileType.toLowerCase())) {
                        isFault = false;
                        break;
                    }
                }
                if (isFault) {
                    throw new BusinessException(ResponseStatus.NOT_ALLOWED.getCode(), "文件格式不正确");
                }
            }
        } finally {
            // 清理大小限制、类型限制等数据（支持并发上传）
            this.maxSize.set(null);
            this.fileTypes.set(null);
        }
    }

    @Data
    @ApiModel("上传结果")
    @AllArgsConstructor
    public static class UploadResult {

        @ApiModelProperty("源文件名称")
        private String originalFilename;

        @ApiModelProperty("文件的key")
        private String fileKey;

        @ApiModelProperty("访问路径/下载路径")
        private String accessUri;
    }
}
