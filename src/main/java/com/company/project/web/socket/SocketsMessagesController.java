package com.company.project.web.socket;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.SocketsMessages;
import com.company.project.service.SocketsMessagesService;
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
@RequestMapping("/sockets/messages")
public class SocketsMessagesController {
    @Resource
    private SocketsMessagesService socketsMessagesService;

    @PostMapping("/add")
    public Result add(SocketsMessages socketsMessages) {
        socketsMessagesService.save(socketsMessages);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        socketsMessagesService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(SocketsMessages socketsMessages) {
        socketsMessagesService.update(socketsMessages);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        SocketsMessages socketsMessages = socketsMessagesService.findById(id);
        return ResultGenerator.genSuccessResult(socketsMessages);
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SocketsMessages> list = socketsMessagesService.findAll();
        PageInfo<SocketsMessages> pageInfo = new PageInfo<SocketsMessages>(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
