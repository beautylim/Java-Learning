package org.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stu")
public class StudentController {

    @PreAuthorize("hasAuthority('stu:select')")
    @GetMapping("/{id}")
    public String get(@PathVariable String id) {
        return "Query student " + id;
    }

    @PreAuthorize("hasAuthority('stu:select')")
    @GetMapping()
    public String getAll() {
        return "Query all students";
    }


    @PreAuthorize("hasAuthority('stu:insert')")
    @PostMapping
    public String post() {
        return "Add students";
    }

    @PreAuthorize("hasAuthority('stu:update')")
    @PutMapping("/{id}")
    public String put(@PathVariable String id) {
        return "Change student " + id;
    }

    @PreAuthorize("hasAuthority('stu:delete')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        return "Delete student " + id;
    }
}
