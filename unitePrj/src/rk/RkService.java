package rk;

import java.sql.Connection;

import member.EcoDto;

import static util.MiniConn.*;

public class RkService {
	
	public RkDto rankBonus(EcoDto ed) {
		Connection conn = null;
		RkDto rd = null;
		try {
			conn = getConnection();
			rd = new RkDao().rankBonus(conn,ed);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}
		
		return rd;
	}
}
