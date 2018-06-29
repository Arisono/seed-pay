package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.BookmarkClassic;
import com.company.project.service.BookmarkClassicService;
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
* Created by CodeGenerator on 2018/05/14.
*/
@RestController
@RequestMapping("/bookmark/classic")
public class BookmarkClassicController {
    @Resource
    private BookmarkClassicService bookmarkClassicService;

    @PostMapping("/add")
    public Result add(BookmarkClassic bookmarkClassic) {
        bookmarkClassicService.save(bookmarkClassic);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        bookmarkClassicService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(BookmarkClassic bookmarkClassic) {
        bookmarkClassicService.update(bookmarkClassic);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        BookmarkClassic bookmarkClassic = bookmarkClassicService.findById(id);
        return ResultGenerator.genSuccessResult(bookmarkClassic);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @GetMapping("/list") 
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<BookmarkClassic> list = bookmarkClassicService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
