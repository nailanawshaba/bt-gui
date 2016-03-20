package org.ernest.applications.bt.gui.controllers;

import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

import org.ernest.applications.bt.gui.entities.StageDto;
import org.ernest.applications.bt.gui.entities.StagePointDto;
import org.ernest.applications.bt.gui.entities.TeamDto;
import org.ernest.applications.bt.gui.entities.UserDto;
import org.ernest.applications.bt.gui.services.CommentDataService;
import org.ernest.applications.bt.gui.services.TeamDataService;
import org.ernest.applications.bt.gui.services.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TeamsController {

	@Autowired
	CommentDataService commentDataService;
	
	@Autowired
	UserDataService userDataService;
	
	@Autowired
	TeamDataService teamDataService;
	
	@RequestMapping("/teams")
	public String getTeams(Model model) {
		UserDto userDto = userDataService.getUser((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		model.addAttribute("teams", userDto.getTeamsJoined().stream().map(teamId -> {return teamDataService.getTeam(teamId);}).collect(Collectors.toList()));
		  
		return "teams";
	}
	
	@RequestMapping(value= "/dashboard/comment/create", method = RequestMethod.POST)
	@ResponseBody
    public void createComment(@RequestParam(value="content") String content, @RequestParam(value="teamId") String teamId) {
		String commentId = commentDataService.create((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal(), content);
		teamDataService.addComment(teamId, commentId);
	}
	
	@RequestMapping(path = "/team/{teamId}", method = RequestMethod.GET)
	public String getTeam(@PathVariable("teamId") String teamId, Model model) {
		TeamDto teamDto = teamDataService.getTeam(teamId);

		
		model.addAttribute("totalMembers", teamDto.getMembers().size());
		model.addAttribute("totalStages", teamDto.getStages().size());
		model.addAttribute("totalKilomiters", teamDto.getStages().stream().reduce(0, (sum, stage) -> sum += stage.getKilomitersTotal(), (sum1, sum2) -> sum1 + sum2));
		model.addAttribute("totalBikes", teamDto.getMembers().stream().reduce(0, (sum, member) -> sum += member.getBikesList().size(), (sum1, sum2) -> sum1 + sum2));
		
		model.addAttribute("memberId", (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		model.addAttribute("stages", teamDto.getStages().stream().sorted(Comparator.comparing(StageDto::getDate)).collect(Collectors.toList()));
		StageDto stageCloser = teamDto.getStages().stream()
									  .sorted(Comparator.comparing(StageDto::getDate))
									  .filter(stage -> stage.getDate().compareTo(new Date()) >= 0)
									  .findFirst()
									  .orElse(buildEmptyStage());
		model.addAttribute("stageCloser", stageCloser); 
		model.addAttribute("members", teamDto.getMembers());
		model.addAttribute("comments", teamDto.getComments());
		
		  
		return "team";
	}
	
	private StageDto buildEmptyStage() {
		StageDto stageDto = new StageDto();
		stageDto.setName("Join a team or create a stage...");
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		stageDto.getStagePoints().add(new StagePointDto(0.0, 0.0, 0.0, 0.0));
		return stageDto;
	}
}
