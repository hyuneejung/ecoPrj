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

}
