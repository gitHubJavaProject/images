package com.img.images.controller.business;

import com.img.images.model.Image;
import com.img.images.service.CategoryService;
import com.img.images.service.ImageService;
import com.img.images.util.R;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("business/p/images")
public class ImageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
    public static final int RANDOM_RANGE = 65535;
    @Autowired
    private ImageService imageService;

    @Autowired
    private CategoryService categoryService;

    @Value("${upload.file.root}")
    private String uploadFileRoot;

    @Value("${upload.file.imageType}")
    private String imageType;

    @RequestMapping("list")
    public ModelAndView images(ModelAndView mv,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                               @RequestParam(value = "name", required = false) String name) {

        mv.setViewName("business/protect/images");
        mv.addObject("name", null == name ? "" : name);
        mv.addObject("page", page);
        mv.addObject("size", size);
        mv.addObject("images", imageService.search(page, size, name));
        return mv;
    }

    @RequestMapping("_")
    public ModelAndView getDetail(ModelAndView mv) {
        mv.setViewName("business/protect/image_detail");
        mv.addObject("categories", categoryService.findAll(null,null));
        return mv;
    }

    @RequestMapping("list/{id}")
    public ModelAndView getDetailById(ModelAndView mv, @PathVariable("id") Long id) {
        mv.setViewName("business/protect/image_detail");
        mv.addObject("categories", categoryService.findAll(null,null));
        mv.addObject("image", imageService.get(id));
        return mv;
    }

    @PostMapping("add")
    public R createImage(Image image) {
        try {
            if (StringUtils.isBlank(image.getName()) || StringUtils.isBlank(image.getShowImage())
                    || StringUtils.isBlank(image.getFileUrl()) || null == image.getWidth()
                    || null == image.getHeight() || null == image.getTag() || null == image.getType()) {
                return R.error(400, "参数不正确！").put("icon", "warning");
            }
            imageService.save(image);
            return R.ok(201, "保存成功！").put("icon", "success").put("image", image);
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @PutMapping("update/{id}")
    public R updateImage(Image image, @PathVariable("id") Long id) {
        try {
            if (StringUtils.isBlank(image.getName()) || StringUtils.isBlank(image.getShowImage())
                    || StringUtils.isBlank(image.getFileUrl()) || null == image.getWidth()
                    || null == image.getHeight() || null == image.getTag() || null == image.getType()) {
                return R.error(400, "参数不正确！").put("icon", "warning");
            }
            Image oldImage = imageService.get(id);
            if (null == oldImage) {
                return R.error(404, "此数据不存在！").put("icon", "warning");
            }
            image.setId(id);
            imageService.update(image);
            return R.ok(204, "保存成功！").put("icon", "success");
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @DeleteMapping("delete/{id}")
    public R deleteImage(@PathVariable("id") Long id) {
        try {
            Image image = imageService.get(id);
            if (null == image) {
                return R.error(404, "数据不存在！").put("icon", "warning");
            }
            imageService.delete(id);
            return R.ok(204, "操作成功！").put("icon", "success");
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
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

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map<String, Object> upload(@RequestParam("file") List<MultipartFile> list) {
        Integer errno = 0;
        List<String> urls = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(2);
        if (list.size() == 0) {
            errno = 1;
        }
        for (MultipartFile file : list) {
            // 此处调用自己的上传文件方法
            String url = uploadFile(file);
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
