package com.itheima.controller;

import com.itheima.pojo.Items;
import com.itheima.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/10/18
 */
@Controller
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;

    @GetMapping("list")// GetMapping,PutMapping, PostMapping, DeleteMapping RestFul用得多
    //@RequestMapping(value = "list", method = RequestMethod.GET)
    public String findAll(Model model){
        List<Items> items = itemsService.findAll();
        model.addAttribute("items",items);
        return "items";
    }
}
