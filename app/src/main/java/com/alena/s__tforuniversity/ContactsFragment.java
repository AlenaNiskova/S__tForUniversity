package com.alena.s__tforuniversity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class ContactsFragment extends Fragment {

    private static final String Contact_Id = ContactsContract.Contacts._ID;
    private static final String Name = ContactsContract.Contacts.DISPLAY_NAME;
    private static final String HasNumber = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    private static final String Number = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String Phone_Id = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        ListView lv = (ListView) v.findViewById(R.id.contacts_lv);
        ArrayList<HashMap<String, String>> contacts = new ArrayList<>();
        HashMap<String, String> contact;

        ContentResolver cr = getContext().getContentResolver();
        HashMap<Integer, ArrayList<String>> conts = new HashMap<>();
        Cursor c = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{Number, Phone_Id},
                null, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    Integer contId = c.getInt(c.getColumnIndex(Phone_Id));
                    ArrayList<String> c_conts = new ArrayList<>();
                    if (conts.containsKey(contId)) {
                        c_conts = conts.get(contId);
                    }
                    c_conts.add(c.getString(c.getColumnIndex(Number)));
                    conts.put(contId, c_conts);
                }
                Cursor cur = cr.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{Contact_Id, Name, HasNumber},
                        HasNumber + " > 0",
                        null,
                        Name);
                if (cur != null) {
                    if (cur.getCount() > 0) {
                        while (cur.moveToNext()) {
                            int id = cur.getInt(cur.getColumnIndex(Contact_Id));
                            contact = new HashMap<>();
                            contact.put("Name", cur.getString(cur.getColumnIndex(Name)));
                            contact.put("Number", conts.get(id).toArray()[0].toString());
                            contacts.add(contact);
                        }
                    }
                }
                cur.close();
            }
        }
        c.close();

        SimpleAdapter adapter = new SimpleAdapter(getContext(), contacts, R.layout.contact_item,
                new String[]{"Name", "Number"},
                new int[]{R.id.contact_name, R.id.contact_number});

        lv.setAdapter(adapter);

        return v;
    }
}