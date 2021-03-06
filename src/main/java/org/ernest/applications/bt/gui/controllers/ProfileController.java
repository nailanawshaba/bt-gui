package org.ernest.applications.bt.gui.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.ernest.applications.bt.gui.entities.UserDto;
import org.ernest.applications.bt.gui.entities.ValidationInfo;
import org.ernest.applications.bt.gui.services.TeamDataService;
import org.ernest.applications.bt.gui.services.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileController {
	
	@Autowired
	UserDataService userDataService;
	
	@Autowired
	TeamDataService teamDataService;

	@RequestMapping("/profile")
	public String getProfile(Model model) {
		
		UserDto userDto = userDataService.getUser(((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
		
		model.addAttribute("memberName", ((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserName());
		model.addAttribute("name", userDto.getName());
		model.addAttribute("avatarIcon", userDto.getAvatarIcon());
		model.addAttribute("description", userDto.getDescription());
		model.addAttribute("statistics", userDto.getStatistics());
		model.addAttribute("bikes", userDto.getBikesList());
		model.addAttribute("teams", userDto.getTeamsJoined().stream().map(teamId -> {return teamDataService.getTeam(teamId);}).collect(Collectors.toList()));
		model.addAttribute("stagesJoined", userDataService.getUserStagesJoinnedMap(((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId()));
		model.addAttribute("currentMonth", new Date().getMonth());
		
		return "profile";
	}

	@RequestMapping(value= "/profile/savepersonalinformation", method = RequestMethod.POST)
	@ResponseBody
    public void savePersonalInformation(@RequestParam(value="name") String name, @RequestParam(value="description") String description, @RequestParam(value="icon") String icon) {
		if(!name.isEmpty()) userDataService.saveName(((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId(), name);
		if(!description.isEmpty()) userDataService.saveDescription(((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId(), description);
		userDataService.saveAvatarIcon(((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId(), icon);
		
	}
	
	@RequestMapping(value= "/profile/saveskills", method = RequestMethod.POST)
	@ResponseBody
    public void saveSkills(@RequestParam(value="resistence") int resistence, 
    					   @RequestParam(value="sprint") int sprint,
    					   @RequestParam(value="montain") int montain,
    					   @RequestParam(value="flat") int flat,
    					   @RequestParam(value="btt") int btt,
    					   @RequestParam(value="road") int road) {
		
		userDataService.saveSkills(((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId(), resistence, sprint, montain, flat, btt, road);
	}
	
	@RequestMapping(value = "/profile/addbike", method = RequestMethod.POST)
	@ResponseBody
	public void addBike(@RequestParam(value="name") String name) throws IOException {
		
		userDataService.addBike(((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId(), name);
	}
	
	@RequestMapping(value= "/profile/deltebikes", method = RequestMethod.POST)
	@ResponseBody
    public void deleteBikes(@RequestParam(value="ids") String bikesIds) {
		userDataService.deleteBikes(((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId(), Arrays.asList(bikesIds.split(",")));
	}
	
	@RequestMapping(value= "/profile/createteam", method = RequestMethod.POST)
	@ResponseBody
    public void createTeam(@RequestParam(value="name") String name) {
		String userId = ((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
		String teamId = teamDataService.create(name, userId);
		userDataService.joinTeam(teamId, userId);
	}
	
	@RequestMapping(value= "/profile/unjointeam", method = RequestMethod.POST)
	@ResponseBody
    public void unjoinTeam(@RequestParam(value="teamId") String teamId) {
		String userId = ((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
		teamDataService.unjoinTeam(teamId, userId);
		userDataService.unjoinTeam(teamId, userId);
	}
	
	@RequestMapping(value= "/profile/jointeam", method = RequestMethod.POST)
	@ResponseBody
    public void joinTeam(@RequestParam(value="teamId") String teamId) {
		String userId = ((ValidationInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
		teamDataService.joinTeam(teamId, userId);
		userDataService.joinTeam(teamId, userId);
	}
}