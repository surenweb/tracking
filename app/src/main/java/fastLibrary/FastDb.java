package fastLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FastDb
{
    SQLiteDatabase db;

    public FastDb(String fileName, Context context)
    {
        db = context.openOrCreateDatabase(fileName, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.setVersion(1);
        db.setLocale(Locale.getDefault());
        // db.setLockingEnabled(true);
    }

    public Boolean RunDml(final String sql)
    { 
        try {
        	 db.execSQL(sql); return true ;
        } catch (Exception e) {
            // This will catch any exception, because they are all descended from Exception
        	 Log.d("myLog","Error: Query=" +sql + " |Message: "+ e.getMessage());
            return false;
        }
       
    }
   

    public List<FastRow> RunSql(final String sql)
    {
       
        List<FastRow> lstResult = new ArrayList<FastRow>();
       
        try {
        	
        	 Cursor cursor = db.rawQuery(sql, null); 
        	 if (cursor.moveToFirst())
             {
             	do
                 {
                     int columns = cursor.getColumnCount();
                     FastRow row = new FastRow();

                     for (int i=0; i<columns; i++)
                     {
                         String col = cursor.getColumnName(i);
                         String val = cursor.getString(i);                        
                         row.add(col,val);
                     }

                     lstResult.add(row);
                 }
                 while (cursor.moveToNext());
             }
             cursor.close();
             
        } catch (Exception e) {           
        	 Log.d("myLog","Error: Query=" +sql + " |Message: "+ e.getMessage());
           return null ;
        } 
        
        return lstResult;
    }
    
    
    
    public FastKeyValue GetDumpText(String sql)
	{
		       
        String csvID = "";
        String csvRow="";
        
        try {
        	
        	 Cursor cursor = db.rawQuery(sql, null); 
        	 if (cursor.moveToFirst())
             {
             	do
                 {
                     int totColumn = cursor.getColumnCount();                                      
                     String strVal = "";
                     
                     for (int i=0; i<totColumn; i++)
                     {                    	
                         String col = cursor.getColumnName(i); 
                         String val = cursor.getString(i); 
                         
                         strVal += FastTask.sq(val) +",";
                     }
                     if(strVal.length()>1 ) strVal =  strVal.substring(0,strVal.length()-1 ) ; // Remove last , seperetor
                     
                     csvRow += "("+ strVal+ ")"+ ",";
                     csvID +=  cursor.getString(0) +",";
                     
                     Log.d("myLog","Unsynced Row : "+ strVal );
                 }
                 while (cursor.moveToNext());
             }
             cursor.close();
             
        } catch (Exception e) {           
        	Log.d("myLog","Error: Query=" +sql + " |Message: "+ e.getMessage());
           return null ;
        }     
        if(csvID.length()>1 ) csvID = csvID.substring(0,csvID.length()-1 ); // Remove last , seperetor of csvID 
        if(csvRow.length()>1 ) csvRow = csvRow.substring(0,csvRow.length()-1 ); // Remove last , seperetor of csvRow        
      
        return new FastKeyValue(csvID,csvRow);
	}
    
    
    public void close()
    {
        db.close();
    }

}

