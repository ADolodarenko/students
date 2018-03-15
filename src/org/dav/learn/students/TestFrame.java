package org.dav.learn.students;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestFrame extends JFrame implements ListSelectionListener, ActionListener
{
	private JList list;
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame frame = new TestFrame();
				frame.setName("Indexes");
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
	
	public TestFrame()
	{
		list = new JList();
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		DefaultListModel model = new DefaultListModel();
		list.setModel(model);
		
		list.addListSelectionListener(this);
		
		JButton addButton = new JButton("Add");
		JButton delButton = new JButton("Delete");
		
		addButton.addActionListener(this);
		delButton.addActionListener(this);
		
		addButton.setName("add");
		delButton.setName("del");
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(addButton);
		panel.add(delButton);
		
		getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		setBounds(100, 100, 200, 200);
	}
	
	/**
	 * Invoked when an action occurs.
	 *
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		DefaultListModel model = (DefaultListModel)list.getModel();
		
		JButton sender = (JButton)e.getSource();
		
		if ("add".equals(sender.getName()))
			model.addElement(String.valueOf(model.getSize()));
		else if ("del".equals(sender.getName()) && list.getSelectedIndex() >= 0)
			model.remove(list.getSelectedIndex());
	}
	
	/**
	 * Called whenever the value of the selection changes.
	 *
	 * @param e the event that characterizes the change.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
			setName("New index: " + list.getSelectedIndex());
	}
}
