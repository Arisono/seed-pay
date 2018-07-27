package com.company.project.web.socket;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Sockets;
import com.company.project.service.SocketsService;
import com.company.project.socket.SocketServerResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/06/20.
*/
@RestController
@RequestMapping("/sockets")
public class SocketsController {
	
    @Resource
    private SocketsService socketsService;
    
    @Resource
    private SocketServerResponse sockets;

    @PostMapping("/add")
    public Result add(Sockets sockets) {
        socketsService.save(sockets);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        socketsService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Sockets sockets) {
        socketsService.update(sockets);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Sockets sockets = socketsService.findById(id);
        return ResultGenerator.genSuccessResult(sockets);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Sockets> list = socketsService.findAll();
        PageInfo<Sockets> pageInfo = new PageInfo<Sockets>(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    
    
    /**
     * 向指定客户端发送ip协议
     * @param ip
     * @param msg
     * @return
     */
    @PostMapping("/send")
    public Result send(@RequestParam String ip,@RequestParam String msg) {
      sockets.sendMessage(ip, msg);
      return ResultGenerator.genSuccessResult();
    }
    
    
    
    /**
     * 强制断开设备连接
     * @param ip
     * @return
     */
    @PostMapping("/stop")
    public Result stop(@RequestParam String ip){
	  sockets.stopSocket(ip);
	  return ResultGenerator.genSuccessResult();
    }
    
    
   
    /**
     * 开锁命令
     * @return
     */
    @GetMapping("/openDevice")
    public  Result openDevice(@RequestParam String ip){
    	sockets.actionDevid(ip,"01020f0f01","8004");
    	return ResultGenerator.genSuccessResult();
    }
    
    
    /**
     * 用户请求结算
     * @return
     */
    @GetMapping("/payDevice")
    public Result payDevice(@RequestParam String ip){
    	sockets.actionDevid(ip,"01020f0f01","800C");
    	return ResultGenerator.genSuccessResult();
    }
    
    
    @GetMapping("/actionDevice")
    public Result payDevice(@RequestParam String ip,@RequestParam String data,@RequestParam String command){
    	sockets.actionDevid(ip,data,command);
    	return ResultGenerator.genSuccessResult();
    }
    
}
