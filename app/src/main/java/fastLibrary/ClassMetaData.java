package fastLibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/15/2016.
 */
public class ClassMetaData {
    public int ID;
    public String Tag;
    public String Value;
    public String Title;
    public String TitleNp;
    public int Serial;
    public int IsActive ;
    public String UpdateDate ;

    public ClassMetaData()
    {
        ID=0;Tag="";Value="";Title="";TitleNp="";Serial=1;IsActive=1;UpdateDate="";
    }

    public ClassMetaData(int argID)
    {	ID=0;Tag="";Value="";Title="";TitleNp="";Serial=1;IsActive=1;UpdateDate="";

        if(argID>0){
            String sql ="Select * from app_meta_data where ID="+argID ;
            FastDb db = new FastDb("dbLocation",FastApp.getContext());
            List<FastRow> lstResult = db.RunSql(sql);

            if(lstResult.size()>0 )
            {
                FastRow row = lstResult.get(0);

                ID= Integer.parseInt(row.get("ID"));
                Tag= row.get("Tag");
                Value= row.get("Value");
                Title= row.get("Title");
                TitleNp= row.get("TitleNp");
                Serial=Integer.parseInt(row.get("Serial"));
                IsActive=Integer.parseInt(row.get("IsActive"));
                UpdateDate=row.get("UpdateDate");

            }

        }
    }
    public List<FastKeyValue> GetAllByTag( String tag )
    {

        String sql ="Select Value,Title from app_meta_data" ;

        if(FastConfig.appLanguage.equalsIgnoreCase("NP"))
            sql ="Select Value,TitleNp from app_meta_data" ;

        if(tag.length() > 0)
            sql += " WHERE Tag ='"+tag+"'" ;

        FastDb db = new FastDb("dbLocation",FastApp.getContext());
        List<FastRow> lstResult = db.RunSql(sql);
        List<FastKeyValue> lstKeyVal = new ArrayList<FastKeyValue>();

        for(FastRow row : lstResult){
            FastKeyValue kv = new FastKeyValue(row.get(0),row.get(1));
            lstKeyVal.add(kv);
        }

        return lstKeyVal;
    }

    public boolean Update() {
        String sql ="";
        if(this.ID==0)
        {
            sql ="INSERT INTO app_meta_data (ID, Tag, Value, Title, TitleNp, Serial, IsActive, UpdateDate) VALUES "+
                    "("+ID+",'"+ Tag+"','"+ Value+"','"+ Title+"','"+ TitleNp+"',"+ Serial+","+ IsActive+","+ UpdateDate+" )";
        }
        else
        {	sql = "UPDATE app_meta_data SET Tag='"+Tag+"',Value='"+Value+"',Title='"+Title+"',TitleNp='"+TitleNp+"' "+
                " ,Serial="+Serial+",IsActive="+IsActive+",UpdateDate='"+UpdateDate+"' WHERE ID="+ID ;
        }
        FastDb db = new FastDb("dbLocation",FastApp.getContext());
        return db.RunDml(sql);
    }
}
