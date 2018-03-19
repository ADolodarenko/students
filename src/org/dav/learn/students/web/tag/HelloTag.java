package org.dav.learn.students.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class HelloTag extends TagSupport
{
	private String name = null;
	
	public int doStartTag() throws JspException
	{
		try
		{
			if (name == null)
				pageContext.getOut().write("Hello, world!");
			else
				pageContext.getOut().write("Hello, world! I'm " + name);
		}
		catch (IOException ioe)
		{
			throw new JspTagException(ioe.getMessage());
		}
		
		return SKIP_BODY;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void release()
	{
		super.release();
		name = null;
	}
}
