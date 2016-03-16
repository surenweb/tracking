package fastLibrary;

import android.util.Log;

import java.util.List;

/**
 * Created by root on 3/13/2016.
 */
public class ClassEvent {

    public int ID;
    public int MobileID;
    public String Event;
    public String EventDetail;
    public String EventImage;
    public String Lat ;
    public String Lon ;
    public String Accuracy ;
    public String CreatedDate ;
    public int SyncStatus;

    //SyncStat : 0--> Not Ready To Sync , 1--> Ready To Sync , 2--> Already Synced

    public ClassEvent()
    {
        ID=0;
        MobileID=Integer.parseInt(FastConfig.appMobileID );
        Event="";EventDetail ="";EventImage="";Lat=""; Lon="";Accuracy=""; CreatedDate ="";
        SyncStatus=0;
    }
    public ClassEvent(int argID)
    {	ID=0;
        MobileID=Integer.parseInt(FastConfig.appMobileID );
        Event="";EventDetail ="";EventImage="";Lat=""; Lon="";Accuracy=""; CreatedDate ="";
        SyncStatus=0;

        if(argID>0){
            String sql = "select * from event  where id="+argID;

            FastDb db = new FastDb("dbLocation",FastApp.getContext());
            List<FastRow> lstResult = db.RunSql(sql);

            if(lstResult.size()>0 )
            {
                FastRow row = lstResult.get(0);

                ID = Integer.parseInt( row.get("ID"));
                MobileID = Integer.parseInt(row.get("MobileID"));
                Event = row.get("Event");
                EventDetail = row.get("EventDetail");
                EventImage=row.get("EventImage");
                Lat= row.get("Lat");
                Lon= row.get("Lon");
                Accuracy= row.get("Accuracy").toString();
                CreatedDate =row.get("CreatedDate");
                SyncStatus= Integer.parseInt(row.get("SyncStatus"));
            }

        }
    }

    public List<FastRow> GetAll()
    {
        String sql ="Select * from event" ;
        FastDb db = new FastDb("dbLocation",FastApp.getContext());
        List<FastRow> lstResult = db.RunSql(sql);
        return lstResult;
    }

    public FastKeyValue GetFson(int limit )
    {
        //SELECT ONLY COLUMNS FOR SERVER
        String sql ="Select ID,MobileID,Event,EventDetail,EventImage,Lat, Lon, Accuracy,CreatedDate FROM event where SyncStatus=1 ORDER BY ID LIMIT " +limit ;
        FastDb db = new FastDb("dbLocation",FastApp.getContext());
        FastKeyValue id_value= db.GetDumpText(sql);
        return id_value;
    }

    public boolean SetSynced(String arrID )
    {
        String sql = "UPDATE event SET SyncStatus=2 "+
                " WHERE ID IN ("+arrID + ")" ;

        FastDb db = new FastDb("dbLocation",FastApp.getContext());
        return db.RunDml(sql);
    }

    public Integer TopInsertID ()
    {
        String sql = "SELECT ID from event order by ID DESC limit 1 ";
        FastDb db = new FastDb("dbLocation",FastApp.getContext());

        List<FastRow> lstResult = db.RunSql(sql);
        if(lstResult!=null && lstResult.size()>0 )
        {
            FastRow row = lstResult.get(0);
            return Integer.parseInt(row.get("ID"));
        }
        return -1 ;
    }

    public Boolean Update() {
        String sql ="";
        if(this.ID==0)
        {
            sql ="INSERT INTO event (MobileID,Event,EventDetail,EventImage,Lat, Lon, Accuracy,CreatedDate,SyncStatus) VALUES " +
                    "("+MobileID+","+ FastTask.sq(Event)+","+ FastTask.sq(EventDetail)+","+FastTask.sq(EventImage)+",'"+Lat+"','"+Lon+"',"+
                    "'"+Accuracy+"','"+CreatedDate+"',1)";
        }
        else
        {	sql = "UPDATE patrol SET " +
                " MobileID=" +MobileID +",Event= "+FastTask.sq(Event)+ ",EventDetail="+FastTask.sq(EventDetail)+"," +
                " EventImage="+FastTask.sq(EventImage)+"," +
                " Lat='"+Lat+"' , Lon='"+Lon+"',Accuracy='"+Accuracy+"', "+
                " CreatedDate ='"+CreatedDate+"' WHERE ID="+ID ;
        }
        FastDb db = new FastDb("dbLocation",FastApp.getContext());
        return db.RunDml(sql);

    }
}
