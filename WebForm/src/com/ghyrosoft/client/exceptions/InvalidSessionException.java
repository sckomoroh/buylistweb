package com.ghyrosoft.client.exceptions;

import com.ghyrosoft.client.common.StringUtil;

/**
 * User: ezvigunov
 * Date: 1/25/13
 * Time: 3:07 PM
 */
public class InvalidSessionException extends Exception {
	private static final long serialVersionUID = 1L;

	private String message;

    public void buildMessage(String session, String user)
    {
        message = StringUtil.format(
                "The session '{0}' for user '{1}' is invalid",
                session,
                user);
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
