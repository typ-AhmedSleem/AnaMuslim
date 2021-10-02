/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

public class TelegramChatPermissions extends TelegramObject {

	private boolean canSendMessages;
	private boolean canSendMedia;
	private boolean canSendPolls;
	private boolean canSendOtherMessages;
	private boolean canSendWebPagePreviews;
	private boolean canChangeInfo;
	private boolean canInviteUsers;
	private boolean canPinMessages;

	public TelegramChatPermissions() {
		this.canSendMessages = false;
		this.canSendMedia = false;
		this.canSendPolls = false;
		this.canSendOtherMessages = false;
		this.canSendWebPagePreviews = false;
		this.canChangeInfo = false;
		this.canInviteUsers = false;
		this.canPinMessages = false;
	}

	public TelegramChatPermissions(boolean canSendMessages, boolean canSendMedia, boolean canSendPolls, boolean canSendOtherMessages, boolean canSendWebPagePreviews, boolean canChangeInfo, boolean canInviteUsers, boolean canPinMessages) {
		this.canSendMessages = canSendMessages;
		this.canSendMedia = canSendMedia;
		this.canSendPolls = canSendPolls;
		this.canSendOtherMessages = canSendOtherMessages;
		this.canSendWebPagePreviews = canSendWebPagePreviews;
		this.canChangeInfo = canChangeInfo;
		this.canInviteUsers = canInviteUsers;
		this.canPinMessages = canPinMessages;
	}

	public boolean canSendMessages() {
		return canSendMessages;
	}

	public boolean canSendMedia() {
		return canSendMedia;
	}

	public boolean canSendPolls() {
		return canSendPolls;
	}

	public boolean canSendOtherMessages() {
		return canSendOtherMessages;
	}

	public boolean canSendWebPagePreviews() {
		return canSendWebPagePreviews;
	}

	public boolean canChangeInfo() {
		return canChangeInfo;
	}

	public boolean canInviteUsers() {
		return canInviteUsers;
	}

	public boolean canPinMessages() {
		return canPinMessages;
	}

	public TelegramChatPermissions setCanSendMessages(boolean canSendMessages) {
		this.canSendMessages = canSendMessages;
		return this;
	}

	public TelegramChatPermissions setCanSendMedia(boolean canSendMedia) {
		this.canSendMedia = canSendMedia;
		return this;
	}

	public TelegramChatPermissions setCanSendPolls(boolean canSendPolls) {
		this.canSendPolls = canSendPolls;
		return this;
	}

	public TelegramChatPermissions setCanSendOtherMessages(boolean canSendOtherMessages) {
		this.canSendOtherMessages = canSendOtherMessages;
		return this;
	}

	public TelegramChatPermissions setCanSendWebPagePreviews(boolean canSendWebPagePreviews) {
		this.canSendWebPagePreviews = canSendWebPagePreviews;
		return this;
	}

	public TelegramChatPermissions setCanChangeInfo(boolean canChangeInfo) {
		this.canChangeInfo = canChangeInfo;
		return this;
	}

	public TelegramChatPermissions setCanInviteUsers(boolean canInviteUsers) {
		this.canInviteUsers = canInviteUsers;
		return this;
	}

	public TelegramChatPermissions setCanPinMessages(boolean canPinMessages) {
		this.canPinMessages = canPinMessages;
		return this;
	}

	@Override
	public String toString() {
		return "TelegramChatPermission{" +
		       "canSendMessages=" + canSendMessages +
		       ", canSendMedia=" + canSendMedia +
		       ", canSendPolls=" + canSendPolls +
		       ", canSendOtherMessages=" + canSendOtherMessages +
		       ", canSendWebPagePreviews=" + canSendWebPagePreviews +
		       ", canChangeInfo=" + canChangeInfo +
		       ", canInviteUsers=" + canInviteUsers +
		       ", canPinMessages=" + canPinMessages +
		       '}';
	}
}
