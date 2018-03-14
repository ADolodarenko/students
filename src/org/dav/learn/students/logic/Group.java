package org.dav.learn.students.logic;

public class Group
{
	// поле ИД ГРУППЫ
	private int groupId;
	// поле ИМЯ ГРУППЫ
	private String nameGroup;
	// поле КУРАТОР
	private String curator;
	// поле СПЕЦИАЛЬНОСТЬ
	private String speciality;
	
	@Override
	public String toString()
	{
		return nameGroup;
	}
	
	public int getGroupId()
	{
		return groupId;
	}
	
	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}
	
	public String getNameGroup()
	{
		return nameGroup;
	}
	
	public void setNameGroup(String nameGroup)
	{
		this.nameGroup = nameGroup;
	}
	
	public String getCurator()
	{
		return curator;
	}
	
	public void setCurator(String curator)
	{
		this.curator = curator;
	}
	
	public String getSpeciality()
	{
		return speciality;
	}
	
	public void setSpeciality(String speciality)
	{
		this.speciality = speciality;
	}
}
