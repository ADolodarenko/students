package org.dav.learn.students.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Student implements Comparable<Student>
{
	// поле ИД СТУДЕНТА
	private int studentId;
	// поле ИМЯ
	private String firstName;
	// поле ФАМИЛИЯ
	private String surName;
	// поле ОТЧЕСТВО
	private String patronymic;
	// поле ДАТА РОЖДЕНИЯ
	private Date dateOfBirth;
	// поле ПОЛ
	private char sex;
	// поле ИД ГРУППЫ
	private int groupId;
	// поле ГОД ОБУЧЕНИЯ
	private int educationYear;
	
	public Student(ResultSet rs) throws SQLException
	{
		setStudentId(rs.getInt(1));
		setFirstName(rs.getString(2));
		setPatronymic(rs.getString(3));
		setSurName(rs.getString(4));
		setSex(rs.getString(5).charAt(0));
		setDateOfBirth(rs.getDate(6));
		setGroupId(rs.getInt(7));
		setEducationYear(rs.getInt(8));
	}
	
	@Override
	public int compareTo(Student o)
	{
		Collator collator = Collator.getInstance(new Locale("ru"));
		collator.setStrength(Collator.PRIMARY);
		return collator.compare(this.toString(), o.toString());
	}
	
	@Override
	public String toString()
	{
		return surName + " " + firstName + " " + patronymic + ", "
				+ DateFormat.getDateInstance(DateFormat.SHORT).format(dateOfBirth)
				+ ", Группа ИД=" + groupId + " Год:" + educationYear;
	}
	
	public int getStudentId()
	{
		return studentId;
	}
	
	public void setStudentId(int studentId)
	{
		this.studentId = studentId;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getSurName()
	{
		return surName;
	}
	
	public void setSurName(String surName)
	{
		this.surName = surName;
	}
	
	public String getPatronymic()
	{
		return patronymic;
	}
	
	public void setPatronymic(String patronymic)
	{
		this.patronymic = patronymic;
	}
	
	public Date getDateOfBirth()
	{
		return dateOfBirth;
	}
	
	public void setDateOfBirth(Date dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}
	
	public char getSex()
	{
		return sex;
	}
	
	public void setSex(char sex)
	{
		this.sex = sex;
	}
	
	public int getGroupId()
	{
		return groupId;
	}
	
	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}
	
	public int getEducationYear()
	{
		return educationYear;
	}
	
	public void setEducationYear(int educationYear)
	{
		this.educationYear = educationYear;
	}
}
