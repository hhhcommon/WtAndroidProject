package com.wotingfm.ui.intercom.main.contacts.view;

import com.wotingfm.ui.intercom.main.contacts.model.Contact;

import java.util.Comparator;

public class PinyinComparator implements Comparator<Contact.user> {

	public int compare(Contact.user o1, Contact.user o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
