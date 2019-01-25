package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.StudentDO;
import com.company.project.service.StudentDOService;
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
@RequestMapping("/student/d/o")
public class StudentDOController {
    @Resource
    private StudentDOService studentDOService;

    @PostMapping("/add")
    public Result add(StudentDO studentDO) {
        studentDOService.save(studentDO);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        studentDOService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(StudentDO studentDO) {
        studentDOService.update(studentDO);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        StudentDO studentDO = studentDOService.findById(id);
        return ResultGenerator.genSuccessResult(studentDO);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
	@GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<StudentDO> list = studentDOService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
