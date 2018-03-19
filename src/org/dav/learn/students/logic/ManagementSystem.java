package org.dav.learn.students.logic;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ManagementSystem
{
	private static ManagementSystem instance;
	private static DataSource dataSource;
	private static Connection con;
	
	public static synchronized ManagementSystem getInstance()
	{
		if (instance == null)
			try
			{
				instance = new ManagementSystem();
				Context ctx = new InitialContext();
				instance.dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/StudentsDS");
				con = dataSource.getConnection();
			}
			catch (NamingException e)
			{
				e.printStackTrace();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		
		return instance;
	}
	
	public List getGroups() throws SQLException
	{
		List groups = new ArrayList();
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT group_id, groupName, curator, speciality FROM groups");
		
		while (rs.next())
		{
			Group gr = new Group();
			
			gr.setGroupId(rs.getInt(1));
			gr.setNameGroup(rs.getString(2));
			gr.setCurator(rs.getString(3));
			gr.setSpeciality(rs.getString(4));
			
			groups.add(gr);
		}
		
		rs.close();
		stmt.close();
		
		return groups;
	}
	
	public Collection getAllStudents() throws SQLException
	{
		Collection students = new ArrayList();
		
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT student_id, firstName, patronymic, surName, "
				+ "sex, dateOfBirth, group_id, educationYear FROM students ORDER BY surName, firstName, patronymic");
		
		while (rs.next())
		{
			Student st = new Student(rs);
			students.add(st);
		}
		
		rs.close();
		stmt.close();
		
		return students;
	}
	
	public Collection getStudentsFromGroup(Group group, int year) throws SQLException
	{
		Collection students = new ArrayList();
		
		PreparedStatement stmt = con.prepareStatement("SELECT student_id, firstName, patronymic, surName, "
				+ "sex, dateOfBirth, group_id, educationYear FROM students "
				+ "WHERE group_id =  ? AND  educationYear =  ? "
				+ "ORDER BY surName, firstName, patronymic");
		stmt.setInt(1, group.getGroupId());
		stmt.setInt(2, year);
		
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next())
		{
			Student st = new Student(rs);
			students.add(st);
		}
		
		rs.close();
		stmt.close();
		
		return students;
	}
	
	public Student getStudentById(int studentId) throws SQLException
	{
		Student student = null;
		
		PreparedStatement stmt = con.prepareStatement("SELECT student_id, firstName, patronymic, surName,"
				+ "sex, dateOfBirth, group_id, educationYear FROM students WHERE student_id = ?");
		stmt.setInt(1, studentId);
		
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next())
			student = new Student(rs);
		
		rs.close();
		stmt.close();
		
		return student;
	}
	
	public void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear) throws SQLException
	{
		PreparedStatement stmt = con.prepareStatement("UPDATE students SET group_id =  ?, educationYear=? "
				+ "WHERE group_id =  ? AND  educationYear = ?");
		stmt.setInt(1, newGroup.getGroupId());
		stmt.setInt(2, newYear);
		stmt.setInt(3, oldGroup.getGroupId());
		stmt.setInt(4, oldYear);
		
		stmt.execute();
		
		stmt.close();
	}
	
	public void removeStudentsFromGroup(Group group, int year) throws SQLException
	{
		PreparedStatement stmt = con.prepareStatement("DELETE FROM students WHERE group_id = ? AND educationYear = ?");
		stmt.setInt(1, group.getGroupId());
		stmt.setInt(2, year);
		
		stmt.execute();
		
		stmt.close();
	}
	
	public void insertStudent(Student student) throws SQLException
	{
		PreparedStatement stmt = con.prepareStatement("INSERT INTO students "
				+ "(firstName, patronymic, surName, sex, dateOfBirth, group_id, educationYear)"
				+ "VALUES( ?,  ?,  ?,  ?,  ?,  ?,  ?)");
		stmt.setString(1, student.getFirstName());
		stmt.setString(2, student.getPatronymic());
		stmt.setString(3, student.getSurName());
		stmt.setString(4, new String(new char[]{student.getSex()}));
		stmt.setDate(5, new Date(student.getDateOfBirth().getTime()));
		stmt.setInt(6, student.getGroupId());
		stmt.setInt(7, student.getEducationYear());
		
		stmt.execute();
		
		stmt.close();
	}
	
	public void updateStudent(Student student) throws SQLException
	{
		PreparedStatement stmt = con.prepareStatement("UPDATE students "
				+ "SET firstName=?, patronymic=?, surName=?, sex=?, dateOfBirth=?, group_id=?,"
				+ "educationYear=? WHERE student_id=?");
		stmt.setString(1, student.getFirstName());
		stmt.setString(2, student.getPatronymic());
		stmt.setString(3, student.getSurName());
		stmt.setString(4, new String(new char[]{student.getSex()}));
		stmt.setDate(5, new Date(student.getDateOfBirth().getTime()));
		stmt.setInt(6, student.getGroupId());
		stmt.setInt(7, student.getEducationYear());
		stmt.setInt(8, student.getStudentId());
		
		stmt.execute();
		
		stmt.close();
	}
	
	public void deleteStudent(Student student) throws SQLException
	{
		PreparedStatement stmt = con.prepareStatement("DELETE FROM students WHERE student_id =  ?");
		stmt.setInt(1, student.getStudentId());
		
		stmt.execute();
		
		stmt.close();
	}
	
	/*
	private List<Group> groups;
	private Collection<Student> students;
	
	public void loadGroups()
	{
		// Проверяем - может быть наш список еще не создан вообще
		if (groups == null)
			groups = new ArrayList<>();
		else
			groups.clear();
		
		Group g = new Group();
		g.setGroupId(1);
		g.setNameGroup("Первая");
		g.setCurator("Доктор Борменталь");
		g.setSpeciality("Создание собачек из человеков");
		groups.add(g);
		
		g = new Group();
		g.setGroupId(2);
		g.setNameGroup("Вторая");
		g.setCurator("Профессор Преображенский");
		g.setSpeciality("Создание человеков из собачек");
		groups.add(g);
	}
	
	public void loadStudents()
	{
		if (students == null)
			// Мы используем коллекцию, которая автоматически сортирует свои элементы
			students = new TreeSet<>();
		else
			students.clear();
			
		Calendar c = Calendar.getInstance();
		
		// Вторая группа
		Student s = new Student();
		s.setStudentId(1);
		s.setFirstName("Иван");
		s.setPatronymic("Сергеевич");
		s.setSurName("Степанов");
		s.setSex('М');
		c.set(1990, 3, 20);
		s.setDateOfBirth(c.getTime());
		s.setGroupId(2);
		s.setEducationYear(2006);
		students.add(s);
		
		s = new Student();
		s.setStudentId(2);
		s.setFirstName("Наталья");
		s.setPatronymic("Андреевна");
		s.setSurName("Чичикова");
		s.setSex('Ж');
		c.set(1990, 6, 10);
		s.setDateOfBirth(c.getTime());
		s.setGroupId(2);
		s.setEducationYear(2006);
		students.add(s);
		
		// Первая группа
		s = new Student();
		s.setStudentId(3);
		s.setFirstName("Петр");
		s.setPatronymic("Викторович");
		s.setSurName("Сушкин");
		s.setSex('М');
		c.set(1991, 3, 12);
		s.setDateOfBirth(c.getTime());
		s.setEducationYear(2006);
		s.setGroupId(1);
		students.add(s);
		
		s = new Student();
		s.setStudentId(4);
		s.setFirstName("Вероника");
		s.setPatronymic("Сергеевна");
		s.setSurName("Ковалева");
		s.setSex('Ж');
		c.set(1991, 7, 19);
		s.setDateOfBirth(c.getTime());
		s.setEducationYear(2006);
		s.setGroupId(1);
		students.add(s);
	}
	
	// Получить список групп
	public List<Group> getGroups()
	{
		return groups;
	}
	
	// Получить список всех студентов
	public Collection<Student> getAllStudents()
	{
		return students;
	}
	
	// Получить список студентов для определенной группы
	public Collection<Student> getStudentsFromGroup(Group group, int year)
	{
		Collection<Student> l = new TreeSet<>();
		
		for (Student si : students)
			if (si.getGroupId() == group.getGroupId() && si.getEducationYear() == year)
				l.add(si);
		
		return l;
	}
	
	// Перевести студентов из одной группы с одним годом обучения в другую группу с другим годом обучения
	public void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear)
	{
		for (Student si : students)
			if (si.getGroupId() == oldGroup.getGroupId() && si.getEducationYear() == oldYear)
			{
				si.setGroupId(newGroup.getGroupId());
				si.setEducationYear(newYear);
			}
	}
	
	// Удалить всех студентов из определенной группы
	public void removeStudentsFromGroup(Group group, int year)
	{
		// Мы создадим новый список студентов БЕЗ тех, кого мы хотим удалить.
		// Возможно не самый интересный вариант. Можно было бы продемонстрировать
		// более элегантный метод, но он требует погрузиться в коллекции более глубоко
		// Здесь мы не ставим себе такую цель
		Collection<Student> tmp = new TreeSet<>();
		
		for (Student si : students)
			if (si.getGroupId() != group.getGroupId() || si.getEducationYear() != year)
				tmp.add(si);
		
		students = tmp;
	}
	
	// Добавить студента
	public void insertStudent(Student student)
	{
		// Просто добавляем объект в коллекцию
		students.add(student);
	}
	
	// Обновить данные о студенте
	public void updateStudent(Student student)
	{
		// Надо найти нужного студента (по его ИД) и заменить поля
		Student updStudent = null;
		
		for (Student si : students)
			if (si.getStudentId() == student.getStudentId())
			{
				// Вот этот студент - запоминаем его и прекращаем цикл
				updStudent = si;
				break;
			}
			
		updStudent.setFirstName(student.getFirstName());
		updStudent.setPatronymic(student.getPatronymic());
		updStudent.setSurName(student.getSurName());
		updStudent.setSex(student.getSex());
		updStudent.setDateOfBirth(student.getDateOfBirth());
		updStudent.setGroupId(student.getGroupId());
		updStudent.setEducationYear(student.getEducationYear());
	}
	
	// Удалить студента
	public void deleteStudent(Student student)
	{
		// Надо найти нужного студента (по его ИД) и удалить
		Student delStudent = null;
		
		for (Student si : students)
			if (si.getStudentId() == student.getStudentId())
			{
				// Вот этот студент - запоминаем его и прекращаем цикл
				delStudent = si;
				break;
			}
		
		students.remove(delStudent);
	}*/
	
	/*public static void main(String[] args)
	{
		// Этот код позволяет нам перенаправить стандартный вывод в файл
		// Т.к. на экран выводится не совсем удобочитаемая кодировка,
		// файл в данном случае более удобен
		try
		{
			System.setOut(new PrintStream("out.txt"));
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
			return;
		}
		
		ManagementSystem ms = ManagementSystem.getInstance();
		
		// Просмотр полного списка групп
		printString("Полный список групп");
		printString("*******************");
		
		List<Group> allGroups = ms.getGroups();
		
		for (Group gi : allGroups)
			printString(gi);
		
		printString();
		
		// Просмотр полного списка студентов
		printString("Полный список студентов");
		printString("***********************");
		
		Collection<Student> allStudends = ms.getAllStudents();
		
		for (Student si : allStudends)
			printString(si);
		
		printString();
		
		// Вывод списков студентов по группам
		printString("Список студентов по группам");
		printString("***************************");
		
		List<Group> groups = ms.getGroups();
		
		// Проверяем все группы
		for (Group gi : groups)
		{
			printString("---> Группа:" + gi.getNameGroup());
			// Получаем список студентов для конкретной группы
			Collection<Student> students = ms.getStudentsFromGroup(gi, 2006);
			
			for (Student si : students)
				printString(si);
		}
		
		printString();
		
		// Создадим нового студента и добавим его в список
		Student s = new Student();
		s.setStudentId(5);
		s.setFirstName("Игорь");
		s.setPatronymic("Владимирович");
		s.setSurName("Перебежкин");
		s.setSex('М');
		Calendar c = Calendar.getInstance();
		c.set(1991, 8, 31);
		s.setDateOfBirth(c.getTime());
		s.setGroupId(1);
		s.setEducationYear(2006);
		
		printString("Добавление студента:" + s);
		printString("********************");
		
		ms.insertStudent(s);
		
		printString("--->> Полный список студентов после добавления");
		
		allStudends = ms.getAllStudents();
		
		for (Student si : allStudends)
			printString(si);
		
		printString();
		
		// Изменим данные о студенте - Перебежкин станет у нас Новоперебежкиным
		// Но все остальное будет таким же - создаем студента с таким же ИД
		s = new Student();
		s.setStudentId(5);
		s.setFirstName("Игорь");
		s.setPatronymic("Владимирович");
		s.setSurName("Новоперебежкин");
		s.setSex('М');
		c = Calendar.getInstance();
		c.set(1991, 8, 31);
		s.setDateOfBirth(c.getTime());
		s.setGroupId(1);
		s.setEducationYear(2006);
		
		printString("Редактирование данных студента:" + s);
		printString("*******************************");
		
		ms.updateStudent(s);
		
		printString("--->> Полный список студентов после редактирования");
		
		allStudends = ms.getAllStudents();
		
		for (Student si : allStudends)
			printString(si);
		
		printString();
		
		// Удалим нашего студента
		printString("Удаление студента:" + s);
		printString("******************");
		
		ms.deleteStudent(s);
		
		printString("--->> Полный список студентов после удаления");
		
		allStudends = ms.getAllStudents();
		
		for (Student si : allStudends)
			printString(si);
		
		printString();
		
		// Здесь мы переводим всех студентов одной группы в другую
		// Мы знаем, что у нас 2 группы
		// Не совсем элегантное решение, но пока сделаем так
		Group g1 = groups.get(0);
		Group g2 = groups.get(1);
		
		printString("Перевод студентов из 1-ой во 2-ю группу");
		printString("***************************************");
		
		ms.moveStudentsToGroup(g1, 2006, g2, 2007);
		
		printString("--->> Полный список студентов после перевода");
		
		allStudends = ms.getAllStudents();
		
		for (Student si : allStudends)
			printString(si);
		
		printString();
		
		// Удаляем студентов из группы
		printString("Удаление студентов из группы:" + g2 + " в 2006 году");
		printString("*****************************");
		
		ms.removeStudentsFromGroup(g2, 2006);
		
		printString("--->> Полный список студентов после удаления");
		
		allStudends = ms.getAllStudents();
		
		for (Iterator i = allStudends.iterator(); i.hasNext();)
			printString(i.next());
		
		printString();
	}
	
	// Этот код позволяет нам изменить кодировку
	// Такое может произойти если используется IDE - например NetBeans.
	// Тогда вы получаете просто одни вопросы, что крайне неудобно читать
	public static void printString(Object s)
	{
		System.out.println(s.toString());
		try
		{
			System.out.println(new String(s.toString().getBytes("windows-1251"), "windows-1252"));
		}
		catch (UnsupportedEncodingException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void printString()
	{
		System.out.println();
	}
	*/
}
