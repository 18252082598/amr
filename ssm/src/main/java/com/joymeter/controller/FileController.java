package com.joymeter.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.joymeter.util.DownloadUtil;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import com.joymeter.service.FileService;

/**
 * 文件操作
 *
 * @author Joymeter
 */
@Controller
@RequestMapping(value = "background/fileOperate")
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 上传导入的Excel文档
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "upload")
    public ModelAndView upload(HttpServletRequest request) throws Exception {
        fileService.upload(request);
        return null;
    }

    /**
     * 下载导入Excel文档
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "downloadModel")
    public ModelAndView downloadModel1(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String ctxPath = request.getSession().getServletContext().getRealPath("/file");
        String language = request.getParameter("lang1");
        String ModelName = language.equals("Chinese") ? "导入用户标准格式.xlsx" : "ImportModel.xlsx";
        String downLoadPath = ctxPath + "/" + ModelName;
        InputStream inStream = new FileInputStream(downLoadPath);
        OutputStream os = response.getOutputStream();
        response.reset();
        response.setContentType("bin");
        response.setHeader("Content-disposition", "attachment; filename="
                + new String(ModelName.getBytes("utf-8"), "ISO8859-1"));
        DownloadUtil.downloadLocal(inStream, os);
        return null;
    }
}
