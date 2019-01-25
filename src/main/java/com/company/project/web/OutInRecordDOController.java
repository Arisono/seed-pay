package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.OutInRecordDO;
import com.company.project.service.OutInRecordDOService;
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
* Created by CodeGenerator on 2019/01/18.
*/
@RestController
@RequestMapping("/out/in/record/d/o")
public class OutInRecordDOController {
    @Resource
    private OutInRecordDOService outInRecordDOService;

    @PostMapping("/add")
    public Result add(OutInRecordDO outInRecordDO) {
        outInRecordDOService.save(outInRecordDO);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        outInRecordDOService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(OutInRecordDO outInRecordDO) {
        outInRecordDOService.update(outInRecordDO);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        OutInRecordDO outInRecordDO = outInRecordDOService.findById(id);
        return ResultGenerator.genSuccessResult(outInRecordDO);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
	@GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<OutInRecordDO> list = outInRecordDOService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
