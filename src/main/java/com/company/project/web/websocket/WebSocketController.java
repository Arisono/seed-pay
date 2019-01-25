package com.company.project.web.websocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.dao.DeviceDOMapper;
import com.company.project.dao.OutInRecordDOMapper;
import com.company.project.dao.StudentDOMapper;
import com.company.project.model.DeviceDO;
import com.company.project.model.StudentDO;
import com.company.project.model.school.Command;
import com.company.project.utils.ImgUtil;
import com.company.project.web.socket.WebSocket;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@RestController
@Api(value = "设备业务接口", description = "设备业务接口")
@RequestMapping("/websocket")
public class WebSocketController {

	@Autowired
	private WebSocket webSocketServer;
	@Resource
	public DeviceDOMapper deviceDOMapper;
	@Resource
	public StudentDOMapper studentDOMapper;
	@Resource
	public OutInRecordDOMapper outInRecordDOMapper;

	@SuppressWarnings("static-access")
	@ApiOperation(value = "向设备发送指令", notes = "")
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public Result send(@RequestParam String message, @RequestParam String ip) {
		try {
			webSocketServer.sendMessage(message, ip);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("发送异常：" + e.toString());
		}

		return ResultGenerator.genSuccessResult("发送成功！");
	}
	@SuppressWarnings("static-access")
	@ApiOperation(value = "查看已连接设备列表", notes = "")
	@GetMapping("/list")
	public Result list() {
		String[] data = webSocketServer
				.getSessionIP(webSocketServer.getSessionPool());
		return ResultGenerator.genSuccessResult(data);
	}

	@SuppressWarnings("static-access")
	@ApiOperation(value = "设置单个设备超时时间", notes = "")
	@GetMapping("/wsSetTimeOut")
	public Result setTimeOut(@RequestParam long time, @RequestParam String ip) {
		try {
			webSocketServer.setSessionTimeOut(time, ip);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("发生异常：" + e.toString());
		}
		return ResultGenerator.genSuccessResult("设置成功！");
	}

	@ApiOperation(value = "设备设置授权码", notes = "设备只有设置好授权码才能正常使用")
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/wsSendAuthCode", method = RequestMethod.GET)
	public Result sendAuthCode(@RequestParam String ip) {
		String authCode = "{\"cmd\":\"16\",\"data\":\"cDjkGSV8arRqR92ZJOCRQBWU3qShbug2pw9vPb3fgHzJbd7Ka2jFaHXJgeXs8KHhCUaN3AVzjLeF\\nVU79aes8DEVtPUaQJ/2j/gFzWzPeN9nN7OtW4QElN4EHB44jnrH+N4PAhwsEKEtw3XRn5phwUABU\\nmclAmzm83osxyEUCMVM=\"}";
		webSocketServer.sendMessage(authCode, ip);
		return ResultGenerator.genSuccessResult("设置成功！");
	}

