package com.joymeter.service.serviceImp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.joymeter.dao.UserDao;
import com.joymeter.entity.Result;
import com.joymeter.entity.User;
import com.joymeter.readExecl.ReadExcel;
import com.joymeter.util.FileUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import com.joymeter.service.FileService;
import com.joymeter.service.UserService;
import java.util.logging.Logger;

@Service
public class FileServiceImp implements FileService {

    @Resource
    private UserDao userDao;
    @Resource
    private UserService userService;

    /**
     * 保存Excel上传的客户信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Result upload(HttpServletRequest request) throws Exception {
        Result result = new Result();
        try {
            int i = 0;
            int success = 0;
            int exist = 0;
            OutputStream outputStream;
            String fileName;
            User user;
            Map<String, Integer> num = new HashMap<>();
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = mRequest.getFileMap();
            String uploadDir = request.getSession().getServletContext().getRealPath("/") + FileUtil.UPLOADDIR;
            File file = new File(uploadDir);
            if (!file.exists()) {
                file.mkdir();
            }
            for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext(); i++) {
                Map.Entry<String, MultipartFile> entry = it.next();
                MultipartFile mFile = entry.getValue();
                fileName = mFile.getOriginalFilename();
                String storeName = FileUtil.rename(fileName);
                String noExcelName = uploadDir + storeName;
                String excelName = FileUtil.excelName(noExcelName);
                outputStream = new FileOutputStream(excelName);
                FileCopyUtils.copy(mFile.getInputStream(), outputStream);
                ReadExcel xlsMain = new ReadExcel();
                List<User> list = xlsMain.readXls(request, storeName);
                for (int j = 0; j < list.size(); j++) {
                    user = list.get(j);
                    if (user.getMeter_no() == null) {
                        continue;
                    }
                    User existUser = userDao.checkMeterNoExist(user.getMeter_no());
                    if (existUser == null) {
                        user.setOnline_synv_code(user.getId_card_no());
                        if (user.getId_card_no() == null || user.getId_card_no().isEmpty()) {
                            user.setId_card_no(user.getUser_address_room());
                        }
                        userService.regist(user);
                        success++;
                    } else {
                        existUser.setOnline_synv_code(user.getId_card_no());
                        userDao.updateOnline_synv_code(existUser);
                        exist++;
                    }
                }
            }
            num.put("success", success);
            num.put("exist", exist);
            result.setStatus(1);
            result.setData(num);
        } catch (IOException e) {
            result.setStatus(0);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
}
