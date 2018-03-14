package org.dav.learn.students.frame;

import org.dav.learn.students.logic.Group;
import org.dav.learn.students.logic.ManagementSystem;
import org.dav.learn.students.logic.Student;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.Vector;

public class StudentsFrame extends JFrame
{
	ManagementSystem ms = null;
	private JList grpList;
	private JList stdList;
	private JSpinner spYear;
	
	public StudentsFrame() throws Exception
	{
		// Устанавливаем layout для всей клиентской части формы
		getContentPane().setLayout(new BorderLayout());
		
		// Создаем верхнюю панел, где будет поле для ввода года
		JPanel top = new JPanel();
		// Устанавливаем для нее layout
		top.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Вставляем пояснительную надпись
		top.add(new JLabel("Год обучения:"));
		// Делаем спин-поле
		//   1. Задаем модель поведения - только цифры
		//   2. Вставляем в панель
		SpinnerModel sm = new SpinnerNumberModel(2006, 1900, 2100, 1);
		spYear = new JSpinner(sm);
		top.add(spYear);
		
		// Создаем нижнюю панель и задаем ей layout
		JPanel bot = new JPanel();
		bot.setLayout(new BorderLayout());
		
		// Создаем левую панель для вывода списка групп
		JPanel left = new JPanel();
		// Задаем layout и задаем "бордюр" вокруг панели
		left.setLayout(new BorderLayout());
		left.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		// Нам необходимо обработать ошибку при обращении к базе данных
		Vector gr = null;
		Vector st = null;
		// Попробуем получить коннект к базе данных
		ms = ManagementSystem.getInstance();
		
		// Получаем список групп
		gr = new Vector<>(ms.getGroups());
		// Создаем надпись
		left.add(new JLabel("Группы:"), BorderLayout.NORTH);
		// Создаем визуальный список и вставляем его в скроллируемую
		// панель, которую в свою очередь уже кладем на панель left
		grpList = new JList(gr);
		left.add(new JScrollPane(grpList), BorderLayout.CENTER);
		
		// Создаем правую панель для вывода списка студентов
		JPanel right = new JPanel();
		// Задаем layout и задаем "бордюр" вокруг панели
		right.setLayout(new BorderLayout());
		right.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		// Получаем список студентов
		st = new Vector<>(ms.getAllStudents());
		// Создаем надпись
		right.add(new JLabel("Студенты:"), BorderLayout.NORTH);
		// Создаем визуальный список и вставляем его в скроллируемую
		// панель, которую в свою очередь уже кладем на панель right
		stdList = new JList(st);
		right.add(new JScrollPane(stdList), BorderLayout.CENTER);
		
		// Вставляем панели со списками групп и студентов в нижнюю панель
		bot.add(left, BorderLayout.WEST);
		bot.add(right, BorderLayout.CENTER);
		
		// Вставляем верхнюю и нижнюю панели в форму
		getContentPane().add(top, BorderLayout.NORTH);
		getContentPane().add(bot, BorderLayout.CENTER);
		
		// Задаем границы формы
		setBounds(100, 100, 600, 400);
	}
	
	public static void main(String[] args)
	{
		// Запуск формы лучше производить в специальном треде
		// event-dispatching thread - EDT
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				StudentsFrame sf = null;
				try
				{
					sf = new StudentsFrame();
					sf.setDefaultCloseOperation(EXIT_ON_CLOSE);
					sf.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}