	@ApiOperation(value = "设备启动", notes = "设备UDP启动广播")
	@GetMapping("/setUdp")
	public Result sendUdp(@RequestParam(required = false) String message) {
		String host = "255.255.255.255";
		int port = 10085;
		if (StringUtils.isEmpty(message)) {
			// 本服务的ws服务地址
			message = "10.1.80.196:8085";
		}
		try {
			InetAddress adds = InetAddress.getByName(host);
			@SuppressWarnings("resource")
			DatagramSocket ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(message.getBytes(),
					message.length(), adds, port);
			ds.send(dp);
			return ResultGenerator.genSuccessResult("发送成功！");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	/**
	 * 断开ws连接
	 * 
	 * @param ip
	 * @return
	 */
	@SuppressWarnings("static-access")
	@ApiOperation(value = "断开设备连接", notes = "断开websocket连接")
	@GetMapping("/wsClose")
	public Result closeDevice(@RequestParam String ip) {
		webSocketServer.close(ip);
		return ResultGenerator.genSuccessResult("关闭成功！");
	}

	/**
	 * 
	 * 上传白名单
	 * 
	 * @return
	 */
	@ApiOperation(value = "上传学生头像", notes = "上传和更新学生头像")
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/wsUploadPerson", method = RequestMethod.POST)
	public Result wsUploadPerson(@RequestParam String studId,
			@RequestParam String ip, @RequestParam MultipartFile file) {
		FileOutputStream fos;
		FileInputStream fs;
		System.out.println("文件名：" + file.getOriginalFilename());
		String filePath = "C://Users//Arison//Desktop//upload//"
				+ file.getOriginalFilename();
		try {
			fos = new FileOutputStream(new File(filePath));
			fs = (FileInputStream) file.getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fs.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StudentDO stuDao = new StudentDO();
		stuDao.setStuId(Long.valueOf(studId));
		stuDao = studentDOMapper.selectOne(stuDao);
		stuDao.setStuPhoto(filePath);
		Condition condition = new Condition(DeviceDO.class);
		Criteria c1 = condition.createCriteria();
		c1.andCondition("stu_id=", studId);
		studentDOMapper.updateByCondition(stuDao, condition);

		String stuPhoto = filePath;

		DeviceDO dModel = new DeviceDO();
		dModel.setDeviceip(ip);
		DeviceDO fModel = deviceDOMapper.selectOne(dModel);

		System.out.println("存入头像：" + stuPhoto);

		String serialnum = fModel.getSerialnumber();// 设备的序列号
		String base64 = ImgUtil.imgToBaseStr(stuPhoto);// 图片的Base64图片
		Map<String, Object> map = new HashMap<>();
		map.put("code", stuDao.getStuId()); // 工号
		map.put("name", stuDao.getStuName()); // 姓名
		map.put("identity", String.valueOf(stuDao.getStuId())); // 身份证号
		map.put("cardNum", stuDao.getStuId()); // IC卡编号
		map.put("type", "1"); // 类型（1白名单，2黑名单）
		map.put("image", base64); // 头像图片（Base64）
		map.put("photoname", stuDao.getStuName()); // 入库相片名称
		map.put("sex", "1"); // 性别（男，女）
		map.put("organization", String.valueOf(stuDao.getSchoolId())); // 所属组织
		map.put("position", "学生"); // 职位名称
		map.put("mail", "784602719@qq.com"); // 邮箱
		map.put("hiredate", stuDao.getStuEnrollDate().toString()); // 入校日期
		map.put("entrancenum", "01"); // 门禁编号
		map.put("serialNo", serialnum); // 序列号
		map.put("time", String.valueOf(new Date().getTime())); // 创建时间

		if (fModel.getIsonline() == 1 && fModel.getIsauthorized() == 1) {
			webSocketServer.sendMessage(new Command<>("3", map).toJsonStr(),
					fModel.getDeviceip());
			return ResultGenerator.genSuccessResult("发送成功！");
		} else {
			return ResultGenerator.genSuccessResult("没有上线");
		}

	}

	/**
	 * 删除设备注册人员
	 * 
	 * @param ip
	 * @param stuId
	 * @return
	 */
	@ApiOperation(value = "删除设备学生人脸照", notes = "删除设备注册是白名单和黑名单人员")
	@SuppressWarnings("static-access")
	@GetMapping("/wsDeletePerson")
	public Result wsDeletePerson(@RequestParam String ip,
			@RequestParam String stuId) {
		webSocketServer.sendMessage(new Command<>("9", stuId).toJsonStr(), ip);
		return ResultGenerator.genSuccessResult("发送成功！");
	}

	/**
	 * 刷新 设备的上传识别记录
	 * 
	 * @param ip
	 * @return
	 */
	@ApiOperation(value = "获取设备识别记录", notes = "获取设备识别记录")
	@SuppressWarnings("static-access")
	@GetMapping("/wsGetPhotoHistory")
	public Result wsGetPhotoHistory(@RequestParam String ip) {
		webSocketServer.sendMessage(new Command<>("11").toJsonStr(), ip);
		return ResultGenerator.genSuccessResult("发送成功！");
	}

	/**
	 * 查询设备是否是手动还是自动
	 * 
	 * @param ip
	 * @return
	 */
	@ApiOperation(value = "查询设备开闸模式", notes = "在设备表中更新开闸状态")
	@SuppressWarnings("static-access")
	@GetMapping("/wsGetDeviceModel")
	public Result wsGetDeviceModel(@RequestParam String ip) {
		webSocketServer.sendMessage(new Command<>("18").toJsonStr(), ip);
		return ResultGenerator.genSuccessResult("发送成功！");
	}

	/**
	 * 设置设备开闸模式
	 * 
	 * @param ip
	 * @param auto
	 * @return
	 */
	@SuppressWarnings("static-access")
	@ApiOperation(value = "设置设备开闸模式", notes = "自动开闸yes  手动开闸no")
	@GetMapping("/wsSetDeviceModel")
	public Result wsSetDeviceModel(@RequestParam String ip,
			@RequestParam String auto) {
		Map<String, String> map = new HashMap<>();
		DeviceDO dModel = new DeviceDO();
		dModel.setDeviceip(ip);
		DeviceDO fModel = deviceDOMapper.selectOne(dModel);
		map.put("auto", auto); // 工号
		map.put("serialNumber", String.valueOf(fModel.getSerialnumber())); // 设备唯一序列号
		map.put("url", ""); // 办公检测接口
		map.put("way", "0"); // 1 进，0 出
		webSocketServer.sendMessage(new Command<>("17", map).toJsonStr(), ip);
		return ResultGenerator.genSuccessResult("发送成功！");
	}

	/**
	 * 远程设备开门
	 * 
	 * @param ip
	 * @return
	 */
	@ApiOperation(value = "远程设备开门", notes = "")
	@SuppressWarnings("static-access")
	@GetMapping("/wsDeviceOpen")
	public Result wsDeviceOpen(@RequestParam String ip) {
		webSocketServer.sendMessage(new Command<>("12").toJsonStr(), ip);
		return ResultGenerator.genSuccessResult("发送成功！");
	}

	/**
	 * 远程设备关机
	 * 
	 * @param ip
	 * @return
	 */
	@ApiOperation(value = "远程设备关机", notes = "")
	@SuppressWarnings("static-access")
	@GetMapping("/wsDeviceShutdown")
	public Result wsDeviceShutdown(@RequestParam String ip) {
		webSocketServer.sendMessage(new Command<>("13").toJsonStr(), ip);
		return ResultGenerator.genSuccessResult("发送成功！");
	}

	/**
	 * 远程设备重启
	 * 
	 * @param ip
	 * @return
	 */
	@ApiOperation(value = "远程设备重启", notes = "")
	@SuppressWarnings("static-access")
	@GetMapping("/wsDeviceRestart")
	public Result wsDeviceRestart(@RequestParam String ip) {
		webSocketServer.sendMessage(new Command<>("14").toJsonStr(), ip);
		return ResultGenerator.genSuccessResult("发送成功！");
	}

	/**
	 * 手工添加设备
	 * 
	 * @return
	 */
	@ApiOperation(value = "添加设备", notes = "")
	@PostMapping("/addDevice")
	public Result addDevice(@RequestParam String devicename,
			@RequestParam String serialnumber, @RequestParam String secretkey,
			@RequestParam int schoolid) {
		DeviceDO deviceDO = new DeviceDO();
		deviceDO.setDevicename(devicename);
		deviceDO.setSerialnumber(serialnumber);
		deviceDO.setSecretkey(secretkey);
		deviceDO.setIsonline(0);
		deviceDO.setIsauthorized(0);
		deviceDO.setIsautomatic(0);
		deviceDO.setSchoolid(schoolid);
		deviceDOMapper.insert(deviceDO);
		return ResultGenerator.genSuccessResult("添加成功！");
	}
}
