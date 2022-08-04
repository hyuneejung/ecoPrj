package rankUpdate;

import java.sql.Connection;

import member.EcoDto;
import static util.MiniConn.*;

public class RankUpdateService {

	public void rankUpdate(EcoDto ed) {
		Connection conn = null;
		try {
			conn = getConnection();
			int result = new RankUpdateDao().rankUpdate(ed,conn);
			if(result == 1) {
				commit(conn);
			}else {
				rollback(conn);
			}
		} catch(Exception e) {
			rollback(conn);
			e.printStackTrace();
		} finally {
			close(conn);
		}
	}
	
	public String changeRank(EcoDto ed) {
		Connection conn = null;
		String rank = null;
		try {
			conn = getConnection();
			rank = new RankUpdateDao().changeRank(ed,conn);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}
		return rank;
	}

}
