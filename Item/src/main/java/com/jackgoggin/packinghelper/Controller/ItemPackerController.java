package com.jackgoggin.packinghelper.Controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.jackgoggin.packinghelper.Model.PackSize;
import com.jackgoggin.packinghelper.Service.ItemPackerService;

@Controller
public class ItemPackerController {

    @Autowired
    private ItemPackerService itemPackerService;

    @GetMapping("/order")
    public String getMethodName() {
        return "index.html";
    }

    @PostMapping("/order")
    public String postMethodName(@RequestParam("orderSize") Integer orderSize, Model model) {
        HashMap<PackSize, Integer> packs = itemPackerService.packageItems(orderSize);
        System.out.println(packs);

        model.addAttribute("packs", packs);
        return "index.html";
    }

}
