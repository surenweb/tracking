package fastLibrary;

import java.util.List;

import android.util.Log;

public class ClassPatrol {
	FastConfig share = new FastConfig();
	public int ID;public int MobileID;public String CaseTitle;public String StartDate;public String EndDate;public int IsFinished ;
	public int SyncStatus;	public int TotalSyncTrack;	public int TotalUnSyncTrack ;
	public String PatrolType ;
	public String PatrolOn ;
	public String TotalNafri ;
	//SyncStat : 0--> Not Ready To Sync , 1--> Ready To Sync , 2--> Already Synced 
	
	public ClassPatrol()
	{
		ID=0;
		MobileID=Integer.parseInt(FastConfig.appMobileID );
		CaseTitle="";PatrolType ="";PatrolOn="";TotalNafri=""; StartDate="";EndDate="";
		SyncStatus=0;TotalSyncTrack =0 ;TotalUnSyncTrack =0 ;
	}
	public ClassPatrol(int argID)	
	{	ID=0;
		MobileID=Integer.parseInt(FastConfig.appMobileID);
		CaseTitle="";PatrolType ="";PatrolOn="";  TotalNafri=""; StartDate="";EndDate="";
		SyncStatus=0;TotalSyncTrack =0 ;TotalUnSyncTrack =0 ;
		
		if(argID>0){
				String sql = "select ifnull(b.TotalSync,'0') as TotalSync,ifnull(c.TotalUnSync,'0') as TotalUnSync , a.* from patrol a 	left join " + 
							"(select PatrolID,count(ID) as TotalSync From patrol_track where PatrolID ="+argID+" AND SyncStatus=2 ) b on a.ID = b.PatrolID  " +
							" left join " +
							" (select PatrolID,count(ID) as TotalUnSync From patrol_track where PatrolID ="+argID+" AND SyncStatus=1 ) c on a.ID = c.PatrolID " +
							" where a.id="+argID;				
			
			FastDb db = new FastDb("dbLocation",FastApp.getContext());
			List<FastRow> lstResult = db.RunSql(sql);
					
			if(lstResult.size()>0 ) 
			{
				FastRow row = lstResult.get(0);				
				
				ID = Integer.parseInt( row.get("ID"));
				MobileID = Integer.parseInt( row.get("MobileID"));
				CaseTitle = row.get("CaseTitle");
				PatrolType =row.get("PatrolType");
				PatrolOn=row.get("PatrolOn");
				TotalNafri = row.get("TotalNafri");
				
				StartDate = row.get("StartDate");
				EndDate = row.get("EndDate");				
				
				IsFinished =  Integer.parseInt( row.get("IsFinished"));
				
				SyncStatus =  Integer.parseInt( row.get("SyncStatus"));
				TotalSyncTrack = Integer.parseInt( row.get("TotalSync"));
				TotalUnSyncTrack = Integer.parseInt( row.get("TotalUnSync"));
			}
				
		}
	}
	
	
	public String GetRunningPatrolID()
	{
	    	FastDb userDb = new FastDb("dbLocation",FastApp.getContext());
	    	String sql = " Select ID from patrol WHERE IsFinished != 1 "; // IsActive 0 --> Running , 1 --> Stopped 
	    	List<FastRow> result = userDb.RunSql(sql);
	    	
	    	Log.d("myApp","Total unfinished Patrol : "+result.size() );
	    	
	    	if(result.size()== 0)
	    		return "";
	    	else
	    	{
	    		FastRow row = result.get(0);			
				return row.get("ID");
	    	}
	    		
	}
	 
	public List<FastRow> GetAll()
	{
		String sql ="Select * from patrol" ;
		FastDb db = new FastDb("dbLocation",FastApp.getContext());
		List<FastRow> lstResult = db.RunSql(sql);
		return lstResult;		
	}
	
	public FastKeyValue GetFson(int limit )
	{
		//SELECT ONLY COLUMNS FOR SERVER  
		String sql ="Select ID,MobileID,CaseTitle,PatrolType,PatrolOn,TotalNafri,StartDate,EndDate from patrol where SyncStatus=1 ORDER BY ID LIMIT " +limit ;
		FastDb db = new FastDb("dbLocation",FastApp.getContext());
		FastKeyValue id_value= db.GetDumpText(sql);
		return id_value;		
	}
	
	public boolean SetSynced(String arrID )
	{
		String sql = "UPDATE patrol SET SyncStatus=2 "+
				 	" WHERE ID IN ("+arrID + ")" ;
		
		FastDb db = new FastDb("dbLocation",FastApp.getContext());
		return db.RunDml(sql);
	}
	
	public Boolean Update() {
		String sql ="";
		if(this.ID==0)
		{
			sql ="INSERT INTO patrol (MobileID,CaseTitle,PatrolType,PatrolOn,TotalNafri,StartDate,EndDate,SyncStatus) VALUES " + 
					"(" +MobileID+",'"+CaseTitle+"','"+PatrolType+"','"+PatrolOn+"','"+TotalNafri+"','"+StartDate+"','"+EndDate +"',1)";
		}
		else
		{	sql = "UPDATE patrol SET MobileID=" +MobileID +",CaseTitle= '"+CaseTitle+ "',PatrolType='"+PatrolType+"',PatrolOn='"+PatrolOn+"'" +
					", TotalNafri='"+TotalNafri+"' , StartDate='"+StartDate+"',EndDate='"+EndDate+"'"+
					" ,IsFinished ="+IsFinished+" , SyncStatus="+SyncStatus+" WHERE ID="+ID ;
		}
		FastDb db = new FastDb("dbLocation",FastApp.getContext());
		return db.RunDml(sql);
				
	}
	


}
