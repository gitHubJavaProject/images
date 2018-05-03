package com.img.images.controller.front;

import com.img.images.controller.business.ImageController;
import com.img.images.util.R;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("front")
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
    public static final int CERT_IMAGE_WIDTH = 800;
    public static final int PRODUCT_IMAGE_WIDTH = 700;
    public static final int RANDOM_RANGE = 65535;

    @Value("${upload.file.root}")
    private String uploadFileRoot;

    @Value("${upload.file.imageType}")
    private String imageType;

    @RequestMapping("create_page")
    public ModelAndView uploadPage(ModelAndView mv) {
        mv.setViewName("front/protect/myself_upload");
        return mv;
    }

    @RequestMapping(value = "addImgUrl", method = RequestMethod.POST)
    public Object addImg(@RequestParam(value = "id", required = false) final Long id, @RequestParam("file") final MultipartFile file) {
            /*if (image.getUserId() != getLoginUser().getId().longValue()) {
                return R.error(403, "您不能操作此条数据！");
            }*/
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        if (!imageType.contains(ext)) {
            return R.error(403, "请上传以下图片类型："+imageType);
        }
        String url = uploadFile(file);
        if (StringUtils.isNotBlank(url)) {
            return R.ok(204, "上传成功！").put("url", url);
        } else {
            return R.error(500, "系统原因，设置失败！稍后重试或联系管理员！");
        }
    }

    private String uploadFile(MultipartFile fileData) {
        try {
            String fileFullPath = getFilePath(fileData, "showImage");
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

    private String getFilePath(MultipartFile fileData, String type) {
        String fileName = createFileName();
        String ext = fileData.getOriginalFilename().substring(fileData.getOriginalFilename().lastIndexOf(".")+1);
        String fileNameWithExtension = fileName + "." + ext;
        StringBuilder subPath = new StringBuilder();
        return type + "/" + subPath.toString() + createPathBasedData() + "/" + fileNameWithExtension;
    }

    private String createFileName() {
        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
        Random random = new Random();
        return dateFormat.format(new Date()) + random.nextInt(RANDOM_RANGE);
    }

    private String createPathBasedData() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date());
    }

}
