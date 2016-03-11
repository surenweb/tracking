package fastLibrary;

import java.util.List;

public class ClassPatrolTrack {
	
	public int ID;public int PatrolID;public String Lat;public String Lon;public String GpsDate; public double Accuracy;
	public int SyncStatus ;
	
	public ClassPatrolTrack()
	{
		ID=0;PatrolID=0;Lat="";Lon="";GpsDate="";Accuracy=0.0;
	}
	
	public ClassPatrolTrack(int argID)	
	{	ID=0;PatrolID=0;Lat="";Lon="";GpsDate="";Accuracy=0.0;
		
		if(argID>0){
			String sql ="Select * from patrol_track where ID="+argID ;
			FastDb db = new FastDb("dbLocation",FastApp.getContext());
			List<FastRow> lstResult = db.RunSql(sql);
					
			if(lstResult.size()>0 ) 
			{
				FastRow row = lstResult.get(0);				
				
				ID = Integer.parseInt( row.get("ID"));
				PatrolID = Integer.parseInt( row.get("PatrolID"));
				Lat = row.get("Lat");
				Lon = row.get("Lon");
				GpsDate = row.get("GpsDate");
				Accuracy =Double.parseDouble(row.get("Accuracy")) ;
			}
				
		}
	}
	public List<FastRow> GetAll( Integer... patrolID )
	{
		String sql ="Select * from patrol_track" ;
		if(patrolID.length > 0)
			sql += " WHERE PatrolID ="+patrolID ; 
		
		FastDb db = new FastDb("dbLocation",FastApp.getContext());
		List<FastRow> lstResult = db.RunSql(sql);
		return lstResult;		
	}

	public FastKeyValue GetFson(int limit )
	{
		//SELECT ONLY COLUMNS FOR SERVER  
		String sql ="Select ID,PatrolID,Lat,Lon,GpsDate,Accuracy from patrol_track where SyncStatus=1 ORDER BY GpsDate  LIMIT " +limit  ;
		FastDb db = new FastDb("dbLocation",FastApp.getContext());
		FastKeyValue id_value= db.GetDumpText(sql);
		return id_value;		
	}
	public boolean SetSynced(String arrID )
	{
		String sql = "UPDATE patrol_track SET SyncStatus=2 "+
				 	" WHERE ID IN ("+arrID + ")" ;
		
		FastDb db = new FastDb("dbLocation",FastApp.getContext());
		return db.RunDml(sql);
	}
	
	public boolean Update() {
		String sql ="";
		if(this.ID==0)
		{
			sql ="INSERT INTO patrol_track (PatrolID,Lat,Lon,GpsDate,Accuracy,SyncStatus) VALUES "+
					"('"+PatrolID+"','"+Lat+"','"+Lon+"','"+GpsDate + "','"+Accuracy+"',1)";
		}
		else
		{	sql = "UPDATE patrol_track SET PatrolID=" +PatrolID +",Lat= '"+Lat+"',Lon='"+Lon+"',GpsDate='"+GpsDate+"' "+
				 " ,Accuracy='"+Accuracy+"' WHERE ID="+ID ;
		}
		FastDb db = new FastDb("dbLocation",FastApp.getContext());
		return db.RunDml(sql);			
	}
	
}
