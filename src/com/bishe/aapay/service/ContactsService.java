package com.bishe.aapay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ContactsService {
	private static ContactsService contactsService;
	private ContentResolver resolver;
	private ContactsService(ContentResolver resolver) {
		this.resolver = resolver;
	}
	public static ContactsService getInstance(ContentResolver resolver) {
		if(contactsService == null) {
			contactsService = new ContactsService(resolver);
		}
		return contactsService;
	}
	
	/**
	 * ��ȡ��ϵ�˵������͵绰
	 * @return
	 */
	public ArrayList<Map<String, String>> getNameAndPhone() {
		ArrayList<Map<String, String>> contacts = new ArrayList<Map<String,String>>();
		//��ѯ��ϵ������
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null,null, null);
		while(cursor.moveToNext()) {
			
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			
			//��ѯ��ϵ�˵ĵ绰��Ϣ
			Cursor phones = resolver.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID
						+ " = " + contactId, null, null);
			
			while(phones.moveToNext()) {
				String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				Map<String, String> map = new HashMap<String, String>();
				map.put("part_name", name);
				map.put("phone", phoneNumber);
				contacts.add(map);
			}
			phones.close();
		}
		return contacts;
	}
}
