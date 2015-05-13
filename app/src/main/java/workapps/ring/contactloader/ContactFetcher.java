package workapps.ring.contactloader;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.content.CursorLoader;
import android.util.Log;

// new ContactFetcher(this).fetchAll();
public class ContactFetcher {
    private Context context;

    public ContactFetcher(Context c) {
        this.context = c;
    }

    public ArrayList<Contact> fetchStared() {
        ArrayList<Contact> listContacts = new ArrayList<Contact>();
        CursorLoader cursorLoader = new CursorLoader(context, RawContacts.CONTENT_URI,
                null, // the columns to retrieve (all)
                "starred=?", // the selection criteria (none)
                new String[]{"1"}, // the selection args (none)
                null // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();
        if (c.moveToFirst()) {
            do {
                Contact contact = loadContactData(c);
                listContacts.add(contact);
            } while (c.moveToNext());
        }
        c.close();
        return listContacts;
    }

    public ArrayList<Contact> fetchPhone() {
        ArrayList<Contact> listContacts = new ArrayList<Contact>();

        CursorLoader cursorLoader = new CursorLoader(context, RawContacts.CONTENT_URI,
                null, // the columns to retrieve (all)
                ContactsContract.Contacts._ID + "=?", // the selection criteria (none)
                new String[]{"4171"}, // the selection args (none)
                null // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();
        if (c.moveToFirst()) {
            do {
                Contact contact = loadContactData(c);
                listContacts.add(contact);
            } while (c.moveToNext());
        }
        c.close();
        return listContacts;
    }

    public ArrayList<Contact> fetchAll() {
        ArrayList<Contact> listContacts = new ArrayList<Contact>();
        CursorLoader cursorLoader = new CursorLoader(context, RawContacts.CONTENT_URI,
                null, // the columns to retrieve (all)
                null, // the selection criteria (none)
                null, // the selection args (none)
                null // the sort order (default)
        );

        Cursor c = cursorLoader.loadInBackground();
        if (c.moveToFirst()) {
            do {
                Contact contact = loadContactData(c);
                listContacts.add(contact);
            } while (c.moveToNext());
        }
        c.close();
        return listContacts;
    }

    private Contact loadContactData(Cursor c) {
        // Get Contact ID
        int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
        String contactId = c.getString(idIndex);
        // Get Contact Name
        int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String contactDisplayName = c.getString(nameIndex);


        Contact contact = new Contact(contactId, contactDisplayName);
        fetchContactNumbers(c, contact, contactDisplayName);
        return contact;
    }

    public void fetchContactNumbers(Cursor cursor, Contact contact, String contactDisplayName) {
        // Get numbers
        cursor = context.getContentResolver().query(Phone.CONTENT_URI,
                null,
                Phone.DISPLAY_NAME + "=?",
                new String[]{contactDisplayName},
                null);
//        int contactIdIdx = cursor.getColumnIndex(Phone._ID);
//        int nameIdx = cursor.getColumnIndex(Phone.DISPLAY_NAME);
//        int cidIdx = cursor.getColumnIndex(Phone.CONTACT_ID);

        cursor.moveToFirst();
        do {

            String phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));

            String phoneType = cursor.getString(cursor.getColumnIndex(Phone.TYPE));

            contact.addNumber(phoneNumber, phoneType.toString());

        } while (cursor.moveToNext());

//        final String[] numberProjection = new String[]{Phone.NUMBER, Phone.TYPE,};

//        Cursor phone = new CursorLoader(context, Phone.CONTENT_URI, numberProjection,
//                Phone.CONTACT_ID + " = ?", new String[]{String.valueOf(contact.id)}, null)
//                .loadInBackground();
//
//        if (phone.moveToFirst()) {
//            final int contactNumberColumnIndex = phone.getColumnIndex(Phone.NUMBER);
//            final int contactTypeColumnIndex = phone.getColumnIndex(Phone.TYPE);
//
//            while (!phone.isAfterLast()) {
//                final String number = phone.getString(contactNumberColumnIndex);
//                final int type = phone.getInt(contactTypeColumnIndex);
//                String customLabel = "Custom";
//                CharSequence phoneType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(
//                        context.getResources(), type, customLabel);
//                contact.addNumber(number, phoneType.toString());
//                phone.moveToNext();
//            }
//
//        }
//        phone.close();
    }


}