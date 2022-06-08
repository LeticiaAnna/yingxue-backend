package cn.annna;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.annna.dao.UserMapper;
import cn.annna.entity.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MasterApplication.class)
public class TestPOI {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testExportEntity(){
        try {
            ExportParams exportParams = new ExportParams("应学平台用户表","users");
            List<User> list = userMapper.selectAll();
            System.out.println(list);
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, list);

            FileOutputStream fileOutputStream = new FileOutputStream("c:\\user.xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(sdf.format(new Date()));
        System.out.println(date);
    }

}
