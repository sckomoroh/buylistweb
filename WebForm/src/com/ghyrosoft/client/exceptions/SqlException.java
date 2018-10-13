package com.ghyrosoft.client.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: sckomoroh
 * Date: 1/30/13
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class SqlException extends Exception
{
	private static final long serialVersionUID = 1L;

	private String message;
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return this.message;
	}
}
