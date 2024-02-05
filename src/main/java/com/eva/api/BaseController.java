package com.eva.api;

import com.eva.core.model.AppConfig;
import com.eva.core.model.LoginUserInfo;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseController {

    @Resource
    protected AppConfig projectConfig;

    /**
     * 获取当前登录用户
     */
    protected LoginUserInfo getLoginUser () {
        return Utils.Session.getLoginUser();
    }

    /**
     * 获取ID集合
     *
     * @param ids 使用","隔开的多个ID
     * @return List<Integer>
     */
    protected List<Integer> getIdList (String ids) {
        String [] idArray = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String id : idArray) {
            idList.add(Integer.valueOf(id));
        }
        return idList;
    }

    /**
     * 获取文件字节流
     *
     * @param is 输入流
     * @return ByteArrayOutputStream
     */
    protected ByteArrayOutputStream getOutputStream (InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] bs = new byte[is.available()];
        int len;
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        return os;
    }
}
