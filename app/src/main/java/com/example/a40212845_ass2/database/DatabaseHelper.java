package com.example.a40212845_ass2.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.a40212845_ass2.Access;
import com.example.a40212845_ass2.StudentProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String TAG = "DatabaseHelper";
    public DatabaseHelper(FragmentActivity context){
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_STUDENT_TABLE = "CREATE TABLE "
                + Config.STUDENT_PROFILE_TABLE_NAME
                + " (" + Config.COLUMN_STUDENT_ID + " INTEGER NOT NULL, "
                + Config.COLUMN_STUDENT_SURNAME + " TEXT NOT NULL, "
                + Config.COLUMN_STUDENT_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_STUDENT_GPA + " REAL NOT NULL, "
                + Config.COLUMN_DATE_CREATED + " TEXT NOT NULL)";

        Log.d(TAG, CREATE_STUDENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_STUDENT_TABLE);
        Log.d(TAG, "db created");

        String CREATE_ACCESS_TABLE = "CREATE TABLE "
                + Config.ACCESS_TABLE_NAME
                + " (" + Config.COLUMN_ACCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_PROFILE_ID + " TEXT NOT NULL, "
                + Config.COLUMN_ACCESS_TYPE + " TEXT NOT NULL, "
                + Config.COLUMN_TIMESTAMP + " TEXT NOT NULL)";

        Log.d(TAG, CREATE_ACCESS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ACCESS_TABLE);
        Log.d(TAG, "db created");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long insertStudentProfile(StudentProfile studentProfile){
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_STUDENT_SURNAME, studentProfile.getSurname());
        contentValues.put(Config.COLUMN_STUDENT_NAME, studentProfile.getName());
        contentValues.put(Config.COLUMN_STUDENT_ID, studentProfile.getProfileID());
        contentValues.put(Config.COLUMN_STUDENT_GPA, studentProfile.getGPA());
        contentValues.put(Config.COLUMN_DATE_CREATED, studentProfile.getDateCreated());
        try{
            id = db.insertOrThrow(Config.STUDENT_PROFILE_TABLE_NAME, null, contentValues);
        }
        catch (SQLException e){
            Log.d(TAG, "EXCEPTION " + e);
            Toast.makeText(context, "O " + e, Toast.LENGTH_SHORT).show();
        }
        finally{
            db.close();
        }
        return id;
    }
    public long deleteStudentProfile(String surname){
        long id = -1;
        SQLiteDatabase db =this.getReadableDatabase();
        try{
            id = db.delete(Config.STUDENT_PROFILE_TABLE_NAME, Config.COLUMN_STUDENT_SURNAME + "=?", new String[]{surname});
        }
        catch (SQLException e){
            Log.d(TAG, "EXCEPTION " + e);
            Toast.makeText(context, "Failed: " + e, Toast.LENGTH_SHORT).show();
        }
        finally{
            db.close();
        }
        return id;
    }
    public long insertAccess(Access access){
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PROFILE_ID, access.getProfileId());
        contentValues.put(Config.COLUMN_ACCESS_TYPE, access.getAccessType());
        contentValues.put(Config.COLUMN_TIMESTAMP, access.getTimestamp());
        try{
            id = db.insertOrThrow(Config.ACCESS_TABLE_NAME, null, contentValues);
        }
        catch (SQLException e){
            Log.d(TAG, "EXCEPTION " + e);
            Toast.makeText(context, "O " + e, Toast.LENGTH_SHORT).show();
        }
        finally{
            db.close();
        }
        return id;
    }
    //Function necessary for ListView
    public List<StudentProfile> getAllProfiles(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(Config.STUDENT_PROFILE_TABLE_NAME, null, null, null, null, null, null);

            if(cursor!=null){
                if(cursor.moveToFirst()){
                    List<StudentProfile> studentProfiles = new ArrayList<>();
                    do{
                        @SuppressLint("Range") String surname = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_SURNAME));
                        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_NAME));
                        @SuppressLint("Range") int profileID = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_STUDENT_ID));
                        @SuppressLint("Range") float gpa = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_STUDENT_GPA));
                        @SuppressLint("Range") String dateCreated = cursor.getString(cursor.getColumnIndex(Config.COLUMN_DATE_CREATED));

                        studentProfiles.add(new StudentProfile(surname, name, profileID, gpa, dateCreated));
                    }while(cursor.moveToNext());
                    return studentProfiles;
                }
            }
        }
        catch(SQLException e){
            Log.d(TAG, "EXCEPTION " + e);
            Toast.makeText(context, "Operation Failed: " + e, Toast.LENGTH_SHORT).show();
        }
        finally {
            if(cursor!=null)
                cursor.close();
            db.close();
        }
        return Collections.emptyList();
    }
    public List<Access> getAllAccess(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(Config.ACCESS_TABLE_NAME, null, null, null, null, null, null);

            if(cursor!=null){
                if(cursor.moveToFirst()){
                    List<Access> access = new ArrayList<>();

                    do{
                        @SuppressLint("Range") int profileId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PROFILE_ID));
                        if(id == profileId) {
                            @SuppressLint("Range") int accessId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ACCESS_ID));
                            @SuppressLint("Range") String timestamp = cursor.getString(cursor.getColumnIndex(Config.COLUMN_TIMESTAMP));
                            @SuppressLint("Range") String accessType = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ACCESS_TYPE));
                            access.add(new Access(accessId, profileId, accessType, timestamp));
                        }
                    }while(cursor.moveToNext());
                    return access;
                }
            }
        }
        catch(SQLException e){
            Log.d(TAG, "EXCEPTION " + e);
            Toast.makeText(context, "Operation Failed: " + e, Toast.LENGTH_SHORT).show();
        }
        finally {
            if(cursor!=null)
                cursor.close();
            db.close();
        }
        return Collections.emptyList();
    }
    public int getTotalProfiles(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int totalProfiles = 0;

        try{
            cursor = db.query(Config.STUDENT_PROFILE_TABLE_NAME, null, null, null, null, null, null);

            if(cursor!=null){
                if(cursor.moveToFirst()){
                    do{
                        totalProfiles++;
                    }while(cursor.moveToNext());
                    return totalProfiles;
                }
            }
        }
        catch(SQLException e){
            Log.d(TAG, "EXCEPTION " + e);
            Toast.makeText(context, "Operation Failed: " + e, Toast.LENGTH_SHORT).show();
        }
        finally {
            if(cursor!=null)
                cursor.close();
            db.close();
        }
        return 0;
    }
}
