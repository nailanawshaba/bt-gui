package org.ernest.applications.bt.gui.entities;

import java.util.ArrayList;
import java.util.List;

public class TeamDto {

	private String id;
	private String name;
	private List<UserDto> members;
	private List<StageDto> stages;
	private List<CommentDto> comments;
	private List<NoticeDto> notices;
	
	public TeamDto(){
		members = new ArrayList<UserDto>();
		stages = new ArrayList<StageDto>();
		comments = new ArrayList<CommentDto>();
		notices = new ArrayList<NoticeDto>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<UserDto> getMembers() {
		return members;
	}
	public void setMembers(List<UserDto> members) {
		this.members = members;
	}
	public List<StageDto> getStages() {
		return stages;
	}
	public void setStages(List<StageDto> stages) {
		this.stages = stages;
	}
	public List<CommentDto> getComments() {
		return comments;
	}
	public void setComments(List<CommentDto> comments) {
		this.comments = comments;
	}
	public List<NoticeDto> getNotices() {
		return notices;
	}
	public void setNotices(List<NoticeDto> notices) {
		this.notices = notices;
	}
}