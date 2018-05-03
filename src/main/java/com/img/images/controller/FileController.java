package com.img.images.controller;

import com.img.images.model.Image;
import com.img.images.service.ImageService;
import com.img.images.util.R;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("file")
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    public static final int RANDOM_RANGE = 65535;

    @Autowired
    private ImageService imageService;

    @Value("${upload.file.root}")
    private String uploadFileRoot;

    @Value("${upload.file.imageType}")
    private String imageType;

    @RequestMapping(value = "show")
    public ResponseEntity<byte[]> show(@RequestParam("path") String path, HttpServletResponse response) {
        try {
            // IE could not read ResponseEntity
            if (StringUtils.isNotBlank(path)) {
                File imageFile = new File(this.uploadFileRoot, path);
                if (imageFile.exists()) {
                    response.setHeader("Pragma", "No-cache");
                    response.setHeader("Cache-Control", "No-cache");
                    response.setHeader("Content-Type", MediaType.IMAGE_JPEG_VALUE);
                    response.setContentLength((int) imageFile.length());
                    response.setContentType("image/" + path.substring(path.lastIndexOf('.') + 1));
                    FileCopyUtils.copy(FileCopyUtils.copyToByteArray(imageFile),response.getOutputStream());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @RequestMapping(value = "download/{id}")
    @Transactional
    public ResponseEntity<byte[]> download(@RequestParam("path") String path,
                                           @PathVariable("id") Long id,
                                           @RequestParam(value = "fileName", required = false) String fileName,
                                           HttpServletResponse response) {
        try {
            if (StringUtils.isNotBlank(path)) {
                File file = new File(this.uploadFileRoot, path);
                if (file.exists()) {
                    Image image = imageService.get(id);
                    Image newImage = new Image();
                    newImage.setId(image.getId());
                    Long n = image.getDownloadNumber() + 1;
                    newImage.setDownloadNumber(n);
                    imageService.addDownloadNumber(newImage);
                    response.setHeader("Pragma", "No-cache");
                    response.setHeader("Cache-Control", "No-cache");
                    response.setHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
                    if (StringUtils.isNotBlank(fileName)) {
                        fileName = StringUtils.replace(URLEncoder.encode(fileName, "UTF-8"), "+", "%20");
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName
                                + "\";filename*=UTF-8''" + fileName);
                    } else {
                        String curFileName = StringUtils.replace(URLEncoder.encode(file.getName(), "UTF-8"), "+", "%20");
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + curFileName
                                + "\";filename*=UTF-8''" + curFileName);
                    }
                    FileCopyUtils.copy(FileCopyUtils.copyToByteArray(file),response.getOutputStream());
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 单文件上传
     * @param id
     * @param file
     * @return
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Object addImg(@RequestParam("file") final MultipartFile file,
                         @RequestParam(value = "type", defaultValue = "upload") String type) {
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        if (!imageType.contains(ext)) {
            return R.error(403, "请上传以下图片类型："+imageType);
        }
        String url = uploadFile(file, type);
        if (StringUtils.isNotBlank(url)) {
            return R.ok(204, "上传成功！").put("url", url);
        } else {
            return R.error(500, "系统原因，设置失败！稍后重试或联系管理员！");
        }
    }

    /**
     * 多文件上传
     * @param list
     * @return
     */
    @RequestMapping(value = "uploads", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestParam("file") List<MultipartFile> list,
                                      @RequestParam(value="type", defaultValue = "upload") String type) {
        Integer errno = 0;
        List<String> urls = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(2);
        if (list.size() == 0) {
            errno = 1;
        }
        for (MultipartFile file : list) {
            // 此处调用自己的上传文件方法
            String url = uploadFile(file,type);
            if (StringUtils.isNotBlank(url)) {
                urls.add("/file/show?path=" + url);
            } else {
                errno = 2;
            }
        }
        map.put("errno", errno);
        map.put("data", urls);
        return map;
    }

    private String uploadFile(MultipartFile fileData, String type) {
        try {
            String fileFullPath = getFilePath(fileData, type);
            File saveFile = new File(this.uploadFileRoot, fileFullPath);
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            fileData.transferTo(saveFile);
            return fileFullPath;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 生成文件路径
     * @param fileData
     * @param type
     * @return
     */
    private String getFilePath(MultipartFile fileData, String type) {
        String fileName = createFileName();
        String ext = fileData.getOriginalFilename().substring(fileData.getOriginalFilename().lastIndexOf(".")+1);
        String fileNameWithExtension = fileName + "." + ext;
        StringBuilder subPath = new StringBuilder();
        return type + "/" + subPath.toString() + createPathBasedData() + "/" + fileNameWithExtension;
    }

    /**
     * 生产文件名，生成随机数
     * @return
     */
    private String createFileName() {
        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
        Random random = new Random();
        return dateFormat.format(new Date()) + random.nextInt(RANDOM_RANGE);
    }

    /**
     * 创建文件日期路径
     * @return
     */
    private String createPathBasedData() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }
}

