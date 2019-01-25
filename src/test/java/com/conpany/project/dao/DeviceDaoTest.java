package com.conpany.project.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.alibaba.fastjson.JSON;
import com.alipay.api.domain.ArrangementBaseSelector;
import com.company.project.dao.DeviceDOMapper;
import com.company.project.dao.OutInRecordDOMapper;
import com.company.project.dao.StudentDOMapper;
import com.company.project.model.DeviceDO;
import com.company.project.model.OutInRecordDO;
import com.company.project.model.StudentDO;
import com.company.project.model.school.Command;
import com.company.project.utils.ImgUtil;
import com.company.project.web.socket.WebSocket;
import com.conpany.project.Tester;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

public class DeviceDaoTest  extends Tester {
  
	@Resource
	public DeviceDOMapper deviceDOMapper;
	@Resource
	public StudentDOMapper studentDOMapper;
	@Resource
	public OutInRecordDOMapper outInRecordDOMapper;
	
	
	@Autowired
    private WebSocket webSocketServer;
	
	 // 事务自动回滚，默认是true。可以不写
    /**
     * 设备添加，注册
     */
    @Test
	public void testAddDevice(){
		DeviceDO deviceDO=new DeviceDO();
		deviceDO.setDevicename("Face recognition");
		deviceDO.setDevicemodel("ght");
		deviceDO.setSerialnumber("3100-1d17-72be-ed51-cac9");
		deviceDO.setVersion("1.1");
		deviceDO.setDevicemac("10:d0:7a:36:ae:4e");
		deviceDO.setDevicemaker("SZ GHT");
		deviceDO.setSecretkey("123456");
		deviceDO.setDeviceip("10.1.80.31");
		
		deviceDO.setIsonline(1);
		deviceDO.setIsauthorized(1);
		deviceDO.setIsautomatic(1);
		deviceDO.setSchoolid(10001);
		deviceDOMapper.insert(deviceDO);
	}
    
    /**
     * 查找所有设备
     */
    @Rollback(false)
    @Test
	public void testFindDeviceList(){
		 List<DeviceDO> devices=new ArrayList<>();
		 devices=deviceDOMapper.selectAll();
		 System.out.println("设备列表："+JSON.toJSONString(devices));
	
	}
    
    /**
     * 更新设备状态
     */
    @Rollback(false)
    @Test
    public void testUpdateDeviceState(){
    	 Condition condition = new Condition(DeviceDO.class);
         Criteria c1 = condition.createCriteria();
        // c1.orCondition("deviceIp=", "10.1.80.31");
         c1.andCondition("serialNumber=","3100-1d17-72be-ed51-cac9");
    	//修改设备状态，授权状态，在线状态，手动和自动开闸状态，修改IP地址
    	//更新方法二：
    	 DeviceDO dModel=	new DeviceDO();
    	 dModel.setDeviceip("10.1.80.30");;
    	 DeviceDO fModel= deviceDOMapper.selectOne(dModel);
    	 System.out.println("查询单个设备："+JSON.toJSONString(fModel));
    	 fModel.setIsautomatic(0);
    	 fModel.setIsauthorized(0);
    	 deviceDOMapper.updateByCondition(fModel, condition);
    	
    	//更新方法一：
//         List<DeviceDO> devices=	deviceDOMapper.selectByCondition(condition);
//         System.out.println("设备列表："+JSON.toJSONString(devices));
//    	 if (devices!=null) {
//			if (devices.size()>0) {
//				devices.get(0).setDeviceip("10.1.80.31");
//				devices.get(0).setIsonline(0);
//			deviceDOMapper.updateByCondition(devices.get(0), condition);
//			 List<DeviceDO> udevices=	deviceDOMapper.selectByCondition(condition);
//	         System.out.println("设备列表更新："+JSON.toJSONString(udevices));
//			}
//		}
    	 
    }
    
    
    /**
     * 批量更新学生头像信息
     */
    @Rollback(false)
    @Test
    public void testUpdateStudentState(){
    	StudentDO stuDao=new StudentDO();
    	stuDao.setStuId(10001L);
    	stuDao= studentDOMapper.selectOne(stuDao);
    	stuDao.setStuName("寇二");
    	System.out.println("学生10001："+JSON.toJSONString(stuDao));
    	
    	
     	Condition condition = new Condition(DeviceDO.class);
        Criteria c1 = condition.createCriteria();
        c1.andCondition("stu_id=","10001");
         //上传学生头像功能
        stuDao.setStuPhoto("C:\\Users\\Arison\\Desktop\\stuPhoto\\liujie.jpg");
        
        studentDOMapper.updateByCondition(stuDao, condition);
    }
    
    /**
     * 添加白名单到设备
     */
    @Test
    public void testWSAddPerson(){
	  StudentDO stuDao=new StudentDO();
	  stuDao.setStuId(10001L);
	  stuDao= studentDOMapper.selectOne(stuDao);
	  String stuPhoto=stuDao.getStuPhoto();
	
      System.out.println("存入头像："+stuPhoto);
    	  
	  String serialnum = "3100-1d17-72be-ed51-cac9";//设备的序列号
      String base64 = ImgUtil.imgToBaseStr(stuPhoto);//图片的Base64图片
      Map<String, String> map = new HashMap<>();
      map.put("code", String.valueOf(stuDao.getStuId())); //工号
      map.put("name", "寇二"); //姓名
      map.put("identity", String.valueOf(stuDao.getStuId())); //身份证号
      map.put("cardNum", "123123");  //IC卡编号
      map.put("type", "1");  //类型（1白名单，2黑名单）
      map.put("image", base64);  //头像图片（Base64）
      map.put("photoname", "name");  //入库相片名称
      map.put("sex", "1"); //性别（男，女）
      map.put("organization", "");  //所属组织
      map.put("position", "学生");  //职位名称
      map.put("mail", "784602719@qq.com");  //邮箱
      map.put("hiredate", "日期");  //入校日期
      map.put("entrancenum", "01");  //门禁编号
      map.put("serialNo", serialnum);  //序列号
      map.put("time", String.valueOf(new Date().getTime()));  //创建时间
          
          
          
     	 DeviceDO dModel=	new DeviceDO();
    	 dModel.setSerialnumber("3100-1d17-72be-ed51-cac9");
    	 DeviceDO fModel= deviceDOMapper.selectOne(dModel);
    	 System.out.println("发送命令："+new Command<>("3", map).toJsonStr());
          if (fModel.getIsonline()==1) {
        	  webSocketServer.sendMessage(new Command<>("3", map).toJsonStr(), fModel.getDeviceip());
		}else{
			System.out.println("设备"+fModel.getDeviceip()+"没有上线");
			
		}
        
    }
    
    
    /**
     * 删除白名单人员
     */
    @Test
    public void testWSDeletePerson(){
    	
    }
    
    
    /**
     * 获取白名单列表
     */
    @Test
    public  void  testWSGetPersonList(){
    	
    }
    
    
    /**
     * 获取上传识别记录
     * 识别记录保存
     */
    @Rollback(false)
    @Test
    public void testWSGetHistoryList(){
    	OutInRecordDO outModel=new OutInRecordDO();
    	try {
			outModel.setInDate(DateUtils.parseDate("2019-08-12 08:00:12", "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	outModel.setRecordDetails("C://Users//Arison//Desktop//upload//liuie.jpg");
    	outModel.setRecordName("刘杰进校记录");
    	outModel.setStuId(10001L);
    	outModel.setSchoolId(10001L);
    	outInRecordDOMapper.insert(outModel);
    }
    
    public void testWS(){
    	
    }
    
    
}
