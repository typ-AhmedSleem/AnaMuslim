/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

public class TelegramUser extends TelegramObject{

	public int id;
	public boolean isBot;
	public String firstName;
	public String lastName;
	public String username;
	public boolean canJoinGroups;
	public boolean canReadAllGroupMsgs;
	public boolean supportsInlineQueries;

	public TelegramUser(int id, boolean isBot, String firstName, String lastName, String username, boolean canJoinGroups, boolean canReadAllGroupMsgs, boolean supportsInlineQueries) {
		this.id = id;
		this.isBot = isBot;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.canJoinGroups = canJoinGroups;
		this.canReadAllGroupMsgs = canReadAllGroupMsgs;
		this.supportsInlineQueries = supportsInlineQueries;
	}

	public int getId() {
		return id;
	}

	public boolean isBot() {
		return isBot;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUsername() {
		return username;
	}

	public boolean canJoinGroups() {
		return canJoinGroups;
	}

	public boolean canReadAllGroupMsgs() {
		return canReadAllGroupMsgs;
	}

	public boolean supportsInlineQueries() {
		return supportsInlineQueries;
	}

	@Override
	public String toString() {
		return "TelegramUser{" +
		       "id=" + id +
		       ", isBot=" + isBot +
		       ", firstName='" + firstName + '\'' +
		       ", lastName='" + lastName + '\'' +
		       ", username='" + username + '\'' +
		       ", canJoinGroups=" + canJoinGroups +
		       ", canReadAllGroupMsgs=" + canReadAllGroupMsgs +
		       ", supportsInlineQueries=" + supportsInlineQueries +
		       '}';
	}
}
