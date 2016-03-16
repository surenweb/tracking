package fastLibrary;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by root on 3/14/2016.
 */
public class ClassImage {
    public int ID;
    public int MobileID;
    public int EventID;
    public String Title;
    public String FilePath ;
    public int SyncStatus;

    private Bitmap image;
    public Bitmap getImage() {  return image;} //Consume Memory , TODO: load Offline  Later

    public ClassImage(Bitmap image, String title, String filePath) {
        super();

        ID = 0;
        MobileID=Integer.parseInt(FastConfig.appMobileID );
        EventID=0;

        this.Title = title;
        this.FilePath = filePath ;

        this.image = image;
    }
    /*
        public ClassImage() {
            ID = 0;
            MobileID=Integer.parseInt(FastConfig.appMobileID );
            EventID=0;Title ="";FilePath="";
        }
    */
    public ClassImage(int argID ) {
        ID=0;
        MobileID=Integer.parseInt(FastConfig.appMobileID );
        EventID=0;Title ="";FilePath="";

        if(argID>0){
            String sql = "select * from event_image  where id="+argID;

            FastDb db = new FastDb("dbLocation",FastApp.getContext());
            List<FastRow> lstResult = db.RunSql(sql);

            if(lstResult.size()>0 )
            {
                FastRow row = lstResult.get(0);

                ID = Integer.parseInt(row.get("ID"));
                EventID=Integer.parseInt(row.get("EventID"));
                Title = row.get("Title");
                FilePath=row.get("FilePath");
                SyncStatus= Integer.parseInt(row.get("SyncStatus"));
            }

        }
    }

    public void LoadTopImage ()
    {
        ID=0;
        MobileID=Integer.parseInt(FastConfig.appMobileID );
        EventID=0;Title ="";FilePath="";

        String sql = "SELECT * from event_image WHERE SyncStatus=1 order by ID DESC limit 1";
        FastDb db = new FastDb("dbLocation",FastApp.getContext());
        List<FastRow> lstResult = db.RunSql(sql);

        if(lstResult.size()>0 )
        {
            FastRow row = lstResult.get(0);
            ID = Integer.parseInt(row.get("ID"));
            EventID=Integer.parseInt(row.get("EventID"));
            Title = row.get("Title");
            FilePath=row.get("FilePath");
            SyncStatus= Integer.parseInt(row.get("SyncStatus"));
        }
    }

    public boolean SetSynced()
    {
        String sql = "UPDATE event_image SET SyncStatus=2 "+
                " WHERE ID = "+ID ;

        FastDb db = new FastDb("dbLocation",FastApp.getContext());
        return db.RunDml(sql);
    }

    public Boolean Update() {
        String sql ="";
        if(this.ID==0)
        {
            sql ="INSERT INTO event_image (MobileID,EventID,Title,FilePath,SyncStatus) VALUES " +
                    "("+MobileID+","+ EventID +","+ FastTask.sq(Title)+","+FastTask.sq(FilePath)+",1)";
        }
        else
        {	sql = "UPDATE event_image SET " +
                " MobileID=" +MobileID +",EventID= "+EventID+ ",Title="+FastTask.sq(Title)+"," +
                " FilePath="+FastTask.sq(FilePath)+" WHERE ID="+ID ;
        }
        FastDb db = new FastDb("dbLocation",FastApp.getContext());
        return db.RunDml(sql);

    }



}
