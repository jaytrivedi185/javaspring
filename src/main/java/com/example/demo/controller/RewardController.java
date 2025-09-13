package com.example.demo.controller;

import com.example.demo.model.Reward;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rewards")
public class RewardController {

    private List<Reward> rewardList = new ArrayList<>();
    private long currentId = 1;

    // Initial sample data
    public RewardController() {
        rewardList.add(new Reward(currentId++, "Gold Coin", 100.0, "Shiny gold coin"));
        rewardList.add(new Reward(currentId++, "Silver Medal", 50.0, "Silver medal reward"));
    }



    
    // VIEW all rewards
    @GetMapping
    public String viewRewards(Model model) {
        model.addAttribute("rewards", rewardList);
        return "rewards";
    }

    // ADD form
    @GetMapping("/add")
    public String addRewardForm(Model model) {
        model.addAttribute("reward", new Reward());
        return "add-reward";
    }

    // SAVE reward
    @PostMapping("/save")
    public String saveReward(@ModelAttribute Reward reward) {
        if (reward.getId() == null) {
            reward.setId(currentId++);
            rewardList.add(reward);
        } else {
            rewardList.stream()
                .filter(r -> r.getId().equals(reward.getId()))
                .findFirst()
                .ifPresent(r -> {
                    r.setName(reward.getName());
                    r.setPrice(reward.getPrice());
                    r.setDescription(reward.getDescription());
                });
        }
        return "redirect:/rewards";
    }

    // EDIT form
    @GetMapping("/edit/{id}")
    public String editRewardForm(@PathVariable Long id, Model model) {
        Optional<Reward> rewardOpt = rewardList.stream().filter(r -> r.getId().equals(id)).findFirst();
        if (rewardOpt.isPresent()) {
            model.addAttribute("reward", rewardOpt.get());
            return "edit-reward";
        } else {
            return "redirect:/rewards";
        }
    }

    // DELETE reward
    @GetMapping("/delete/{id}")
    public String deleteReward(@PathVariable Long id) {
        rewardList.removeIf(r -> r.getId().equals(id));
        return "redirect:/rewards";
    }
}
